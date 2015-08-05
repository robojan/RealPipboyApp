package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;

/**
 * Created by s120330 on 11-7-2015.
 */
public class StatusList {
    public Array<StatusItem> items = new Array<StatusItem>();
    public boolean hasEffect = false;

    public void add(StatusItem item) {
        items.add(item);
        hasEffect |= item.extra != null;
    }

    public boolean equivalent(Object other) {
        if(other == null || other.getClass() != StatusList.class)
            return false;
        StatusList list = (StatusList)other;
        if(hasEffect != list.hasEffect)
            return false;
        boolean equals = items.size == list.items.size;
        if(equals) {
            for(int i = 0; i < items.size && equals; i++){
                equals = items.get(i).equivalent(list.items.get(i));
            }
        }
        return equals;
    }

    public String toString() {
        return items.toString();
    }
}
