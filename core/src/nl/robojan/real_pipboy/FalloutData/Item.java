package nl.robojan.real_pipboy.FalloutData;

import java.util.Objects;

/**
 * Created by s120330 on 12-7-2015.
 */
public class Item {
    protected String mName;
    protected int mAmount;
    protected int mValue;
    protected float mWeight;
    protected String mIcon = null;
    protected String mBadge = null;
    protected boolean mEquippable = false;
    protected boolean mEquipped = false;
    protected String mEffect = null;
    protected long mId = 0;

    public Item(Long id, String name, int amount, int value, float weight, String icon, String badge,
                boolean equippable, boolean equipped, String effect){
        mId = id;
        mName = name;
        mAmount = amount;
        mValue = value;
        mWeight = weight;
        if(icon != null)
            mIcon = icon;
        mBadge = badge;
        mEquippable = equippable;
        mEquipped = equipped;
        mEffect = effect;
    }

    public Item(Long id, String name, int amount, int value, float weight, String icon, String badge,
                boolean equippable, boolean equipped){
        mId = id;
        mName = name;
        mAmount = amount;
        mValue = value;
        mWeight = weight;
        if(icon != null)
            mIcon = icon;
        mBadge = badge;
        mEquippable = equippable;
        mEquipped = equipped;
    }

    public Item(Long id, String name, int amount, int value, float weight, String icon, String badge){
        mId = id;
        mName = name;
        mAmount = amount;
        mValue = value;
        mWeight = weight;
        if(icon != null)
            mIcon = icon;
        mBadge = badge;
    }

    public Item(Long id, String name, int amount, int value, float weight, String icon, boolean equippable,
                boolean equipped, String effect){
        mId = id;
        mName = name;
        mAmount = amount;
        mValue = value;
        mWeight = weight;
        if(icon != null)
            mIcon = icon;
        mEquippable = equippable;
        mEquipped = equipped;
        mEffect = effect;
    }

    public Item(Long id, String name, int amount, int value, float weight, String icon, boolean equippable,
                boolean equipped){
        mId = id;
        mName = name;
        mAmount = amount;
        mValue = value;
        mWeight = weight;
        if(icon != null)
            mIcon = icon;
        mEquippable = equippable;
        mEquipped = equipped;
    }

    public Item(Long id, String name, int amount, int value, float weight, String icon){
        mId = id;
        mName = name;
        mAmount = amount;
        mValue = value;
        mWeight = weight;
        if(icon != null)
            mIcon = icon;
    }

    public Item(Long id, String name, int amount, int value, float weight){
        mId = id;
        mName = name;
        mAmount = amount;
        mValue = value;
        mWeight = weight;
    }

    public String getNameWithAmount(boolean forceAmount) {
        String result = mName;
        if(mAmount > 1 || forceAmount) {
            result += "(" + Integer.toString(mAmount) + ")";
        }
        return result;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int mAmount) {
        this.mAmount = mAmount;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int mValue) {
        this.mValue = mValue;
    }

    public float getWeight() {
        return mWeight;
    }

    public void setWeight(float mWeight) {
        this.mWeight = mWeight;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public String getBadge() {
        return mBadge;
    }

    public void setBadge(String mBadge) {
        this.mBadge = mBadge;
    }

    public void setEquippable(boolean equippable) {
        mEquippable = equippable;
    }

    public boolean isEquippable() {
        return mEquippable;
    }

    public void setEquipped(boolean equipped) {
        this.mEquipped = equipped;
    }

    public boolean isEquipped() {
        return mEquipped;
    }

    public String getEffect() {
        return mEffect;
    }

    public void setEffect(String effect) {
        this.mEffect = effect;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public boolean equivalent(Object obj) {
        if(obj == null || !(Item.class.isAssignableFrom(obj.getClass())))
            return false;
        Item other = (Item)obj;

        return Objects.equals(this.mName, other.mName) &&
                Objects.equals(this.mIcon, other.mIcon) &&
                Objects.equals(this.mBadge, other.mBadge) &&
                Objects.equals(this.mEffect, other.mEffect) &&
                this.mAmount == other.mAmount &&
                this.mWeight == other.mWeight &&
                this.mValue == other.mValue &&
                this.mEquipped == other.mEquipped &&
                this.mEquippable == other.mEquippable &&
                this.mId == other.mId;
    }
}
