package com.example.pacmanapp.markers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.map.MapPosition;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class Character extends Marker implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "Character";

    //>>> Constructors for character <<<//

    /**
     * Create a character marker for specified context, activity and alike.
     *
     * @param frameId   FrameId reference to map area that the character is placed on
     * @param latitude  Latitude used to position character on map area
     * @param longitude Longitude used to position character on map area
     * @param drawable  Drawable used as display for the marker
     * @param markerId  MarkerId set to ImageView for potential reference
     * @param context   Context in which the marker is created
     * @param activity  Activity in which the marker is placed
     */
    Character(int frameId, double latitude, double longitude, @NotNull Drawable drawable, int markerId,
              @NotNull Context context, @NotNull AppCompatActivity activity) {
        super(frameId, latitude, longitude, drawable, markerId, context, activity, true
        );
    }

    /**
     * Create a character marker for specified context, activity and alike.
     *
     * @param frameId    FrameId reference to map area that the marker is placed on
     * @param latitude   Latitude used to position character on map area
     * @param longitude  Longitude used to position character on map area
     * @param drawableId DrawableId used to get drawable to display for the marker
     * @param markerId   MarkerId set to ImageView for potential reference
     * @param context    Context in which the marker is created
     * @param activity   Activity in which the marker is placed
     */
    Character(int frameId, double latitude, double longitude, int drawableId, int markerId,
              @NotNull Context context, @NotNull AppCompatActivity activity) {
        super(frameId, latitude, longitude, drawableId, markerId,
                true, context, activity);
    }

    /**
     * Create a character marker for specified context, activity and alike.
     *
     * @param frameId   FrameId reference to map area that the marker is placed on
     * @param latitude  Latitude used to position character on map area
     * @param longitude Longitude used to position character on map area
     * @param markerId  MarkerId set to ImageView for potential reference
     * @param context   Context in which the marker is created
     * @param activity  Activity in which the marker is placed
     */
    Character(int frameId, double latitude, double longitude, int markerId,
              @NotNull Context context, @NotNull AppCompatActivity activity) {
        super(frameId, latitude, longitude, markerId, true,
                context, activity);
    }

    //>>> Methods to control the character <<<//

    /**
     * Move the character to a new target location.
     *
     * @param latitude Latitude of the target location
     * @param longitude Longitude of the target location
     */
    public void move(double latitude, double longitude) {
        // Get the map location for the specified values
        MapPosition mapPosition =
                MapPosition.getPosition(frameId, latitude, longitude, getWidth(), getHeight());

        // Get target x and y value
        int targetX = mapPosition.getX();
        int targetY = mapPosition.getY();

        // Set the rotation of the character to the movement direction
        setRotation(getDirection(targetX, targetY));

        // Animate the character to the target position
        Runnable relocate = () -> {
            place(latitude, longitude);
            //updateDistances(); TODO Do game logic computation on character location change event.
        };
        animate().x(targetX).y(targetY).withEndAction(relocate)
                .setDuration(getContext().getResources().getInteger(R.integer.moveAnimationTime))
                .start();

        Log.d(TAG, "Moving character"); // TODO remove line
    }

    /**
     * Computes the direction of a target location from the characters location.
     *
     * @param targetX X location of the target
     * @param targetY Y location of the target
     * @return Direction that the target is in relative to character location
     */
    private Direction getDirection(int targetX, int targetY) {
        int xDirection = targetX - (int) getX();
        int yDirection = targetY - (int) getY();

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
