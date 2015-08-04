package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by s120330 on 29-7-2015.
 */
public class Rect extends Control {

    protected float mWidth, mHeight;

    public Rect(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }
}
