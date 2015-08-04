package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;


/**
 * Created by s120330 on 8-7-2015.
 */
public class SuperTextBox extends TextBox {

    private boolean mSelected = false;

    public SuperTextBox(float x, float y, String text) {
        super(x, y, text, 40, 20);
        setSelected(mSelected);
    }

    public SuperTextBox(float x, float y, String text, float fixedWidth) {
        super(x, y, text, fixedWidth, 40, 20);
        setSelected(mSelected);
    }

    public SuperTextBox(float x, float y, String text, boolean selected) {
        super(x, y, text, 40, 20);
        setSelected(selected);
    }

    public SuperTextBox(float x, float y, String text, boolean selected, float horbuf,
                        float verbuf) {
        super(x, y, text, horbuf, verbuf);
        setSelected(selected);
    }

    public SuperTextBox(float x, float y, String text, boolean selected, float horbuf,
                        float verbuf, int align, Color color) {
        super(x, y, text, horbuf, verbuf, align, color);
        setSelected(selected);
    }

    public SuperTextBox(float x, float y, String text, boolean selected, float fixedWidth,
                        float horbuf, float verbuf, int align, Color color) {
        super(x, y, text, fixedWidth, horbuf, verbuf, align, color);
        setSelected(selected);
    }

    public SuperTextBox(float x, float y, String text, boolean selected, float horbuf,
                        float verbuf, int align, boolean glow, Color color) {
        super(x, y, text, horbuf, verbuf, align, glow, color);
        setSelected(selected);
    }

    public SuperTextBox(float x, float y, String text, boolean selected, float fixedWidth,
                        float horbuf, float verbuf, int align, boolean glow, Color color) {
        super(x, y, text, fixedWidth, horbuf, verbuf, align, glow, color);
        setSelected(selected);
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
        Color color = super.getColor();
        color.a = mSelected ? 1.0f : 0.5f;
        super.setColor(color);
        super.setBoxVisible(selected);
    }
}
