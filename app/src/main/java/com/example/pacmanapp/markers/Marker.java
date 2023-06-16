package com.example.pacmanapp.markers;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

public class Marker {
    ImageView imageView;

    /**
     * Create a marker for specified context, activity and alike.
     *
     * @param latitude Latitude used to position marker on map
     * @param longitude Longitude used to position marker on map
     * @param markerWidth Marker width used to position and size marker
     * @param markerHeight Marker height used to position and size marker
     * @param drawable Drawable used as display for the marker
     * @param markerId MarkerId set to ImageView for potential reference
     * @param animate Boolean animate to state if drawable should animate
     * @param context Context in which the marker is created
     * @param activity Activity in which the marker is placed
     * @return ImageView of created marker
     */
    ImageView create(double latitude, double longitude, int markerWidth, int markerHeight,
                       Drawable drawable, int markerId, boolean animate,
                       Context context, AppCompatActivity activity) {

        // Create layout params for marker in relative layout
        RelativeLayout.LayoutParams layoutParams =
                createLayoutParams(latitude, longitude, markerWidth, markerHeight, activity);

        // Gets the relative layout with markers id that marker will be added to
        RelativeLayout layout = activity.findViewById(R.id.markers);

        // Create imageView for marker
        this.imageView = createImageView(context, layoutParams, markerId, drawable);

        // Add the imageView to the layout
        layout.addView(imageView);

        // If animate is true, cast and start to animate the drawable.
        if (animate && drawable != null) {
            animate((AnimationDrawable) drawable);
        }

        return imageView;
    }

    /**
     * Create a marker for specified context, activity and alike.
     *
     * @param latitude Latitude used to position marker on map
     * @param longitude Longitude used to position marker on map
     * @param markerWidth Marker width used to position and size marker
     * @param markerHeight Marker height used to position and size marker
     * @param drawableId DrawableId used to get drawable to display for the marker
     * @param markerId MarkerId set to ImageView for potential reference
     * @param animate Boolean animate to state if drawable should animate
     * @param context Context in which the marker is created
     * @param activity Activity in which the marker is placed
     * @return ImageView of created marker
     */
    ImageView create(double latitude, double longitude, int markerWidth, int markerHeight,
                            int drawableId, int markerId, boolean animate,
                            Context context, AppCompatActivity activity) {
        Drawable drawable = AppCompatResources.getDrawable(context, drawableId);
        return create(latitude, longitude, markerWidth, markerHeight, drawable, markerId, animate,
                context, activity);
    }

    /**
     * Create ImageView for marker.
     *
     * @param context Context to create imageView in
     * @param layoutParams Layout params to use for imageView
     * @param markerId Id for marker reference
     * @param drawable Drawable to display in imageView
     * @return ImageView that is created
     */
    private ImageView createImageView(Context context, RelativeLayout.LayoutParams layoutParams,
                                      int markerId, Drawable drawable) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);
        imageView.setId(markerId);
        imageView.setImageDrawable(drawable);
        return imageView;
    }

    /**
     * Create layout params for marker in relative layout.
     *
     * @param latitude Latitude to use for marker map location
     * @param longitude Longitude to use for marker map location
     * @param markerWidth Marker width used for layout params and map location
     * @param markerHeight Marker height used for layout params and map location
     * @param activity Activity to use for map location context
     * @return Layout params that is created
     */
    private RelativeLayout.LayoutParams createLayoutParams(double latitude, double longitude,
                                                           int markerWidth, int markerHeight,
                                                           AppCompatActivity activity) {
        // Create the relative layout params with specified height and width
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(markerWidth, markerHeight);

        // Create map location from latitude and longitude
        MapLocation mapLocation = new MapLocation(latitude, longitude, activity,
                markerWidth, markerHeight);

        // set the margins of the layout params
        layoutParams.setMargins(mapLocation.getX(), mapLocation.getY(), 0, 0);
        return layoutParams;
    }

    /**
     * Place the character to a new location.
     *
     * @param newX X location
     * @param newY Y location
     */
    public void place(int newX, int newY) {
        imageView.setX(newX);
        imageView.setY(newY);
    }

    /**
     * Place the character to a new map location.
     *
     * @param mapLocation the new map location.
     */
    public void place(MapLocation mapLocation) {
        place(mapLocation.getX(), mapLocation.getY());
    }


    /**
     * Animate the animation drawable.
     *
     * @param animationDrawable animationDrawable to start animation for.
     */
    private static void animate(@NotNull AnimationDrawable animationDrawable) {
        // Start the animation drawable
        animationDrawable.start();
    }


}
