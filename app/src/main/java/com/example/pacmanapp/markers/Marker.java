package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapPosition;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectionCrier;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Marker implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "Marker";
    private double latitude;
    private double longitude;
    private final int markerId;
    private final int drawableId; // Should always be able to be used as icon for the marker
    private boolean displayOnTop = false;
    private boolean animate = false;
    private transient Collection<LocationListener> locationListeners;

    //>>> Constructors for marker <<<//

    /**
     * Create a marker for specified context, activity and alike.
     *
     * @param latitude   Latitude used to position marker on map area
     * @param longitude  Longitude used to position marker on map area
     * @param drawableId DrawableId used to get drawable to display for the marker
     * @param markerId   MarkerId set to ImageView for potential reference
     */
    Marker(double latitude, double longitude, int drawableId, int markerId) {
        // Set marker values
        this.latitude = latitude;
        this.longitude = longitude;
        this.drawableId = drawableId;
        this.markerId = markerId;
        locationListeners = new ArrayList<>();
    }

    //>>> Methods for marker creation <<<//

    /**
     * Get the marker view from the current values.
     *
     * @param mapArea Map Area to create the marker view with
     * @return markerView Marker view that was created
     */
    protected MarkerView createView(@NotNull MapArea mapArea) {
        MarkerView markerView = new MarkerView(mapArea, this);
        markerView.createView();
        return markerView;
    }

    /**
     * Add the marker view to the specified view group.
     *
     * @param mapArea Map Area to create the marker view with
     * @param viewGroup View group to add marker view to
     */
    public MarkerView addView(@NotNull MapArea mapArea, @NotNull ViewGroup viewGroup) {
        MarkerView markerView = createView(mapArea);
        if (displayOnTop) {
            viewGroup.addView(markerView);
        } else {
            viewGroup.addView(markerView, 0);
        }
        markerView.addOnLayoutChangeListener(
                (view, i, i1, i2, i3, i4, i5, i6, i7) -> markerView.updatePlacement());
        return markerView;
    }

    // TODO instead of loading a marker to a single map it would be better to allow for a listener to update on the location changes the marker makes.
    //  Marker should be the truth instance and be a data storage to use instead of actively trying to update UI itself.
    //  So it should have a geo-location, image view representation to get and other values or methods to compare.
    //  Getting image view could also be more dynamic, like having parameter scale(for width and height), and if it is marked selected or not.
    //  Of course scale parameter should be constrained to have minimum/maximum width and height, which can also be defined per marker.

    /**
     * Set if the drawable should animate, class default is false.
     *
     * @param animate Truth assignment, if drawable should animate
     */
    protected void setAnimate(boolean animate) {
        this.animate = animate;
    }

    /**
     * Check if drawable is animated.
     *
     * @return Truth assignment, if drawable is animated.
     */
    public boolean isAnimated() {
        return animate;
    }

    /**
     * Set if the drawable should be displayed on top, class default is false.
     *
     * @param displayOnTop Truth assignment, if drawable should be displayed on top
     */
    protected void setDisplayOnTop(boolean displayOnTop) {
        this.displayOnTop = displayOnTop;
    }

    /**
     * Get the marker id.
     *
     * @return Marker id
     */
    public int getMarkerId() {
        return markerId;
    }

    //>>> Methods to set marker values <<<//

    /**
     * Get the pixel width for the marker.
     *
     * @param context Context to get the dimension with
     * @return pixel width in int
     */
    int getPixelWidth(@NotNull Context context) {
        return (int) context.getResources().getDimension(R.dimen.defaultMarkerSize);
    }

    /**
     * Get the pixel height for the marker.
     *
     * @param context Context to get the dimension with
     * @return pixel height in int
     */
    int getPixelHeight(@NotNull Context context) {
        return (int) context.getResources().getDimension(R.dimen.defaultMarkerSize);
    }

    /**
     * Get latitude location of the marker.
     *
     * @return double latitude location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get longitude location of the marker.
     *
     * @return double longitude location
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Get the drawable id of the marker.
     * (Can be used as icon for the marker)
     *
     * @return Drawable id of the marker
     */
    public int getDrawableId() {
        return drawableId;
    }

    /**
     * Set marker latitude and longitude to the specified values.
     *
     * @param latitude y axis location of the marker.
     * @param longitude x axis location of the marker.
     */
    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        while(locationListeners.contains(null)) {
            locationListeners.remove(null);
        };
        for (LocationListener listener: locationListeners) {
            listener.onLocationChange(latitude, longitude);
        }
    }

    /**
     * Get the centered map position of the marker on specified the map area.
     *
     * @param mapArea Map area to get map position from
     * @param context Context to get marker size from to get central position
     * @return MapPosition of current latitude and longitude on the map area
     */
    public MapPosition getMapPosition(@NotNull MapArea mapArea, @NotNull Context context) {
        return MapPosition.getPosition(mapArea, getLatitude(),
                getLongitude(), getPixelWidth(context),
                getPixelHeight(context));
    }

    /**
     * Add location listener to the marker.
     *
     * @param listener Listener to receive future location updates
     */
    void addLocationListener(@NotNull LocationListener listener) {
        locationListeners.add(listener);
    }

    /**
     * Remove location listener to the marker.
     *
     * @param listener Listener to remove from listeners collection
     */
    void removeLocationListener(@NotNull LocationListener listener) {
        locationListeners.remove(listener);
    }

    /**
     * Serializable read object method, constructor for deserialization.
     *
     * @param objectInputStream Input stream of objects to reconstruct class with
     */
    private void readObject(ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        locationListeners = new ArrayList<>();
    }

    public interface LocationListener {
        /**
         * Called when location of marker changes.
         *
         * @param latitude New y axis location of the marker
         * @param longitude New x axis location of the marker
         */
        void onLocationChange(double latitude, double longitude);
    }

    @SuppressLint("ViewConstructor")
    public static class MarkerView extends AppCompatImageView implements LocationListener {
        private final String TAG = "MarkerView";

        protected final MapArea mapArea;
        protected Marker marker;

        /**
         * Create the marker view for the specified map area and marker.
         *
         * @param mapArea Map area to create view for
         * @param marker Marker to create view for
         * @return markerView MarkerView that is created
         */
        protected MarkerView(@NotNull MapArea mapArea, @NotNull Marker marker) {
            super(mapArea.getContext());
            this.mapArea = mapArea;
            this.marker = marker;
            marker.addLocationListener(this);
        }

        /**
         * Get the marker that the marker view is based on.
         *
         * @return Marker that the view is based on
         */
        private Marker getMarker() {
            return marker;
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            marker.removeLocationListener(this);
        }

        /**
         * Create the marker view.
         *
         * @return markerView Marker view that has been created
         */
        protected void createView() {
            setOnClickListener(this::onClick);

            setOnLongClickListener(view -> {
                onLongClick(view);
                return true;
            });

            setLayoutParams();
            setId(marker.getMarkerId());
            setDrawable(marker.getDrawableId());
        }

        /**
         * Set drawable for given drawable Id.
         *
         * @param drawableId Id to set drawable with
         */
        private void setDrawable(int drawableId) {
            Drawable drawable = AppCompatResources.getDrawable(getContext(), drawableId);
            if (drawable == null) {
                Log.e(TAG, "Could not set drawable on marker for id: " + drawableId);
            }
            setDrawable(drawable);
        }

        /**
         * Set drawable for given drawable
         *
         * @param drawable Drawable to set for view
         */
        protected void setDrawable(Drawable drawable) {
            setImageDrawable(drawable);
            tryAnimate();
        }

        /**
         * Try to animate the animation drawable.
         */
        void tryAnimate() {
            // If animate is true and drawable an instance of animation drawable,
            // then cast and start to animate the drawable.
            if (marker.isAnimated() && getDrawable() instanceof AnimationDrawable) {
                // Cast the drawable to animation drawable
                AnimationDrawable animationDrawable = ((AnimationDrawable) getDrawable());

                // Start the animation drawable
                animationDrawable.start();
            }
        }

        @Override
        public void onLocationChange(double latitude, double longitude) {
            updatePlacement();
        }

        /**
         * Update placement of the marker view in the map area with the current position.
         */
        public void updatePlacement() {
            MapPosition mapPosition = getMapPosition();
            setX(mapPosition.getX());
            setY(mapPosition.getY());
        }

        /**
         * Get the map position for the marker view on the current location on the map area.
         *
         * @return MapPosition of current latitude and longitude on the map area
         */
        public MapPosition getMapPosition() {
            return MapPosition.getPosition(mapArea, marker.getLatitude(),
                    marker.getLongitude(), getWidth(), getHeight());
        }

        /**
         * Set layout params for marker in relative layout.
         */
        private void setLayoutParams() {
            // Set the marker clickable and focusable
            setClickable(true);
            setFocusable(true);

            // Create the relative layout params with specified height and width
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(marker.getPixelWidth(getContext()),
                            marker.getPixelHeight(getContext()));

            // Create map location from latitude and longitude
            MapPosition mapPosition = getMapPosition();

            // set the margins of the layout params
            layoutParams.setMargins(mapPosition.getX(), mapPosition.getY(), 0, 0);
            setLayoutParams(layoutParams);
        }

        /**
         * On click method called for the marker.
         *
         * @param view View from the click event
         */
        public void onClick(View view) {
            if (marker instanceof Selectable) {
                SelectionCrier.getInstance().select((Selectable) marker);
                // TODO Display marker as selected and set on next selection listener to remove selected look.
            }
        }

        /**
         * On long click method called for the marker.
         *
         * @param view View from the long click event
         */
        public void onLongClick(View view) {
            if (this instanceof Selectable) {
                InspectActivity.open((AppCompatActivity) view.getContext(), (Selectable) this);
            }
        }

        /**
         * Get the real distance to another marker.
         *
         * @param marker Marker to get distance to
         * @return Distance between the marker from this view and the specified marker.
         */
        public float distanceTo(Marker marker) {
            return distanceTo(marker.getLatitude(), marker.getLongitude());
        }

        /**
         * Get the real distance to another location.
         *
         * @param latitude  to get distance to
         * @param longitude to get distance to
         * @return Distance between the marker from this view and the specified location.
         */
        public float distanceTo(double latitude, double longitude) {
            float[] results = new float[3];
            Location.distanceBetween(marker.getLatitude(), marker.getLongitude(), latitude, longitude, results);
            return results[0];
        }

    }
}

