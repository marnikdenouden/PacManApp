package com.example.pacmanapp.map;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pacmanapp.R;
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.markers.Marker;
import com.example.pacmanapp.selection.SelectionCrier;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressLint("ViewConstructor")
public class MarkerLayout extends RelativeLayout implements MapMarkers.MarkerListener {
    private final static String TAG = "MarkerLayout";
    private final MapArea mapArea;
    private final Map<Marker, Marker.MarkerView> markerMap = new HashMap<>();
    private final static int markerLayoutId = R.id.markerLayout;

    public MarkerLayout(@NotNull MapArea mapArea) {
        super(mapArea.getContext());
        Log.d(TAG, "Constructing marker layout");
        this.mapArea = mapArea;

        // Setup the relative layout
        setViewValues();
        setLayoutParams(mapArea.getMapView());

        // Add marker layout to the map area
        mapArea.addView(this);
    }

    /**
     * Set values for the view, such as id.
     */
    private void setViewValues() {
        setId(markerLayoutId);
        setClickable(false);
        setFocusable(false);
    }

    /**
     * Set the layout params for this marker layout.
     *
     * @param mapView Map view to get constraints for
     */
    private void setLayoutParams(MapView mapView) {
        // Create layout params with 0dp width and height
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(0,0);

        // Set constraint to match the map view
        layoutParams.bottomToBottom = mapView.getId();
        layoutParams.startToStart = mapView.getId();
        layoutParams.endToEnd = mapView.getId();
        layoutParams.topToTop = mapView.getId();

        // Set the layout parameters to the marker layout
        setLayoutParams(layoutParams);
    }

    /**
     * Add marker to the marker layout.
     *
     * @param marker Marker to add view for
     */
    public void addMarker(@NotNull Marker marker) {
        if (markerMap.containsKey(marker)) {
            Log.w(TAG, "Marker of class " + marker.getClass().getSimpleName() +
                    " was already added to the marker layout, when trying to add it.");
            removeMarker(marker);
        }

        Marker.MarkerView markerView = marker.addView(mapArea, this);
        markerMap.put(marker, markerView);

        Log.d(TAG, "Added marker of class " + marker.getClass().getSimpleName() +
                " to the marker layout");
    }

    /**
     * Remove marker's view from the marker layout.
     *
     * @param marker Marker to remove view for
     */
    public void removeMarker(@NotNull Marker marker) {
        if (markerMap.containsKey(marker)) {
            Log.w(TAG, "Marker of class " + marker.getClass().getSimpleName() +
                    " could not be removed from marker layout as it was not stored.");
            return;
        }

        Marker.MarkerView markerView = markerMap.get(marker);
        removeView(markerView);

        Log.d(TAG, "Removed marker of class " + marker.getClass().getSimpleName() +
                " from the marker layout");
    }

    /**
     * Gets the map collection of marker views.
     *
     * @return Marker view collection that is currently stored
     */
    private Collection<Marker.MarkerView> getMarkerViews() {
        return markerMap.values();
    }

    /**
     * Gets a collection of the map marker views.
     *
     * @return new Set of the map marker view collection
     */
    public Collection<Marker.MarkerView> getMapMarkerViews() {
        return new HashSet<>(getMarkerViews());
    }

    /**
     * Check if marker views contains a view with specified class.
     *
     * @param markerViewClass Class to check if a marker view from the collection is an instance of
     * @return Truth assignment, if there exists a view that is an instance of the specified class
     */
    public boolean hasMarkerViewWithClass(Class<?> markerViewClass) {
        for (Marker.MarkerView markerView: getMarkerViews()) {
            if (markerViewClass.isInstance(markerView)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the collection of marker views with the specified class.
     *
     * @param markerViewClass Class of marker views to get
     * @return Collection of marker views with the specified class
     */
    public <ViewClass> Collection<ViewClass> getMarkerViewsWithClass(
            Class<ViewClass> markerViewClass) {
        Collection<ViewClass> markers = new HashSet<>();
        for (Marker.MarkerView markerView: getMarkerViews()) {
            if (markerViewClass.isInstance(markerView)) {
                markers.add(markerViewClass.cast(markerView));
            }
        }
        return markers;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllMarkers();
    }

    /**
     * Remove all (marker) views from the layout.
     */
    public void removeAllMarkers() {
        markerMap.clear();
        removeAllViews();
    }

}
