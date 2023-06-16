package com.example.pacmanapp.markers;

import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

public class MapLocation {
    private int xLocation;
    private int yLocation;

    private final static String TAG = "MapLocation";

    MapLocation(int x, int y) {
        xLocation = x;
        yLocation = y;
    }

    MapLocation(double latitude, double longitude, AppCompatActivity activity,
                    int markerWidth, int markerHeight) {
        convertLocation(latitude, longitude, activity, markerWidth, markerHeight);
    }

    int getX() {
        return xLocation;
    }

    int getY() {
        return yLocation;
    }

    private void convertLocation(double latitude, double longitude,
                                       AppCompatActivity activity,
                                       int markerWidth, int markerHeight) {
        // latitude range
        final double latitudeStart = 51.424203344;
        final double latitudeHeight = -0.0084914531;

        // longitude range
        final double longitudeStart = 5.48382758497;
        final double longitudeWidth = 0.013472188;

        Log.v(TAG, "latitude " + latitude + ", longitude, " + longitude);

        // Get the map object to
        ImageView map = activity.findViewById(R.id.map_image);

        // Convert the latitude and longitude to X and Y values on the map
        double rawX = (longitude - longitudeStart) * map.getWidth() / longitudeWidth;
        double rawY = (latitude - latitudeStart) * map.getHeight() / latitudeHeight;

        Log.v(TAG, "RawX " + rawX + ", RawY " + rawY);

        // Set the X and Y position of the marker to the center of the image
        int markerX = (int) (rawX) - (markerWidth / 2);
        int markerY = (int) (rawY) - (markerHeight / 2);

        Log.v(TAG, "MarkerX " + markerX + ", MarkerY " + markerY);

        // Limit the X and Y position to ensure the maker is completely within map bounds
        xLocation = Math.max(0, Math.min(markerX, map.getWidth() - markerWidth));
        yLocation = Math.max(0, Math.min(markerY, map.getHeight() - markerHeight));
    }

}
