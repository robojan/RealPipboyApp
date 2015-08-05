package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;

/**
 * Created by s120330 on 9-7-2015.
 */
public class ScrollbarVert extends Control {
    private final static String ARROW_UP = "textures/interface/shared/scrollbar/arrow_up.dds";
    private final static String ARROW_DOWN = "textures/interface/shared/scrollbar/arrow_down.dds";

    private float mWidth = 32;
    private float mHeight = 450;

    private int mNumberOfItems = 9;
    private int mNumberOfVisibleItems =1;

    private int mStepSize = 1;
    private int mJumpSize;

    private boolean mShowAlways = false;
    private boolean mHasArrows = true;

    private boolean mScaleMarkerImage = true;
    private float mMarkerWidth = 0;
    private float mMarkerHeight = 0;
    private String mMarkerImage = "textures/interface/shared/scrollbar/vert_marker.dds";
    private Color mMarkerColor = new Color(1,1,1,1);

    private int mWheelMoved = 0;

    private float mCurrentPercentage;
    private int mCurrentValue;

    private float mHeightWithoutCursors;

    private boolean mPanning = false;
    private float mDragOffset = 0;
    private float mDragY = 0;

    private Image mVertUp;
    private Image mVertDown;
    private Image mVertMarker;

    // false if scrollbar was altered by dragging, true if other method was used
    private boolean mDragFlag = true;

    public ScrollbarVert(float x, float y) {
        super(x, y);
        setNumberOfVisibleItems(5);
        create();
        createArrowListeners();
    }

    public ScrollbarVert(float x, float y, float height) {
        super(x, y);
        mHeight = height;
        setNumberOfVisibleItems(5);
        create();
        createArrowListeners();
    }

    public ScrollbarVert(float x, float y, float height, int numberOfItems,
                         int numberOfVisibleItems) {
        super(x, y);
        mHeight = height;
        mNumberOfItems = numberOfItems;
        setNumberOfVisibleItems(numberOfVisibleItems);
        create();
        createArrowListeners();
    }

    public void create(){
        clearChilds();
        int hasArrows = mHasArrows ? 1 : 0;
        mHeightWithoutCursors = mHeight/* - 2 * 32 * hasArrows*/;

        if(!mScaleMarkerImage) {
            mVertMarker = new Image(12, 0, mMarkerImage, mMarkerWidth, mMarkerHeight, mMarkerColor);
        }else {
            int divider = mNumberOfItems - mNumberOfVisibleItems + 1;
            if(divider > 32)
                divider = 32;
            mVertMarker = new Image(0, 0, mMarkerImage, 32, mHeightWithoutCursors/divider);
        }

        setMarkerPosition();

        mVertUp = new Image(0,0, ARROW_UP, 32 * hasArrows, 16 * hasArrows, mMarkerColor);
        mVertUp.setY(-mVertUp.getHeight());
        mVertDown = new Image(0, mHeightWithoutCursors, ARROW_DOWN, 32 * hasArrows, 16 * hasArrows,
                mMarkerColor);
        addChild(mVertMarker);
        addChild(mVertUp);
        addChild(mVertDown);


    }

    public void createArrowListeners(){
        ClickableListener listener = new ClickableListener() {
            @Override
            public void onClickableEvent(Control source, Object user) {
                if(!isEnabled())
                    return;
                if(source == mVertUp) {
                    setCurrentValueNoFlag(mCurrentValue - mStepSize);
                } else if (source == mVertDown) {
                    setCurrentValueNoFlag(mCurrentValue + mStepSize);
                }
                mDragFlag = false;
            }
        };
        mVertUp.addClickableListener(listener);
        mVertDown.addClickableListener(listener);
    }

    public void setCurrentValue(int value){
        setCurrentValueNoFlag(value);
        mDragFlag = false;
    }

    private void setCurrentValueNoFlag(int value) {
        mCurrentValue = value;
        int nonVisibleItems = mNumberOfItems - mNumberOfVisibleItems;
        if(nonVisibleItems <= 0)
            nonVisibleItems = 0;
        if(mCurrentValue >= nonVisibleItems)
            mCurrentValue = nonVisibleItems;
        if(mCurrentValue < 0)
            mCurrentValue = 0;
        if(nonVisibleItems != 0){
            mCurrentPercentage = ((float)mCurrentValue)/nonVisibleItems;
        } else {
            mCurrentPercentage = 1.0f;
        }
        Gdx.app.debug("VSCROLL", String.format("Val: %d(%f%%)", mCurrentValue,
                mCurrentPercentage * 100));
    }

    public void setMarkerPosition(){
        float markerY = 0;
        if(mNumberOfItems - mNumberOfVisibleItems > 0) {
            float stepHeight = (mHeightWithoutCursors - mVertMarker.getHeight()) /
                    (mNumberOfItems - mNumberOfVisibleItems);

            if (mDragFlag) {
                markerY += mDragOffset + mDragY;
            } else {
                markerY += mCurrentValue * stepHeight;
            }

            if (markerY < 0)
                markerY = 0;
            if (markerY > mHeightWithoutCursors - mVertMarker.getHeight())
                markerY = mHeightWithoutCursors - mVertMarker.getHeight();
        }
        mVertMarker.setY(markerY);
    }

    public void setNumberOfVisibleItems(int items){
        mNumberOfVisibleItems = items;
        mJumpSize = items - 1;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        mPanning = false;
        mDragOffset = mDragOffset + mDragY;
        mDragY = 0;
        return super.panStop(x, y, pointer, button);
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        boolean handled = super.pan(x, y, deltaX, deltaY);
        x -= mX;
        y -= mY;
        Rectangle marker = new Rectangle(0, mVertMarker.getTop(), mWidth, mVertMarker.getBottom());
        if(marker.contains(x,y) && !mPanning) {
            mPanning = true;
            mDragOffset = y;
        }
        if(mPanning){
            mDragY += deltaY;
            mDragFlag = true;
            Gdx.app.debug("VSCROLL", String.format("Pan dy:%f, dragOffset:%f, dragy:%f", deltaY,
                    mDragOffset, mDragY));
            int numInvisible = (mNumberOfItems - mNumberOfVisibleItems);
            if(numInvisible > 0) {
                float stepHeight = (mHeightWithoutCursors - mVertMarker.getHeight()) / numInvisible;
                int value = (int) ((mDragOffset + mDragY) / stepHeight);
                setCurrentValueNoFlag(value);
            } else {
                setCurrentValueNoFlag(0);
            }
            return true;
        }
        return handled;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        boolean handled = super.tap(x, y, count, button);
        if(!handled && getSize().contains(x, y)){
            // Check for area between the marker and arrows
            x -= mX;
            y -= mY;
            Rectangle pageUp = new Rectangle(0,mVertUp.getBottom(), mWidth, mVertMarker.getTop());
            Rectangle pageDown = new Rectangle(0, mVertMarker.getBottom(), mWidth,
                    mVertDown.getTop());
            if(pageUp.contains(x,y)){
                setCurrentValueNoFlag(mCurrentValue - mJumpSize);
            } else if(pageDown.contains(x,y)) {
                setCurrentValueNoFlag(mCurrentValue + mJumpSize);
            }
            mDragFlag = false;
            return true;
        }
        return handled;
    }

    @Override
    public void update(Context context) {
        setMarkerPosition();
        if(!isShowAlways()) {
            mVertMarker.setVisible(mNumberOfVisibleItems < mNumberOfItems);
            mVertUp.setVisible(mNumberOfVisibleItems < mNumberOfItems && mHasArrows);
            mVertDown.setVisible(mNumberOfVisibleItems < mNumberOfItems && mHasArrows);
        }
        super.update(context);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    public float getWidth() {
        return mWidth;
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(float mHeight) {
        this.mHeight = mHeight;
        create();
    }

    public int getNumberOfItems() {
        return mNumberOfItems;
    }

    public void setNumberOfItems(int mNumberOfItems) {
        this.mNumberOfItems = mNumberOfItems;
        if(mScaleMarkerImage) {
            int divider = mNumberOfItems - mNumberOfVisibleItems + 1;
            if (divider > 32)
                divider = 32;
            mVertMarker.setHeight(mHeightWithoutCursors/divider);
        }
    }

    public int getStepSize() {
        return mStepSize;
    }

    public void setStepSize(int mStepSize) {
        this.mStepSize = mStepSize;
    }

    public int getNumberOfVisibleItems() {
        return mNumberOfVisibleItems;
    }

    public int getJumpSize() {
        return mJumpSize;
    }

    public void setJumpSize(int mJumpSize) {
        this.mJumpSize = mJumpSize;
    }

    public boolean isShowAlways() {
        return mShowAlways;
    }

    public void setShowAlways(boolean mShowAlways) {
        this.mShowAlways = mShowAlways;
    }

    public boolean isHasArrows() {
        return mHasArrows;
    }

    public void setHasArrows(boolean mHasArrows) {
        this.mHasArrows = mHasArrows;
        create();
    }

    public boolean isScaleMarkerImage() {
        return mScaleMarkerImage;
    }

    public void setScaleMarkerImage(boolean mScaleMarkerImage) {
        this.mScaleMarkerImage = mScaleMarkerImage;
        create();
    }

    public float getMarkerWidth() {
        return mMarkerWidth;
    }

    public void setMarkerWidth(float mMarkerWidth) {
        this.mMarkerWidth = mMarkerWidth;
        if(!mScaleMarkerImage) {
            mVertMarker.setWidth(mMarkerWidth);
        }
    }

    public float getMarkerHeight() {
        return mMarkerHeight;
    }

    public void setMarkerHeight(float mMarkerHeight) {
        this.mMarkerHeight = mMarkerHeight;
        if(!mScaleMarkerImage) {
            mVertMarker.setHeight(mMarkerHeight);
        }
    }

    public String getMarkerImage() {
        return mMarkerImage;
    }

    public void setMarkerImage(String mMarkerImage) {
        this.mMarkerImage = mMarkerImage;
        mVertMarker.setFile(mMarkerImage);
    }

    public Color getMarkerColor() {
        return mMarkerColor;
    }

    public void setMarkerColor(Color mMarkerColor) {
        this.mMarkerColor = mMarkerColor;
        mVertMarker.setColor(mMarkerColor);
        mVertUp.setColor(mMarkerColor);
        mVertDown.setColor(mMarkerColor);
    }

    public float getCurrentPercentage() {
        return mCurrentPercentage;
    }

    public int getCurrentValue() {
        return mCurrentValue;
    }
}
