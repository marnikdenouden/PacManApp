package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.map.MapArea;

@SuppressLint("ViewConstructor")
public class PacMan extends Character {
    private final static int drawableId = R.drawable.pacman_marker_animation;
    private final static int markerId = R.id.pacman;

    /**
     * PacMan marker to display on the map and control.
     *
     * @param frameId   FrameId reference to map area that pacman is placed on
     * @param latitude  latitude that pacman starts at
     * @param longitude longitude that pacman start at
     * @param context   Context that pacman is created in
     * @param activity  Activity that pacman is placed in
     */
    public PacMan(int frameId, double latitude, double longitude, Context context, AppCompatActivity activity) {
        super(frameId, latitude, longitude, drawableId, markerId, context, activity);
    }

    @Override
    void setRotation(Direction direction) {
        setRotation(direction.getDegrees());
    }
}
