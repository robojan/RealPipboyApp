package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;

/**
 * Created by s120330 on 11-7-2015.
 */
public class EffectInfo {
    public String name;
    public String effects;

    public EffectInfo(String name, Array<Effect> effects) {
        this.name = name;
        this.effects = toEffectString(effects);
    }

    public EffectInfo(String name, String effects) {
        this.name = name;
        this.effects = effects;
    }

    public String toEffectString(Array<Effect> effects) {
        String effectString = "";
        for (int j = 0; j < effects.size; j++) {
            effectString += effects.get(j).toString(true);
            if (j < effects.size - 1) {
                effectString += ", ";
            }
        }
        return effectString;
    }

}
