package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.map.MapManager;
import com.example.pacmanapp.map.MapPosition;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectionCrier;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@SuppressLint("ViewConstructor")
public class Marker implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "Marker";
    private transient ImageView imageView;
    private final int frameId; // mapFrameId reference to the single map area,
                               //   that this marker is placed on
    private double latitude;
    private double longitude;
    private final int markerId;
    private final int drawableId; // Should always be able to be used as icon for the marker
    private boolean displayOnTop = false;
    private boolean animate = false;
    private transient Context context;

    //>>> Constructors for marker <<<//

    /**
     * Create a marker for specified context, activity and alike.
     *
     * @param frameId    FrameId reference to map area that the marker is placed on
     * @param latitude   Latitude used to position marker on map area
     * @param longitude  Longitude used to position marker on map area
     * @param drawableId DrawableId used to get drawable to display for the marker
     * @param markerId   MarkerId set to ImageView for potential reference
     * @param context    Context in which the marker is created
     */
    Marker(int frameId, double latitude, double longitude, int drawableId, int markerId,
           @NotNull Context context) {
        // Set marker values
        this.frameId = frameId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.drawableId = drawableId;
        this.markerId = markerId;
        this.context = context;
    }

    //>>> Methods for marker creation <<<//

    /**
     * Add the image view of the marker to a given map area.
     *
     * @pre Map manager has map are for markers frame id
     * @param context to load marker in
     */
    public void loadOnMapArea(Context context) {
        this.context = context;
        if (!MapManager.hasMapArea(frameId)) {
            return; // No map area available for the frame id
        }

        // Remove image view from the parent before adding marker to the map
        if (imageView != null && imageView.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) imageView.getParent();
            viewGroup.removeView(imageView);
        }

        // Create image view for the markers context
        createImageView(context);
        tryAnimate(animate);

        // Add the marker to the map area
        MapManager.getMapArea(frameId).addMarker(this, displayOnTop);
    }

    /**
     * Create the image view to display the marker with.
     *
     * @param context Context to create image view for
     */
    protected void createImageView(Context context) {
        // Create imageView for on the map area
        imageView = new ImageView(context);
        imageView.setOnClickListener(Marker.this::onClick);

        imageView.setOnLongClickListener(view -> {
            Marker.this.onLongClick(view);
            return true;
        });

        setLayoutParams(latitude, longitude);
        imageView.setId(markerId);
        setDrawable(drawableId);
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
     * Set the current imageView appearance for this marker.
     *
     * @pre imageView has been created for this marker
     *
     * @param drawable Drawable to set on imageView
     */
    protected void setDrawable(Drawable drawable) {
        if (imageView == null) {
            Log.e(TAG, "Could not set drawable, image view is null");
            return;
        }
        imageView.setImageDrawable(drawable);
    }

    /**
     * Set if the drawable should animate, class default is false.
     *
     * @param animate Truth assignment, if drawable should animate
     */
    protected void setAnimate(boolean animate) {
        this.animate = animate;
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
     * Get the marker id.
     *
     * @return Marker id
     */
    public int getMarkerId() {
        return markerId;
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
     * On click method called for the marker.
     *
     * @param view View from the click event
     */
    public void onClick(View view) {
        if (this instanceof Selectable) {
            SelectionCrier.getInstance().select((Selectable) this);
        }
    }

    /**
     * On long click method called for the marker.
     *
     * @param view View from the long click event
     */
    public void onLongClick(View view) {
        if (this instanceof Selectable) {
            SelectionCrier.getInstance().select((Selectable) this);
            Navigate.navigate((AppCompatActivity) view.getContext(), InspectActivity.class);
        }
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
     * Checks if marker belongs on map with frame id.
     *
     * @param frameId Frame id to check for
     * @return Truth assignment, if marker belongs on map with specified frame id
     */
    public boolean belongsOnMap(int frameId) {
        return this.frameId == frameId;
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
     * Get the drawable id of the marker.
     * (Can be used as icon for the marker)
     *
     * @return Drawable id of the marker
     */
    public int getDrawableId() {
        return drawableId;
    }

    /**
     * Context of the image view.
     *
     * @return Context of the image view
     */
    protected Context getContext() {
        return context;
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

}

