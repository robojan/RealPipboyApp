package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;

/**
 * Created by s120330 on 11-7-2015.
 */
public class StatusEffects {


    public Array<EffectInfo> effects = new Array<EffectInfo>();

    public void add(EffectInfo info) {
        effects.add(info);
    }

    public Array<String> getEffectNames() {
        Array<String> result = new Array<String>();

        for (int i = 0; i < effects.size; i++) {
            result.add(effects.get(i).name);
        }
        return result;
    }

    public Array<String> getEffectStrings() {
        Array<String> result = new Array<String>();

        for (int i = 0; i < effects.size; i++) {
            result.add(effects.get(i).effects);
        }
        return result;
    }

    public String toString() {
        return effects.toString();
    }
}
