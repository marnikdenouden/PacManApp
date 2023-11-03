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

import java.util.HashMap;
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
