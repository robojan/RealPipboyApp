package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;

/**
 * Created by s120330 on 12-7-2015.
 */
public class Reputations {

    public Array<Reputation> reputations;

    public Reputations(){
        reputations = new Array<Reputation>();
    }

    public void add(Reputation item) {
        reputations.add(item);
    }

    public boolean equivalent(Object other) {
        if(other == null || !(other instanceof Reputations))
            return false;
        Reputations list = (Reputations)other;
        boolean equals = reputations.size == list.reputations.size;
        if(equals) {
            for(int i = 0; i < reputations.size && equals; i++){
                equals &= reputations.get(i).equivalent(list.reputations.get(i));
            }
        }
        return equals;
    }
}
