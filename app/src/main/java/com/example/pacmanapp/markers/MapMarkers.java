package com.example.pacmanapp.markers;

import android.content.Context;
import android.location.Location;

import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.location.LocationUpdater;
import com.example.pacmanapp.storage.SaveManager;
import com.example.pacmanapp.storage.SaveObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MapMarkers extends SaveObject implements Serializable {
    private static final long serialVersionUID = 1L;
// TODO add code to do updates on all of type x. For example move all characters.

    // TODO currently being assumed is that the marker collection map has the key class equal to the class of the collection object.
    //  Furthermore we assume that each class that extends Marker has a method load that returns an loaded instance of its own class.
    private transient LocationUpdater locationUpdater;

    private final Collection<Marker> mapMarkers;

    /**
     * Create a collection of markers that can be serialized.
     *
     * @param saveName String name of the game save to add map marker to
     * @param context Context to create save object with
     */
    public MapMarkers(String saveName, Context context) {
        super(saveName, context);
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
    public MapMarkers load(Context context) {
        MapMarkers newMapMarkers = new MapMarkers(getSaveName(), context);
        for (Marker marker: mapMarkers) {
            newMapMarkers.addMarker(marker.load(context));
        }
        return newMapMarkers;
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
