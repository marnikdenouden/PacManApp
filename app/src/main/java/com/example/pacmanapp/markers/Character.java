package com.example.pacmanapp.markers;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;

import com.example.pacmanapp.R;
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.map.MapPosition;
import com.example.pacmanapp.storage.SavePlatform;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;

public abstract class Character extends Marker implements Serializable, LocationObserver {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "Character";
    private transient Location lastLocation;

    //>>> Constructors for character <<<//

    /**
     * Create a character marker for specified context, activity and alike.
     *
     * @param frameId    FrameId reference to map area that the marker is placed on
     * @param latitude   Latitude used to position character on map area
     * @param longitude  Longitude used to position character on map area
     * @param drawableId DrawableId used to get drawable to display for the marker
     * @param markerId   MarkerId set to ImageView for potential reference
     * @param context    Context in which the marker is created
     */
    Character(int frameId, double latitude, double longitude, int drawableId, int markerId,
              @NotNull Context context) {
        super(frameId, latitude, longitude, drawableId, markerId, context);
        setAnimate(true);
        setDisplayOnTop(true);
    }

    //>>> Methods to control the character <<<//

    /**
     * Move the character to a new target location.
     *
     * @param location Location that character moves to
     */
    public void move(@NotNull Location location) {
        // Get the latitude and longitude from the location
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Get the map location for the specified values
        MapPosition mapPosition =
                MapPosition.getPosition(getFrameId(), latitude, longitude, getWidth(), getHeight());

        // Get target x and y value
        int targetX = mapPosition.getX();
        int targetY = mapPosition.getY();

        // Set the rotation of the character to the movement direction
        setRotation(getDirection(targetX, targetY));

        // Animate the character to the target position
        Runnable relocate = () -> {
            place(location);

            // Check to ensure there is a save to use
            if (!SavePlatform.hasSave()) {
                Log.w(TAG, "Could not get current save from save platform when moving character");
                return;
            }

            // Notify Visitable markers that character has moved
//            MapMarkers mapMarkers = MapMarkers.getFromSave(SavePlatform.getSave());
//            Collection<Visitable> visitableMarkers =
//                    mapMarkers.getMarkersWithClass(getFrameId(), Visitable.class);
//            for (Visitable visitableMarker: visitableMarkers) {
//                visitableMarker.visit(this, location);
//            } TODO improve visit dynamics in the implementation
            lastLocation = location;
        };

        getImageView().animate().x(targetX).y(targetY).withEndAction(relocate)
                .setDuration(getContext().getResources().getInteger(R.integer.moveAnimationTime))
                .start();

        Log.d(TAG, "Moving character to x: " + targetX + " and y:" + targetY);
    }

    /**
     * Computes the direction of a target location from the characters location.
     *
     * @param targetX X location of the target
     * @param targetY Y location of the target
     * @return Direction that the target is in relative to character location
     */
    private Direction getDirection(int targetX, int targetY) {
        int xDirection = targetX - (int) getImageView().getX();
        int yDirection = targetY - (int) getImageView().getY();

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

    @Override
    public void onLocationUpdate(@NotNull Location location) {
        move(location);
        Log.i(TAG, "Moving character with new location update");
    }

    @Override
    public void onClick(View view) {
        if (lastLocation == null) {
            Log.i(TAG, "Last location is null, so can't on click visit markers yet");
            return;
        }

        super.onClick(view);
        MapMarkers mapMarkers = MapMarkers.getFromSave(SavePlatform.getSave());
        Collection<Visitable> visitableMarkers =
                mapMarkers.getMarkersWithClass(getFrameId(), Visitable.class);
        for (Visitable visitableMarker: visitableMarkers) {
            visitableMarker.visit(this, lastLocation);
        }
    }

    public interface Visitable {
        /**
         * Called when character has moved to specified location.
         *
         * @param character Character that has moved to location
         * @param location Location that character has moved to
         */
        void visit(@NotNull Character character, @NotNull Location location);

    }

}
