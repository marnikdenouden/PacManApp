package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.edit.EditPowerPelletActivity;
import com.example.pacmanapp.activities.inspect.InspectPowerPelletActivity;
import com.example.pacmanapp.selection.Selectable;

@SuppressLint("ViewConstructor")
public class PowerPellet extends Marker implements Selectable {
    private static final long serialVersionUID = 1L;
    private final static int drawableId =  R.drawable.power_pellet;
    private final static int markerId = R.id.powerpellet;

    /**
     * PowerPellet marker to display on the map and use.
     *
     * @param frameId   FrameId reference to map area that the marker is placed on
     * @param latitude  latitude that the power pellet is placed at
     * @param longitude longitude that the power pellet is placed at
     * @param context   Context that the power pellet is created in
     */
    public PowerPellet(int frameId, double latitude, double longitude, Context context) {
        super(frameId, latitude, longitude, drawableId, markerId, context);
    }

    /**
     * Get the power pellet size for width and height.
     *
     * @return Pixel size for the width and height of the power pellet
     */
    private int getPowerPelletSize() {
        return (int) getContext().getResources().getDimension(R.dimen.powerPelletSize);
    }

    @Override
    int getPixelWidth() {
        return getPowerPelletSize();
    }

    @Override
    int getPixelHeight() {
        return getPowerPelletSize();
    }

    @Override
    public Class<? extends AppCompatActivity> getInspectPage() {
        return InspectPowerPelletActivity.class;
    }

    @Override
    public Class<? extends AppCompatActivity> getEditPage() {
        return EditPowerPelletActivity.class;
    }

    @Override
    public String getLabel() {
        return "Power pellet";
    }

    @Override
    public int getIconId() {
        return R.drawable.power_pellet;
    }

    @Override
    public String getDescription() {
        return "Power pellets allow pacman to eat ghosts for a short duration when collected.";
    }
}
