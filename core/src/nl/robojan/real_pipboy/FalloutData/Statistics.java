package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;

/**
 * Created by s120330 on 12-7-2015.
 */
public class Statistics {

    public Array<Statistic> stats;

    public Statistics(){
        stats = new Array<Statistic>();
    }

    public void add(Statistic item) {
        stats.add(item);
    }

    public boolean equivalent(Object other) {
        if(other == null || !(other instanceof Statistics))
            return false;
        Statistics list = (Statistics)other;
        boolean equals = stats.size == list.stats.size;
        if(equals) {
            for(int i = 0; i < stats.size && equals; i++){
                equals &= stats.get(i).equivalent(list.stats.get(i));
            }
        }
        return equals;
    }
}
