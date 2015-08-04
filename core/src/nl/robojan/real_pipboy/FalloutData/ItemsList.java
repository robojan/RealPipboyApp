package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;

/**
 * Created by s120330 on 12-7-2015.
 */
public class ItemsList {

    public Array<Item> list;

    public ItemsList(ItemsList other) {
        this.list = new Array<Item>(other.list);
    }

    public ItemsList(){
        list = new Array<Item>();
    }

    public void add(Item item) {
        list.add(item);
    }

    public ItemsList getItemsOfType(Class cls) {
        ItemsList result = new ItemsList();
        for(Item item : list){
            if(cls.isAssignableFrom(item.getClass())) {
                result.list.add(item);
            }
        }
        return result;
    }

    public boolean equivalent(Object obj) {
        if(obj == null || !(getClass().isAssignableFrom(obj.getClass())))
            return false;
        ItemsList other = (ItemsList)obj;
        boolean equals = list.size == other.list.size;
        if(equals) {
            for(int i = 0; i < list.size && equals; i++){
                equals &= list.get(i).equivalent(other.list.get(i));
            }
        }
        return equals;
    }
}
