package nl.robojan.real_pipboy.PipBoy.Status;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.Effect;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;

/**
 * Created by s120330 on 9-7-2015.
 */
public class RadiationStatus extends Control {
    private static final String RAD_EFFECTS_BRACKET = "textures/interface/stats/rad_effects_bracket.dds";
    private static final String RAD_METER_BRACKET = "textures/interface/stats/rad_meter_bracket.dds";
    private static final String POINTER_TAIL = "textures/interface/shared/arrow/pointer_tail.dds";

    private static final int MAX_EFFECT_ROWS = 2;

    private float mWidth = 0;
    private float mHeight = 0;
    private float mZoom = 0;

    private Image stats_rad_effects_bracket;
    private Text stats_rad_effects_label;
    private RadiationStatusResist stats_rad_resist;
    private Image stats_rad_meter;
    private Text stats_rads_label;
    private Text stats_rads_zero;
    private Text stats_rads_mid;
    private Text stats_rads_max;
    private Image stats_rads_arrow;
    private Text stats_player_rads;

    private Text[] stats_rad_effect_magnitude;
    private Text[] stats_rad_effect_abbrev;

    private int mMaxRads = 1;
    private int mRads = 0;
    private int mRadResist = 0;

    public RadiationStatus(float x, float y, float width, float height, float zoom) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mZoom = zoom;
        create();
    }

    private void create() {
        float left_edge = 83*mZoom;
        float zeroPos = 186*mZoom;
        float fullPos = 482*mZoom;

        stats_rad_effects_bracket = new Image(0, 220, RAD_EFFECTS_BRACKET);
        stats_rad_effects_bracket.setWidth(stats_rad_effects_bracket.getWidth() * mZoom);
        stats_rad_effects_bracket.setHeight(stats_rad_effects_bracket.getHeight() * mZoom);
        stats_rad_effects_bracket.setX(mWidth - stats_rad_effects_bracket.getWidth());
        addChild(stats_rad_effects_bracket);

        stats_rad_effects_label = new Text(stats_rad_effects_bracket.getX() + left_edge,
                stats_rad_effects_bracket.getY() + 20, GameString.getString("sStatsEFFAbbrev"));
        addChild(stats_rad_effects_label);

        stats_rad_resist = new RadiationStatusResist(0, 360, stats_rad_effects_label.getX() - 10,
                stats_rad_effects_bracket.getHeight(), mZoom);
        addChild(stats_rad_resist);

        // Add meter
        stats_rad_meter = new Image(stats_rad_effects_bracket.getX(), stats_rad_resist.getY(),
                RAD_METER_BRACKET);
        stats_rad_meter.setWidth(stats_rad_meter.getWidth() * mZoom);
        stats_rad_meter.setHeight(stats_rad_meter.getHeight() * mZoom);
        addChild(stats_rad_meter);
        stats_rads_label = new Text(left_edge, 20, "RADS");
        stats_rad_meter.addChild(stats_rads_label);
        stats_rads_zero = new Text(zeroPos, -28, "0", Align.center, new Color(1,1,1,1));
        stats_rad_meter.addChild(stats_rads_zero);
        stats_rads_mid = new Text(zeroPos + (fullPos - zeroPos)/2, stats_rads_zero.getY(),
                String.format("%d",mMaxRads/2), Align.center, new Color(1,1,1,1));
        stats_rad_meter.addChild(stats_rads_mid);
        stats_rads_max = new Text(stats_rad_meter.getWidth(), stats_rads_zero.getY(),
                String.format("%d",mMaxRads), Align.right, new Color(1,1,1,1));
        stats_rad_meter.addChild(stats_rads_max);

        stats_rads_arrow = new Image(0,30, POINTER_TAIL, 64*mZoom, 64*mZoom);
        stats_rads_arrow.setX(zeroPos - 22 * mZoom / 2);
        stats_rad_meter.addChild(stats_rads_arrow);

        stats_player_rads = new Text(stats_rads_arrow.getX(), stats_rads_arrow.getY()+
                40*mZoom-24,"0", Align.right, new Color(1,1,1,1));
        stats_rad_meter.addChild(stats_player_rads);

        stats_rad_effect_abbrev = new Text[MAX_EFFECT_ROWS * 3];
        stats_rad_effect_magnitude = new Text[MAX_EFFECT_ROWS * 3];
        float tabStop[] = new float[3];
        tabStop[2] = stats_rad_effects_bracket.getWidth()-70;
        tabStop[1] = tabStop[2] - 120;
        tabStop[0] = tabStop[1] - 120;
        for(int i = 0; i < MAX_EFFECT_ROWS * 3; i++)
        {
            float x = tabStop[i % 3];
            float y = 40  * (i / 3);
            stats_rad_effect_magnitude[i] = new Text(x, y + 20, "0", Align.right,
                    new Color(1,1,1,1));
            stats_rad_effect_magnitude[i].setVisible(false);
            stats_rad_effect_abbrev[i] = new Text(x + 10, y + 20,
                    GameString.getString("sStatsEFFAbbrev"));
            stats_rad_effect_abbrev[i].setVisible(false);
            stats_rad_effects_bracket.addChild(stats_rad_effect_magnitude[i]);
            stats_rad_effects_bracket.addChild(stats_rad_effect_abbrev[i]);
        }
    }

    public void updateEffects(Array<Effect> effects)
    {
        int i = 0;
        for(; i < effects.size; i++){
            Effect effect = effects.get(i);
            stats_rad_effect_abbrev[i].setText(effect.abbrev);
            stats_rad_effect_abbrev[i].setVisible(true);
            stats_rad_effect_magnitude[i].setText(effect.toEffectString());
            stats_rad_effect_magnitude[i].setVisible(true);
        }
        for(; i < MAX_EFFECT_ROWS * 3; i++){
            stats_rad_effect_abbrev[i].setVisible(false);
            stats_rad_effect_magnitude[i].setVisible(false);
        }
    }

    public void updateArrow() {
        float zeroPos = 186*mZoom;
        float fullPos = 482*mZoom;
        stats_rads_arrow.setX(zeroPos - 22*mZoom/2 + (fullPos-zeroPos) * mRads/mMaxRads);
        stats_player_rads.setX(stats_rads_arrow.getX());
        stats_player_rads.setText(mRads == 0 ? "" :String.format("%d", mRads));
    }

    @Override
    public void update(Context context) {
        IFalloutData data = context.foData;
        Array<Effect> effects = data.getRadEffects();
        if(effects.size == 0)
        {
            stats_rad_effects_bracket.setVisible(false);
            stats_rad_effects_label.setVisible(false);
        } else {
            stats_rad_effects_bracket.setVisible(true);
            stats_rad_effects_label.setVisible(true);
        }
        updateEffects(effects);
        if(data.getMaxRads() != mMaxRads) {
            mMaxRads = data.getMaxRads();
            stats_rads_max.setText(String.format("%d", mMaxRads));
            stats_rads_mid.setText(String.format("%d", mMaxRads / 2));
            updateArrow();
        }
        if(data.getRads() != mRads) {
            mRads = data.getRads();
            updateArrow();
        }

        if(data.getRadResist() != mRadResist) {
            mRadResist = data.getRadResist();
            stats_rad_resist.setRadResist(mRadResist);
        }

        super.update(context);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }


    public class RadiationStatusResist extends Control {
        private static final String RAD_RESIST_BRACKET = "textures/interface/stats/rad_resist_bracket.dds";

        private float mWidth = 0;
        private float mHeight = 0;
        private float mZoom = 0;

        private int mRadResist = 0;

        private Image stats_rad_resist_bracket;
        private Text stats_rad_resist_label;
        private Text stats_rad_resist_level;

        public RadiationStatusResist(float x, float y, float width, float height, float zoom) {
            super(x, y);
            mWidth = width;
            mHeight = height;
            mZoom = zoom;
            create();
        }

        private void create() {
            stats_rad_resist_bracket = new Image(0, 0, RAD_RESIST_BRACKET);
            stats_rad_resist_bracket.setWidth(stats_rad_resist_bracket.getWidth()*mZoom);
            stats_rad_resist_bracket.setHeight(stats_rad_resist_bracket.getHeight() * mZoom);
            stats_rad_resist_bracket.setX(mWidth - stats_rad_resist_bracket.getWidth());
            addChild(stats_rad_resist_bracket);

            stats_rad_resist_label = new Text(0, 20, GameString.getString("sStatsRadResist"));
            addChild(stats_rad_resist_label);

            stats_rad_resist_level = new Text(mWidth - 20, 20, String.format("%d%%", mRadResist),
                    Align.right, new Color(1,1,1,1));
            addChild(stats_rad_resist_level);
        }

        public int getRadResist() {
            return mRadResist;
        }

        public void setRadResist(int radResist) {
            this.mRadResist = radResist;
            stats_rad_resist_level.setText(String.format("%d%%", mRadResist));
        }

        @Override
        public Rectangle getSize() {
            return new Rectangle(mX, mY, mWidth, mHeight);
        }
    }
}

