package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;


/**
 * Created by s120330 on 28-7-2015.
 */
public class NoteList {
    public Array<Note> list = new Array<Note>();

    public void add(Note item) {
        list.add(item);
    }

    public boolean equivalent(Object obj) {
        if(obj == null || obj.getClass() != NoteList.class)
            return false;
        NoteList other = (NoteList)obj;
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
