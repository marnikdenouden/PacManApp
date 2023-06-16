package com.example.pacmanapp.markers;

import android.content.Context;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

public class PacMan extends Character {

    /**
     * PacMan marker to display on the map and control.
     *
     * @param latitude latitude that the marker starts at
     * @param longitude longitude that the marker start at
     * @param context Context that the marker is created in
     * @param activity Activity that the marker is placed in
     */
    public PacMan(double latitude, double longitude, Context context, AppCompatActivity activity) {
        create(latitude, longitude, context, activity);
    }

    /**
     * Create a pacman marker for specified context, activity and alike.
     *
     * @param latitude Latitude used to position marker on map
     * @param longitude Longitude used to position marker on map
     * @param context Context in which the marker is created
     * @param activity Activity in which the marker is placed
     * @return ImageView of created pacman marker
     */
    ImageView create(double latitude, double longitude, Context context, AppCompatActivity activity) {
        int markerSize = activity.getResources().getDimensionPixelSize(R.dimen.pacmanMarkerSize);
        return super.create(latitude, longitude, markerSize, markerSize,
                R.drawable.pacman_marker_animation, R.id.pacman, context, activity);
    }

    @Override
    void setRotation(Direction direction) {
        imageView.setRotation(direction.getDegrees());
    }
}
