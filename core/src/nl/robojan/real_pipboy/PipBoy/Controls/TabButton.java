package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.PipBoy.Controls.Line;
import nl.robojan.real_pipboy.PipBoy.Controls.SuperTextBox;

/**
 * Created by s120330 on 12-7-2015.
 */
public class TabButton extends SuperTextBox {
    private Line mLeftLine;
    private float mLeftLineLength = 0;

    public TabButton(float x, float y, String text) {
        super(x, y, text, false, 35, 15);
    }

    public void create(float leftLineLength) {
        mLeftLineLength = leftLineLength;
        mLeftLine = new Line(-mLeftLineLength, getHeight()/2, mLeftLineLength);
        addChild(mLeftLine);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        mLeftLine.setColor(color);
    }

    @Override
    public void setX(float x) {
        super.setX(x + mLeftLineLength);
    }

    @Override
    public Rectangle getSize() {
        Rectangle rect = super.getSize();
        rect.setX(rect.getX() - mLeftLineLength);
        rect.setWidth(rect.getWidth() + mLeftLineLength);
        return rect;
    }
}
