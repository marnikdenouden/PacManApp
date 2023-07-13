package com.example.pacmanapp.markers;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.location.DynamicLocation;
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapManager;
import com.example.pacmanapp.storage.GameSave;
import com.example.pacmanapp.storage.SaveObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class MapMarkers extends SaveObject implements Serializable {
    private final static String TAG = "MapMarkers";
    private static final long serialVersionUID = 1L;
    public static int mapMarkerId = R.id.mapMarkers;
    private final Collection<Marker> mapMarkers;

    /**
     * Create a collection of markers that can be serialized.
     *
     * @param gameSave Save to add game settings to
     */
    public MapMarkers(GameSave gameSave) {
        super(mapMarkerId, gameSave);
        mapMarkers = new HashSet<>();
    }

    /**
     * Add marker to marker collection for the map.
     *
     * @param marker Marker to add to the collection
     */
    public void addMarker(Marker marker) {
        mapMarkers.add(marker);
        Log.i(TAG, "Marker of class " + marker.getClass().getSimpleName()
                + " was added to the collection");
    }

    /**
     * Removes a marker from the marker collection for the map.
     *
     * @param marker Marker to add to the collection
     */
    public void removeMarker(Marker marker) {
        mapMarkers.remove(marker);
        Log.i(TAG, "Marker of class " + marker.getClass().getSimpleName()
                + " was removed from the collection");
    }

    /**
     * Gets or creates the map marker for the current save of the specified save manager.
     *
     * @param gameSave Save to get map marker from
     * @return Map marker from current save of the specified save manager
     */
    public static MapMarkers getFromSave(GameSave gameSave) {
        if (gameSave.hasSaveObject(mapMarkerId)) {
            return (MapMarkers) gameSave.getSaveObject(mapMarkerId);
        }
        MapMarkers mapMarkers = new MapMarkers(gameSave);
        gameSave.addSaveObject(mapMarkers);
        return mapMarkers;
    }

    /**
     * Load map for a specified activity and frame id.
     *
     * @param appCompatActivity Activity to load map for
     * @param frameId Frame id to load map for
     */
    public void loadMap(AppCompatActivity appCompatActivity, int frameId) {
        // Get the map area to load
        MapArea mapArea = MapArea.getMapArea(appCompatActivity, frameId);

        // Set the map area to control the frame id
        MapManager.getMapManager().setMapArea(frameId, mapArea);

        // Load the map with the specified frame id
        if (!MapManager.hasMapArea(frameId)) {
            Log.e(TAG, "Could not load map with frame id " + frameId
                    + " as no map area was set for it");
            return;
        }

        // Check if activity has dynamic location
        boolean hasDynamicLocation = appCompatActivity instanceof DynamicLocation;

        for (Marker marker: mapMarkers) {
            // Check if the marker belongs on the frame id map that is being loaded
            if (!marker.belongsOnMap(frameId)) {
                continue;
            }

            // Load marker on the map area
            marker.loadOnMapArea(mapArea.getContext());

            // Add location observer markers to receive location updates
            if (hasDynamicLocation && marker instanceof LocationObserver) {
                ((DynamicLocation) appCompatActivity).addObserver((LocationObserver) marker);
            }
        }
        Log.i(TAG, "Loaded map markers for frame id " + frameId);
    }

}