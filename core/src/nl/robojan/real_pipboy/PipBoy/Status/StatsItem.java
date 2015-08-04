package nl.robojan.real_pipboy.PipBoy.Status;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.PipBoy.Controls.ListBoxItem;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;

/**
 * Created by s120330 on 11-7-2015.
 */
public class StatsItem extends ListBoxItem {
    private Text listItemText;
    private Text stats_list_number;
    private Text stats_list_extra;

    private String mName;
    private String mValue;
    private String mExtra;

    private float mWidth;
    private float mHeight = 0;

    private float mRightBuffer = 20;

    public StatsItem(float x, float y, float width, String name, String value, String extra) {
        super(x, y);
        mWidth = width;
        mName = name;
        mValue = value;
        mExtra = extra;
        create();

    }

    public StatsItem(float x, float y, float width, String name, String value) {
        super(x, y);
        mWidth = width;
        mName = name;
        mValue = value;
        mExtra = null;
        create();
    }

    public StatsItem(float x, float y, float width, String name) {
        super(x, y);
        mWidth = width;
        mName = name;
        mValue = null;
        mExtra = null;
        create();
    }

    private void create() {
        clearChilds();

        listItemText = new Text(15, 20, mName,2);
        if(mHeight == 0) {
            mHeight = listItemText.getHeight() + mVerticalSpacing + 10;
        }
        addChild(listItemText);
        if(mValue != null) {
            stats_list_number = new Text(mWidth - mRightBuffer, listItemText.getY(), mValue,2, 0,
                    Align.right);
            addChild(stats_list_number);
            if(mExtra != null) {
                stats_list_extra = new Text(stats_list_number.getX() + 10, stats_list_number.getY(),
                        mExtra, 2, 0, Align.left);
                addChild(stats_list_extra);
            }
        }
    }

    public void setRightBuffer(float width) {
        mRightBuffer = width;
    }

    public float getRightBuffer() {
        return mRightBuffer;
    }

    @Override
    public void setWidth(float width) {
        mWidth = width;
        create();
    }

    @Override
    public boolean isEquivalent(ListBoxItem item) {
        if(item.getClass() != StatsItem.class) {
            return false;
        }
        StatsItem i = (StatsItem) item;
        boolean result = i.mName.equals(mName);
        if(mValue == null) {
            result &= i.mValue == null;
        } else {
            result &= i.mValue != null && mValue.equals(i.mValue);
        }
        if(mExtra == null) {
            result &= i.mExtra == null;
        } else {
            result &= i.mExtra != null && mExtra.equals(i.mExtra);
        }

        return result;
    }



    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof StatsItem)) {
            throw new ClassCastException("Can't compare " + this.getClass().toString() + " to " +
                    o.getClass().toString());
        }
        if(o == null) {
            throw new NullPointerException();
        }
        String str1 = mName.toLowerCase();
        String str2 = ((StatsItem)o).mName.toLowerCase();
        return str1.compareTo(str2);
    }
}
