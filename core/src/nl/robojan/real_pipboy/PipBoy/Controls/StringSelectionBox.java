package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

/**
 * Created by s120330 on 2-8-2015.
 */
public class StringSelectionBox extends Control {
    private static final String LEFTARROW = "textures/interface/shared/arrow/left.dds";
    private static final String RIGHTARROW = "textures/interface/shared/arrow/right.dds";
    private static final String CLICKSOUND = "sound/fx/ui/menu/ui_menu_ok.wav";

    private String[] mOptions;
    private boolean mLooping = false;

    private Image mLeftArrow;
    private Image mRightArrow;
    private Text mOptionText;

    private int mSelected = 0;
    private float mWidth;
    private float mHeight = 32;

    public StringSelectionBox(float x, float y, float width, String[] options) {
        super(x, y);
        mWidth = width;
        mOptions = options;
        create();
    }

    private void updateArrowVisibility() {
        mLeftArrow.setVisible(mSelected > 0 || mLooping);
        mRightArrow.setVisible(mSelected < (mOptions.length - 1) || mLooping);
    }

    private void create() {
        mSelected = 0;
        mLeftArrow = new Image(0, 0, LEFTARROW, 32, 32);
        mLeftArrow.addClickableListener(mArrowListener);
        mLeftArrow.setClickSound(CLICKSOUND);
        addChild(mLeftArrow);
        mRightArrow = new Image(mWidth - 32, 0, RIGHTARROW, 32, 32);
        mRightArrow.addClickableListener(mArrowListener);
        mRightArrow.setClickSound(CLICKSOUND);
        addChild(mRightArrow);
        updateArrowVisibility();
        mOptionText = new Text(mLeftArrow.getRight(), 6, getSelected(), 2,
                mRightArrow.getLeft() - mLeftArrow.getRight(), Align.center);
        addChild(mOptionText);
        mHeight = mOptionText.getHeight();
    }

    public boolean isLooping() {
        return mLooping;
    }

    public void setLooping(boolean looping) {
        mLooping = looping;
        updateArrowVisibility();
    }

    public int getSelectionIndex() {
        return mSelected;
    }

    public String getSelected() {
        if(mSelected >= 0 && mSelected < mOptions.length) {
            return mOptions[mSelected];
        }
        return "";
    }

    public String[] getOptions() {
        return mOptions;
    }

    public int length() {
        return mOptions.length;
    }

    public void setSelected(int index) {
        if(index < 0 || index >= mOptions.length) {
            index = 0;
        }
        mSelected = index;
        updateArrowVisibility();
        mOptionText.setText(getSelected());

    }

    public void setSelected(String item) {
        for(int i = 0; i < mOptions.length; i++) {
            if(mOptions[i].equals(item)) {
                setSelected(i);
                return;
            }
        }
    }

    public void setOptions(String[] options) {
        mOptions = options;
        setSelected(0);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    private ClickableListener mArrowListener = new ClickableListener() {
        @Override
        public void onClickableEvent(Control source, Object user) {
            if(source == mLeftArrow) {
                mSelected -= 1;
                if(mSelected < 0 && mLooping) {
                    mSelected = mOptions.length - 1;
                }
            } else if(source == mRightArrow) {
                mSelected += 1;
                if(mSelected >= mOptions.length && mLooping) {
                    mSelected = 0;
                }
            }
            if(mSelected >= mOptions.length)
                mSelected = mOptions.length - 1;
            if(mSelected < 0)
                mSelected = 0;
            setSelected(mSelected);
        }
    };
}
