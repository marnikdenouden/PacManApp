package com.example.pacmanapp.markers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;

public abstract class Character extends Marker {

    /**
     * Create a character marker for specified context, activity and alike.
     *
     * @param latitude Latitude used to position marker on map
     * @param longitude Longitude used to position marker on map
     * @param markerWidth Marker width used to position and size marker
     * @param markerHeight Marker height used to position and size marker
     * @param drawableId DrawableId used to get drawable to display for the marker
     * @param markerId MarkerId set to ImageView for potential reference
     * @param context Context in which the marker is created
     * @param activity Activity in which the marker is placed
     * @return ImageView of created marker
     */
    ImageView create(double latitude, double longitude, int markerWidth, int markerHeight,
                    int drawableId, int markerId, Context context, AppCompatActivity activity) {
        return super.create(latitude, longitude, markerWidth, markerHeight, drawableId, markerId,
                true, context, activity);
    }

    /**
     * Create a character marker for specified context, activity and alike.
     *
     * @param latitude Latitude used to position marker on map
     * @param longitude Longitude used to position marker on map
     * @param markerWidth Marker width used to position and size marker
     * @param markerHeight Marker height used to position and size marker
     * @param drawable Drawable used as display for the marker
     * @param markerId MarkerId set to ImageView for potential reference
     * @param context Context in which the marker is created
     * @param activity Activity in which the marker is placed
     * @return ImageView of created marker
     */
    ImageView create(double latitude, double longitude, int markerWidth, int markerHeight,
                            Drawable drawable, int markerId, Context context, AppCompatActivity activity) {
        return super.create(latitude, longitude, markerWidth, markerHeight, drawable, markerId,
                true, context, activity);
    }

    /**
     * Move the character to a new target location.
     *
     * @param latitude Latitude of the target location
     * @param longitude Longitude of the target location
     * @param context Context in which it takes place
     * @param activity Activity in which to move
     */
    public void move(double latitude, double longitude,
                     Context context, AppCompatActivity activity) {
        // Get the map location for the specified values
        MapLocation mapLocation = new MapLocation(latitude, longitude, activity,
                imageView.getWidth(), imageView.getHeight());

        // Get target x and y value
        int targetX = mapLocation.getX();
        int targetY = mapLocation.getY();

        // Set the rotation of the character to the movement direction
        setRotation(getDirection(targetX, targetY));

        // Animate the character to the target position
        Runnable relocate = () -> {
            place(mapLocation.getX(), mapLocation.getY());
            //updateDistances(); TODO Do game logic computation on character location change event.
        };
        imageView.animate().x(targetX).y(targetY).setDuration(1000)
                .withEndAction(relocate).start();
    }

    /**
     * Computes the direction of a target location from the characters location.
     *
     * @param targetX X location of the target
     * @param targetY Y location of the target
     * @return Direction that the target is in relative to character location
     */
    private Direction getDirection(int targetX, int targetY) {
        int currentX = (int) imageView.getX();
        int currentY = (int) imageView.getY();

        int xDirection = targetX - currentX;
        int yDirection = targetY - currentY;

        if (Math.abs(xDirection) > Math.abs(yDirection)) {
            if (xDirection < 0) {
                return Direction.LEFT;
            } else {
                return Direction.RIGHT;
            }
        } else {
            if (yDirection < 0) {
                return Direction.UP;
            } else {
                return Direction.DOWN;
            }
        }
    }

    /**
     * Sets the visual rotation of the character to the specified direction.
     *
     * @param direction Direction to rotate the character to
     */
    abstract void setRotation(Direction direction);
}
