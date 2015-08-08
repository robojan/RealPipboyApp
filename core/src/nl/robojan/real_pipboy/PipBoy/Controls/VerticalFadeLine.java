package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.Assets;
import nl.robojan.real_pipboy.Constants;
import nl.robojan.real_pipboy.RenderContext;

/**
 * Created by s120330 on 7-7-2015.
 */
public class VerticalFadeLine extends Control {
    private static final String TEXTURE_TB_FILE = "textures/interface/shared/line/fade_to_bottom.dds";
    private static final String TEXTURE_BT_FILE = "textures/interface/shared/line/fade_to_top.dds";

    private Texture mTexture;
    private String mSelectedTexture;

    private boolean mBottomToTop;
    private int mBrightness;
    private float mHeight;
    private Color mColor;

    private boolean mVisible = true;

    public VerticalFadeLine(float x, float y) {
        super(x,y);
        this.mBottomToTop = false;
        this.mBrightness = 2;
        this.mHeight = 60;
        this.mColor = new Color(1,1,1,1);
    }

    public VerticalFadeLine(float x, float y, boolean bottomToTop)
    {
        super(x,y);
        this.mBottomToTop = bottomToTop;
        this.mBrightness = 2;
        this.mHeight = 60;
        this.mColor = new Color(1,1,1,1);
    }

    public VerticalFadeLine(float x, float y, boolean bottomToTop, float length)
    {
        super(x,y);
        this.mBottomToTop = bottomToTop;
        this.mBrightness = 2;
        this.mHeight = length;
        this.mColor = new Color(1,1,1,1);
    }

    @Override
    public void dispose() {
        super.dispose();
        if(isLoaded() && Assets.manager.isLoaded(mSelectedTexture))
            Assets.manager.unload(mSelectedTexture);
    }

    @Override
    public void load() {
        super.load();
        if(mBottomToTop) {
            mSelectedTexture = TEXTURE_BT_FILE;
        } else {
            mSelectedTexture = TEXTURE_TB_FILE;
        }
        Assets.manager.load(mSelectedTexture, Texture.class);
    }

    @Override
    public void render(RenderContext context) {
        if(mTexture == null)
        {
            if(!Assets.manager.isLoaded(mSelectedTexture)) {
                super.render(context);
                return;
            }
            mTexture = Assets.manager.get(mSelectedTexture, Texture.class);
        }

        if(mVisible) {
            float y = 0;
            if (mBottomToTop) {
                y -= mHeight;
            }
            context.batch.setColor(mColor);
            context.batch.draw(mTexture, 0, Constants.PIPBOY_HEIGHT - (y + mHeight),
                    Constants.LINE_THICKNESS, mHeight);
        }
        super.render(context);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mBottomToTop ? mY - mHeight : mY, Constants.LINE_THICKNESS,
                mHeight);
    }

    @Override
    public float getBottom() {
        return mBottomToTop ? mY : mY + mHeight;
    }

    @Override
    public float getTop() {
        return mBottomToTop ? mY - mHeight : mY;
    }

    public boolean isBottomToTop() {
        return mBottomToTop;
    }

    public void setBottomToTop(boolean BottomToTop) {
        dispose();
        this.mBottomToTop = BottomToTop;
        load();
    }

    public int getBrightness() {
        return mBrightness;
    }

    public void setBrightness(int mBrightness) {
        this.mBrightness = mBrightness;
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(float mHeight) {
        this.mHeight = mHeight;
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color mColor) {
        this.mColor = mColor;
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    public boolean getVisible() {
        return mVisible;
    }
}
