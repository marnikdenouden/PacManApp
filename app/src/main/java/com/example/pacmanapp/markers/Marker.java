package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.map.MapManager;
import com.example.pacmanapp.map.MapPosition;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@SuppressLint("ViewConstructor")
public class Marker implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "Marker";
    private transient ImageView imageView;
    private final int frameId; // mapFrameId reference to the map area that marker is placed on
    private double latitude;
    private double longitude;
    private final int markerId;
    private int drawableId;
    protected boolean animate; // Determines if drawable gets animated

    //>>> Constructors for marker <<<//

    /**
     * Create a marker for specified context, activity and alike.
     *
     * @param frameId   FrameId reference to map area that the marker is placed on
     * @param latitude  Latitude used to position marker on map area
     * @param longitude Longitude used to position marker on map area
     * @param drawable  Drawable used as display for the marker
     * @param markerId  MarkerId set to ImageView for potential reference
     * @param context   Context in which the marker is created
     * @param animate   Boolean animate to state if drawable should animate
     */
    Marker(int frameId, double latitude, double longitude, @NotNull Drawable drawable, int markerId,
           @NotNull Context context, boolean animate) {
        // Set marker values
        this.animate = animate;
        this.frameId = frameId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerId = markerId;

        load(context);
        setDrawable(drawable);
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
     */
    Marker(int frameId, double latitude, double longitude, int markerId, boolean animate,
           @NotNull Context context) {
        // Set marker values
        this.animate = animate;
        this.frameId = frameId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerId = markerId;

        load(context);
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
     */
    Marker(int frameId, double latitude, double longitude, int drawableId, int markerId, boolean animate,
           Context context) {
        // Set marker values
        this.animate = animate;
        this.frameId = frameId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.drawableId = drawableId;
        this.markerId = markerId;

        load(context);
    }

    // TODO can not serialize this class as it does not have a no-arg constructor. Maybe a good opportunity to rewrite and include a good way to load and unload a set of markers?
    //  Maybe we can use visibility of GONE and visible to load and unload?
    /**
     * Add marker to map area from frame Id reference.
     */
    void addToMapArea(int frameId) {
        if (imageView.getParent() != null) {
            return; // Marker already has a parent
        }
        if (!MapManager.hasMapArea(frameId)) {
            return; // Frame id does not have a map area attached
        }
        MapManager.getMapArea(frameId).addMarker(this);
    }

    //>>> Methods to set marker values <<<//

    /**
     * Get the pixel width for the marker.
     *
     * @return pixel width in int
     */
    int getPixelWidth() {
        return (int) imageView.getResources().getDimension(R.dimen.defaultMarkerSize);
    }

    /**
     * Get the pixel height for the marker.
     *
     * @return pixel height in int
     */
    int getPixelHeight() {
        return (int) imageView.getResources().getDimension(R.dimen.defaultMarkerSize);
    }

    /**
     * Get the width from the imageView.
     *
     * @return pixel width in int
     */
    public int getWidth() {
        return imageView.getWidth();
    }

    /**
     * Get the height from the imageView.
     *
     * @return pixel height in int
     */
    public int getHeight() {
        return imageView.getHeight();
    }

    /**
     * Get the frame id for the marker.
     *
     * @return Frame id of the map area that the marker is placed in
     */
    public int getFrameId() {
        return frameId;
    }

    /**
     * Get the latitude for the marker.
     *
     * @return Latitude of the marker location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get the longitude for the marker.
     *
     * @return Longitude of the marker location
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Set drawable for given drawable Id.
     *
     * @param drawableId Id to set drawable with
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
        imageView.setImageDrawable(drawable);
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
                AnimationDrawable animationDrawable = ((AnimationDrawable) imageView.getDrawable());

                // Start the animation drawable
                animationDrawable.start();
            } catch(ClassCastException classCastException) {
                Log.e(TAG, "Could not cast drawable to animationDrawable," +
                        " while animate is set to true");
            }
        }

    }

    /**
     * Set the id for this marker.
     *
     * @param markerId marker id to set
     */
    private void setMarkerId(int markerId) {
        imageView.setId(markerId);
    }

    /**
     * Set layout params for marker in relative layout.
     */
    private void setLayoutParams(double latitude, double longitude) {
        // Set the marker clickable and focusable
        imageView.setClickable(true);
        imageView.setFocusable(true);

        // Create the relative layout params with specified height and width
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(getPixelWidth(), getPixelHeight());

        // Create map location from latitude and longitude
        MapPosition mapPosition = MapPosition.getPosition(frameId, latitude, longitude,
                getPixelWidth(), getPixelHeight());

        // set the margins of the layout params
        layoutParams.setMargins(mapPosition.getX(), mapPosition.getY(), 0, 0);
        imageView.setLayoutParams(layoutParams);
    }

    /**
     * Get the image view of the marker.
     *
     * @return ImageView of the marker.
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Context of the image view.
     *
     * @return Context of the image view
     */
    public Context getContext() {
        return imageView.getContext();
    }

    //>>> place marker methods <<<//

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
        imageView.setX(mapPosition.getX());
        imageView.setY(mapPosition.getY());
    }

    //>>> Public methods to use on marker <<<//

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
        int xPosition = (int) imageView.getX();
        int yPosition = (int) imageView.getY();
        return new MapPosition(xPosition, yPosition);
    }

    /**
     * Get the distance to another marker.
     *
     * @param marker Marker to get distance to
     * @return Distance between this marker and the specified marker.
     */
    public float distanceTo(Marker marker) {
        return distanceTo(marker.latitude, marker.longitude);
    }

    /**
     * Get the distance to another location.
     *
     * @param latitude to get distance to
     * @param longitude to get distance to
     * @return Distance between this marker and the specified location.
     */
    public float distanceTo(double latitude, double longitude) {
        float[] results = new float[3];
        Location.distanceBetween(this.latitude, this.longitude, latitude, longitude, results);
        return results[0];
    }

    /**
     * Load the marker for the given context.
     *
     * @param context Context to load marker in
     */
    void load(Context context) {
        imageView = new ImageView(context);

        // Instantiate marker and imageView values
        addToMapArea(frameId);
        setLayoutParams(latitude, longitude);
        setMarkerId(markerId);
        if (drawableId != 0) {
            setDrawable(drawableId);
        }
    }

}

