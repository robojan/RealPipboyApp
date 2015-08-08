package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.Constants;

/**
 * Created by s120330 on 7-7-2015.
 */
public class CardInfo extends Control {

    private float mWidth, mHeight;
    private String mTitle, mValue;
    private Color mColor;

    private Line mTopLine;
    private VerticalFadeLine mRightVert;
    private Text mTitleText;
    private Text mValueText;

    private boolean mVisible = true;

    public CardInfo(float x, float y, float width, float height, String title, String value,
                    Color color)
    {
        super(x,y);
        mWidth = width;
        mHeight = height;
        mTitle = title;
        mValue = value;
        mColor = color;

        create();
    }

    public CardInfo(float x, float y, float width, float height, String title, String value)
    {
        super(x,y);
        mWidth = width;
        mHeight = height;
        mTitle = title;
        mValue = value;
        mColor = new Color(1,1,1,1);

        create();
    }

    public CardInfo(float x, float y, float width, String title, String value)
    {
        super(x,y);
        mWidth = width;
        mHeight = 60;
        mTitle = title;
        mValue = value;
        mColor = new Color(1,1,1,1);

        create();
    }

    private void create()
    {
        mTopLine = new Line(0,0,mWidth);
        mRightVert = new VerticalFadeLine(mWidth - Constants.LINE_THICKNESS, 0, false, mHeight);
        mTitleText = new Text(5,20, mTitle, 2);

        float endX = mWidth - 10;
        float startX = mTitleText.getSize().width + 15;

        mValueText = new Text(startX, 20, mValue, 2, endX-startX, Align.right);

        addChild(mTopLine);
        addChild(mRightVert);
        addChild(mTitleText);
        addChild(mValueText);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(super.mX, mY, mWidth, mHeight);
    }

    public float getWidth() {
        return mWidth;
    }

    public void setWidth(float width) {
        this.mWidth = width;
        mTopLine.setLength(width);
        mRightVert.setX(width- Constants.LINE_THICKNESS);
        float endX = mWidth - 10;
        float startX = mTitleText.getSize().width + 15;
        mValueText.setTargetWidth(endX-startX);
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(float height) {
        this.mHeight = height;
        mRightVert.setHeight(height);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
        mTitleText.setText(title);
        float endX = mWidth - 10;
        float startX = mTitleText.getSize().width + 15;
        mValueText.setTargetWidth(endX - startX);
        mValueText.setX(startX);
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
        mValueText.setText(value);
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color color) {
        mColor = color;
        mTopLine.setColor(color);
        mRightVert.setColor(color);
        mTitleText.setColor(color);
        mValueText.setColor(color);
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
        mTopLine.setVisible(visible);
        mRightVert.setVisible(visible);
        mTitleText.setVisible(visible);
        mValueText.setVisible(visible);
    }

    public boolean getVisible() {
        return mVisible;
    }

}