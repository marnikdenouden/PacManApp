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
    //Map<Class<? extends Marker>, Collection<? extends Marker>> markerCollectionMap;
    private transient LocationUpdater locationUpdater;

    private final Collection<Marker> mapMarkers;

    /**
     * Create a collection of markers that can be serialized.
     *
     * @param saveName String name of the game save to add map marker to.
     */ // TODO update java docs
    public MapMarkers(String saveName, Context context) {
        super(saveName, context);
        mapMarkers = new HashSet<>();
        //markerCollectionMap = new HashMap<>();
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


//    /**
//     * Add marker to marker collection for the map.
//     *
//     * @param marker Marker to add to the collection
//     * @param <Type> Type of the marker
//     */
//    public <Type extends Marker> void addMarker(Type marker) { // TODO test if this method is not extremely flawed, also used in GameSave class
//        Class<? extends Marker> markerType = marker.getClass();
//        if (markerCollectionMap.containsKey(markerType)) {
//            @SuppressWarnings("unchecked")
//            Collection<Type> collection = (Collection<Type>) markerCollectionMap.get(markerType);
//            assert collection != null;
//            collection.add(marker);
//            return;
//        }
//        Collection<Type> collection = new HashSet<>();
//        markerCollectionMap.put(markerType, collection);
//        collection.add(marker);
//    }
//
//    /**
//     * Removes a marker from the marker collection for the map.
//     *
//     * @param marker Marker to remove from the collection
//     * @param <Type> Type of the marker
//     */
//    public <Type extends Marker> void removeMarker(Type marker) {
//        Class<? extends Marker> markerType = marker.getClass();
//        if (markerCollectionMap.containsKey(markerType)) {
//            @SuppressWarnings("unchecked")
//            Collection<Type> collection = (Collection<Type>) markerCollectionMap.get(markerType);
//            assert collection != null;
//            collection.remove(marker);
//        }
//    }

//    /**
//     * Loads all the markers to the view by adding them to their map area.
//     *
//     * @param context Context to load markers with
//     */
//    @Override
//    public void loadObjects(Context context) { // TODO note, injecting context here allows the map markers to be loaded in different contexts without having to reconstruct this class.
//        //Map<Class<? extends Marker>, Collection<? extends Marker>> newMarkerCollectionMap = new HashMap<>();
//        for (Collection<? extends Marker> collection: markerCollectionMap.values()) {
//            loadMarkers(collection, context);
//        }
//    }
//
//    /**
//     * Load all the markers of a collection with a specified marker type.
//     *
//     * @param collection Collection of markers of same type to load
//     * @param context Context to load markers with
//     * @param <Type> Type of the markers in the collection to load
//     */
//    public <Type extends Marker> void loadMarkers(Collection<Type> collection, Context context) {
//        for (Type marker: collection) {
//            Method loadMethod = null;
//            try {
//                loadMethod = marker.getClass().getMethod("load", (Class<?>[]) new Class[]{Context.class});
//                if (loadMethod.getReturnType().equals(marker.getClass())) {
//                    marker = (Type) loadMethod.invoke(marker, context);
//                    //if (marker instanceof LocationObserver) { // TODO location updater needs to be set before we can add the markers as listener...
//                    //    locationUpdater.addListener((LocationObserver) marker);
//                    //}
//                }
//            } catch (NoSuchMethodException | SecurityException e) {
//                // Your exception handling goes here
//            } catch (InvocationTargetException e) {
//                throw new RuntimeException(e);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

//    /**
//     * Write the marker object to the object output stream.
//     *
//     * @param objectOutputStream Object output stream to write to
//     * @throws IOException Thrown when exception occurs
//     */
//    private void writeObject(ObjectOutputStream objectOutputStream)
//            throws IOException {
//        // Should store the marker collection map in the data stream.
//    }
//
//    /**
//     * Read the marker object from the object input stream.
//     *
//     * @param objectInputStream Object input stream to read from
//     * @throws ClassNotFoundException Thrown when class was not found
//     * @throws IOException Thrown when exception occurs
//     */
//    private void readObject(ObjectInputStream objectInputStream)
//            throws ClassNotFoundException, IOException {
//        // Should recreate the marker collection map from the data stream.
//    }

}
