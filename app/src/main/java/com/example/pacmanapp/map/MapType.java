package com.example.pacmanapp.map;

import com.example.pacmanapp.R;

public enum MapType {
    PacMan(R.drawable.map, R.color.mapBackground, R.dimen.defaultMapSize, R.dimen.defaultMapSize,
            51.424203344, -0.0084914531, 5.48382758497, 0.013472188);

    private final int drawable;
    private final int backgroundColor;
    private final int width;
    private final int height;
    private final double latitudeStart;
    private final double latitudeHeight;
    private final double longitudeStart;
    private final double longitudeWidth;

    MapType(int drawable, int backgroundColor, int width, int height, double latitudeStart, double latitudeHeight,
            double longitudeStart, double longitudeWidth) {
        this.drawable = drawable;
        this.backgroundColor = backgroundColor;
        this.width = width;
        this.height = height;
        this.latitudeStart = latitudeStart;
        this.latitudeHeight = latitudeHeight;
        this.longitudeStart = longitudeStart;
        this.longitudeWidth = longitudeWidth;
    }

    /**
     * Get the drawable for the map.
     *
     * @return Drawable reference
     */
    public int getDrawable() {
        return drawable;
    }

    /**
     * Get the background color for the map.
     *
     * @return Background color reference
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Get the width size for the map.
     *
     * @return Width size reference
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height size for the map.
     *
     * @return Height size reference
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the latitude start for the map.
     *
     * @return Latitude start location
     */
    public double getLatitudeStart() {
        return latitudeStart;
    }

    /**
     * Get the latitude height of the map.
     *
     * @return Latitude height size
     */
    public double getLatitudeHeight() {
        return latitudeHeight;
    }

    /**
     * Get the longitude start for the map.
     *
     * @return Longitude start location
     */
    public double getLongitudeStart() {
        return longitudeStart;
    }

    /**
     * Get the longitude width of the map.
     *
     * @return Longitude width size
     */
    public double getLongitudeWidth() {
        return longitudeWidth;
    }
}
