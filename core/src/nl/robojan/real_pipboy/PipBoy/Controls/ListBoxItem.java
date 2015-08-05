package nl.robojan.real_pipboy.PipBoy.Controls;

/**
 * Created by s120330 on 10-7-2015.
 */
public abstract class ListBoxItem extends Control {
    public ListBoxItem(float x, float y) {
        super(x, y);
    }
    protected float mVerticalSpacing = 20;


    public abstract boolean isEquivalent(ListBoxItem item);

    public abstract void setWidth(float width);

}