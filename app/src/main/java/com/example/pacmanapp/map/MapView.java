package com.example.pacmanapp.map;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;

public class MapView extends androidx.appcompat.widget.AppCompatImageView {
    private final static String TAG = "MapView";
    private final static int mapDrawable = R.drawable.map; // Drawable for the map
    private final static int mapViewId = R.id.mapView;
    private final MapArea mapArea;

    MapView(MapArea mapArea) {
        super(mapArea.getContext());
        this.mapArea = mapArea;

        // Setup the image view
        setId(mapViewId);
        setMapDrawable();
        setLayoutParams(mapArea);

        // Center the image and adjust the bounds to the image size
        setScaleType(ImageView.ScaleType.CENTER);
        setAdjustViewBounds(true);

        // Add map view to the map area on the background
        mapArea.addView(this, 0);
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
     *
     * @param mapArea Map area to get constraints for
     */
    private void setLayoutParams(MapArea mapArea) {
        // Create layout params with default map size
        int mapSize = getResources().getDimensionPixelSize(R.dimen.defaultMapSize);
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(mapSize, mapSize);

        // Set constraint to match the map area
        //layoutParams.bottomToBottom = mapArea.getId();
        //layoutParams.startToStart = mapArea.getId();
        //layoutParams.endToEnd = mapArea.getId();
        //layoutParams.topToTop = mapArea.getId();

        // Set the layout parameters to the map view
        setLayoutParams(layoutParams);
    }

    @Override
    public boolean performClick() {
        return false; // Map image should not be clicked
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false; // Map image should not be touched
    }

}
