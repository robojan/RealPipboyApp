package nl.robojan.real_pipboy.FalloutData;

import nl.robojan.real_pipboy.util.Objects;

/**
 * Created by s120330 on 12-7-2015.
 */
public class Statistic {
    public String name;
    public String value;

    public Statistic(String name, String value){
        this.name = name;
        this.value = value;
    }

    public Statistic(String name, int value) {
        this.name = name;
        this.value = Integer.toString(value);
    }

    public boolean equivalent(Object obj) {
        if(obj == null || !(obj instanceof Statistic))
            return false;
        Statistic other = (Statistic)obj;

        return Objects.equals(this.name, other.name) &&
                Objects.equals(this.value, other.value);
    }
}
