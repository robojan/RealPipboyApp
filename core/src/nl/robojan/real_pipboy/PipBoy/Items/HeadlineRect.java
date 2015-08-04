package nl.robojan.real_pipboy.PipBoy.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.PipBoy.Controls.CardInfo;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Line;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;
import nl.robojan.real_pipboy.PipBoy.Controls.VerticalFadeLine;

/**
 * Created by s120330 on 12-7-2015.
 */
public class HeadlineRect extends Control {
    private float mWidth, mHeight;

    private VerticalFadeLine IM_Headline_v1;
    private Line IM_Headline_h1;
    private Text IM_Headline_Title;
    private Line IM_Headline_h2;
    private CardInfo IM_Headline_PlayerWGInfo;
    private CardInfo IM_Headline_PlayerHPInfo;
    private CardInfo IM_Headline_PlayerDRInfo;
    private CardInfo IM_Headline_PlayerDTInfo;
    private CardInfo IM_Headline_PlayerCapsInfo;

    // Animation
    private float mDTDRTime = 0;
    private float mDTDRAlpha = 0;
    private static final float DTDR_ANIM_PERIOD = 5;
    private static final float DTDR_ANIM_START_INCR = 0.3f;
    private static final float DTDR_ANIM_END_INCR = 0.5f;
    private static final float DTDR_ANIM_START_DECR = 0.8f;
    private static final float DTDR_ANIM_END_DECR = 1.0f;

    public HeadlineRect(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        create();
    }

    private void create() {
        IM_Headline_v1 = new VerticalFadeLine(0, 0);
        IM_Headline_h1 = new Line(0, 0, 50);
        IM_Headline_Title = new Text(70, 0, GameString.getString("sInventoryItems"), 4);
        IM_Headline_h2 = new Line(90 + IM_Headline_Title.getWidth(), 0, 215);
        float length = 215 - IM_Headline_h2.getX();
        if (length < 0)
            length = 0;
        IM_Headline_h2.setLength(length);
        IM_Headline_PlayerWGInfo = new CardInfo(215, 0, 155,
                GameString.getString("sInventoryWeight"), "*/*");
        IM_Headline_PlayerHPInfo = new CardInfo(380, 0, 155,
                GameString.getString("sHitPointsShort"), "*/*");
        IM_Headline_PlayerDRInfo = new CardInfo(545, 0, 110,
                GameString.getString("sInventoryDamageResistance"), "*");
        IM_Headline_PlayerDTInfo = new CardInfo(545, 0, 110,
                GameString.getString("sInventoryDamageThreshold"), "*");
        IM_Headline_PlayerCapsInfo = new CardInfo(665, 0, 190,
                GameString.getString("sInventoryCaps"), "*");


        addChild(IM_Headline_v1);
        addChild(IM_Headline_h1);
        addChild(IM_Headline_Title);
        addChild(IM_Headline_h2);
        addChild(IM_Headline_PlayerWGInfo);
        addChild(IM_Headline_PlayerHPInfo);
        addChild(IM_Headline_PlayerDRInfo);
        addChild(IM_Headline_PlayerDTInfo);
        addChild(IM_Headline_PlayerCapsInfo);
    }

    @Override
    public void update(Context context) {
        IFalloutData data = context.foData;
        IM_Headline_PlayerWGInfo.setValue(String.format("%.0f/%.0f", data.getWeight(),
                data.getMaxWeight()));
        IM_Headline_PlayerHPInfo.setValue(String.format("%d/%d", data.getHP(),
                data.getMaxHP()));
        float dt = data.getDamageThreshold();
        float dr = data.getDamageResistance();
        IM_Headline_PlayerDRInfo.setValue(String.format("%.1f", dr));
        IM_Headline_PlayerDTInfo.setValue(String.format("%.1f", dt));
        IM_Headline_PlayerCapsInfo.setValue(Integer.toString(data.getCaps()));

        // update the animation
        updateDTDRAnim();
        // When the player has a damage resistance the DT and DR values start fading
        if (dr > 0) {
            IM_Headline_PlayerDTInfo.setColor(new Color(1, 1, 1, 1 - mDTDRAlpha));
            IM_Headline_PlayerDRInfo.setColor(new Color(1, 1, 1, mDTDRAlpha));
        } else {
            IM_Headline_PlayerDTInfo.setColor(new Color(1, 1, 1, 1));
            IM_Headline_PlayerDRInfo.setColor(new Color(1, 1, 1, 0));
        }

        super.update(context);
    }

    public void updateDTDRAnim() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        mDTDRTime += deltaTime;
        if (mDTDRTime > DTDR_ANIM_PERIOD)
            mDTDRTime = 0;
        if (mDTDRTime < DTDR_ANIM_START_INCR * DTDR_ANIM_PERIOD) {
            mDTDRAlpha = 0;
        } else if (mDTDRTime < DTDR_ANIM_END_INCR * DTDR_ANIM_PERIOD) {
            mDTDRAlpha += 1 * deltaTime /
                    ((DTDR_ANIM_END_INCR - DTDR_ANIM_START_INCR) * DTDR_ANIM_PERIOD);
        } else if (mDTDRTime < DTDR_ANIM_START_DECR * DTDR_ANIM_PERIOD) {
            mDTDRAlpha = 1;
        } else if (mDTDRTime < DTDR_ANIM_END_DECR * DTDR_ANIM_PERIOD) {
            mDTDRAlpha -= 1 * deltaTime /
                    ((DTDR_ANIM_END_DECR - DTDR_ANIM_START_DECR) * DTDR_ANIM_PERIOD);
        } else {
            mDTDRAlpha = 0;
        }
        if (mDTDRAlpha > 1)
            mDTDRAlpha = 1;
        if (mDTDRAlpha < 0)
            mDTDRAlpha = 0;
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }
}
