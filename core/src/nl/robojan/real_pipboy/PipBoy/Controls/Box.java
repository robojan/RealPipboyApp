package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.Assets;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.RenderContext;

/**
 * Created by s120330 on 7-7-2015.
 */
public class Box extends Control {

    private final static String TEXTURE_FILE = "textures/interface/shared/solid.dds";

    private Texture mTexture;

    private float mWidth;
    private float mHeight;
    private boolean mTopVisible = true;
    private boolean mBottomVisible = true;
    private boolean mLeftVisible = true;
    private boolean mRightVisible = true;
    private Color mColor = new Color(1,1,1,1);
    private Color mFillColor = new Color(1,1,1,0);
    private float mBrightness  = Constants.BACKGROUND_FILL_BRIGHTNESS;

    public Box(float x, float y, float width, float height)
    {
        super(x, y);
        mWidth = width;
        mHeight = height;
    }

    public Box(float x, float y, float width, float height, boolean visible,
               Color color)
    {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mTopVisible = visible;
        mBottomVisible = visible;
        mLeftVisible = visible;
        mRightVisible = visible;
        mColor = color;
    }

    public Box(float x, float y, float width, float height, boolean topVisible,
               boolean bottomVisible, boolean leftVisisble, boolean rightVisible,
               Color color, int brightness)
    {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mTopVisible = topVisible;
        mBottomVisible = bottomVisible;
        mLeftVisible = leftVisisble;
        mRightVisible = rightVisible;
        mColor = color;
        mBrightness = brightness;
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    @Override
    public void render(RenderContext context) {
        if(mTexture == null)
        {
            if(!Assets.manager.isLoaded(TEXTURE_FILE))
                return;
            mTexture = Assets.manager.get(TEXTURE_FILE, Texture.class);
            mTexture.setAssetManager(Assets.manager);
        }

        if(mBottomVisible || mTopVisible || mLeftVisible || mRightVisible) {
            context.batch.setColor(mFillColor);
            context.batch.draw(mTexture, 0, Constants.PIPBOY_HEIGHT - mHeight, mWidth, mHeight);
        }

        context.batch.setColor(mColor);
        // Top
        if(mTopVisible)
        {
            context.batch.draw(mTexture, 0, Constants.PIPBOY_HEIGHT - Constants.LINE_THICKNESS,
                    mWidth, Constants.LINE_THICKNESS);
        }

        // Left
        if(mLeftVisible)
        {
            context.batch.draw(mTexture, 0,
                    Constants.PIPBOY_HEIGHT - mHeight + Constants.LINE_THICKNESS,
                    Constants.LINE_THICKNESS, mHeight - 2 * Constants.LINE_THICKNESS);
        }

        // Bottom
        if(mBottomVisible)
        {
            context.batch.draw(mTexture, 0,
                    Constants.PIPBOY_HEIGHT - mHeight,
                    mWidth, Constants.LINE_THICKNESS);
        }

        // Right
        if(mRightVisible)
        {
            context.batch.draw(mTexture, mWidth - Constants.LINE_THICKNESS,
                    Constants.PIPBOY_HEIGHT - mHeight + Constants.LINE_THICKNESS,
                    Constants.LINE_THICKNESS, mHeight - 2 * Constants.LINE_THICKNESS);
        }

        super.render(context);
    }

    @Override
    public void load() {
        super.load();
        Assets.manager.load(TEXTURE_FILE, Texture.class);
    }

    @Override
    public void dispose() {
        super.dispose();
        if(isLoaded() && Assets.manager.isLoaded(TEXTURE_FILE))
            Assets.manager.unload(TEXTURE_FILE);
    }

    public float getWidth() {
        return mWidth;
    }

    public void setWidth(float width) {
        this.mWidth = width;
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(float height) {
        this.mHeight = height;
    }

    public boolean isTopVisible() {
        return mTopVisible;
    }

    public void setTopVisible(boolean topVisible) {
        this.mTopVisible = topVisible;
    }

    public boolean isBottomVisible() {
        return mBottomVisible;
    }

    public void setBottomVisible(boolean bottomVisible) {
        this.mBottomVisible = bottomVisible;
    }

    public boolean isLeftVisible() {
        return mLeftVisible;
    }

    public void setLeftVisible(boolean leftVisible) {
        this.mLeftVisible = leftVisible;
    }

    public boolean isRightVisible() {
        return mRightVisible;
    }

    public void setRightVisible(boolean rightVisible) {
        this.mRightVisible = rightVisible;
    }

    public boolean isVisible() {
        return mLeftVisible || mRightVisible || mTopVisible || mBottomVisible;
    }

    public void setVisible(boolean visible)
    {
        mLeftVisible = visible;
        mRightVisible = visible;
        mTopVisible = visible;
        mBottomVisible = visible;
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color color) {
        this.mColor = color;
    }

    public Color getFillColor() {
        return mFillColor;
    }

    public void setFillColor(Color color) {
        this.mFillColor = color;
    }
}