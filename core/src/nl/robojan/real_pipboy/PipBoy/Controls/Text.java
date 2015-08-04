package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.Assets;
import nl.robojan.real_pipboy.FalloutFont.FalloutFont;
import nl.robojan.real_pipboy.FalloutFont.FalloutGlyphLayout;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.RenderContext;

/**
 * Created by s120330 on 7-7-2015.
 */
public class Text extends Control {
    private int mFontIndex;

    private FalloutFont mFont;
    private String mText;
    private FalloutGlyphLayout mLayout;
    private float mTargetWidth = 0;
    private boolean mWrap = false;
    private int mAlign = Align.left;
    private Color mColor = new Color(1,1,1,1);
    private int mBrightness = 2;
    private boolean mVisible = true;

    public Text(float x, float y, String text) {
        super(x, y);
        mText = text;
        setFontIndex(2);
    }

    public Text(float x, float y, String text, int fontIndex) {
        super(x, y);
        mText = text;
        setFontIndex(fontIndex);
    }

    public Text(float x, float y, String text, int fontIndex, float targetWidth, int align) {
        super(x, y);
        mText = text;
        mAlign = align;
        mTargetWidth = targetWidth;
        mWrap = targetWidth != 0;
        setFontIndex(fontIndex);
    }

    public Text(float x, float y, String text, int align, Color color) {
        super(x, y);
        mText = text;
        mAlign = align;
        mColor = color;
        setFontIndex(2);
    }

    public Text(float x, float y, String text, int fontIndex, float targetWidth, int align,
                Color color, int brightness) {
        super(x, y);
        mText = text;
        mTargetWidth = targetWidth;
        mWrap = targetWidth != 0;
        mAlign = align;
        mColor = color;
        mBrightness = brightness;
        setFontIndex(fontIndex);
    }

    @Override
    public void render(RenderContext context) {
        if(mVisible) {
            context.batch.setColor(mColor);
            mFont.draw(context.batch, mLayout, 0, Constants.PIPBOY_HEIGHT + mFont.getAscent());
        }

        super.render(context);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mLayout.width, mLayout.height);
    }

    private void calculateLayout() {
        mLayout = new FalloutGlyphLayout(mFont.getData(), mText, mTargetWidth, mAlign, mWrap, 1,1);
    }

    public int getFontIndex() {
        return mFontIndex;
    }

    public void setFontIndex(int mFontIndex) {
        this.mFontIndex = mFontIndex;
        this.mFont = Assets.getFont(mFontIndex);
        calculateLayout();
    }

    public FalloutFont getFont() {
        return mFont;
    }

    public int getBrightness() {
        return mBrightness;
    }

    public void setBrightness(int mBrightness) {
        this.mBrightness = mBrightness;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        if(this.mText.equals(mText))
            return;
        this.mText = mText;
        calculateLayout();
    }

    public float getTargetWidth() {
        return mTargetWidth;
    }

    public void setTargetWidth(float mTargetWidth) {
        this.mTargetWidth = mTargetWidth;
        calculateLayout();
    }

    public boolean isWrap() {
        return mWrap;
    }

    public void setWrap(boolean mWrap) {
        this.mWrap = mWrap;
        calculateLayout();
    }

    public int getAlign() {
        return mAlign;
    }

    public void setAlign(int mAlign) {
        this.mAlign = mAlign;
        calculateLayout();
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color mColor) {
        this.mColor = mColor;
    }

    public boolean isVisible(){
        return mVisible;
    }

    public void setVisible(boolean visible){
        this.mVisible = visible;
    }
}
