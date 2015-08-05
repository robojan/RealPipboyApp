package nl.robojan.real_pipboy.PipBoy.Data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.ListBoxItem;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;

/**
 * Created by s120330 on 12-7-2015.
 */
public class DataItemMarker extends ListBoxItem {

    private static final String SQUARE = "textures/interface/shared/marker/square.dds";
    private static final String SQUARE_FILLED = "textures/interface/shared/marker/square_filled.dds";

    private Text mListItemText;
    private Image mItemMarker;

    private boolean mSelected = false;
    private boolean mActive = true;
    private boolean mSquareVisible = false;
    private String mName = null;

    private float mWidth = 400;
    private float mHeight = 0;

    public DataItemMarker(float x, float y, String name, boolean selected) {
        super(x, y);

        mSelected = selected;
        mName = name;
        create();
    }

    public DataItemMarker(float x, float y, String name, boolean selected, boolean active,
                          boolean squareAlwaysVisible) {
        super(x, y);

        mSelected = selected;
        mName = name;
        mActive = active;
        mSquareVisible = squareAlwaysVisible;
        create();
    }

    private void create() {
        mListItemText = new Text(40, 20, mName, 2, 310, Align.left);
        addChild(mListItemText);
        mItemMarker = new Image(15, 20, SQUARE_FILLED, 25, 25);
        setSelected(mSelected);
        addChild(mItemMarker);
        mHeight = mListItemText.getHeight() + mVerticalSpacing * 2;
        setActive(mActive);
    }

    public Color getColor() {
        return mListItemText.getColor();
    }

    public void setColor(Color color) {
        mListItemText.setColor(color);
        mItemMarker.setColor(color);
    }

    @Override
    public void setWidth(float width) {
        mWidth = width;
        mListItemText.setTargetWidth(width - mListItemText.getX());
        mHeight = mListItemText.getHeight() + mVerticalSpacing * 2;
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    @Override
    public boolean isEquivalent(ListBoxItem item) {
        if(item == null || item.getClass() != getClass())
            return false;
        DataItemMarker other = (DataItemMarker)item;
        return mName.equals(other.mName)  && mSelected == other.mSelected;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
        mItemMarker.setFile(selected ? SQUARE_FILLED : SQUARE);
        mItemMarker.setVisible(selected || mSquareVisible);
    }

    public boolean isSquareAlwaysVisible() {
        return mSquareVisible;
    }

    public void setSquareAlwaysVisible(boolean squareVisible) {
        mSquareVisible = squareVisible;
        mItemMarker.setVisible(mSelected || mSquareVisible);
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
        setColor(active ? new Color(1,1,1,1) : new Color(1,1,1,0.5f));
    }

    @Override
    public int compareTo(Control o) {
        if(o == null) {
            throw new NullPointerException();
        }
        if(!(o instanceof DataItemMarker)) {
            throw new ClassCastException("Can't compare " + this.getClass().toString() + " to " +
                o.getClass().toString());
        }
        String str1 = mName.toLowerCase();
        String str2 = ((DataItemMarker)o).mName.toLowerCase();
        return str1.compareTo(str2);
    }
}
