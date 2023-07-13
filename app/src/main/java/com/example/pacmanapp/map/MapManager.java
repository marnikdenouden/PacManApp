package com.example.pacmanapp.map;

import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

public class MapManager {
    private final static String TAG = "MapManager";
    private final Map<Integer, MapArea> mapReference;
    private static MapManager mapManager;

    private MapManager() {
        mapReference = new HashMap<>();
    }

    /**
     * Set the map area to control the frame id.
     *
     * @param mapArea to control the frame id
     */
    public void setMapArea(int frameId, MapArea mapArea) {
        Log.i(TAG, "Set map area to control frame id " + frameId);
        mapReference.put(frameId, mapArea);
    }

    /**
     * Gets the single existing map manager.
     *
     * @return Map manager
     */
    public static MapManager getMapManager() {
        if (mapManager == null) {
            mapManager = new MapManager();
        }
        return mapManager;
    }

    /**
     * Get the map area that corresponds with the frame id.
     *
     * @pre Map frame exists with the frame Id and contains map area
     * @throws NullPointerException if map area could not be found for the frame id
     * @return Frame id of the the map frame the map area was added to
     */
    public static MapArea getMapArea(int frameId) {
        ViewGroup mapFrame = MapManager.getMapManager().mapReference.get(frameId);
        if (mapFrame == null) {
            throw new NullPointerException("Map manager could not find map frame with id " + frameId);
        }

        MapArea mapArea = mapFrame.findViewById(MapArea.getMapAreaId());
        if (mapArea == null) {
            throw new NullPointerException("Map manager could not find a map area added to the frame with id " + frameId);
        }

        return mapArea;
    }

    /**
     * Check if the frame id has a corresponding map area.
     *
     * @param frameId Frame id to check for
     * @return Truth assignment, if map area is stored for frame id
     */
    public static boolean hasMapArea(int frameId) {
        return MapManager.getMapManager().mapReference.containsKey(frameId);
    }

}
