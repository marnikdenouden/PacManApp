package com.example.pacmanapp.map;

import android.location.Location;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.markers.Marker;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class MapSave implements Serializable, LocationObserver {
    private final static String TAG = "MapSave";
    private static final long serialVersionUID = 1L;

    private final int frameId;
    private final MapType mapType;
    private final MapMarkers mapMarkers;
    private int xOffset = 0;
    private int yOffset = 0;

    public MapSave(int frameId, @NotNull MapType mapType) {
        this.frameId = frameId;
        this.mapType = mapType;
        this.mapMarkers = new MapMarkers();
    }

    /**
     * Load the map in to the specified view group.
     *
     * @param viewGroup View group to get or add map area to
     * @return mapArea MapArea that is (new) child of the specified view group
     */
    public MapArea loadMap(@NotNull ViewGroup viewGroup) {
        Log.i(TAG, "Loading map area for map save with frame id " + frameId);
        if (!MapArea.hasMapArea(viewGroup, frameId)) {
            return createMapArea(viewGroup);
        } else {
            return MapArea.getMapArea(viewGroup, frameId);
        }
    }

    /**
     * Get the map markers of this map save.
     *
     * @return mapMarker Map markers stored in the map save
     */
    public MapMarkers getMapMarkers() {
        return mapMarkers;
    }

    /**
     * Create a new map area for the specified view group.
     *
     * @param viewGroup View group to create map area for
     * @return mapArea MapArea that was created and setup for the map
     */
    private MapArea createMapArea(@NotNull ViewGroup viewGroup) {
        Log.i(TAG, "Creating map area for map save with frame id " + frameId);
        MapArea mapArea = MapArea.createMap(viewGroup, this);

        for (Marker marker: mapMarkers.getMapMarkers()) {
            mapArea.addMarker(marker);
        }

        mapMarkers.addMarkerListener(mapArea.getMarkerListener());

        return mapArea;
    }

    /**
     * Unload the specified map area.
     *
     * @param mapArea Map area that was previously loaded
     */
    public void unloadMap(@NotNull MapArea mapArea) {
        Log.i(TAG, "Unloading map area for map save with frame id " + frameId);
        mapMarkers.removeMarkerListener(mapArea.getMarkerListener());
        mapArea.removeMap();
    }

    @Override
    public void onLocationUpdate(@NonNull Location location) {
        mapMarkers.onLocationUpdate(location);
    }

    /**
     * Get the map type of this map save.
     *
     * @return mapType Map type store in the map save
     */
    public MapType getMapType() {
        return mapType;
    }

    /**
     * Get the frame id of the map save.
     *
     * @return frame id int of the map save
     */
    public int getFrameId() {
        return frameId;
    }

    /**
     * Set the map offset position.
     *
     * @param xOffSet Horizontal offset of the map
     * @param yOffset Vertical offset of the map
     */
    public void setOffset(int xOffSet, int yOffset) {
        this.xOffset = xOffSet;
        this.yOffset = yOffset;
    }

    /**
     * Get the x off set of the map.
     *
     * @return xOffset that determines the horizontal map position
     */
    public int getXOffset() {
        return xOffset;
    }

    /**
     * Get the y off set of the map.
     *
     * @return yOffset that determines the vertical map position
     */
    public int getYOffset() {
        return yOffset;
    }

    // Use a lot of protected methods in map area, then have method calls here that use them and store values to reconstruct it on new creation.
    // For example position of the map can be store here as x and y translation and then use a method in map area to move the map.
    // Or scale the map, with value from here and method in map area.
    // This prevents accidental use outside the package,
    //  which means the map area can be gotten from loading it and then passed on as token that can not be used.

}
