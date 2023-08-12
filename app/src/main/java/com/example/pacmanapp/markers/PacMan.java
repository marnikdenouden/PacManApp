package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.pacmanapp.R;
import com.example.pacmanapp.location.LocationObserver;

import java.io.Serializable;

@SuppressLint("ViewConstructor")
public class PacMan extends Character implements Serializable, LocationObserver {
    private static final long serialVersionUID = 1L;
    private final static int drawableId = R.drawable.pacman_marker_animation;
    private final static int markerId = R.id.pacman;

    /**
     * PacMan marker to display on the map and control.
     *
     * @param frameId   FrameId reference to map area that pacman is placed on
     * @param latitude  latitude that pacman starts at
     * @param longitude longitude that pacman start at
     * @param context   Context that pacman is created in
     */
    public PacMan(int frameId, double latitude, double longitude, Context context) {
        super(frameId, latitude, longitude, drawableId, markerId, context);
    }

    @Override
    void setRotation(Direction direction) {
        getImageView().setRotation(direction.getDegrees());
    }
}
