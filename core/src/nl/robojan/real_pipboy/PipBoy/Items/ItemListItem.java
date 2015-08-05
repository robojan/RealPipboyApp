package nl.robojan.real_pipboy.PipBoy.Items;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;


import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.ListBoxItem;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;

/**
 * Created by s120330 on 12-7-2015.
 */
public class ItemListItem extends ListBoxItem {

    private static final String SQUARE = "textures/interface/shared/marker/square.dds";
    private static final String SQUARE_FILLED = "textures/interface/shared/marker/square_filled.dds";

    private Text mListItemText;
    private Image mItemMarker;

    private boolean mEquippable = false;
    private boolean mEquipped = false;
    private String mName = null;

    private float mWidth = 393;
    private float mHeight = 0;

    public ItemListItem(float x, float y, String name, boolean equippable, boolean equipped ) {
        super(x, y);

        mEquippable = equippable;
        mEquipped = equipped;
        mName = name;
        create();
    }

    private void create() {
        mListItemText = new Text(40, 20, mName, 2, 300, Align.left);
        addChild(mListItemText);
        String square = mEquipped ? SQUARE_FILLED : SQUARE;
        mItemMarker = new Image(15, 18, square, 25, 25);
        mItemMarker.setVisible(mEquippable);
        addChild(mItemMarker);
        mHeight = mListItemText.getHeight() + mVerticalSpacing * 2;
    }

    @Override
    public void setWidth(float width) {
        mWidth = width;
        mListItemText.setTargetWidth(width - mListItemText.getX());
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    @Override
    public boolean isEquivalent(ListBoxItem item) {
        if( item == null || item.getClass() != getClass())
            return false;
        ItemListItem other = (ItemListItem)item;
        return mName.equals(other.mName)  && mEquipped == other.mEquipped &&
                mEquippable == other.mEquippable;
    }

    @Override
    public int compareTo(Control o) {
        if(o == null) {
            throw new NullPointerException();
        }
        if(!(o instanceof ItemListItem)) {
            throw new ClassCastException("Can't compare " + this.getClass().toString() + " to " +
                    o.getClass().toString());
        }
        String str1 = mName.toLowerCase();
        String str2 = ((ItemListItem)o).mName.toLowerCase();
        return str1.compareTo(str2);
    }
}
