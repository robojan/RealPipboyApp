package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.Assets;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.RenderContext;

/**
 * Created by s120330 on 6-7-2015.
 */
public class Line extends Control {

    private final static String TEXTURE_FILE = "textures/interface/shared/solid.dds";

    private Texture mTexture;

    private Color mColor;
    private boolean mClips;
    private int mBrightness;
    private boolean mHorizontal;
    private float mLength;

    private boolean mVisible = true;

    public Line(float x, float y, float length)
    {
        super(x, y);
        this.mColor = new Color(1,1,1,1);
        this.mClips = true;
        this.mBrightness = 2;
        this.mHorizontal = true;
        this.mLength = length;
    }

    public Line(float x, float y, float length, boolean horizontal, Color color, int brightness)
    {
        super(x, y);
        this.mColor = color;
        this.mClips = true;
        this.mBrightness = brightness;
        this.mHorizontal = horizontal;
        this.mLength = length;
    }

    @Override
    public void dispose() {
        super.dispose();
        if(isLoaded() && Assets.manager.isLoaded(TEXTURE_FILE))
            Assets.manager.unload(TEXTURE_FILE);
    }

    @Override
    public void load()
    {
        super.load();
        Assets.manager.load(TEXTURE_FILE, Texture.class);
    }

    @Override
    public void render(RenderContext context)
    {
        if(mTexture == null)
        {
            if(!Assets.manager.isLoaded(TEXTURE_FILE))
                return;
            mTexture = Assets.manager.get(TEXTURE_FILE, Texture.class);
            mTexture.setAssetManager(Assets.manager);
        }
        if(mVisible) {
            context.batch.setColor(mColor);
            context.batch.draw(mTexture, 0, Constants.PIPBOY_HEIGHT, getWidth(), getHeight());
        }

        super.render(context);
    }

    @Override
    public Rectangle getSize()
    {
        if(mHorizontal)
        {
            return new Rectangle(mX, mY, mLength, Constants.LINE_THICKNESS);
        } else {
            return new Rectangle(mX, mY, Constants.LINE_THICKNESS, mLength);
        }
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color Color) {
        this.mColor = Color;
    }

    public boolean isClips() {
        return mClips;
    }

    public void setClipping(boolean Clips) {
        this.mClips = Clips;
    }

    public int getBrightness() {
        return mBrightness;
    }

    public void setBrightness(int Brightness) {
        this.mBrightness = Brightness;
    }

    public boolean isHorizontal() {
        return mHorizontal;
    }

    public void setHorizontal(boolean Horizontal) {
        this.mHorizontal = Horizontal;
    }

    public float getLength() {
        return mLength;
    }

    public void setLength(float Length) {
        this.mLength = Length;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
    }
}
