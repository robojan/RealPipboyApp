package nl.robojan.real_pipboy.FalloutData;

/**
 * Created by s120330 on 13-7-2015.
 */
public class ApparelItem extends Item {
    protected float mDT = 0;
    protected float mDR = 0;
    protected float mCondition = 0;

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon, String badge,
                       boolean equippable, boolean equipped, String effect, float dr, float dt,
                       float condition) {
        super(id, name, amount, value, weight, icon, badge, equippable, equipped, effect);
        mDT = dr;
        mDR = dt;
        mCondition = condition;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon,
                       boolean equippable, boolean equipped, String effect, float dr, float dt,
                       float condition) {
        super(id, name, amount, value, weight, icon, equippable, equipped, effect);
        mDT = dr;
        mDR = dt;
        mCondition = condition;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon, boolean equippable,
                      boolean equipped, float dr, float dt, float condition) {
        super(id, name, amount, value, weight, icon, equippable, equipped);
        mDT = dr;
        mDR = dt;
        mCondition = condition;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon, float dr,
                      float dt, float condition) {
        super(id, name, amount, value, weight, icon);
        mDT = dr;
        mDR = dt;
        mCondition = condition;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, String icon, String badge,
                      boolean equippable, boolean equipped, float dt, float dr, float condition) {
        super(id, name, amount, value, weight, icon, badge, equippable, equipped);
        mDT = dt;
        mDR = dr;
        mCondition = condition;
    }

    public ApparelItem(Long id, String name, int amount, int value, float weight, float dt, float dr,
                      float condition) {
        super(id, name, amount, value, weight);
        mDT = dt;
        mDR = dr;
        mCondition = condition;
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

    @Override
    public boolean equivalent(Object obj) {
        if(obj == null || !(getClass().isAssignableFrom(obj.getClass())))
            return false;
        ApparelItem other = (ApparelItem)obj;
        return super.equivalent(obj) &&
                mCondition == other.mCondition &&
                mDT == other.mDT &&
                mDR == other.mDR;
    }
}
