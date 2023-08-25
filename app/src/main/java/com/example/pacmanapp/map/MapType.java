package com.example.pacmanapp.map;

import android.content.Context;

import com.example.pacmanapp.R;
import com.google.android.material.color.MaterialColors;

public enum MapType {
    PACMAN_GERARDUS(R.drawable.map_gerardus, R.color.white, R.color.black, R.dimen.defaultMapSize, R.dimen.defaultMapSize,
            51.424203344, -0.0084914531, 5.48382758497, 0.013472188),
    PACMAN_FRANSEBAAN(R.drawable.map_fransebaan, R.color.white, R.color.black, R.dimen.defaultMapSize, R.dimen.defaultMapSize,
            51.424203344, -0.0084914531, 5.48382758497, 0.013472188);

    private final int drawable;
    private final int backgroundColor;
    private final int lineColor;
    private final int width;
    private final int height;
    private final double latitudeStart;
    private final double latitudeHeight;
    private final double longitudeStart;
    private final double longitudeWidth;

    MapType(int drawable, int backgroundColor, int lineColor, int width, int height,
            double latitudeStart, double latitudeHeight,
            double longitudeStart, double longitudeWidth) {
        this.drawable = drawable;
        this.backgroundColor = backgroundColor;
        this.lineColor = lineColor;
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
     * @param context Context to get color with
     * @return int Background color value
     */
    public int getBackgroundColor(Context context) {
        return MaterialColors.getColor(context, com.google.android.material.R.attr.colorSurface,
                backgroundColor); // Fall back color in case material does not work.
    }

    /**
     * Get the line color for the map.
     *
     * @param context Context to get color with
     * @return int Line color value
     */
    public int getLineColor(Context context) {
        return MaterialColors.getColor(context, com.google.android.material.R.attr.colorPrimary,
                lineColor); // Fall back color in case material does not work.
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
