package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;

/**
 * Created by s120330 on 10-8-2015.
 */
public class RadioList {
    public Array<Radio> list = new Array<Radio>();
    private boolean mPlaying = false;

    public void add(Radio item) {
        list.add(item);
        mPlaying |= item.isPlaying();
    }

    public boolean equivalent(Object obj) {
        if(obj == null || obj.getClass() != RadioList.class)
            return false;
        RadioList other = (RadioList)obj;
        boolean equals = list.size == other.list.size;
        equals &= mPlaying == other.mPlaying;
        if(equals) {
            for(int i = 0; i < list.size && equals; i++){
                equals = list.get(i).equivalent(other.list.get(i));
            }
        }
        return equals;
    }

    public boolean isPlaying() {
        return mPlaying;
    }

    public String toString() {
        return list.toString();
    }
}
