package com.example.pacmanapp.markers;

import android.content.Context;

import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.location.LocationUpdater;
import com.example.pacmanapp.storage.SaveManager;
import com.example.pacmanapp.storage.SaveObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class MapMarkers extends SaveObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient LocationUpdater locationUpdater;
    private final Collection<Marker> mapMarkers;

    /**
     * Create a collection of markers that can be serialized.
     *
     * @param saveManager SaveManager to add map markers to current save of.
     */
    public MapMarkers(SaveManager saveManager) {
        super(saveManager);
        mapMarkers = new HashSet<>();
    }

    /**
     * Add marker to marker collection for the map.
     *
     * @param marker Marker to add to the collection
     */
    public void addMarker(Marker marker) {
        mapMarkers.add(marker);
        if (locationUpdater != null && marker instanceof LocationObserver) {
            locationUpdater.addListener((LocationObserver) marker);
        }
    }

    /**
     * Removes a marker from the marker collection for the map.
     *
     * @param marker Marker to add to the collection
     */
    public void removeMarker(Marker marker) {
        mapMarkers.remove(marker);
    }

    /**
     * Loads all the markers to the view by adding them to their map area.
     *
     * @param context Context to load markers with
     */
    public void load(Context context) {
        for (Marker marker: mapMarkers) {
            marker.load(context);
        }
    }

    /**
     * Set the location updater for the map markers.
     *
     * @param locationUpdater Location updater to add markers to
     */
    public void setLocationUpdater(LocationUpdater locationUpdater) {
        this.locationUpdater = locationUpdater;
        for (Marker marker: mapMarkers) {
            if (marker instanceof LocationObserver) {
                locationUpdater.addListener((LocationObserver) marker);
            }
        }
    }

}
