package nl.robojan.real_pipboy.PipBoy.Data;

import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.Rect;

/**
 * Created by s120330 on 2-8-2015.
 */
public class WaveformRect extends Rect {
    private static final String HMETERIMAGE = "textures/interface/shared/meter/tickmark_horiz.dds";
    private static final String VMETERIMAGE = "textures/interface/shared/meter/tickmark_vert.dds";
    private static final String SOLIDIMAGE = "textures/interface/shared/solid.dds";

    private Image mMeterHoriz;
    private Image mMeterVert;
    private Image mBaseLine;

    public WaveformRect(float x, float y, float width, float height) {
        super(x, y, width, height);

        mMeterHoriz = new Image(0, height - 30, HMETERIMAGE, width, 32);
        addChild(mMeterHoriz);
        mMeterVert = new Image(width - 30, 0, VMETERIMAGE, 32, height);
        addChild(mMeterVert);
        mBaseLine = new Image(0, (height - 2) / 2 - 8, SOLIDIMAGE, width - 5, 2);
        addChild(mBaseLine);
    }
}
