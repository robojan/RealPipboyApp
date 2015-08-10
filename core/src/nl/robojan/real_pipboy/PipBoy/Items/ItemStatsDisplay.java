package nl.robojan.real_pipboy.PipBoy.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.AidItem;
import nl.robojan.real_pipboy.FalloutData.AmmoItem;
import nl.robojan.real_pipboy.FalloutData.ApparelItem;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.FalloutData.Item;
import nl.robojan.real_pipboy.FalloutData.MiscItem;
import nl.robojan.real_pipboy.FalloutData.WeaponItem;
import nl.robojan.real_pipboy.PipBoy.Controls.CardInfo;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.Meter;

/**
 * Created by s120330 on 12-7-2015.
 */
public class ItemStatsDisplay extends Control {
    private static final String ARROW_FILE = "textures/interface/shared/arrow/repair_meter_pips.dds";

    private float mWidth, mHeight;
    private int mFontIndex;

    private CardInfo mDamageResistInfo;
    private CardInfo mDPSInfo;
    private CardInfo mDAMInfo;
    private CardInfo mWeightInfo;
    private CardInfo mValueInfo;
    private CardInfo mConditionInfo;
    private Meter mConditionMeter;
    private Image mCNDArrows;
    private CardInfo mAmmoInfo;
    private CardInfo mEffectsInfo;
    private CardInfo mStrengthReqInfo;

    
    // Animation
    private float mFadeTime = 0;
    private float mFadeAlpha = 0;
    private static final float FADE_ANIM_PERIOD = 5;
    private static final float FADE_ANIM_START_INCR = 0.3f;
    private static final float FADE_ANIM_END_INCR = 0.5f;
    private static final float FADE_ANIM_START_DECR = 0.8f;
    private static final float FADE_ANIM_END_DECR = 1.0f;

    private boolean mDoFading = false;

    public ItemStatsDisplay(float x, float y, float width, float height, int fontIndex) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mFontIndex = fontIndex;
        create();
    }

    private void create() {
        float stdWidth = mWidth/3;
        float stdHeight = mHeight/3;

        mDamageResistInfo = new CardInfo(0,0,stdWidth, stdHeight,
                GameString.getString("sInventoryDamageResistance"), "*");
        mDamageResistInfo.setVisible(false);
        addChild(mDamageResistInfo);
        mDPSInfo = new CardInfo(0,0,stdWidth, stdHeight,
                GameString.getString("sInventoryDamagePerSecond"), "*");
        mDPSInfo.setVisible(false);
        addChild(mDPSInfo);
        mDAMInfo = new CardInfo(0,0,stdWidth, stdHeight,
                GameString.getString("sInventoryDamage"), "*");
        mDAMInfo.setVisible(false);
        addChild(mDAMInfo);
        mWeightInfo = new CardInfo(stdWidth+10, 0, stdWidth, stdHeight,
                GameString.getString("sInventoryWeightUpper"), "*");
        mWeightInfo.setVisible(false);
        addChild(mWeightInfo);
        mValueInfo = new CardInfo((stdWidth+10)*2, 0, stdWidth, stdHeight,
                GameString.getString("sInventoryValue"), "*");
        mValueInfo.setVisible(false);
        addChild(mValueInfo);
        mConditionInfo = new CardInfo(0, stdHeight, stdWidth, stdHeight,
                GameString.getString("sInventoryCondition"), "");
        mConditionInfo.setVisible(false);
        addChild(mConditionInfo);

        mConditionMeter = new Meter(62, 15, mConditionInfo.getWidth()-72,20, true, true);
        mConditionInfo.addChild(mConditionMeter);
        mCNDArrows = new Image(mConditionMeter.getX() + mConditionMeter.getWidth()/2,
                mConditionMeter.getY(), ARROW_FILE, 20, 20);
        mConditionInfo.addChild(mCNDArrows);

        mAmmoInfo = new CardInfo(stdWidth + 10, stdHeight, stdWidth * 2 + 10, stdHeight, "", "");
        mAmmoInfo.setVisible(false);
        addChild(mAmmoInfo);

        mEffectsInfo = new CardInfo(0, stdHeight, stdWidth*3+20, stdHeight,
                GameString.getString("sInventoryEffects"), "");
        mEffectsInfo.setVisible(false);
        addChild(mEffectsInfo);

        mStrengthReqInfo = new CardInfo(mValueInfo.getX(),
                mValueInfo.getY() - mValueInfo.getHeight(), stdWidth, stdHeight,
                GameString.getString("sInventoryStrReq"), "0");
        mStrengthReqInfo.setVisible(true);
        addChild(mStrengthReqInfo);
    }

    private void setConditionArrowPosition(boolean weapon) {
        if(weapon) {
            mCNDArrows.setX(mConditionMeter.getX() + mConditionMeter.getWidth() / 2 +
                mConditionMeter.getWidth() / 4);
        } else {
            mCNDArrows.setX(mConditionMeter.getX() + mConditionMeter.getWidth() / 2);
        }
    }

    public void setItem(Item item) {
        if(item == null) {
            mDPSInfo.setVisible(false);
            mDAMInfo.setVisible(false);
            mWeightInfo.setVisible(false);
            mValueInfo.setVisible(false);
            mConditionInfo.setVisible(false);
            mConditionMeter.setVisible(false);
            mCNDArrows.setVisible(false);
            mDamageResistInfo.setVisible(false);
            mStrengthReqInfo.setVisible(false);
            mAmmoInfo.setVisible(false);
            mEffectsInfo.setVisible(false);
            return;
        }
        float weight = item.getWeight();
        mWeightInfo.setValue(weight == 0 ? "--" : String.format("%.2f", weight));
        mValueInfo.setValue(Integer.toString(item.getValue()));
        mWeightInfo.setVisible(true);
        mValueInfo.setVisible(true);
        if(WeaponItem.class.isAssignableFrom(item.getClass())) {
            WeaponItem weapon = (WeaponItem)item;
            mDoFading = true;
            mDPSInfo.setValue(String.format("%.0f", weapon.getDPS()));
            mDAMInfo.setValue(String.format("%.0f", weapon.getDAM()));
            mConditionMeter.setValue(weapon.getCondition());
            setConditionArrowPosition(true);
            mAmmoInfo.setTitle(weapon.getAmmo());
            mStrengthReqInfo.setValue(Integer.toString(weapon.getStrReq()));
            mEffectsInfo.setY(2 * mHeight / 3);

            mConditionMeter.setVisible(true);
            mCNDArrows.setVisible(true);
            mConditionInfo.setVisible(true);
            mDPSInfo.setVisible(true);
            mDAMInfo.setVisible(true);
            mDamageResistInfo.setVisible(false);
            mAmmoInfo.setVisible(true);
            mStrengthReqInfo.setVisible(true);
        } else if(ApparelItem.class.isAssignableFrom(item.getClass())) {
            ApparelItem apparel = (ApparelItem)item;
            mConditionMeter.setValue(apparel.getCondition());
            float dr = apparel.getDR();
            float dt = apparel.getDT();
            if(dt == 0) {
                mDamageResistInfo.setTitle(GameString.getString("sInventoryDamageResistance"));
                mDamageResistInfo.setValue(dr == 0 ? "--" : String.format("%.0f", dr));
            } else {
                mDamageResistInfo.setTitle(GameString.getString("sInventoryDamageThreshold"));
                mDamageResistInfo.setValue(String.format("%.0f", dt));
            }
            setConditionArrowPosition(false);
            mEffectsInfo.setY(2 * mHeight / 3);
            mAmmoInfo.setTitle(apparel.getArmorType());
            mAmmoInfo.setVisible(!apparel.getArmorType().isEmpty());

            mConditionMeter.setVisible(true);
            mCNDArrows.setVisible(true);
            mConditionInfo.setVisible(true);
            mDPSInfo.setVisible(false);
            mDAMInfo.setVisible(false);
            mDamageResistInfo.setVisible(true);
            mStrengthReqInfo.setVisible(false);
        } else if(AidItem.class.isAssignableFrom(item.getClass())) {
            mEffectsInfo.setY( mHeight / 3);
            mConditionMeter.setVisible(false);
            mCNDArrows.setVisible(false);
            mConditionInfo.setVisible(false);
            mDPSInfo.setVisible(false);
            mDAMInfo.setVisible(false);
            mDamageResistInfo.setVisible(false);
            mAmmoInfo.setVisible(false);
            mStrengthReqInfo.setVisible(false);
        } else if(MiscItem.class.isAssignableFrom(item.getClass())) {
            mEffectsInfo.setY(mHeight / 3);
            mConditionMeter.setVisible(false);
            mCNDArrows.setVisible(false);
            mConditionInfo.setVisible(false);
            mDPSInfo.setVisible(false);
            mDAMInfo.setVisible(false);
            mDamageResistInfo.setVisible(false);
            mAmmoInfo.setVisible(false);
            mStrengthReqInfo.setVisible(false);
        } else if(AmmoItem.class.isAssignableFrom(item.getClass())) {
            mEffectsInfo.setY(mHeight / 3);
            mConditionMeter.setVisible(false);
            mCNDArrows.setVisible(false);
            mConditionInfo.setVisible(false);
            mDPSInfo.setVisible(false);
            mDAMInfo.setVisible(false);
            mDamageResistInfo.setVisible(false);
            mAmmoInfo.setVisible(false);
            mStrengthReqInfo.setVisible(false);
        }
        String effect = item.getEffect();
        if(effect != null) {
            mEffectsInfo.setValue(effect);
            mEffectsInfo.setVisible(true);
        } else {
            mEffectsInfo.setVisible(false);
        }
    }

    @Override
    public void update(Context context) {
        // update the animation
        updateFadeAnim();
        if(mDoFading) {
            mDPSInfo.setColor(new Color(1,1,1,1 - mFadeAlpha));
            mDAMInfo.setColor(new Color(1,1,1,mFadeAlpha));
        } else {
            mDPSInfo.setColor(new Color(1,1,1,1));
            mDAMInfo.setColor(new Color(1,1,1,1));
        }

        super.update(context);
    }

    public void updateFadeAnim() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        mFadeTime += deltaTime;
        if (mFadeTime > FADE_ANIM_PERIOD)
            mFadeTime = 0;
        if (mFadeTime < FADE_ANIM_START_INCR * FADE_ANIM_PERIOD) {
            mFadeAlpha = 0;
        } else if (mFadeTime < FADE_ANIM_END_INCR * FADE_ANIM_PERIOD) {
            mFadeAlpha += 1 * deltaTime /
                    ((FADE_ANIM_END_INCR - FADE_ANIM_START_INCR) * FADE_ANIM_PERIOD);
        } else if (mFadeTime < FADE_ANIM_START_DECR * FADE_ANIM_PERIOD) {
            mFadeAlpha = 1;
        } else if (mFadeTime < FADE_ANIM_END_DECR * FADE_ANIM_PERIOD) {
            mFadeAlpha -= 1 * deltaTime /
                    ((FADE_ANIM_END_DECR - FADE_ANIM_START_DECR) * FADE_ANIM_PERIOD);
        } else {
            mFadeAlpha = 0;
        }
        if (mFadeAlpha > 1)
            mFadeAlpha = 1;
        if (mFadeAlpha < 0)
            mFadeAlpha = 0;
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }
}
