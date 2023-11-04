package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.pacmanapp.R;
import com.example.pacmanapp.map.MapArea;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class PacMan extends Character implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static int drawableId = R.drawable.pacman_marker_animation;
    private final static int markerId = R.id.pacman;

    /**
     * PacMan marker to display on the map and control.
     *
     * @param latitude  latitude that pacman starts at
     * @param longitude longitude that pacman start at
     */
    public PacMan(double latitude, double longitude) {
        super(latitude, longitude, drawableId, markerId);
    }

    @Override
    protected PacManView createView(@NotNull MapArea mapArea) {
        PacManView pacManView = new PacManView(mapArea, this);
        pacManView.createView();
        return pacManView;
    }

    @SuppressLint("ViewConstructor")
    public static class PacManView extends CharacterView {
        private final String TAG = "PacManView";

        protected PacManView(@NonNull MapArea mapArea, @NonNull PacMan pacMan) {
            super(mapArea, pacMan);
        }

        @Override
        void setRotation(Direction direction) {
            setRotation(direction.getDegrees());
        }
    }

}
