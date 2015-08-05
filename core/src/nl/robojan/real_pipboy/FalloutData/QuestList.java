package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;

/**
 * Created by s120330 on 29-7-2015.
 */
public class QuestList {
    public Array<Quest> list = new Array<Quest>();

    public void add(Quest quest) {
        list.add(quest);
    }

    public boolean equivalent(Object obj) {
        if(obj == null || obj.getClass() != QuestList.class)
            return false;
        QuestList other = (QuestList)obj;
        boolean equals = list.size == other.list.size;
        if(equals) {
            for(int i = 0; i < list.size && equals; i++){
                equals = list.get(i).equivalent(other.list.get(i));
            }
        }
        return equals;
    }

    public String toString() {
        return list.toString();
    }
}
