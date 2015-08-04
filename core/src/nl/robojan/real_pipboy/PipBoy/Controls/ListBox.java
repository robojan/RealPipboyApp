package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Collections;

import nl.robojan.real_pipboy.RenderContext;

/**
 * Created by s120330 on 9-7-2015.
 */
public class ListBox extends Control{

    private float mWidth = 0;
    private float mHeight = 0;
    private Color mLineColor = new Color(1,1,1,1);
    private Color mFillColor = new Color(1,1,1,0.16f);
    private float mItemOffset = 0;
    private int mNumberOfVisibleItems = 1;
    private int mScrollbarVis = -1;
    private boolean mRightScrollbar = false;
    private float mScrollDelta = 26;
    private float mItemsHeight = 0;
    private int mSelectedItem = -1;
    private boolean mSorted = false;

    private Box mHighlightBox;
    private ScrollbarVert mScrollbar;
    private ClippingRect mClippingBox;

    private float mYOffset = 0;

    private boolean mPanning = false;

    private ArrayList<ListBoxItem> mItems = new ArrayList<ListBoxItem>();

    public ListBox(float x, float y, float width) {
        super(x, y);
        mWidth = width;
        create();
    }

    public ListBox(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        create();
    }

    public ListBox(float x, float y, float width, float height, boolean rightScrollbar) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mRightScrollbar = rightScrollbar;
        create();
    }

    public ListBox(float x, float y, float width, float height, boolean rightScrollbar,
                   float itemOffset) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mRightScrollbar = rightScrollbar;
        mItemOffset = itemOffset;
        create();
    }

    private void create(){
        clearChilds();
        if(mClippingBox != null)
            mClippingBox.clearChilds();

        mScrollbar = new ScrollbarVert(0,16,mHeight-32, 1,mNumberOfVisibleItems);
        if(mRightScrollbar){
            mScrollbar.setX(mWidth - mScrollbar.getWidth());
        }
        mScrollbar.setJumpSize((int) (mHeight / mScrollDelta));
        mScrollbar.setMarkerColor(mLineColor);

        mClippingBox = new ClippingRect(0, 0, mWidth, mHeight);
        addChild(mClippingBox);

        mHighlightBox = new Box(0, 0, mWidth - mScrollbar.getWidth(),
                0 -1);
        mHighlightBox.setFillColor(mFillColor);
        float highLightLeft = 0;
        if(!mRightScrollbar)
        {
            highLightLeft += mItemOffset;
        }
        if(mScrollbarVis != 0 && !mRightScrollbar) {
            highLightLeft += mScrollbar.getRight();
        }
        mHighlightBox.setX(highLightLeft);
        mHighlightBox.setWidth(mWidth - mScrollbar.getWidth() - mHighlightBox.getX());
        mHighlightBox.setVisible(false);
        mClippingBox.addChild(mHighlightBox);

        if(mScrollbarVis != 0 ){
            addChild(mScrollbar);
        }
    }

    public void setWidth(float width) {
        mWidth = width;
        create();
        for(ListBoxItem item : mItems) {
            item.setWidth(width);
        }
    }

    private void updateNumVisibleItems() {
        int num = 0;
        float y = mYOffset;
        for(ListBoxItem item : mItems) {
            if(y >= 0 &&  y + item.getHeight() < mHeight) {
                num++;
            }
            y += item.getHeight();
        }
        setNumberOfVisibleItems(num);
    }

    public void addItem(ListBoxItem item) {
        ListBoxItem selectedItem = getSelectedItem();
        item.setX(mHighlightBox.getX());
        item.setWidth(mHighlightBox.getWidth());
        mItems.add(item);
        if(!item.isLoaded())
            item.load();

        if(mSorted) {
            Collections.sort(mItems);
        }

        mScrollbar.setNumberOfItems(mItems.size());
        mItemsHeight += item.getHeight();
        updateNumVisibleItems();
        highLightItem(selectedItem, true);
    }

    public void removeItem(ListBoxItem item) {
        ListBoxItem selectedItem = getSelectedItem();
        int i = mItems.lastIndexOf(item);
        if(i < 0) {
            return;
        }
        mItemsHeight -= item.getHeight();
        mItems.get(i).dispose();
        mItems.remove(i);

        if(mSorted) {
            Collections.sort(mItems);
        }

        updateNumVisibleItems();
        if(mItems.size() == 0) {
            mScrollbar.setNumberOfItems(1);
        } else {
            mScrollbar.setNumberOfItems(mItems.size());
        }
        highLightItem(selectedItem, true);
    }

    public void clearItems() {
        for(ListBoxItem item : mItems) {
            item.dispose();
        }
        mItems = new ArrayList<ListBoxItem>();
        mScrollbar.setNumberOfItems(1);
        updateNumVisibleItems();
        highLightItem(-1);
        mItemsHeight = 0;
    }

    public boolean contains(ListBoxItem item) {
        for(ListBoxItem own : mItems){
            if(item.isEquivalent(own))
                return true;
        }
        return false;
    }

    public void setNumberOfVisibleItems(int num) {
        if(num < 1)
            num = 1;
        mNumberOfVisibleItems = num;
        mScrollbar.setNumberOfVisibleItems(num);
    }

    public int getNumberOfVisibleItems() {
        return mNumberOfVisibleItems;
    }

    public void highLightItem(ListBoxItem item, boolean identity) {
        // Find corresponding index
        if(item == null) {
            highLightItem(-1);
        } else {
            int index = -1;
            if(identity) {
                for(ListBoxItem listItem : mItems) {
                    if(listItem == item) {
                        index = mItems.indexOf(listItem);
                    }
                }
            } else {
                for(ListBoxItem listItem : mItems) {
                    if(listItem.isEquivalent(item)) {
                        index = mItems.indexOf(listItem);
                    }
                }
            }
            highLightItem(index);
        }
    }

    public void highLightItem(int index) {
        // Find
        if(index >= 0 && index < mItems.size()) {
            mSelectedItem = index;
            mHighlightBox.setVisible(true);
        } else {
            mSelectedItem = -1;
            mHighlightBox.setVisible(false);
        }
    }

    public int getSelectedIndex() {
        return mSelectedItem;
    }

    public ListBoxItem getSelectedItem() {
        if(mSelectedItem < mItems.size() && mSelectedItem >= 0)
            return mItems.get(mSelectedItem);
        return null;
    }

    private void setOffsetFromScrollbar() {
        int selectedItem = mScrollbar.getCurrentValue();
        mYOffset = 0;
        for(int i = 0; i < selectedItem && i < mItems.size(); i++) {
            mYOffset -= mItems.get(i).getHeight();
        }
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        boolean handled = super.tap(x, y, count, button);

        x -= mX;
        y -= mY;

        if(!handled) {
            // Go through the items
            for(int i = 0; i < mItems.size(); i++) {
                ListBoxItem item = mItems.get(i);
                if(item.getTop() >= 0 - item.getHeight()/2 &&
                        item.getTop() <= mHeight - item.getHeight()/2) {
                    handled |= item.tap(x, y, count, button);
                    if (handled)
                        break;
                }
            }
        } else {
            setOffsetFromScrollbar();
        }

        return handled;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        boolean handled = super.pan(x, y, deltaX, deltaY);

        x -= mX;
        y -= mY;

        if(getSize().contains(x,y) && mEnabled) {
            if(!handled && mNumberOfVisibleItems < mItems.size()) {
                mPanning = true;
            }else {
                setOffsetFromScrollbar();
            }
        }
        if(mPanning) {
            mYOffset += deltaY;
            if(mYOffset < -mItemsHeight + mHeight) {
                mYOffset = -mItemsHeight + mHeight;
            } else if(mYOffset > 0) {
                mYOffset = 0;
            }

            int index = 0;
            for(float itemY = mYOffset; itemY < 0 && index < mItems.size(); index++) {
                ListBoxItem item = mItems.get(index);
                itemY += item.getHeight();
            }
            if(index >= mItems.size()) {
                index = mItems.size() -1;
            }
            mScrollbar.setCurrentValue(index);
            return true;
        }

        return handled;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        mPanning = false;
        return super.panStop(x, y, pointer, button);
    }

    @Override
    public void setEnabled(boolean enabled) {
        mScrollbar.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    public Color getLineColor() {
        return mLineColor;
    }

    public void setLineColor(Color lineColor) {
        mLineColor = lineColor;
        mScrollbar.setMarkerColor(mLineColor);
        mHighlightBox.setColor(lineColor);
    }

    public Color getFillColor() {
        return mFillColor;
    }

    public void setFillColor(Color fillColor) {
        mFillColor = fillColor;
        mHighlightBox.setFillColor(fillColor);
    }

    public boolean isSorted() {
        return mSorted;
    }

    public void setSorted(boolean sorted) {
        mSorted = sorted;
        if(sorted) {
            Collections.sort(mItems);
        }
    }

    private class ClippingRect extends Rect {
        public ClippingRect(float x, float y, float width, float height) {
            super(x, y, width, height);
            setClipping(true);
        }

        @Override
        public void render(RenderContext context) {
            float y = mYOffset;
            for(int i = 0; i < mItems.size(); i++) {
                ListBoxItem item = mItems.get(i);
                item.setY(y);
                y += item.getHeight();
                if(y > mY && y - item.getHeight() < mY + mHeight) {
                    item.render(context);
                }
                if(i == mSelectedItem) {
                    mHighlightBox.setY(y - item.getHeight());
                    mHighlightBox.setHeight(item.getHeight());
                }
            }
            super.render(context);
        }
    }
}

