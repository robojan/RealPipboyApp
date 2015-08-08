package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import nl.robojan.real_pipboy.Constants;

/**
 * Created by s120330 on 12-7-2015.
 */
public class TabLine extends Control {
    private float mWidth;
    private float mHeight = 60;
    private Color mColor = new Color(1,1,1,1);

    private String[] mButtonTexts;

    private Array<TabButton> mButtons = new Array<TabButton>();

    private VerticalFadeLine mLeftVert;
    private VerticalFadeLine mRightVert;
    private Line mRightHoriz;

    private float mLeftLineLength = 0;
    private int mNumButtons = 0;
    private int mCurrentTab = 0;


    public TabLine(float x, float y, float width, String[] buttons) {
        super(x, y);
        mWidth = width;
        mButtonTexts = buttons;
        create();
    }

    private void create() {
        mLeftVert = new VerticalFadeLine(0, 0, true, mHeight);
        mLeftVert.setColor(mColor);
        addChild(mLeftVert);

        mRightVert = new VerticalFadeLine(mWidth - Constants.LINE_THICKNESS, 0, true, mHeight);
        mRightVert.setColor(mColor);
        addChild(mRightVert);

        float totalButtonWidth = 0;
        mNumButtons = mButtonTexts.length;
        for(String text : mButtonTexts) {
            TabButton button = new TabButton(0, 0, text);
            totalButtonWidth += button.getWidth();
            mButtons.add(button);
        }
        mLeftLineLength = (mWidth - totalButtonWidth) / (mNumButtons + 1);
        float buttonX = 0;
        for(int i = 0; i<mButtons.size; i++) {
            TabButton button = mButtons.get(i);
            button.create(mLeftLineLength);
            button.setY(button.getHeight() / -2);
            button.setX(buttonX);
            buttonX += button.getWidth();
            button.addClickableListener(mButtonsListener, i);
            button.setClickSound("sound/fx/ui/menu/ui_menu_ok.wav");
            addChild(button);
        }

        mRightHoriz = new Line(mWidth - mLeftLineLength, 0, mLeftLineLength);
        addChild(mRightHoriz);

        mButtons.get(mCurrentTab).setSelected(true);
    }

    private ClickableListener mButtonsListener = new ClickableListener() {
        @Override
        public void onClickableEvent(Control source, Object user) {
            if(source.getClass() != TabButton.class || user.getClass() != Integer.class)
                return;
            setCurrentTab(((Integer)user));
        }
    };

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color color) {
        this.mColor = color;
        mLeftVert.setColor(color);
        mRightVert.setColor(color);
        mRightHoriz.setColor(color);
        for(TabButton button : mButtons) {
            button.setColor(color);
        }
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public void setCurrentTab(int currentTab) {
        mButtons.get(mCurrentTab).setSelected(false);
        mCurrentTab = currentTab;
        mButtons.get(currentTab).setSelected(true);
    }

    public int getNumButtons() {
        return mNumButtons;
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }
}
