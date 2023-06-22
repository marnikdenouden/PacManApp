package com.example.pacmanapp.map;

import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pacmanapp.R;

public class MarkerLayout extends RelativeLayout {
    private final static String TAG = "MarkerLayout";
    private final static int markerLayoutId = R.id.markerLayout;
    private final MapArea mapArea;

    public MarkerLayout(MapArea mapArea) {
        super(mapArea.getContext());
        this.mapArea = mapArea;

        setId(markerLayoutId);

        setLayoutParams(mapArea.getMapView());

        // Add marker layout to the map area
        mapArea.addView(this);
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

    @Override
    public boolean performClick() {
        return false; // Marker layout should not be clicked
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false; // Marker layout should not be touched
    }


}
