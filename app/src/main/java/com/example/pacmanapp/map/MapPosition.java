package com.example.pacmanapp.map;

import android.view.View;

import org.jetbrains.annotations.NotNull;

public class MapPosition {
    private final static String TAG = "MapPosition";
    private final int xPosition;
    private final int yPosition;

    /**
     * Create a map position with a x and y position.
     *
     * @param x X position in pixels on the map
     * @param y Y position in pixels on the map
     */
    public MapPosition(int x, int y) {
        xPosition = x;
        yPosition = y;
    }

    /**
     * Get x position of the map position.
     *
     * @return x position in pixels
     */
    public int getX() {
        return xPosition;
    }

    /**
     * Get y position of the map position.
     *
     * @return y position in pixels
     */
    public int getY() {
        return yPosition;
    }

    /**
     * Gets the top left map position of a square with specified width and height,
     * which has at the center the specified latitude and longitude location on the map area.
     *
     * @param mapArea Map on which to get the map location
     * @param latitude Latitude to convert to y location
     * @param longitude Longitude to convert to x location
     * @param width Width of square to align longitude center of
     * @param height Height of square to align latitude center of
     */
    @NotNull
    public static MapPosition getPosition(@NotNull MapArea mapArea,
                                          double latitude, double longitude,
                                          int width, int height) {
        MapPosition mapPosition = mapArea.getMapPosition(latitude, longitude);
        int xPosition = mapPosition.getX() - (width / 2);
        int yPosition = mapPosition.getY() - (height / 2);

        return new MapPosition(xPosition, yPosition);
    }

    //>>> Code for position calculation <<<// TODO replace code with settings and automation of map movement.

    /**
     * Gets the bounded position of a location on the map area.
     *
     * @param mapArea Map area to convert the location to position on
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @return Bounded position of a location on the map area
     */
    public static MapPosition getPositionBounded(MapArea mapArea, double latitude, double longitude) {
        MapPosition mapPosition = mapArea.getMapPosition(latitude, longitude);
        return boundOnMap(mapArea, mapPosition);
    }

    /**
     * Limits the x and y position to ensure the map position is within map bounds.
     *
     * @param mapArea Map area to constrain the map position in
     * @param mapPosition Map position to constrain to map bounds
     * @return Map position that is within the map bounds
     */
    public static MapPosition boundOnMap(MapArea mapArea, MapPosition mapPosition) {
        int leftBound = 0;
        int rightBound = mapArea.getWidth();
        int topBound = 0;
        int bottomBound = mapArea.getHeight();

        return bound(mapPosition, leftBound, rightBound, topBound, bottomBound);
    }

    /**
     * Gets the top left position of a square with specified width and height, such that the
     *  specified latitude and longitude location on the map area map align to the of the square.
     *
     * @param mapArea Map area to convert the location to the position on
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @param width Width of the square to get left position from and have location centered at
     * @param height Height of the square to get top position from and have location centered at
     * @return Map position that gives the top left position of a square
     *  with the center at the specified location
     */
    public static MapPosition getPositionBounded(MapArea mapArea, double latitude, double longitude,
                                                 int width, int height) {
        MapPosition mapPosition = getPosition(mapArea, latitude, longitude, width, height);
        return boundOnMap(mapArea, mapPosition, width, height);
    }

    /**
     * Limits the x and y position to ensure the attached square is completely within map bounds.
     *
     * @param mapArea Map area to constrain the map position in
     * @param mapPosition Map position located at the top left position of the square
     * @param width Width of the square to stay in map bounds
     * @param height Height of the square to stay in map bounds
     * @return Map position that is within the map bounds
     */
    public static MapPosition boundOnMap(MapArea mapArea, MapPosition mapPosition,
                                         int width, int height) {
        int leftBound = 0;
        int rightBound = mapArea.getWidth() - width;
        int topBound = 0;
        int bottomBound = mapArea.getHeight() - height;

        return bound(mapPosition, leftBound, rightBound, topBound, bottomBound);
    }

    /**
     * Bound a map position between 4 bounds.
     *
     * @param mapPosition Map position to bound
     * @param leftBound Left bound that x position will be greater or equal to
     * @param rightBound Right bound that x position will be less or equal to
     * @param topBound Top bound that y position will be greater or equal to
     * @param bottomBound Bottom bound that y position will be less or equal to
     * @return Map position that is bound
     */
    private static MapPosition bound(MapPosition mapPosition, int leftBound, int rightBound,
                                     int topBound, int bottomBound) {
        int positionX = Math.max(leftBound, Math.min(mapPosition.getX(), rightBound));
        int positionY = Math.max(topBound, Math.min(mapPosition.getY(), bottomBound));

        return new MapPosition(positionX, positionY);
    }

    /**
     * Check if the specified views overlap.
     *
     * @param view1 View 1 to check overlap with view 2
     * @param view2 View 2 to check overlap with view 1
     * @return Truth assignment, if the specified views overlap
     */
    public static boolean doViewsOverlap(@NotNull View view1, @NotNull View view2) {
        boolean horizontalOverlap =
                        view1.getX() + view1.getWidth() >= view2.getX() &&
                        view2.getX() + view2.getWidth() >= view1.getX();
        boolean verticalOverlap =
                        view1.getY() + view1.getHeight() >= view2.getY() &&
                        view2.getY() + view2.getHeight() >= view1.getY();

        return horizontalOverlap && verticalOverlap;
    }

    // TODO add (static?) method to get rough geo location, so map x&y position can be converted to it.

}
