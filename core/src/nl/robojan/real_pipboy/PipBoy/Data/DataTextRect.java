package nl.robojan.real_pipboy.PipBoy.Data;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Rect;
import nl.robojan.real_pipboy.PipBoy.Controls.ScrollbarVert;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;

/**
 * Created by s120330 on 28-7-2015.
 */
public class DataTextRect extends Control {
    private float mWidth;
    private float mHeight;

    private ScrollbarVert mTextScrollbar;
    private Text mDataText;
    private Rect mClippingRect;

    private boolean mPanning;

    public DataTextRect(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;

        mClippingRect = new Rect(0,0, width, height);
        mClippingRect.setClipping(true);
        mDataText = new Text(0, 0, "", 1, width, Align.left);
        mClippingRect.addChild(mDataText);
        addChild(mClippingRect);

        mTextScrollbar = new ScrollbarVert(width, 0, height, 0, (int)height);
        mTextScrollbar.setStepSize(20);
        addChild(mTextScrollbar);

    }

    public void setText(String text) {
        mDataText.setText(text);
        mTextScrollbar.setNumberOfItems((int)mDataText.getHeight());
        mTextScrollbar.setCurrentValue(0);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    @Override
    public void update(Context context) {
        mDataText.setY(-mTextScrollbar.getCurrentValue());

        super.update(context);
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        boolean handled = super.pan(x, y, deltaX, deltaY);

        x -= mX;
        y -= mY;

        if(!handled && getSize().contains(x,y) && mEnabled) {
            mPanning = true;
        }
        if(mPanning) {
            mTextScrollbar.setCurrentValue((int)(mTextScrollbar.getCurrentValue() - deltaY));
            return true;
        }

        return handled;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        mPanning = false;
        return super.panStop(x, y, pointer, button);
    }
}
