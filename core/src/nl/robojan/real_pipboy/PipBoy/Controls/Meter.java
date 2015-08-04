package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

/**
 * Created by s120330 on 9-7-2015.
 */
public class Meter extends Control {
    private final static String SOLID = "textures/interface/shared/solid.dds";
    private final static String HUD_TICK_MARK = "textures/interface/shared/solid.dds";

    private float mValue = 0.5f;
    private boolean mSolidMeter = false;
    private boolean mShowBackground = false;

    private boolean mVisible = true;

    private float mHeight = 24;
    private float mWidth = 600;
    private Color mColor = new Color(1,1,1,1);
    private int mAlign = Align.left;

    private Image mMeterBackground;
    private Image mMeterImage;

    public Meter(float x, float y) {
        super(x, y);
        create();
    }

    public Meter(float x, float y, boolean solidMeter, boolean showBackground) {
        super(x, y);
        mSolidMeter = solidMeter;
        mShowBackground = showBackground;
        create();
    }

    public Meter(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        create();
    }

    public Meter(float x, float y, float width, float height, int align) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mAlign = align;
        create();
    }

    public Meter(float x, float y, float width, float height, boolean solidMeter,
                 boolean showBackground) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mSolidMeter = solidMeter;
        mShowBackground = showBackground;
        create();
    }

    public Meter(float x, float y, float width, float height, int align, boolean solidMeter,
                 boolean showBackground) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mSolidMeter = solidMeter;
        mShowBackground = showBackground;
        mAlign = align;
        create();
    }

    private void create(){
        clearChilds();

        float x = 0;
        if((mAlign & Align.center) != 0)
        {
            x -= mWidth/2;
        } else if((mAlign & Align.right) != 0) {
            x -= mWidth;
        }
        // Background
        Color backgroundColor = new Color(mColor);
        backgroundColor.a = backgroundColor.a/4;
        mMeterBackground = new Image(x, 0, SOLID, mWidth, mHeight, backgroundColor);
        if(mShowBackground) {
            addChild(mMeterBackground);
        }

        // Meter image
        mMeterImage = new Image(x, 0, mSolidMeter ? SOLID : HUD_TICK_MARK, mWidth * mValue,
                mHeight, mColor);
        mMeterImage.setTile(!mSolidMeter);
        addChild(mMeterImage);
    }


    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    public int getAlign() {
        return mAlign;
    }

    public void setAlign(int align) {
        this.mAlign = align;
        float x = 0;
        if((mAlign & Align.center) != 0)
        {
            x -= mWidth/2;
        } else if((mAlign & Align.right) != 0) {
            x -= mWidth;
        }
        mMeterBackground.setX(x);
        mMeterImage.setX(x);
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float value) {
        this.mValue = value;
        mMeterImage.setWidth(mWidth*value);
    }

    public boolean isSolidMeter() {
        return mSolidMeter;
    }

    public void setSolidMeter(boolean solidMeter) {
        this.mSolidMeter = solidMeter;
        create();
    }

    public boolean isShowBackground() {
        return mShowBackground;
    }

    public void setShowBackground(boolean showBackground) {
        if(showBackground == mShowBackground)
            return;
        this.mShowBackground = showBackground;
        if(showBackground){
            addChild(mMeterBackground);
        } else {
            removeChild(mMeterBackground);
        }
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(float height) {
        this.mHeight = height;
    }

    public float getWidth() {
        return mWidth;
    }

    public void setWidth(float width) {
        this.mWidth = width;
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color color) {
        this.mColor = color;

        mMeterImage.setColor(color);
        Color backgroundColor = new Color(mColor);
        backgroundColor.a = backgroundColor.a/4;
        mMeterBackground.setColor(color);
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
        mMeterBackground.setVisible(visible);
        mMeterImage.setVisible(visible);
    }

    public boolean getVisible() {
        return mVisible;
    }
}
