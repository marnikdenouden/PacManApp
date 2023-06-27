package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@SuppressLint("ViewConstructor")
public class PacDot extends Marker implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static int drawableId = R.drawable.pac_dot_v1_2;
    private final static int markerId = R.id.pacdot;

    /**
     * Pac-dot marker to display on the map and use.
     *
     * @param frameId   FrameId reference to map area that the pac-dot is placed on
     * @param latitude  latitude that the pac-dot is placed at
     * @param longitude longitude that the pac-dot is placed at
     * @param context   Context that the pac-dot is created in
     */
    public PacDot(int frameId, double latitude, double longitude, Context context) {
        super(frameId, latitude, longitude, drawableId, markerId, false, context);

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setX(getX() + 50);
            }
        });
    }

    @Override
    PacDot load(Context context) {
        return new PacDot(frameId, latitude, longitude, context);
    }

}
