package nl.robojan.real_pipboy.PipBoy.Data;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Line;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;
import nl.robojan.real_pipboy.PipBoy.Controls.VerticalFadeLine;

/**
 * Created by s120330 on 13-7-2015.
 */
class HeadlineRect extends Control {
    private float mWidth, mHeight;

    private VerticalFadeLine MM_Headline_v1;
    private Line MM_Headline_h1;
    private Text MM_Headline_Title;
    private Line MM_Headline_h2;
    private Text MM_Headline_LocationInfo;
    private VerticalFadeLine MM_Headline_v2;
    private Line MM_Headline_h3;
    private Text MM_Headline_TimeDateInfo;
    private VerticalFadeLine MM_Headline_v3;

    private SimpleDateFormat mDateFormat;

    public HeadlineRect(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;

        mDateFormat = new SimpleDateFormat("MM.dd.yy, H:mm");

        MM_Headline_v1 = new VerticalFadeLine(0, 0);
        addChild(MM_Headline_v1);
        MM_Headline_h1 = new Line(0, 0, 50);
        addChild(MM_Headline_h1);
        MM_Headline_Title = new Text(70, 0, GameString.getString("sData"), 4);
        addChild(MM_Headline_Title);
        MM_Headline_h2 = new Line(180, 0, 470);
        addChild(MM_Headline_h2);
        MM_Headline_LocationInfo = new Text(630, 20, "Location", 2, 0, Align.right);
        addChild(MM_Headline_LocationInfo);
        MM_Headline_v2 = new VerticalFadeLine(650 - Constants.LINE_THICKNESS, 0);
        addChild(MM_Headline_v2);
        float h3Len = 855 - MM_Headline_v2.getX() - 10;
        if (h3Len < 0)
            h3Len = 0;
        MM_Headline_h3 = new Line(MM_Headline_v2.getX() + 10, 0, h3Len);
        addChild(MM_Headline_h3);
        MM_Headline_TimeDateInfo = new Text(845, 20, "date", 2, 0, Align.right);
        addChild(MM_Headline_TimeDateInfo);
        MM_Headline_v3 = new VerticalFadeLine(855 - Constants.LINE_THICKNESS, 0);
        addChild(MM_Headline_v3);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    @Override
    public void update(Context context) {
        IFalloutData data = context.foData;
        MM_Headline_LocationInfo.setText(data.getLocationName());
        GregorianCalendar date = data.getDateTime();
        MM_Headline_TimeDateInfo.setText(mDateFormat.format(date.getTime()));
        super.update(context);
    }
}
