package nl.robojan.real_pipboy.PipBoy.Status;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.Meter;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;

/**
 * Created by s120330 on 9-7-2015.
 */
public class ConditionStatus extends Control {

    private static final String VATS_HEAD_DIVIDER = "textures/interface/vats/vats_head_divider.dds";
    private static final String VATS_RIGHT_DIVIDER = "textures/interface/vats/vats_right_divider.dds";
    private static final String VATS_LEFT_DIVIDER = "textures/interface/vats/vats_left_divider.dds";
    private static final String HEAD = "textures/interface/stats/head.dds";
    private static final String LEFT_ARM = "textures/interface/stats/left_arm.dds";
    private static final String RIGHT_ARM = "textures/interface/stats/right_arm.dds";
    private static final String LEFT_LEG = "textures/interface/stats/left_leg.dds";
    private static final String RIGHT_LEG = "textures/interface/stats/right_leg.dds";
    private static final String TORSO = "textures/interface/stats/torso.dds";
    private static final String HEAD_BROKEN = "textures/interface/stats/head_broken.dds";
    private static final String LEFT_ARM_BROKEN = "textures/interface/stats/left_arm_broken.dds";
    private static final String RIGHT_ARM_BROKEN = "textures/interface/stats/right_arm_broken.dds";
    private static final String LEFT_LEG_BROKEN = "textures/interface/stats/left_leg_broken.dds";
    private static final String RIGHT_LEG_BROKEN = "textures/interface/stats/right_leg_broken.dds";
    private static final String TORSO_BROKEN = "textures/interface/stats/torso_broken.dds";
    private static final String FACE_00 = "textures/interface/stats/face_00.dds";
    private static final String FACE_01 = "textures/interface/stats/face_01.dds";
    private static final String FACE_02 = "textures/interface/stats/face_02.dds";
    private static final String FACE_03 = "textures/interface/stats/face_03.dds";
    private static final String FACE_04 = "textures/interface/stats/face_04.dds";
    private static final String FACE_10 = "textures/interface/stats/face_10.dds";

    private static final float METER_HEIGHT = 38;

    private float mWidth, mHeight;
    private float mZoom;

    private Image stats_player_head;
    private Image stats_player_face;
    private Image stats_player_torso;
    private Image stats_player_leftarm;
    private Image stats_player_rightarm;
    private Image stats_player_leftleg;
    private Image stats_player_rightleg;

    private Image stats_player_head_pct;
    private Meter stats_player_head_pct_meter;
    private Text stats_player_head_crippled;
    private Image stats_player_torso_pct;
    private Meter stats_player_torso_pct_meter;
    private Text stats_player_torso_crippled;
    private Image stats_player_leftarm_pct;
    private Meter stats_player_leftarm_pct_meter;
    private Text stats_player_leftarm_crippled;
    private Image stats_player_rightarm_pct;
    private Meter stats_player_rightarm_pct_meter;
    private Text stats_player_rightarm_crippled;
    private Image stats_player_leftleg_pct;
    private Meter stats_player_leftleg_pct_meter;
    private Text stats_player_leftleg_crippled;
    private Image stats_player_rightleg_pct;
    private Meter stats_player_rightleg_pct_meter;
    private Text stats_player_rightleg_crippled;


    public ConditionStatus(float x, float y, float width, float height, float zoom) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mZoom = zoom;
        create();
    }

    private void create()
    {
        stats_player_head = new Image(-85*1.45f/2,0, HEAD, 128*mZoom, 128*mZoom);
        addChild(stats_player_head);
        stats_player_face = new Image(0, 0, FACE_00, 64 *mZoom, 64 *mZoom);
        stats_player_face.setX(21);
        stats_player_face.setY((stats_player_head.getHeight() - stats_player_face.getHeight())/2 );
        stats_player_head.addChild(stats_player_face);

        // Image
        stats_player_torso = new Image(-14*mZoom, 86*mZoom, TORSO, 128 * mZoom, 128 * mZoom);
        stats_player_head.addChild(stats_player_torso);
        stats_player_leftarm = new Image(90*mZoom, 0*mZoom, LEFT_ARM, 128 * mZoom, 64 * mZoom);
        stats_player_torso.addChild(stats_player_leftarm);
        stats_player_rightarm = new Image(-88*mZoom, -5*mZoom, RIGHT_ARM, 128*mZoom, 64*mZoom);
        stats_player_torso.addChild(stats_player_rightarm);
        stats_player_leftleg = new Image(47*mZoom, 86*mZoom, LEFT_LEG, 128*mZoom, 128*mZoom);
        stats_player_torso.addChild(stats_player_leftleg);
        stats_player_rightleg = new Image(-37*mZoom, 83*mZoom, RIGHT_LEG, 128*mZoom, 128*mZoom);
        stats_player_torso.addChild(stats_player_rightleg);

        // Meters
        float meterScale = METER_HEIGHT/128.0f;
        // Head
        stats_player_head_pct = new Image((85*mZoom-128)/2, -METER_HEIGHT-5,
                VATS_HEAD_DIVIDER, 128, METER_HEIGHT);
        stats_player_head.addChild(stats_player_head_pct);
        stats_player_head_crippled = new Text(stats_player_head_pct.getLeft()+10,
                stats_player_head_pct.getY()+24*meterScale,
                GameString.getString("sStatsCrippled"), 2);
        stats_player_head.addChild(stats_player_head_crippled);
        stats_player_head_pct_meter = new Meter(25, 24*meterScale, 77, METER_HEIGHT - 24,
                Align.left, true, false);
        stats_player_head_pct.addChild(stats_player_head_pct_meter);

        // Torso
        stats_player_torso_pct = new Image(stats_player_head_pct.getX(),
                92*mZoom, VATS_HEAD_DIVIDER, 128, METER_HEIGHT);
        stats_player_head.addChild(stats_player_torso_pct);
        stats_player_torso_crippled = new Text(stats_player_torso_pct.getLeft()+10,
                stats_player_torso_pct.getTop()+24*meterScale,
                GameString.getString("sStatsCrippled"), 2);
        stats_player_head.addChild(stats_player_torso_crippled);
        stats_player_torso_pct_meter = new Meter(25, 24*meterScale, 77, METER_HEIGHT - 24,
                Align.left, true, false);
        stats_player_torso_pct.addChild(stats_player_torso_pct_meter);

        // Left arm
        stats_player_leftarm_pct = new Image(150, 44, VATS_LEFT_DIVIDER, 256, METER_HEIGHT);
        stats_player_head.addChild(stats_player_leftarm_pct);
        stats_player_leftarm_crippled = new Text(stats_player_leftarm_pct.getLeft()+10,
                stats_player_leftarm_pct.getTop()+24*meterScale,
                GameString.getString("sStatsCrippled"), 2);
        stats_player_head.addChild(stats_player_leftarm_crippled);
        stats_player_leftarm_pct_meter = new Meter(51, 24*meterScale, 77, METER_HEIGHT - 24,
                Align.left, true, false);
        stats_player_leftarm_pct.addChild(stats_player_leftarm_pct_meter);

        // Right arm
        stats_player_rightarm_pct = new Image(-stats_player_leftarm_pct.getX()-40,
                stats_player_leftarm_pct.getY(), VATS_RIGHT_DIVIDER, 256, METER_HEIGHT);
        stats_player_head.addChild(stats_player_rightarm_pct);
        stats_player_rightarm_crippled = new Text(stats_player_rightarm_pct.getLeft()+10,
                stats_player_rightarm_pct.getTop()+24*meterScale,
                GameString.getString("sStatsCrippled"), 2);
        stats_player_head.addChild(stats_player_rightarm_crippled);
        stats_player_rightarm_pct_meter = new Meter(25, 24*meterScale, 77, METER_HEIGHT - 24,
                Align.left, true, false);
        stats_player_rightarm_pct.addChild(stats_player_rightarm_pct_meter);

        // Left leg
        stats_player_leftleg_pct = new Image(stats_player_leftarm_pct.getX(), 285,
                VATS_LEFT_DIVIDER, 256, METER_HEIGHT);
        stats_player_head.addChild(stats_player_leftleg_pct);
        stats_player_leftleg_crippled = new Text(stats_player_leftleg_pct.getLeft()+10,
                stats_player_leftleg_pct.getTop()+24*meterScale,
                GameString.getString("sStatsCrippled"), 2);
        stats_player_head.addChild(stats_player_leftleg_crippled);
        stats_player_leftleg_pct_meter = new Meter(51, 24*meterScale, 77, METER_HEIGHT - 24,
                Align.left, true, false);
        stats_player_leftleg_pct.addChild(stats_player_leftleg_pct_meter);

        // Right leg
        stats_player_rightleg_pct = new Image(-stats_player_leftleg_pct.getX()-40,
                stats_player_leftleg_pct.getY(), VATS_RIGHT_DIVIDER, 256, METER_HEIGHT);
        stats_player_head.addChild(stats_player_rightleg_pct);
        stats_player_rightleg_crippled = new Text(stats_player_rightleg_pct.getLeft()+10,
                stats_player_rightleg_pct.getTop()+24*meterScale,
                GameString.getString("sStatsCrippled"), 2);
        stats_player_head.addChild(stats_player_rightleg_crippled);
        stats_player_rightleg_pct_meter = new Meter(25, 24*meterScale, 77, METER_HEIGHT - 24,
                Align.left, true, false);
        stats_player_rightleg_pct.addChild(stats_player_rightleg_pct_meter);
    }

    public void updateLimb(float status, Meter meter, Image pct, Text crippled, Image limb,
                           String okLimb, String crippledLimb){
        String limbImage = okLimb;
        if(status <= 0){
            limbImage = crippledLimb;
            if(stats_player_head.containsChild(pct))
                stats_player_head.removeChild(pct);
            if(!stats_player_head.containsChild(crippled))
                stats_player_head.addChild(crippled);
        } else {
            if(!stats_player_head.containsChild(pct))
                stats_player_head.addChild(pct);
            if(stats_player_head.containsChild(crippled))
                stats_player_head.removeChild(crippled);
        }
        if(!limb.getFile().equals(limbImage))
        {
            limb.setFile(limbImage);
        }
        meter.setValue(status);
    }

    @Override
    public void update(Context context) {
        IFalloutData data = context.foData;
        float headStatus = data.getLimbStatus(IFalloutData.Limb.HEAD);
        updateLimb(headStatus, stats_player_head_pct_meter, stats_player_head_pct,
                stats_player_head_crippled, stats_player_head, HEAD, HEAD_BROKEN);
        updateLimb(data.getLimbStatus(IFalloutData.Limb.TORSO), stats_player_torso_pct_meter,
                stats_player_torso_pct, stats_player_torso_crippled, stats_player_torso,
                TORSO, TORSO_BROKEN);
        updateLimb(data.getLimbStatus(IFalloutData.Limb.LEFT_ARM), stats_player_leftarm_pct_meter,
                stats_player_leftarm_pct, stats_player_leftarm_crippled, stats_player_leftarm,
                LEFT_ARM, LEFT_ARM_BROKEN);
        updateLimb(data.getLimbStatus(IFalloutData.Limb.RIGHT_ARM), stats_player_rightarm_pct_meter,
                stats_player_rightarm_pct, stats_player_rightarm_crippled, stats_player_rightarm,
                RIGHT_ARM, RIGHT_ARM_BROKEN);
        updateLimb(data.getLimbStatus(IFalloutData.Limb.LEFT_LEG), stats_player_leftleg_pct_meter,
                stats_player_leftleg_pct, stats_player_leftleg_crippled, stats_player_leftleg,
                LEFT_LEG, LEFT_LEG_BROKEN);
        updateLimb(data.getLimbStatus(IFalloutData.Limb.RIGHT_LEG), stats_player_rightleg_pct_meter,
                stats_player_rightleg_pct, stats_player_rightleg_crippled, stats_player_rightleg,
                RIGHT_LEG, RIGHT_LEG_BROKEN);

        // Update face

        super.update(context);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }
}
