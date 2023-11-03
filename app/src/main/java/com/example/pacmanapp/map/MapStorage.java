package com.example.pacmanapp.map;

import android.util.Log;

import com.example.pacmanapp.R;
import com.example.pacmanapp.storage.GameSave;
import com.example.pacmanapp.storage.SaveObject;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MapStorage extends SaveObject implements Serializable {
    private final static String TAG = "MapStorage";
    private static final int mapStorageId = R.id.mapStorage;
    private final Map<Integer, MapSave> mapSaves;

    private MapStorage(@NotNull GameSave gameSave) {
        super(mapStorageId, gameSave);
        mapSaves = new HashMap<>();
    }

    /**
     * Load the map save for the specified frame id.
     *
     * @param frameId Frame id to get or create map save for
     * @param mapType MapType to create map save for
     */
    public MapSave loadMapSave(int frameId, @NotNull MapType mapType) {
        Log.i(TAG, "Loaded map save for frame id " + frameId + " and map type " + mapType);
        if (!hasMapSave(frameId)) {
            MapSave mapSave = new MapSave(frameId, mapType);
            addMapSave(mapSave);
            return mapSave;
        }
        return getMapSave(frameId);
    }

    /**
     * Check if a map save is stored for the specified frame id.
     *
     * @param frameId Frame id to check for
     * @return Truth assignment, if a map save is stored for the frame id
     */
    public boolean hasMapSave(int frameId) {
        if (!mapSaves.containsKey(frameId)) {
            return false;
        }
        return mapSaves.get(frameId) != null;
    }

    /**
     * Get the map save stored for the specified frame id.
     *
     * @pre hasMapSave(frameId)
     * @param frameId Frame id that specifies the map save
     * @throws NullPointerException if pre condition fails
     * @return mapSave MapSave stored for the frame id.
     */
    public MapSave getMapSave(int frameId) throws NullPointerException {
        if (!mapSaves.containsKey(frameId)) {
            throw new NullPointerException("Map manager has no map save " +
                    "with frame id " + frameId);
        }

        MapSave mapSave = mapSaves.get(frameId);
        if (mapSave == null) {
            throw new NullPointerException("Map manager has null reference " +
                    "for frame with id " + frameId);
        }

        return mapSave;
    }

    /**
     * Add the specified map save to the storage.
     *
     * @param mapSave Map save to add to the storage
     */
    public void addMapSave(@NotNull MapSave mapSave) {
        mapSaves.put(mapSave.getFrameId(), mapSave);
    }

    /**
     * Remove the specified map save from the storage.
     *
     * @param mapSave Map save to remove from the storage
     */
    public void removeMapSave(@NotNull MapSave mapSave) {
        mapSaves.remove(mapSave.getFrameId(), mapSave);
    }

    /**
     * Clear all the map saves from the storage.
     */
    public void clearMapSaves() {
        mapSaves.clear();
    }

    /**
     * Gets or creates the map storage for the specified game save.
     *
     * @param gameSave Save to get map storage from
     * @return map storage from the specified game save
     */
    public static MapStorage getFromSave(GameSave gameSave) {
        if (gameSave.hasSaveObject(mapStorageId)) {
            return (MapStorage) gameSave.getSaveObject(mapStorageId);
        }
        return new MapStorage(gameSave);
    }

}
