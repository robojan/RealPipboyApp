package nl.robojan.real_pipboy.FalloutData;

/**
 * Created by s120330 on 12-7-2015.
 */
public class WeaponItem extends Item {

    protected float mDPS = 0;
    protected float mDAM = 0;
    protected float mCondition = 0;
    protected int mStrReq = 0;


    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon,
                      boolean equippable, boolean equipped, float dps, float dam, float condition,
                      int strReq, String effect) {
        super(id,name, amount, value, weight, icon, equippable, equipped, effect);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
        mStrReq = strReq;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon,
                      boolean equippable, boolean equipped, float dps, float dam, float condition,
                      int strReq) {
        super(id, name, amount, value, weight, icon, equippable, equipped);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
        mStrReq = strReq;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon,
                      boolean equippable, boolean equipped, float dps, float dam, float condition,
                      String effect) {
        super(id, name, amount, value, weight, icon, equippable, equipped, effect);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon,
                      boolean equippable, boolean equipped, float dps, float dam, float condition) {
        super(id, name, amount, value, weight, icon, equippable, equipped);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon, float dps,
                      float dam, float condition, int strReq) {
        super(id, name, amount, value, weight, icon);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
        mStrReq = strReq;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon, float dps,
                      float dam, float condition) {
        super(id, name, amount, value, weight, icon);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon, String badge,
                      boolean equippable, boolean equipped, float dps, float dam, float condition) {
        super(id, name, amount, value, weight, icon, badge, equippable, equipped);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon, String badge,
                      boolean equippable, boolean equipped, float dps, float dam, float condition,
                      String effect) {
        super(id, name, amount, value, weight, icon, badge, equippable, equipped, effect);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon, String badge,
                      boolean equippable, boolean equipped, float dps, float dam, float condition,
                      int strReq) {
        super(id, name, amount, value, weight, icon, badge, equippable, equipped);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
        mStrReq = strReq;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon, String badge,
                      boolean equippable, boolean equipped, float dps, float dam, float condition,
                      int strReq, String effect) {
        super(id, name, amount, value, weight, icon, badge, equippable, equipped, effect);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
        mStrReq = strReq;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, float dps, float dam,
                      float condition) {
        super(id, name, amount, value, weight);
        mDPS = dps;
        mDAM = dam;
        mCondition = condition;
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon, String badge) {
        super(id, name, amount, value, weight, icon, badge);
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight, String icon) {
        super(id, name, amount, value, weight, icon);
    }

    public WeaponItem(Long id, String name, int amount, int value, float weight) {
        super(id, name, amount, value, weight);
    }

    public void setDPS(float dps) {
        mDPS = dps;
    }

    public void setDAM(float dam) {
        mDAM = dam;
    }

    public float getDPS() {
        return mDPS;
    }

    public float getDAM() {
        return mDAM;
    }

    public float getCondition() {
        return mCondition;
    }

    public void setCondition(float mCondition) {
        this.mCondition = mCondition;
    }

    public int getStrReq() {
        return mStrReq;
    }

    public void setStrReq(int strReq) {
        this.mStrReq = strReq;
    }

    @Override
    public boolean equivalent(Object obj) {
        if(obj == null || !(getClass().isAssignableFrom(obj.getClass())))
            return false;
        WeaponItem other = (WeaponItem)obj;
        return super.equivalent(obj) &&
                mCondition == other.mCondition &&
                mDPS == other.mDPS &&
                mDAM == other.mDAM &&
                mStrReq == other.mStrReq;
    }
}
