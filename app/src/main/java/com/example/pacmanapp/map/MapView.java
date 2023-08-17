package com.example.pacmanapp.map;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;

@SuppressLint("ViewConstructor")
public class MapView extends androidx.appcompat.widget.AppCompatImageView {
    private final static String TAG = "MapView";
    private final static int mapViewId = R.id.mapView;

    MapView(MapArea mapArea) {
        super(mapArea.getContext());

        // Setup the image view
        setViewValues();
        setMapDrawable(mapArea.getMapType());
        setLayoutParams(mapArea.getMapType());

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
    private void setMapDrawable(MapType mapType) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),
                mapType.getDrawable(), getContext().getTheme());
        if (drawable == null) {
            Log.e(TAG, "Could not set drawable for map, as drawable of map type " +
                    mapType.name() + " is null");
            return;
        }
        drawable.setTint(mapType.getLineColor(getContext()));
        setImageDrawable(drawable);
    }

    /**
     * Set the layout params for this map view.
     */
    private void setLayoutParams(MapType mapType) {
        // Create layout params with the map size
        int width = getResources().getDimensionPixelSize(mapType.getWidth());
        int height = getResources().getDimensionPixelSize(mapType.getHeight());
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(width, height);

        // Set the layout parameters to the map view
        setLayoutParams(layoutParams);
    }

}
