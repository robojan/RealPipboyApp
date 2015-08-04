package nl.robojan.real_pipboy.PipBoy.Status;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.FalloutData.StatusEffects;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Line;
import nl.robojan.real_pipboy.PipBoy.Controls.ListBox;
import nl.robojan.real_pipboy.PipBoy.Controls.ListBoxItem;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;

/**
 * Created by s120330 on 10-7-2015.
 */
public class EffectsStatus extends Control {
    private float mWidth, mHeight;

    private ListBox mEffectsBox;
    private Text mNoEffects;

    private Array<EffectsItem> mItems = new Array<EffectsItem>(2);

    public EffectsStatus(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        create();
    }

    private void create() {
        mEffectsBox = new ListBox(0, 0, 780, mHeight-50, true, 10);
        mEffectsBox.setX(mWidth - mEffectsBox.getWidth());
        mEffectsBox.setY((mHeight - mEffectsBox.getHeight()) / 2);

        mNoEffects = new Text(0,0, GameString.getString("sStatsNoEffects"));
        mNoEffects.setX((mWidth-mNoEffects.getWidth())/2);
        mNoEffects.setY((mHeight - mNoEffects.getHeight())/2);
        addChild(mNoEffects);
    }

    public void updateEffectsBox(StatusEffects statusEffects) {
        Array<String> effectNames = statusEffects.getEffectNames();
        Array<String> effectStrings = statusEffects.getEffectStrings();
        assert(effectNames.size == effectStrings.size);
        boolean equals = effectNames.size == mItems.size;
        if(equals){
            for(int i = 0; i < effectNames.size && equals; i++){
                EffectsItem item = mItems.get(i);
                equals = item.getName().equals(effectNames.get(i)) &&
                        item.getEffects().equals(effectStrings.get(i));
            }
        }
        if(!equals) {
            mEffectsBox.clearItems();
            mItems.clear();
            int i;
            for (i = 0; i < effectNames.size - 1; i++) {
                EffectsItem item  = new EffectsItem(20, effectNames.get(i), effectStrings.get(i),
                        true);
                mItems.add(item);
                mEffectsBox.addItem(item);
            }
            EffectsItem item  = new EffectsItem(20, effectNames.get(i), effectStrings.get(i),
                    false);
            mItems.add(item);
            mEffectsBox.addItem(item);
        }
    }

    @Override
    public void update(Context context) {
        IFalloutData data = context.foData;
        StatusEffects statusEffects =  data.getStatusEffects();
        if(statusEffects.effects.size > 0){
            mNoEffects.setVisible(false);
            mEffectsBox.setEnabled(true);
            if(!containsChild(mEffectsBox)){
                addChild(mEffectsBox, true);
            }
            updateEffectsBox(statusEffects);
        } else {
            mNoEffects.setVisible(true);
            mEffectsBox.setEnabled(false);
            removeChild(mEffectsBox);
        }
        super.update(context);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    public class EffectsItem extends ListBoxItem {
        private Text stats_effect_name;
        private Text stats_effect_string;
        private Line stats_effects_divider;

        private String mName;
        private String mEffects;
        private boolean mDividingLine;
        private float mHeight = 56;
        private float mWidth;
        private float mOffsetX = 0;

        public EffectsItem(float x, String name, String effects,
                           boolean dividingLine) {
            super(0, 0);
            mOffsetX = x;
            mWidth = 0;
            mName = name;
            mEffects = effects;
            mDividingLine = dividingLine;
            create();
        }

        private void create(){
            stats_effect_name = new Text(mOffsetX + 0, 16, mName);
            stats_effect_string = new Text(mOffsetX + 300, 16, mEffects);
            stats_effects_divider = new Line(mOffsetX + 0, mHeight- Constants.LINE_THICKNESS, mWidth);
            stats_effects_divider.setVisible(mDividingLine);

            addChild(stats_effect_name);
            addChild(stats_effect_string);
            addChild(stats_effects_divider);

        }

        @Override
        public boolean isEquivalent(ListBoxItem item) {
            if(item.getClass() != EffectsItem.class){
                return false;
            }
            EffectsItem e = (EffectsItem)item;
            return this.mName.equals(e.getName()) && this.mEffects.equals(e.getEffects());
        }

        @Override
        public Rectangle getSize() {
            return new Rectangle(mX, mY, mWidth, mHeight);
        }

        public float getWidth() {
            return mWidth;
        }

        public void setWidth(float mWidth) {
            this.mWidth = mWidth;
            stats_effects_divider.setLength(mWidth);
        }

        public String getName() {
            return mName;
        }

        public void setName(String mName) {
            this.mName = mName;
        }

        public String getEffects() {
            return mEffects;
        }

        public void setEffects(String mEffects) {
            this.mEffects = mEffects;
        }

        public boolean isDividingLine() {
            return mDividingLine;
        }

        public void setDividingLine(boolean mDividingLine) {
            this.mDividingLine = mDividingLine;
        }

        public float getHeight() {
            return mHeight;
        }

        public void setHeight(float mHeight) {
            this.mHeight = mHeight;
        }

        @Override
        public int compareTo(Object o) {
            if(!(o instanceof EffectsItem)) {
                throw new ClassCastException("Can't compare " + this.getClass().toString() + " to " +
                        o.getClass().toString());
            }
            if(o == null) {
                throw new NullPointerException();
            }
            String str1 = mName.toLowerCase();
            String str2 = ((EffectsItem)o).mName.toLowerCase();
            return str1.compareTo(str2);
        }
    }
}
