package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.pacmanapp.R;
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapMarkers;
import com.example.pacmanapp.map.MapPosition;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;

public abstract class Character extends Marker implements Serializable, LocationObserver {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "Character";

    //>>> Constructors for character <<<//

    /**
     * Create a character marker for specified context, activity and alike.
     *
     * @param latitude   Latitude used to position character on map area
     * @param longitude  Longitude used to position character on map area
     * @param drawableId DrawableId used to get drawable to display for the marker
     * @param markerId   MarkerId set to ImageView for potential reference
     */
    Character(double latitude, double longitude, int drawableId, int markerId) {
        super(latitude, longitude, drawableId, markerId);
        setAnimate(true);
        setDisplayOnTop(true);
    }

    @Override
    protected abstract CharacterView createView(@NotNull MapArea mapArea);

    @Override
    public void onLocationUpdate(@NonNull Location location) {
        setLocation(location.getLatitude(), location.getLongitude());
    }

    @SuppressLint("ViewConstructor")
    public static abstract class CharacterView extends MarkerView {
        private final String TAG = "CharacterView";
        private final Character character;

        protected CharacterView(@NotNull MapArea mapArea, @NotNull Character character) {
            super(mapArea, character);
            this.character = character;
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
        }

        @Override
        public void onLocationChange(double latitude, double longitude) {
            // Get the map location for the specified values
            MapPosition mapPosition = MapPosition.getPosition(mapArea,
                    latitude, longitude, getWidth(), getHeight());

            // Get target x and y value
            int targetX = mapPosition.getX();
            int targetY = mapPosition.getY();

            // Set the rotation of the character to the movement direction
            setRotation(getDirection(targetX, targetY));

            // Animate the character to the target position
            animate().x(targetX).y(targetY)
                    .withEndAction(this::updatePlacement)
                    .setDuration(getContext().getResources().getInteger(R.integer.moveAnimationTime))
                    .start();

            Log.d(TAG, "Moving character to x: " + targetX + " and y:" + targetY);
        }

        /**
         * On click method called for the marker.
         *
         * @param view View from the click event
         */
        @Override
        public void onClick(View view) {
            super.onClick(view);
            MapMarkers mapMarkers = mapArea.getMapSave().getMapMarkers();
            Collection<Visitable> visitableMarkers =
                    mapMarkers.getMarkersWithClass(Visitable.class);

            Log.d(TAG, "Character is clicked, will visit " +
                    visitableMarkers.size() + " markers");

            for (Visitable visitableMarker: visitableMarkers) {
                visitableMarker.visit(character);
            }
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

        public interface Visitable {
            /**
             * Called when character has moved to specified location.
             *
             * @param character Character that has moved to location
             */
            void visit(@NotNull Character character);
        }

    }
}
