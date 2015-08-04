package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.PipBoy.Constants;

/**
 * Created by s120330 on 28-7-2015.
 */
public class BottomBracket extends Control {
    private static final String SOLID_IMAGE = "textures/interface/shared/solid.dds";
    private static final String FADE_IMAGE = "textures/interface/shared/line/fade_to_top.dds";

    private float mWidth;
    private float mHeight = 50;

    private Color mColor = new Color(1,1,1,1);

    private Image mMainLine;
    private Image mLeftVert;
    private Image mRightVert;

    public BottomBracket(float x, float y, float width) {
        super(x, y);
        mWidth = width;

        create();
    }

    public BottomBracket(float x, float y, float width, float height, Color color) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mColor = color;

        create();
    }

    private void create() {

        clearChilds();

        mMainLine = new Image(0,0, SOLID_IMAGE, mWidth, Constants.LINE_THICKNESS, mColor);
        addChild(mMainLine);

        mLeftVert = new Image(0, -(mHeight - 1), FADE_IMAGE, Constants.LINE_THICKNESS, mHeight,
                mColor);
        addChild(mLeftVert);

        mRightVert = new Image(mWidth - Constants.LINE_THICKNESS, -(mHeight - 1), FADE_IMAGE,
                Constants.LINE_THICKNESS, mHeight, mColor);
        addChild(mRightVert);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }
}
