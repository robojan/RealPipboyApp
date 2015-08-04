package nl.robojan.real_pipboy.PipBoy.Data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.PipBoy.Controls.BottomBracket;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;

/**
 * Created by s120330 on 28-7-2015.
 */
public class DataAudioInfo extends Control {
    private static final String METERIMAGE = "textures/interface/shared/meter/tickmark_horiz.dds";
    private static final String MARKERIMAGE = "textures/interface/shared/solid.dds";

    private float mWidth;
    private float mHeight;

    private Text mTimeRemaining;
    private Image mMeterImage;
    private Image mMarkerImage;
    private BottomBracket mBorder;

    public DataAudioInfo(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;

        mTimeRemaining = new Text(width, 0, "00:00:0 remaining", Align.right, new Color(1,1,1,1));
        addChild(mTimeRemaining);
        mBorder = new BottomBracket(0, mTimeRemaining.getBottom() + 30, mWidth, 30,
                new Color(1,1,1,1));
        addChild(mBorder);
        mMeterImage = new Image(0, mBorder.getY() - 32 + 2, METERIMAGE, width, 32);
        mMeterImage.setTile(true, false);
        addChild(mMeterImage);
        mMarkerImage = new Image(mMeterImage.getWidth()-8, mMeterImage.getY() + 32/4,
                MARKERIMAGE, 8, 32);
        addChild(mMarkerImage);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }
}
