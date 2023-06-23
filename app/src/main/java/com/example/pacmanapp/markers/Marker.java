package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapManager;
import com.example.pacmanapp.map.MapPosition;

import org.jetbrains.annotations.NotNull;

@SuppressLint("ViewConstructor")
public class Marker extends androidx.appcompat.widget.AppCompatImageView {
    private final String TAG = "Marker";
    private double latitude;
    private double longitude;
    protected final int frameId; // mapFrameId reference to the map area that marker is placed on
    private final boolean animate; // Determines if drawable gets animated

    /**
     * Create a marker for specified context, activity and alike.
     *
     * @param frameId   FrameId reference to map area that the marker is placed on
     * @param latitude  Latitude used to position marker on map area
     * @param longitude Longitude used to position marker on map area
     * @param drawable  Drawable used as display for the marker
     * @param markerId  MarkerId set to ImageView for potential reference
     * @param context   Context in which the marker is created
     * @param activity  Activity in which the marker is placed
     * @param animate   Boolean animate to state if drawable should animate
     */
    Marker(int frameId, double latitude, double longitude, @NotNull Drawable drawable, int markerId,
           @NotNull Context context, @NotNull AppCompatActivity activity, boolean animate) {
        super(context);
        // Set marker values
        this.frameId = frameId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.animate = animate;

        // Set imageView values
        setLayoutParams(latitude, longitude, activity);
        setId(markerId);
        setDrawable(drawable);

        // Add marker to map area
        MapManager.getMapArea(frameId).addMarker(this);
    }

    /**
     * Create a marker for specified context, activity and alike.
     *
     * @param frameId   FrameId reference to map area that the marker is placed on
     * @param latitude  Latitude used to position marker on map area
     * @param longitude Longitude used to position marker on map area
     * @param markerId  MarkerId set to ImageView for potential reference
     * @param animate   Boolean animate to state if drawable should animate
     * @param context   Context in which the marker is created
     * @param activity  Activity in which the marker is placed
     */
    Marker(int frameId, double latitude, double longitude, int markerId, boolean animate,
           @NotNull Context context, @NotNull AppCompatActivity activity) {
        super(context);
        // Set marker values
        this.frameId = frameId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.animate = animate;

        // Set imageView values
        setLayoutParams(latitude, longitude, activity);
        setId(markerId);

        // Add marker to map area
        MapManager.getMapArea(frameId).addMarker(this);
    }

    /**
     * Create a marker for specified context, activity and alike.
     *
     * @param frameId    FrameId reference to map area that the marker is placed on
     * @param latitude   Latitude used to position marker on map area
     * @param longitude  Longitude used to position marker on map area
     * @param drawableId DrawableId used to get drawable to display for the marker
     * @param markerId   MarkerId set to ImageView for potential reference
     * @param animate    Boolean animate to state if drawable should animate
     * @param context    Context in which the marker is created
     * @param activity   Activity in which the marker is placed
     */
    Marker(int frameId, double latitude, double longitude, int drawableId, int markerId, boolean animate,
           Context context, AppCompatActivity activity) {
        super(context);
        // Set marker values
        this.frameId = frameId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.animate = animate;

        // Set imageView values
        setLayoutParams(latitude, longitude, activity);
        setId(markerId);
        setDrawable(drawableId);

        // Add marker to map area
        MapManager.getMapArea(frameId).addMarker(this);
    }

    /**
     * Get the pixel width for the marker.
     *
     * @param activity Activity to get dimension from
     * @return pixel width in int
     */
    int getPixelWidth(AppCompatActivity activity) {
        return (int) activity.getResources().getDimension(R.dimen.defaultMarkerSize);
    }

    /**
     * Get the pixel height for the marker.
     *
     * @param activity Activity to get dimension from
     * @return pixel height in int
     */
    int getPixelHeight(AppCompatActivity activity) {
        return (int) activity.getResources().getDimension(R.dimen.defaultMarkerSize);
    }

    /**
     * Set drawable for given drawable Id.
     *
     * @param drawableId Id to get drawable with
     */
    void setDrawable(int drawableId) {
        Drawable drawable = AppCompatResources.getDrawable(getContext(), drawableId);
        if (drawable == null) {
            Log.e(TAG, "Could not set drawable on marker for id: " + drawableId);
            return;
        }
        setDrawable(drawable);
    }

    /**
     * Set drawable for given drawable
     *
     * @param drawable Drawable to set.
     */
    void setDrawable(@NotNull Drawable drawable) {
        setImageDrawable(drawable);
        tryAnimate(animate);
    }

    /**
     * Try to animate the animation drawable.
     *
     * @param animate Boolean if the marker is set to animate
     */
    void tryAnimate(boolean animate) {
        // If animate is true, cast and start to animate the drawable.
        if (animate) {
            try {
                // Cast the drawable to animation drawable
                AnimationDrawable animationDrawable = ((AnimationDrawable) getDrawable());

                // Start the animation drawable
                animationDrawable.start();
            } catch(ClassCastException classCastException) {
                Log.e(TAG, "Could not cast drawable to animationDrawable," +
                        " while animate is set to true");
            }
        }

    }

    /**
     * Set layout params for marker in relative layout.
     *
     * @param latitude Latitude to use for marker map location
     * @param longitude Longitude to use for marker map location
     * @param activity Activity to use for map location context
     */
    private void setLayoutParams(double latitude, double longitude, AppCompatActivity activity) {
        // Set the marker clickable and focusable
        setClickable(true);
        setFocusable(true);

        // Create the relative layout params with specified height and width
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(getPixelWidth(activity), getPixelHeight(activity));

        // Create map location from latitude and longitude
        MapPosition mapPosition = MapPosition.getPosition(frameId, latitude, longitude,
                getPixelWidth(activity), getPixelHeight(activity));

        // set the margins of the layout params
        layoutParams.setMargins(mapPosition.getX(), mapPosition.getY(), 0, 0);
        setLayoutParams(layoutParams);
    }

    /**
     * Place the marker to a new location.
     *
     * @param location to place marker to
     */
    void place(@NotNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        place(latitude, longitude);
    }

    /**
     * Place the marker to a new location.
     *
     * @param latitude Used to set the y location
     * @param longitude Used to set the x location
     */
    void place(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        updatePlacement();
    }

    /**
     * Place the character to a new map location.
     *
     * @param mapPosition the new map location.
     */
    private void place(MapPosition mapPosition) {
        setX(mapPosition.getX());
        setY(mapPosition.getY());
    }

    /**
     * Update the marker position for its stored location.
     */
    public void updatePlacement() {
        MapPosition mapPosition = MapPosition.getPosition(frameId, latitude, longitude,
                getWidth(), getHeight());
        place(mapPosition);
    }

    /**
     * Get the map position of this marker.
     *
     * @return Map position of this marker
     */
    @NotNull
    public MapPosition getMapPosition() {
        int xPosition = (int) getX();
        int yPosition = (int) getY();
        return new MapPosition(xPosition, yPosition);
    }

    /**
     * Get the distance to another marker.
     *
     * @param marker Marker to get distance to
     * @return Distance between this marker and the specified marker.
     */
    public float distanceTo(Marker marker) {
        float[] results = new float[3];
        Location.distanceBetween(latitude, longitude, marker.latitude, marker.longitude, results);
        return results[0];
    }

}
