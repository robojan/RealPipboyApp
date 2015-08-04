package nl.robojan.real_pipboy.PipBoy.Options;

import com.badlogic.gdx.graphics.Color;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.PipBoy.Controls.Rect;
import nl.robojan.real_pipboy.PipBoy.Controls.StringSelectionBox;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;
import nl.robojan.real_pipboy.Settings;

/**
 * Created by s120330 on 2-8-2015.
 */
public class GraphicsOptions extends Rect {

    private static final String[] PIPBOYCOLOROPTIONS = { "GREEN", "BLUE", "AMBER", "WHITE",
            "RED"} ;
    private static final Color[] PIPBOYCOLOROPTIONSVALUES = {
            new Color(0.1f, 1.0f, 0.5f, 1.0f), // Green
            new Color(0.2f, 0.8f, 1.0f, 1.0f), // Blue
            new Color(1.0f, 0.7f, 0.25f, 1.0f), // Amber
            new Color(0.8f, 1.0f, 1.0f, 1.0f), // White
            new Color(0.83f, 0.2f, 0.0f, 1.0f), // Red
    };

    private Text mPipboyColorLabel;
    private StringSelectionBox mPipboyColorSelector;
    private int mSelectedColorOption = -1;

    public GraphicsOptions(float x, float y, float width, float height) {
        super(x, y, width, height);
        Settings settings = Settings.getInstance();

        // Pipboy color
        mPipboyColorLabel = new Text(50, 75, "Pip-Boy Color", 2);
        addChild(mPipboyColorLabel);
        mPipboyColorSelector = new StringSelectionBox(250, 72, 200, PIPBOYCOLOROPTIONS);
        mPipboyColorSelector.setLooping(true);
        Color settingPipboyColor = settings.getPipboyColor();
        for(int i = 0; i < PIPBOYCOLOROPTIONSVALUES.length; i++) {
            if(settingPipboyColor.equals(PIPBOYCOLOROPTIONSVALUES[i])) {
                mSelectedColorOption = i;
                break;
            }
        }
        if(mSelectedColorOption < 0) {
            // Not found, set default
            mSelectedColorOption = 0;
            settings.setPipboyColor(PIPBOYCOLOROPTIONSVALUES[mSelectedColorOption]);
        }
        mPipboyColorSelector.setSelected(mSelectedColorOption);
        addChild(mPipboyColorSelector);
    }

    @Override
    public void update(Context context) {
        super.update(context);
        Settings settings = Settings.getInstance();
        if(mPipboyColorSelector.getSelectionIndex() != mSelectedColorOption) {
            mSelectedColorOption = mPipboyColorSelector.getSelectionIndex();
            settings.setPipboyColor(PIPBOYCOLOROPTIONSVALUES[mSelectedColorOption]);
        }
    }
}
