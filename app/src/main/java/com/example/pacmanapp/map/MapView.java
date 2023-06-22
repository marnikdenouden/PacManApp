package com.example.pacmanapp.map;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;

@SuppressLint("ViewConstructor")
public class MapView extends androidx.appcompat.widget.AppCompatImageView {
    private final static String TAG = "MapView";
    private final static int mapDrawable = R.drawable.map; // Drawable for the map
    private final static int mapViewId = R.id.mapView;

    MapView(MapArea mapArea) {
        super(mapArea.getContext());

        // Setup the image view
        setViewValues();
        setMapDrawable();
        setLayoutParams();

        // Center the image and adjust the bounds to the image size
        setScaleType(ImageView.ScaleType.CENTER);
        setAdjustViewBounds(true);

        // Add map view to the map area on the background
        mapArea.addView(this, 0);
    }

    /**
     * Set values for the view, such as id.
     */
    private void setViewValues() {
        setId(mapViewId);
        setClickable(false);
        setFocusable(false);
    }

    /**
     * Add the map image to this map view.
     */
    private void setMapDrawable() {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),
                mapDrawable, getContext().getTheme());
        setImageDrawable(drawable);
    }

    /**
     * Set the layout params for this map view.
     */
    private void setLayoutParams() {
        // Create layout params with default map size
        int mapSize = getResources().getDimensionPixelSize(R.dimen.defaultMapSize);
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(mapSize, mapSize);

        // Set the layout parameters to the map view
        setLayoutParams(layoutParams);
    }

}
