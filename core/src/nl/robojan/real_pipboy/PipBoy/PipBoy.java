package nl.robojan.real_pipboy.PipBoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import org.w3c.dom.css.Rect;

import nl.robojan.real_pipboy.Assets;
import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Data.DataMenu;
import nl.robojan.real_pipboy.PipBoy.Items.InventoryMenu;
import nl.robojan.real_pipboy.PipBoy.Options.OptionsMenu;
import nl.robojan.real_pipboy.PipBoy.Status.StatusMenu;
import nl.robojan.real_pipboy.RenderContext;
import nl.robojan.real_pipboy.util.ShaderProgramLoader;

/**
 * Created by s120330 on 5-7-2015.
 */
public class PipBoy implements Disposable, InputProcessor, GestureListener{

    private static final String TEXTURE_BACKGROUND = "textures/interface/shared/background/pipboy.dds";
    private static final String VERTEX_SHADER = "shaders/pipboy.vert.glsl";
    private static final String FRAGMENT_SHADER = "shaders/pipboy.frag.glsl";

    private static final float DISPLAY_WIDTH = 950;
    private static final float DISPLAY_HEIGHT = 700;

    private Control mPages[];
    private int mCurrentPage = 0;

    private Texture mTexBackground;
    private Matrix4 mProjectionMatrix;
    private InputMultiplexer mInputMultiplexer;

    public PipBoy() {
        mInputMultiplexer = new InputMultiplexer();
        GestureDetector gd = new GestureDetector(this);
        mInputMultiplexer.addProcessor(gd);
        mInputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(mInputMultiplexer);

        createProjectionMatrix(DISPLAY_WIDTH, DISPLAY_HEIGHT,
                0, -(Constants.PIPBOY_HEIGHT - DISPLAY_HEIGHT));

        // Create the menus
        mPages = new Control[4];
        mPages[0] = new StatusMenu();
        mPages[1] = new InventoryMenu();
        mPages[2] = new DataMenu();
        mPages[3] = new OptionsMenu();
        mCurrentPage = 3;

        load();
    }

    public Rectangle getSize() {
        return new Rectangle(0,0,DISPLAY_WIDTH, DISPLAY_HEIGHT);
    }

    @Override
    public void dispose()
    {
        for(Control page : mPages) {
            page.dispose();
        }
        mTexBackground.dispose();
    }

    private void createProjectionMatrix(float width, float height, float xOffset, float yOffset)
    {
        float xScale = 2.0f/width;
        float yScale = 2.0f/height;
        float xTranslate = xOffset*xScale;
        float yTranslate = yOffset*yScale;
        mProjectionMatrix = new Matrix4(new float[]{
                xScale, 0, 0, 0,
                0, yScale, 0, 0,
                0, 0, 1, 0,
                -1+xTranslate, -1+yTranslate, -1, 1
        });
    }

    public void load()
    {
        Assets.manager.load(TEXTURE_BACKGROUND, Texture.class);
        Assets.manager.load("PipboyShader", ShaderProgram.class,
                new ShaderProgramLoader.ShaderProgramParameter(VERTEX_SHADER, FRAGMENT_SHADER));
        for(Control page : mPages) {
            page.load();
        }
        Assets.manager.finishLoadingAsset("PipboyShader");
    }

    public void update(Context context) {
        getSelectedMenu().update(context);
    }

    public void render(RenderContext context)
    {
        if(context.pipboyShaderProgram == null) {
            context.pipboyShaderProgram = Assets.manager.get("PipboyShader");
            if(!context.pipboyShaderProgram.isCompiled()){
                Gdx.app.error("SHADER", "Error compiling shader: " +
                        context.pipboyShaderProgram.getLog());
                Gdx.app.exit();
            }
        }
        if(mTexBackground == null && Assets.manager.isLoaded(TEXTURE_BACKGROUND))
        {
            mTexBackground= Assets.manager.get(TEXTURE_BACKGROUND, Texture.class);
        }

        context.batch.begin();
        context.batch.setProjectionMatrix(mProjectionMatrix);
        context.batch.setShader(context.pipboyShaderProgram);
        context.pipboyShaderProgram.begin();
        context.pipboyShaderProgram.setUniformf("u_clippingTL", 0, context.screenHeight);
        context.pipboyShaderProgram.setUniformf("u_clippingBR", context.screenWidth, 0);
        context.clippingRectangles.clear();

        // Draw background
        if(mTexBackground != null) {
            context.batch.setColor(1,1,1,1);
            context.batch.draw(mTexBackground, 0, 0, Constants.PIPBOY_WIDTH, Constants.PIPBOY_HEIGHT);
        }

        // Draw pipboy
        getSelectedMenu().render(context);

        context.batch.end();
        if(context.clippingRectangles.size() > 0) {
            Gdx.app.error("RENDER", "ClippingRectangleStack is not empty!");
        }
    }

    public Control getSelectedMenu()
    {
        return mPages[mCurrentPage];
    }

    private Vector2 mapInputToPipboy(Vector2 vec) {
        return mapInputToPipboy(vec.x, vec.y);
    }

    private Vector2 mapInputToPipboy(float x, float y) {
        x = x/Gdx.graphics.getWidth() * DISPLAY_WIDTH;
        y = y/Gdx.graphics.getHeight() * DISPLAY_HEIGHT;
        return new Vector2(x,y);
    }

    private Vector2 mapInputVelocityToPipboy(Vector2 vec) {
        return mapInputVelocityToPipboy(vec.x, vec.y);
    }

    private Vector2 mapInputVelocityToPipboy(float x, float y) {
        x = x/Gdx.graphics.getWidth() * DISPLAY_WIDTH;
        y = y/Gdx.graphics.getHeight() * DISPLAY_HEIGHT;
        return new Vector2(x,y);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 pos = mapInputToPipboy(x, y);
        //x *= Constants.PIPBOY_WIDTH / Gdx.graphics.getWidth();
        //y *= Constants.PIPBOY_HEIGHT / Gdx.graphics.getHeight();
        Gdx.app.debug("EVT", String.format("TouchDown event: x:%f, y:%f, pointer:%d, button:%d",
                pos.x, pos.y, pointer, button));
        return getSelectedMenu().touchDown(pos.x, pos.y, pointer, button);
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector2 pos = mapInputToPipboy(x, y);
        //x *= Constants.PIPBOY_WIDTH / Gdx.graphics.getWidth();
        //y *= Constants.PIPBOY_HEIGHT / Gdx.graphics.getHeight();
        Gdx.app.debug("EVT", String.format("Tap event: x:%f, y:%f, count:%d, button:%d",
                pos.x, pos.y, count, button));
        return getSelectedMenu().tap(pos.x, pos.y, count, button);
    }

    @Override
    public boolean longPress(float x, float y) {
        Vector2 pos = mapInputToPipboy(x, y);
        //x *= Constants.PIPBOY_WIDTH / Gdx.graphics.getWidth();
        //y *= Constants.PIPBOY_HEIGHT / Gdx.graphics.getHeight();
        Gdx.app.debug("EVT", String.format("LongPress event: x:%f, y:%f", pos.x, pos.y));
        return getSelectedMenu().longPress(pos.x, pos.y);
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Vector2 vel = mapInputVelocityToPipboy(velocityX, velocityY);
        velocityX = vel.x;
        velocityY = vel.y;
        Gdx.app.debug("EVT", String.format("Fling event: vX:%f, vY:%f,  button:%d", velocityX,
                velocityY, button));

        float len = vel.len();
        vel = vel.nor();
        float angle = vel.angle();

        if(Math.abs(len) > Gdx.graphics.getWidth()/2) {
            if (angle < 45 || angle > 315) {
                // Right fling
                mCurrentPage--;
            } else if (angle > 135 && angle < 225) {
                // Left fling
                mCurrentPage++;
            }
            if (mCurrentPage >= mPages.length)
                mCurrentPage = mPages.length - 1;
            if (mCurrentPage < 0)
                mCurrentPage = 0;
        }

        return getSelectedMenu().fling(velocityX, velocityY, button);
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector2 pos = mapInputToPipboy(x, y);
        x = pos.x;
        y = pos.y;
        Vector2 delta = mapInputVelocityToPipboy(deltaX, deltaY);
        deltaX = delta.x;
        deltaY = delta.y;
        Gdx.app.debug("EVT", String.format("Pan event: x:%f, y:%f, dX:%f, dY:%f", x, y,
                deltaX, deltaY));
        return getSelectedMenu().pan(x, y, deltaX, deltaY);
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        Vector2 pos = mapInputToPipboy(x, y);
        x = pos.x;
        y = pos.y;
        Gdx.app.debug("EVT", String.format("PanStop event: x:%f, y:%f, pointer:%d, button:%d", x, y,
                pointer, button));
        return getSelectedMenu().panStop(x, y, pointer, button);
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        Gdx.app.debug("EVT", String.format("Zoom event: initialDistance:%f, distance:%f",
                initialDistance, distance));
        return getSelectedMenu().zoom(initialDistance, distance);
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1,
                         Vector2 pointer2) {
        Vector2 ip1 = mapInputToPipboy(initialPointer1);
        Vector2 ip2 = mapInputToPipboy(initialPointer2);
        Vector2 p1 = mapInputToPipboy(pointer1);
        Vector2 p2 = mapInputToPipboy(pointer2);
        Gdx.app.debug("EVT", String.format("Pinch event: IP1:%s, IP2:%s, P1:%s, P2:%s",
                ip1.toString(), ip2.toString(), p1.toString(),
                p2.toString()));
        return getSelectedMenu().pinch(ip1, ip2, p1, p2);
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.debug("EVT", String.format("KeyDown event: char:'%d'", keycode));
        return getSelectedMenu().keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        Gdx.app.debug("EVT", String.format("KeyUp event: char:'%d'", keycode));
        return getSelectedMenu().keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        Gdx.app.debug("EVT", String.format("KeyTyped event: char:'%c'", character));
        return getSelectedMenu().keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 pos = mapInputToPipboy(screenX, screenY);
        screenX = (int)pos.x;
        screenY = (int)pos.y;
        Gdx.app.debug("EVT", String.format("TouchDown(int) event: x:%d, y:%d, pointer:%d, button:%d",
                screenX, screenY, pointer, button));
        return getSelectedMenu().touchDown(screenX,screenY, pointer,button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 pos = mapInputToPipboy(screenX, screenY);
        screenX = (int)pos.x;
        screenY = (int)pos.y;
        Gdx.app.debug("EVT", String.format("TouchUp event: x:%d, y:%d, pointer:%d, button:%d",
                screenX, screenY, pointer, button));
        return getSelectedMenu().touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 pos = mapInputToPipboy(screenX, screenY);
        screenX = (int)pos.x;
        screenY = (int)pos.y;
        Gdx.app.debug("EVT", String.format("TouchDragged event: x:%d, y:%d, pointer:%d", screenX,
                screenY, pointer));
        return getSelectedMenu().touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector2 pos = mapInputToPipboy(screenX, screenY);
        screenX = (int)pos.x;
        screenY = (int)pos.y;
        Gdx.app.debug("EVT", String.format("MouseMoved event: screenX:%d ScreenY:%d", screenX,
                screenY));
        return getSelectedMenu().mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        Gdx.app.debug("EVT", String.format("Scroll event: amount:%d", amount));
        return getSelectedMenu().scrolled(amount);
    }
}

