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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@SuppressLint("ViewConstructor")
public class Marker extends androidx.appcompat.widget.AppCompatImageView implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "Marker";
    protected int frameId; // mapFrameId reference to the map area that marker is placed on
    protected double latitude;
    protected double longitude;
    private int markerId;
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
        super(context);

        // Set marker values
        this.animate = animate;

        // Set imageView values
        addToMapArea(frameId);
        setLayoutParams(latitude, longitude);
        setId(markerId);
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
        super(context);

        // Set marker values
        this.animate = animate;

        // Set imageView values
        addToMapArea(frameId);
        setLayoutParams(latitude, longitude);
        setId(markerId);
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
        super(context);

        // Set marker values
        this.animate = animate;

        // Set imageView values
        addToMapArea(frameId);
        setLayoutParams(latitude, longitude);
        setMarkerId(markerId);
        setDrawable(drawableId);
    }

    // TODO can not serialize this class as it does not have a no-arg constructor. Maybe a good oppurtinity to rewrite and include a good way to load and unload a set of markers?

    /**
     * Add marker to map area from frame Id reference.
     */
    void addToMapArea(int frameId) {
        this.frameId = frameId;
        if (getParent() != null) {
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
        return (int) getResources().getDimension(R.dimen.defaultMarkerSize);
    }

    /**
     * Get the pixel height for the marker.
     *
     * @return pixel height in int
     */
    int getPixelHeight() {
        return (int) getResources().getDimension(R.dimen.defaultMarkerSize);
    }

    /**
     * Set drawable for given drawable Id.
     *
     * @param drawableId Id to set drawable with
     */
    void setDrawable(int drawableId) {
        this.drawableId = drawableId;
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
     * Set the id for this marker.
     *
     * @param markerId marker id to set
     */
    private void setMarkerId(int markerId) {
        this.markerId = markerId;
        setId(markerId);
    }

    /**
     * Set layout params for marker in relative layout.
     */
    private void setLayoutParams(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

        // Set the marker clickable and focusable
        setClickable(true);
        setFocusable(true);

        // Create the relative layout params with specified height and width
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(getPixelWidth(), getPixelHeight());

        // Create map location from latitude and longitude
        MapPosition mapPosition = MapPosition.getPosition(frameId, latitude, longitude,
                getPixelWidth(), getPixelHeight());

        // set the margins of the layout params
        layoutParams.setMargins(mapPosition.getX(), mapPosition.getY(), 0, 0);
        setLayoutParams(layoutParams);
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
        setX(mapPosition.getX());
        setY(mapPosition.getY());
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

    Marker load(Context context) {
        if (drawableId != 0) {
            return new Marker(frameId, latitude, longitude, drawableId, markerId, animate, context);
        } else {
            return new Marker(frameId, latitude, longitude, markerId, animate, context);
        } // TODO will create a new class, while it should instantiate this class again with an image view.
    }

    // TODO while this custom serialization could be good, it doesn't get the instances, which need to be given to map markers.
    /**
     * Write the marker object to the object output stream.
     *
     * @param objectOutputStream Object output stream to write to
     * @throws IOException Thrown when exception occurs
     */
    private void writeObject(ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.writeInt(frameId);
        objectOutputStream.writeDouble(latitude);
        objectOutputStream.writeDouble(longitude);
        objectOutputStream.writeInt(markerId);
        objectOutputStream.writeInt(drawableId);
        objectOutputStream.writeBoolean(animate);
    }

    /**
     * Read the marker object from the object input stream.
     *
     * @param objectInputStream Object input stream to read from
     * @throws ClassNotFoundException Thrown when class was not found
     * @throws IOException Thrown when exception occurs
     */
    private void readObject(ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        frameId = objectInputStream.readInt();
        latitude = objectInputStream.readDouble();
        longitude = objectInputStream.readDouble();
        markerId= objectInputStream.readInt();
        drawableId = objectInputStream.readInt();
        animate = objectInputStream.readBoolean();
    }

}
