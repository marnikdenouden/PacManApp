package com.example.pacmanapp.map;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.Marker;

public class Move {
    // TODO rethink what this class is, maybe it can be redesigned to be
    //  a movement updater, so it can be configured and then just called to move the map.
    //  For example you could configure the map to always fill the map area (So if top left position is set to the center of the map area it will set it to top left instead)
    //  It could have a method onLocationResult() from location observer and use it.
    //  Maybe it could track a marker in different ways or move to selected map positions etc. Also check how map position is configured for such use.


    // Moved from map area to here.
    // TODO Allow the map to be moved to be centered on a location,
    //  since the constraint layout map area provides a view section on the map.
    // TODO Additionally allow the map to be scaled.


    private final MapArea mapArea;
    private final MapView mapView;
    private final MarkerLayout markerLayout;

    Move(MapArea mapArea) {
        this.mapArea = mapArea;
        this.mapView = mapArea.getMapView();
        this.markerLayout = mapArea.getMarkerLayout();
    }

    /**
     * Center the map image in the map area.
     */
    void centerMap() {
        placeCenter(new MapPosition(mapView.getWidth() / 2, mapView.getHeight() / 2));
    }

    /**
     * Move the map image to place a map position in the center of the map area.
     *
     * @param mapPosition Map position to center in the map area
     */
    public void moveToCenter(MapPosition mapPosition) {
        int targetX = mapArea.getWidth() / 2 - mapPosition.getX();
        int targetY = mapArea.getHeight() / 2 - mapPosition.getY();

        move(targetX, targetY);
    }

    /**
     * Move the map image to place a marker in the center of the map area.
     *
     * @param marker Marker to center the map are on
     */
    public void moveToCenter(Marker marker) {
        int targetX = mapArea.getWidth() / 2 - ((int) marker.getImageView().getX() + marker.getWidth() / 2);
        int targetY = mapArea.getHeight() / 2 - ((int) marker.getImageView().getY() + marker.getHeight() / 2);

        move(targetX, targetY);
    }

    /**
     * Move map to the target map position.
     *
     * @param mapPosition map position to move map to
     */
    private void move(MapPosition mapPosition) {
        move(mapPosition.getX(), mapPosition.getY());
    }

    /**
     * Move map to the target x and target y position.
     *
     * @param targetX x position to move the map image to
     * @param targetY y position to move the map image to
     */
    private void move(int targetX, int targetY) {
        // Animate the map image and marker layout to the target position
        Runnable moveMapCenter = () -> {
            place(targetX, targetY);
        };
        int animationTime = mapArea.getContext().getResources().getInteger(R.integer.moveAnimationTime);
        mapView.animate().x(targetX).y(targetY).withEndAction(moveMapCenter)
                .setDuration(animationTime).start();
        markerLayout.animate().x(targetX).y(targetY).setDuration(animationTime).start();
    }

    /**
     * Set the map image to place a map position in the center of the map area.
     *
     * @param mapPosition Map position to center in the map area
     */
    private void placeCenter(MapPosition mapPosition) {
        int xPosition = mapArea.getWidth() / 2 - mapPosition.getX();
        int yPosition = mapArea.getHeight() / 2 - mapPosition.getY();
        place(xPosition, yPosition);
    }

    /**
     * Place the map image to an x and y pixel position.
     *
     * @param xPosition X position to place the map at
     * @param yPosition Y position to place the map at
     */
    private void place(int xPosition, int yPosition) {
        //mapView.setX(xPosition);
        //mapView.setY(yPosition);
        //markerLayout.setX(xPosition);
        //markerLayout.setY(yPosition);
        mapArea.scrollTo(xPosition, yPosition); // TODO test if this works instead
    }

    /**
     * Place the map image to an map position.
     *
     * @param mapPosition map position to place map image to
     */
    private void place(MapPosition mapPosition) {
        place(mapPosition.getX(), mapPosition.getY());
    }

    /**
     * Gets the bounded map position, so the map covers the map area.
     *
     * @param xPosition X position of the map to check for
     * @param yPosition Y position of the map to check for
     * @return Map position that is bounded so the map covers the map area
     */
    public MapPosition boundedOnMapArea(int xPosition, int yPosition) {
        xPosition = Math.min(0, Math.max(xPosition, mapArea.getWidth() - mapView.getWidth()));
        yPosition = Math.min(0, Math.max(yPosition, mapArea.getHeight() - mapView.getHeight()));
        return new MapPosition(xPosition, yPosition);
    }

    /**
     * Place the map to the bounded specified position, so that the map covers the map area.
     *
     * @param xPosition x position to get bounded map position from
     * @param yPosition y position to get bounded map position from
     */
    public void placeBoundedOnMapArea(int xPosition, int yPosition) {
        place(boundedOnMapArea(xPosition, yPosition));
    }

    /**
     * Set the scale of the map.
     *
     * @param mapScale Scale to set map to
     */
    void setScale(float mapScale) {
        mapView.setScaleX(mapScale);
        mapView.setScaleY(mapScale);
        markerLayout.setScaleX(mapScale);
        markerLayout.setScaleY(mapScale); // TODO problem, scaling does not change width and height, so it messes their usage up.
        // TODO Before making this public, make sure the markers are repositioned
        //  and that the mapScale parameter is sensible.
    }
}
