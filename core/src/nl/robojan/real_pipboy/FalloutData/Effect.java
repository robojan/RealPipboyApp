package nl.robojan.real_pipboy.FalloutData;

/**
 * Created by s120330 on 11-7-2015.
 */
public class Effect {
    public String abbrev;
    public int effect;

    public Effect(String abbrev, int effect) {
        this.abbrev = abbrev;
        this.effect = effect;
    }

    public String toEffectString() {
        return String.format("%+d", effect);
    }

    public String toString(boolean firstAbbrev) {
        String effStr = toEffectString();
        if (firstAbbrev) {
            return abbrev + " " + effStr;
        } else {
            return effStr + " " + abbrev;
        }
    }

    public String toString() {
        return toString(false);
    }

}
