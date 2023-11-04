package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.pacmanapp.R;
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapPosition;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectionCrier;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;

public abstract class Character extends Marker implements Serializable, LocationObserver {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "Character";
    private final static int VISIT_DISTANCE = 20;

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
            int moveDuration = getContext().getResources().getInteger(R.integer.moveAnimationTime);
            animate().x(targetX).y(targetY)
                    .withEndAction(() -> {
                        updatePlacement();
                        visitMarkers();
                    })
                    .setDuration(moveDuration)
                    .start();

            Log.d(TAG, "Moving character to x: " + targetX + " and y:" + targetY);
        }

        /**
         * Visits markers that are within visit distance of the current location.
         */
        private void visitMarkers() {
            Collection<Visitable> visitableMarkers = mapArea.getMarkerViewsWithClass(Visitable.class);
            Log.d(TAG, "Character is moving, will check " +
                    visitableMarkers.size() + " markers to visit");

            for (Visitable visitableMarker: visitableMarkers) {
                if (!(visitableMarker instanceof MarkerView)) {
                    Log.w(TAG, "Visitable marker was not instance of marker view.");
                    continue;
                }

                // If marker view overlaps character view we can visit the visitable marker.
                MarkerView markerView = (MarkerView) visitableMarker;

                if (markerView.distanceTo(character) < VISIT_DISTANCE) {
                    Log.d(TAG, "Visiting marker with distance: " +
                            markerView.distanceTo(character));
                    visitableMarker.visit(character);
                }
            }
        }

        /**
         * On click method called for the marker.
         *
         * @param view View from the click event
         */
        @Override
        public void onClick(View view) {
            super.onClick(view);

            // Call click on a marker views that overlap the character.
            for (MarkerView markerView: mapArea.getMapMarkerViews()) {
                if (markerView.equals(this)) {
                    continue;
                }

                if (MapPosition.doViewsOverlap(markerView, this)) {
                    Log.d(TAG, "Calling on click for marker view, since view overlaps.");
                    markerView.callOnClick();
                    return;
                }
            }

            // No marker to click, so we can try to 'click' the character.
            if (character instanceof Selectable) {
                SelectionCrier.getInstance().select((Selectable) character);
                return;
            }

            Log.i(TAG, "No marker found to click and character is also not selectable.");
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
