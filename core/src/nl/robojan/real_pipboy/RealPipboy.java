package nl.robojan.real_pipboy;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;
import java.util.Stack;

import nl.robojan.real_pipboy.Connection.ConnectionManager;
import nl.robojan.real_pipboy.Connection.FileTransferHandler;
import nl.robojan.real_pipboy.Connection.IPacketHandler;
import nl.robojan.real_pipboy.Connection.Packets.DataPacket;
import nl.robojan.real_pipboy.Connection.Packets.HelloPacket;
import nl.robojan.real_pipboy.Connection.Packets.PacketTypes;
import nl.robojan.real_pipboy.FalloutData.FalloutDataManager;
import nl.robojan.real_pipboy.FalloutData.FalloutDataTest;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.PipBoy.PipBoy;
import nl.robojan.real_pipboy.util.ShaderProgramLoader;

public class RealPipboy extends ApplicationAdapter {
	private static final float SWEEP_SPEED = 1.0f/2.0f;

	private PipBoy mPipboy;
	private RenderContext mRenderContext;
	private Context mContext;
	private Mesh mPostMesh;
	private float mSweepPosition = -1.0f;
    private long mLastKeepAliveTime = 0;

	private boolean mConnected = false;
    private boolean mGLStateSet = false;

	private IPacketHandler mOnHelloPacket = new IPacketHandler() {
		@Override
		public void HandlePacket(DataPacket packet) {
			HelloPacket hello = (HelloPacket)packet;
			Gdx.app.debug("CONN", "Connected to " + hello.getName());
		}
	};

	private void createPostProcessingMesh() {
		float [] verts = new float[] {
				// X Y  U V
				-1, -1, 0, 0,
				-1, 1, 0, 1,
				1, 1, 1, 1,
				1, -1, 1, 0
		};
		short [] indices = new short[] {
			0,1,2,0,2,3
		};
		VertexAttributes attributes = new VertexAttributes(
				new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoords")
		);
		mPostMesh = new Mesh(true, 4, 6, attributes);
		mPostMesh.setVertices(verts);
		mPostMesh.setIndices(indices);
	}

	@Override
	public void create () {
		Assets.init();
		GameString.loadStrings();
        Settings.getInstance().loadSettings();

		mPipboy = new PipBoy();
		mRenderContext = new RenderContext();
		mRenderContext.batch = new SpriteBatch();
		mRenderContext.pipboyShaderProgram = null;
		mRenderContext.fbo = new FrameBuffer(Pixmap.Format.RGBA8888,
				Gdx.graphics.getWidth(),  Gdx.graphics.getHeight(), true);
		mRenderContext.clippingRectangles = new Stack<Rectangle>();
		mContext = new Context();
		mContext.random = new Random();
		mRenderContext.context = mContext;

		ConnectionManager.getInstance().registerPacketHandler(
                PacketTypes.getInstance().getType(HelloPacket.class), mOnHelloPacket);

        FileTransferHandler.getInstance().init();

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		mContext.foData = new FalloutDataTest();

		createPostProcessingMesh();

		Assets.manager.load("postProcShader", ShaderProgram.class,
                new ShaderProgramLoader.ShaderProgramParameter("shaders/post.vert.glsl",
                        "shaders/post.frag.glsl"));
		Assets.manager.finishLoadingAsset("postProcShader");
        Texture.setAssetManager(Assets.manager);
		mRenderContext.postShaderProgram = Assets.manager.get("postProcShader");
		if(!mRenderContext.postShaderProgram.isCompiled()){
			Gdx.app.error("SHADER", "Could not load post processing shader: " +
					mRenderContext.postShaderProgram.getLog());
			Gdx.app.exit();
		}
	}

	@Override
	public void resume() {
		mPipboy.load();
		super.resume();
	}

    public void initRenderState() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);

    }

	public void update() {
		Assets.update();

		boolean connected = ConnectionManager.getInstance().getTCPStatus() ==
				ConnectionManager.TCPStatus.Connected;
		if(mConnected != connected) {
			mConnected = connected;
			if(mContext.foData != null)
				mContext.foData.dispose();
			if(connected) {
				mContext.foData = new FalloutDataManager();
			} else {
				mContext.foData = new FalloutDataTest();
			}
		}

		ConnectionManager.getInstance().dispatchPackets();
		mContext.foData.update(mContext);
		mPipboy.update(mContext);
        if(connected) {
            if(System.currentTimeMillis() - mLastKeepAliveTime > 3000){
                mLastKeepAliveTime = System.currentTimeMillis();
                ConnectionManager.getInstance().send(new HelloPacket("Real pipboy app",
                        (short)0x100));
            }
        }

		// Update sweep position
		float deltaTime = Gdx.graphics.getDeltaTime();
		mSweepPosition -= deltaTime * SWEEP_SPEED;
		if(mSweepPosition < -1.0f ) {
			mSweepPosition = 1.0f + 3.0f * mContext.random.nextFloat();
		}
	}

	@Override
	public void resize(int width, int height) {
		if(mRenderContext.fbo != null) {
			mRenderContext.fbo.dispose();
		}
		mRenderContext.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true, false);
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void render () {
		update();

        if(!mGLStateSet) {
            initRenderState();
            mGLStateSet = true;
        }

		mRenderContext.screenHeight = Gdx.graphics.getHeight();
		//mRenderContext.screenWidth = Gdx.graphics.getHeight()*1024/768.0f;
		mRenderContext.screenWidth = Gdx.graphics.getWidth();

		// Render in Frame buffer
		mRenderContext.fbo.begin();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mPipboy.render(mRenderContext);

		// apply post processing
		mRenderContext.fbo.end();

		// Draw the screen
		mRenderContext.postShaderProgram.begin();

		// Bind the textures
		mRenderContext.fbo.getColorBufferTexture().bind(0);

		mRenderContext.postShaderProgram.setUniformi("u_texture", 0);
		mRenderContext.postShaderProgram.setUniformf("u_pipboyColor",
                Settings.getInstance().getPipboyColor());
		mRenderContext.postShaderProgram.setUniformf("u_screenSize",
				new Vector2(mRenderContext.screenWidth, mRenderContext.screenHeight));
		mRenderContext.postShaderProgram.setUniformf("u_interlacePeriod", 0.01f);
		mRenderContext.postShaderProgram.setUniformf("u_interlaceAmplitude", 0.05f);
		mRenderContext.postShaderProgram.setUniformf("u_sweepPosition", mSweepPosition);
		mRenderContext.postShaderProgram.setUniformf("u_sweepLength", 0.07f);
		mRenderContext.postShaderProgram.setUniformf("u_sweepIntensity", 0.1f);


		mPostMesh.render(mRenderContext.postShaderProgram, GL20.GL_TRIANGLES, 0, 6);
		mRenderContext.postShaderProgram.end();

	}
}
