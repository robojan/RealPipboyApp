package nl.robojan.real_pipboy.PipBoy.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import nl.robojan.real_pipboy.Assets;
import nl.robojan.real_pipboy.Constants;
import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.Rect;
import nl.robojan.real_pipboy.RenderContext;

/**
 * Created by s120330 on 2-8-2015.
 */
public class WaveformRect extends Rect {
    private static final String HMETERIMAGE = "textures/interface/shared/meter/tickmark_horiz.dds";
    private static final String VMETERIMAGE = "textures/interface/shared/meter/tickmark_vert.dds";
    private static final String SOLIDIMAGE = "textures/interface/shared/solid.dds";
    private static final String WAVEIMAGE = "textures/interface/radio wave/wave.dds";
    private static final String LEFTIMAGE = "textures/interface/radio wave/left.dds";
    private static final String FORWARDIMAGE = "textures/interface/radio wave/forward.dds";
    private static final String BACKWARDIMAGE = "textures/interface/radio wave/backward.dds";

    private static final float WAVESPEED = 4;

    private Image mMeterHoriz;
    private Image mMeterVert;
    private Image mBaseLine;
    private Texture[] mTextures = new Texture[4];
    private int mCurrentTexture = 0;
    private long mNextChange = 0;
    private float mWaveOffset = 0;

    private boolean mPlaying = false;

    public WaveformRect(float x, float y, float width, float height) {
        super(x, y, width, height);

        mMeterHoriz = new Image(0, height - 30, HMETERIMAGE, width, 32);
        addChild(mMeterHoriz);
        mMeterVert = new Image(width - 30, 0, VMETERIMAGE, 32, height);
        addChild(mMeterVert);
        mBaseLine = new Image(0, (height - 2) / 2 - 8, SOLIDIMAGE, width - 5, 2);
        addChild(mBaseLine);
    }

    public boolean isPlaying() {
        return mPlaying;
    }

    public void setPlaying(boolean playing) {
        mPlaying = playing;
    }

    @Override
    public void dispose() {
        super.dispose();
        if(isLoaded()) {
            if(Assets.manager.isLoaded(WAVEIMAGE))
                Assets.manager.unload(WAVEIMAGE);
            if(Assets.manager.isLoaded(LEFTIMAGE))
                Assets.manager.unload(LEFTIMAGE);
            if(Assets.manager.isLoaded(FORWARDIMAGE))
                Assets.manager.unload(FORWARDIMAGE);
            if(Assets.manager.isLoaded(BACKWARDIMAGE))
                Assets.manager.unload(BACKWARDIMAGE);
        }
    }

    @Override
    public void load() {
        super.load();

        Assets.manager.load(WAVEIMAGE, Texture.class);
        Assets.manager.load(LEFTIMAGE, Texture.class);
        Assets.manager.load(BACKWARDIMAGE, Texture.class);
        Assets.manager.load(FORWARDIMAGE, Texture.class);
    }

    @Override
    public void render(RenderContext context) {
        if(mTextures[0] == null && Assets.manager.isLoaded(WAVEIMAGE)) {
            mTextures[0] = Assets.manager.get(WAVEIMAGE);
        }
        if(mTextures[1] == null && Assets.manager.isLoaded(LEFTIMAGE)) {
            mTextures[1] = Assets.manager.get(LEFTIMAGE);
        }
        if(mTextures[2] == null && Assets.manager.isLoaded(FORWARDIMAGE)) {
            mTextures[2] = Assets.manager.get(FORWARDIMAGE);
        }
        if(mTextures[3] == null && Assets.manager.isLoaded(BACKWARDIMAGE)) {
            mTextures[3] = Assets.manager.get(BACKWARDIMAGE);
        }
        if(mTextures[0] == null || mTextures[1] == null || mTextures[2] == null ||
                mTextures[3] == null) {
            super.render(context);
            return;
        }

        mWaveOffset += Gdx.graphics.getDeltaTime() * WAVESPEED;
        if(mWaveOffset > 1) {
            mWaveOffset -= 1;
        }

        if(mPlaying) {
            float texWidth = 1;
            if(mCurrentTexture == 0) {
                texWidth = 0.25f;
            }

            mTextures[mCurrentTexture].setWrap(Texture.TextureWrap.Repeat,
                    Texture.TextureWrap.Repeat);
            context.batch.draw(mTextures[mCurrentTexture], 0, Constants.PIPBOY_HEIGHT - mHeight,
                    mWidth, mHeight, mWaveOffset + 0, 1, mWaveOffset + texWidth, 0);
        }

        super.render(context);
    }

    @Override
    public void update(Context context) {
        super.update(context);
        if(System.currentTimeMillis() > mNextChange) {
            mNextChange = System.currentTimeMillis() + 2000 + context.random.nextInt(8000);
            mCurrentTexture = context.random.nextInt(4);
        }
        mBaseLine.setVisible(!isPlaying());
    }
}
