package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

/**
 * Created by s120330 on 8-7-2015.
 */
public class TextBox extends Control {

    private float mHorbuf = 20;
    private float mVerbuf = 20;
    private float mFixedWidth = 0;
    private boolean mGlow = false;
    private String mText = "Button Text";
    private int mAlign = Align.left;
    private int mFontIndex = 0;
    private boolean mBoxVisible = false; // default visible on mouseOver
    private boolean mVisible = true;
    private Color mColor = new Color(1,1,1,1);
    private int mBrightness = 2;

    private Box mBox = null;
    private Text mButtonText = null;

    public TextBox (float x, float y, String text) {
        super(x,y);
        mText = text;
        create();
    }

    public TextBox (float x, float y, String text, int align) {
        super(x,y);
        mText = text;
        mAlign = align;
        create();
    }

    public TextBox (float x, float y, String text, float horbuf, float verbuf) {
        super(x,y);
        mText = text;
        mHorbuf = horbuf;
        mVerbuf = verbuf;
        create();
    }

    public TextBox (float x, float y, String text, float fixedWidth, float horbuf, float verbuf) {
        super(x,y);
        mText = text;
        mHorbuf = horbuf;
        mVerbuf = verbuf;
        mFixedWidth = fixedWidth;
        create();
    }

    public TextBox (float x, float y, String text, float horbuf, float verbuf,
                    int align, Color color) {
        super(x,y);
        mText = text;
        mHorbuf = horbuf;
        mVerbuf = verbuf;
        mAlign = align;
        mColor = color;
        create();
    }

    public TextBox (float x, float y, String text, float fixedWidth, float horbuf, float verbuf,
                    int align, Color color) {
        super(x,y);
        mText = text;
        mFixedWidth = fixedWidth;
        mHorbuf = horbuf;
        mVerbuf = verbuf;
        mAlign = align;
        mColor = color;
        create();
    }

    public TextBox (float x, float y, String text, float horbuf, float verbuf,
                    int align, boolean glow, Color color) {
        super(x,y);
        mText = text;
        mHorbuf = horbuf;
        mVerbuf = verbuf;
        mAlign = align;
        mColor = color;
        mGlow = glow;
        create();
    }

    public TextBox (float x, float y, String text, float fixedWidth, float horbuf, float verbuf,
                    int align, boolean glow, Color color) {
        super(x,y);
        mText = text;
        mFixedWidth = fixedWidth;
        mHorbuf = horbuf;
        mVerbuf = verbuf;
        mAlign = align;
        mColor = color;
        mGlow = glow;
        create();
    }

    private void create() {
        if(mButtonText != null)
            removeChild(mButtonText);
        if(mBox != null)
            removeChild(mBox);

        // Determine the font
        int fontIndex = mFontIndex;
        if(fontIndex== 0)
        {
            fontIndex = mGlow ? 7 : 2;
        }

        mButtonText = new Text(0,mVerbuf / 2, mText, fontIndex,
                mFixedWidth == 0 ? 0 : mFixedWidth - mHorbuf,
                mFixedWidth == 0 ? Align.left : mAlign, mColor, mBrightness);

        float height = mButtonText.getSize().getHeight() + mVerbuf;
        float width;
        if(mFixedWidth != 0)
        {
            width = mFixedWidth;
        } else {
            width = mButtonText.getSize().getWidth() + mHorbuf;
        }

        float boxX = 0;
        if((mAlign & Align.center) != 0) {
            boxX -= width/2;
        } else if((mAlign & Align.right) != 0) {
            boxX -= width;
        }

        float textX = boxX + mHorbuf / 2;
        if(mGlow)
            textX -= 4;

        mBox = new Box(boxX, 0, width, height, mBoxVisible, mColor);
        mButtonText.setX(textX);

        addChild(mBox);
        addChild(mButtonText);
        setVisible(mVisible);
    }

    @Override
    public Rectangle getSize() {
        Rectangle rect = mBox.getSize();
        rect.setX(rect.getX() + mX);
        rect.setY(rect.getY() + mY);
        return new Rectangle(rect);
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
        mButtonText.setVisible(visible);
        setBoxVisible(mBoxVisible && mVisible);
    }

    public float getHorbuf() {
        return mHorbuf;
    }

    public void setHorbuf(float mHorbuf) {
        this.mHorbuf = mHorbuf;
        create();
    }

    public float getVerbuf() {
        return mVerbuf;
    }

    public void setVerbuf(float mVerbuf) {
        this.mVerbuf = mVerbuf;
        create();
    }

    public float getFixedWidth() {
        return mFixedWidth;
    }

    public void setFixedWidth(float mFixedWidth) {
        this.mFixedWidth = mFixedWidth;
        create();
    }

    public boolean isGlow() {
        return mGlow;
    }

    public void setGlow(boolean mGlow) {
        this.mGlow = mGlow;
        create();
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
        create();
    }

    public int getAlign() {
        return mAlign;
    }

    public void setAlign(int mAlign) {
        this.mAlign = mAlign;
        create();
    }

    public int getFontIndex() {
        return mFontIndex;
    }

    public void setFontIndex(int mFontIndex) {
        this.mFontIndex = mFontIndex;
        create();
    }

    public boolean isBoxVisible() {
        return mBoxVisible;
    }

    public void setBoxVisible(boolean mBoxVisible) {
        this.mBoxVisible = mBoxVisible;
        mBox.setVisible(mBoxVisible);
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color mColor) {
        this.mColor = mColor;
        mBox.setColor(mColor);
        mButtonText.setColor(mColor);
    }

    public int getBrightness() {
        return mBrightness;
    }

    public void setBrightness(int mBrightness) {
        this.mBrightness = mBrightness;
        mButtonText.setBrightness(mBrightness);
    }
}
