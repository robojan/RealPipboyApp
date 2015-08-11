package nl.robojan.real_pipboy.FalloutData;

import nl.robojan.real_pipboy.util.Objects;

/**
 * Created by s120330 on 13-7-2015.
 */
public class ApparelItem extends Item {
    protected float mDT = 0;
    protected float mDR = 0;
    protected float mCondition = 0;
    protected String mArmorType;

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon, String badge,
                       boolean equippable, boolean equipped, String effect, float dr, float dt,
                       float condition, String armorType) {
        super(id, name, amount, value, weight, icon, badge, equippable, equipped, effect);
        mDT = dr;
        mDR = dt;
        mCondition = condition;
        mArmorType = armorType;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon,
                       boolean equippable, boolean equipped, String effect, float dr, float dt,
                       float condition, String armorType) {
        super(id, name, amount, value, weight, icon, equippable, equipped, effect);
        mDT = dr;
        mDR = dt;
        mCondition = condition;
        mArmorType = armorType;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon, boolean equippable,
                      boolean equipped, float dr, float dt, float condition, String armorType) {
        super(id, name, amount, value, weight, icon, equippable, equipped);
        mDT = dr;
        mDR = dt;
        mCondition = condition;
        mArmorType = armorType;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon, float dr,
                      float dt, float condition, String armorType) {
        super(id, name, amount, value, weight, icon);
        mDT = dr;
        mDR = dt;
        mCondition = condition;
        mArmorType = armorType;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon, String badge,
                      boolean equippable, boolean equipped, float dt, float dr, float condition, String armorType) {
        super(id, name, amount, value, weight, icon, badge, equippable, equipped);
        mDT = dt;
        mDR = dr;
        mCondition = condition;
        mArmorType = armorType;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, float dt, float dr,
                      float condition, String armorType) {
        super(id, name, amount, value, weight);
        mDT = dt;
        mDR = dr;
        mCondition = condition;
        mArmorType = armorType;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon, String badge) {
        super(id, name, amount, value, weight, icon, badge);
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon) {
        super(id, name, amount, value, weight, icon);
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight) {
        super(id, name, amount, value, weight);
    }

    public void setDT(float dt) {
        mDT = dt;
    }

    public void setDR(float dr) {
        mDR = dr;
    }

    public float getDT() {
        return mDT;
    }

    public float getDR() {
        return mDR;
    }

    public float getCondition() {
        return mCondition;
    }

    public void setCondition(float mCondition) {
        this.mCondition = mCondition;
    }

    public String getArmorType() {
        return mArmorType;
    }

    public void setArmorType(String armorType) {
        mArmorType = armorType;
    }

    @Override
    public boolean equivalent(Object obj) {
        if(obj == null || !(getClass().isAssignableFrom(obj.getClass())))
            return false;
        ApparelItem other = (ApparelItem)obj;
        return super.equivalent(obj) &&
                Objects.equals(mArmorType, other.mArmorType) &&
                mCondition == other.mCondition &&
                mDT == other.mDT &&
                mDR == other.mDR;
    }
}
