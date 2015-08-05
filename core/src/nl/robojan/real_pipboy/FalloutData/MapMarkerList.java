package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;

/**
 * Created by s120330 on 5-8-2015.
 */
public class MapMarkerList {
    public Array<MapMarker> markers = new Array<MapMarker>();

    public void add(MapMarker item) {
        markers.add(item);
    }

    public boolean equivalent(Object obj) {
        if(obj == null || obj.getClass() != MapMarkerList.class)
            return false;
        MapMarkerList other = (MapMarkerList)obj;
        boolean equals = markers.size == other.markers.size;
        if(equals) {
            for(int i = 0; i < markers.size && equals; i++){
                equals = markers.get(i).equivalent(other.markers.get(i));
            }
        }
        return equals;
    }

    public String toString() {
        return markers.toString();
    }
}
