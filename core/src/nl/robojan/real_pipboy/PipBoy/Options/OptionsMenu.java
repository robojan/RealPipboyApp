package nl.robojan.real_pipboy.PipBoy.Options;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.Constants;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.TabLine;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;

/**
 * Created by s120330 on 13-7-2015.
 */
public class OptionsMenu extends Control {
    private final static String[] TABLINE_BUTTONS = new String[] {"Connection", "Graphics",
            "Sound"};

    private float mWidth, mHeight;

    private HeadlineRect mHeadlineRect;
    private TabLine MM_Tabline;
    private Control[] mOptionPages;

    private int mCurrentTab = 0;

    public OptionsMenu() {
        super(0, 0);
        mWidth = Constants.PIPBOY_WIDTH;
        mHeight = Constants.PIPBOY_HEIGHT;


        mHeadlineRect = new HeadlineRect(50,50, 855, 20);
        addChild(mHeadlineRect);

        MM_Tabline = new TabLine(50, 650, 855, TABLINE_BUTTONS);
        addChild(MM_Tabline);

        mOptionPages = new Control[TABLINE_BUTTONS.length];

        mOptionPages[0] = new ConnectionOptions(mHeadlineRect.getLeft(), mHeadlineRect.getBottom(),
                mHeadlineRect.getWidth(), MM_Tabline.getTop()-mHeadlineRect.getBottom());
        mOptionPages[1] = new GraphicsOptions(mHeadlineRect.getLeft(), mHeadlineRect.getBottom(),
                mHeadlineRect.getWidth(), MM_Tabline.getTop()-mHeadlineRect.getBottom());
        mOptionPages[2] = new Text(mWidth /2, mHeight/2, "Not implemented", Align.center,
                new Color(1,1,1,1));
        addChild(mOptionPages[mCurrentTab]);
    }


    @Override
    public void update(Context context) {
        if(MM_Tabline.getCurrentTab() != mCurrentTab) {
            removeChild(mOptionPages[mCurrentTab]);
            mCurrentTab = MM_Tabline.getCurrentTab();
            if(!containsChild(mOptionPages[mCurrentTab]))
                addChild(mOptionPages[mCurrentTab], true);
        }

        super.update(context);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }
}
