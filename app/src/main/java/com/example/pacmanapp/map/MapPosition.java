package com.example.pacmanapp.map;

public class MapPosition {
    private final static String TAG = "MapPosition";
    private final int xPosition;
    private final int yPosition;

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
     * Gets the bounded position of a location on the map area.
     *
     * @param mapArea Map area to convert the location to position on
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @return Bounded position of a location on the map area
     */
    public static MapPosition getPositionBounded(MapArea mapArea, double latitude, double longitude) {
        MapPosition mapPosition = mapArea.getMapLocation(latitude, longitude);
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
        int markerX = Math.max(0, Math.min(mapPosition.getX(), mapArea.getWidth()));
        int markerY = Math.max(0, Math.min(mapPosition.getY(), mapArea.getHeight()));

        return new MapPosition(markerX, markerY);
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
     * Gets the top left map position of a square with specified width and height,
     * which has at the center the specified latitude and longitude location on the map area.
     *
     * @param mapArea Map on which to get the map location
     * @param latitude Latitude to convert to y location
     * @param longitude Longitude to convert to x location
     * @param width Width of square to align longitude center of
     * @param height Height of square to align latitude center of
     */
    public static MapPosition getPosition(MapArea mapArea, double latitude, double longitude,
                                          int width, int height) {
        MapPosition mapPosition = mapArea.getMapLocation(latitude, longitude);
        int xPosition = mapPosition.getX() - (width / 2);
        int yPosition = mapPosition.getY() - (height / 2);

        return new MapPosition(xPosition, yPosition);
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
        int markerX = Math.max(0, Math.min(mapPosition.getX(), mapArea.getWidth() - width));
        int markerY = Math.max(0, Math.min(mapPosition.getY(), mapArea.getHeight() - height));

        return new MapPosition(markerX, markerY);
    }

}
