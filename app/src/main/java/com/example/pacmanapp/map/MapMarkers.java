package com.example.pacmanapp.map;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.markers.Marker;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectionCrier;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class MapMarkers implements Serializable, LocationObserver {
    private final static String TAG = "MapMarkers";
    private static final long serialVersionUID = 1L;
    private final List<Marker> mapMarkers;
    private transient Collection<MarkerListener> markerListeners;

    /**
     * Create a collection of markers that can be serialized.
     */
    public MapMarkers() {
        mapMarkers = new ArrayList<>();
        markerListeners = new HashSet<>();
    }

    /**
     * Add marker to marker collection for the map.
     *
     * @param marker Marker to add to the collection
     */
    public void addMarker(@NotNull Marker marker) {
        getMarkers().add(marker);
        while(markerListeners.contains(null)) {
            markerListeners.remove(null);
        }
        for (MarkerListener markerListener: markerListeners) {
            markerListener.addMarker(marker);
        }
        // Select the marker if they are a selectable
        if (marker instanceof Selectable) { // TODO move this to a listener.
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
        getMarkers().remove(marker);
        while(markerListeners.contains(null)) {
            markerListeners.remove(null);
        }
        for (MarkerListener markerListener: markerListeners) {
            markerListener.removeMarker(marker);
        }
        Log.i(TAG, "Marker of class " + marker.getClass().getSimpleName()
                + " was removed from the collection");
    }

    /**
     * Gets the map collection of markers.
     *
     * @return Marker collection that is currently stored
     */
    private Collection<Marker> getMarkers() {
        return mapMarkers;
    }

    /**
     * Gets a collection of the map markers.
     *
     * @return new Set of the map markers collection
     */
    public Collection<Marker> getMapMarkers() {
        return new HashSet<>(getMarkers());
    }

    /**
     * Check if map markers contains a marker with specified class.
     *
     * @param markerClass Class to check if a marker from the collection is an instance of
     * @return Truth assignment, if there exists a marker that is an instance of the specified class
     */
    public boolean hasMarkerWithClass(Class<?> markerClass) {
        for (Marker marker: getMarkers()) {
            if (markerClass.isInstance(marker)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the collection of map markers with the specified class.
     *
     * @param markerClass Class of markers to get
     * @return Collection of map markers with the specified class
     */
    public <MarkerType> Collection<MarkerType> getMarkersWithClass(Class<MarkerType> markerClass) {
        Collection<MarkerType> markers = new HashSet<>();
        for (Marker marker: getMarkers()) {
            if (markerClass.isInstance(marker)) {
                markers.add(markerClass.cast(marker));
            }
        }
        return markers;
    }

    // TODO add methods here for updating markers and such to be used in game save.

    @Override
    public void onLocationUpdate(@NonNull Location location) {
        for (LocationObserver marker: getMarkersWithClass(LocationObserver.class)) {
            marker.onLocationUpdate(location);
        }
    }

    /**
     * Add marker listener to receive future marker updates.
     *
     * @param markerListener MarkerListener to receive future marker updates.
     */
    public void addMarkerListener(@NotNull MarkerListener markerListener) {
        markerListeners.add(markerListener);
    }

    /**
     * Remove marker listener to stop receiving future marker updates.
     *
     * @param markerListener MarkerListener to remove from being called.
     */
    public void removeMarkerListener(@NotNull MarkerListener markerListener) {
        markerListeners.add(markerListener);
    }

    /**
     * Serializable read object method, constructor for deserialization.
     *
     * @param objectInputStream Input stream of objects to reconstruct class with
     */
    private void readObject(ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        markerListeners = new HashSet<>();
    }

    /**
     * Interface Marker listener to notify adding or removing a marker.
     */
    public interface MarkerListener {
        void addMarker(@NotNull Marker marker);
        void removeMarker(@NotNull Marker marker);
    }
}