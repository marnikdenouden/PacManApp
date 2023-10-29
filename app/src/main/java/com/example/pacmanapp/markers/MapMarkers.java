package com.example.pacmanapp.markers;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.location.DynamicLocation;
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapManager;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.storage.GameSave;
import com.example.pacmanapp.storage.SaveObject;
import com.example.pacmanapp.storage.SavePlatform;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MapMarkers extends SaveObject implements Serializable {
    private final static String TAG = "MapMarkers";
    private static final long serialVersionUID = 1L;
    private static final int mapMarkerId = R.id.mapMarkers;
    private final Map<Integer, Collection<Marker>> mapMarkers;
    private boolean hasDynamicLocation;
    private transient AppCompatActivity currentMapContext;

    /**
     * Create a collection of markers that can be serialized.
     *
     * @param gameSave Save to add map markers to
     */
    public MapMarkers(GameSave gameSave) {
        super(mapMarkerId, gameSave);
        mapMarkers = new HashMap<>();
    }

    /**
     * Add marker to marker collection for the map.
     *
     * @param marker Marker to add to the collection
     */
    public void addMarker(Marker marker) {
        getMapCollection(marker.getFrameId()).add(marker);
        loadMarkerOnCurrentMap(marker);
        if (marker instanceof Selectable) {
            SelectionCrier.getInstance().select((Selectable) marker);
        }
        Log.i(TAG, "Marker of class " + marker.getClass().getSimpleName()
                + " was added to the collection");
    }

    /**
     * Removes a marker from the marker collection for the map.
     *
     * @param marker Marker to add to the collection
     */
    public void removeMarker(@NotNull Marker marker) {
        getMapCollection(marker.getFrameId()).remove(marker);
        if (currentMapContext != null) {
            if (marker instanceof LocationObserver) {
                ((DynamicLocation) currentMapContext).removeObserver((LocationObserver) marker);
            }
            // Remove marker from map area for the current map context.
            MapArea.getMapArea(currentMapContext, marker.getFrameId()).removeMarker(marker);
        }
        Log.i(TAG, "Marker of class " + marker.getClass().getSimpleName()
                + " was removed from the collection");
    }

    /**
     * Gets the map collection of markers for a specified frame id.
     *
     * @param frameId Frame id for the map collection to get
     * @return Map collection for the specified frame id
     */
    private Collection<Marker> getMapCollection(int frameId) {
        if (mapMarkers.containsKey(frameId)) {
            return mapMarkers.get(frameId);
        }
        Collection<Marker> mapCollection = new HashSet<>();
        mapMarkers.put(frameId, mapCollection);
        return mapCollection;
    }

    /**
     * Check if map markers contains a marker with specified class.
     *
     * @param markerClass Class to check if a marker from the collection is an instance of
     * @return Truth assignment, if there exists a marker that is an instance of the specified class
     */
    public boolean hasMarkerWithClass(int frameId, Class<?> markerClass) {
        for (Marker marker: getMapCollection(frameId)) {
            if (markerClass.isInstance(marker)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the collection of map markers for the specified frame id with the specified class.
     *
     * @param frameId Frame id to search collection for
     * @param markerClass Class of markers to get
     * @return Collection of map markers with the specified class for the specified frame id
     */
    public <MarkerType> Collection<MarkerType> getMarkersWithClass(int frameId,
                                                  Class<MarkerType> markerClass) {
        Collection<MarkerType> markers = new HashSet<>();
        for (Marker marker: getMapCollection(frameId)) {
            if (markerClass.isInstance(marker)) {
                markers.add(markerClass.cast(marker));
            }
        }
        return markers;
    }

    /**
     * Clears the map markers for the specified frame id.
     *
     * @param frameId Frame id to remove map markers for
     */
    public void clearMapMarkers(int frameId) {
        for (Marker marker: getMapCollection(frameId)) {
            removeMarker(marker);
        }
        mapMarkers.remove(frameId);
    }

    /**
     * Gets or creates the map marker for the specified game save.
     *
     * @param gameSave Save to get map marker from
     * @return Map marker from the specified game save
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
     * Get the map markers from the current save of the save platform.
     *
     * @pre SavePlatform has save.
     * @return MapMarkers from the current save of the save platform
     * @throws NullPointerException if precondition fails.
     */
    public static MapMarkers getFromCurrentSave() {
        if (SavePlatform.hasSave()) {
            return getFromSave(SavePlatform.getSave());
        }
        throw new NullPointerException("Save platform does not have save to get map markers from.");
    }

    /**
     * Load map for a specified activity and frame id.
     *
     * @param appCompatActivity Activity to load map for
     * @param frameId Frame id to load map for
     */
    public void loadMap(AppCompatActivity appCompatActivity, int frameId) {
        // Set current app compat activity as map context for the map markers.
        this.currentMapContext = appCompatActivity;

        // Set truth assignment, if map context has dynamic location
        hasDynamicLocation = currentMapContext instanceof DynamicLocation;

        // Load the map with the specified frame id
        if (!MapManager.hasMapArea(frameId)) {
            Log.e(TAG, "Could not load map with frame id " + frameId
                    + " as no map area was set for it");
            return;
        }

        for (Marker marker: getMapCollection(frameId)) {
            loadMarkerOnCurrentMap(marker);
        }

        Log.i(TAG, "Loaded map markers for frame id " + frameId);
    }

    /**
     * Load marker in the current map context.
     *
     * @param marker Marker to load in current map context
     */
    private void loadMarkerOnCurrentMap(Marker marker) {
        // Check if a map area exists in the current map context for the markers frame id.
        if (!MapArea.hasMapArea(currentMapContext, marker.getFrameId())) {
            return;
        }

        // Load marker on the current map context
        marker.loadOnMapArea(currentMapContext);

        // Add location observer markers to receive location updates
        if (hasDynamicLocation && marker instanceof LocationObserver) {
            ((DynamicLocation) currentMapContext).addObserver((LocationObserver) marker);
        }
    }

}