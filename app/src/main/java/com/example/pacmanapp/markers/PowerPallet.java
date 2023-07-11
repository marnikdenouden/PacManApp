package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.edit.EditPowerPalletActivity;
import com.example.pacmanapp.activities.inspect.InspectPowerPalletActivity;
import com.example.pacmanapp.selection.Selectable;

@SuppressLint("ViewConstructor")
public class PowerPallet extends Marker implements Selectable {
    private static final long serialVersionUID = 1L;
    private final static int drawableId =  R.drawable.power_pallet;
    private final static int markerId = R.id.powerpallet;

    /**
     * PowerPallet marker to display on the map and use.
     *
     * @param frameId   FrameId reference to map area that the marker is placed on
     * @param latitude  latitude that the power pallet is placed at
     * @param longitude longitude that the power pallet is placed at
     * @param context   Context that the power pallet is created in
     */
    public PowerPallet(int frameId, double latitude, double longitude, Context context) {
        super(frameId, latitude, longitude, drawableId, markerId, false, context);
    }

    /**
     * Get the power pallet size for width and height.
     *
     * @return Pixel size for the width and height of the power pallet
     */
    private int getPowerPalletSize() {
        return (int) getContext().getResources().getDimension(R.dimen.powerPalletSize);
    }

    @Override
    int getPixelWidth() {
        return getPowerPalletSize();
    }

    @Override
    int getPixelHeight() {
        return getPowerPalletSize();
    }

    @Override
    public Class<? extends AppCompatActivity> getInspectPage() {
        return InspectPowerPalletActivity.class;
    }

    @Override
    public Class<? extends AppCompatActivity> getEditPage() {
        return EditPowerPalletActivity.class;
    }

    @Override
    public String getLabel() {
        return "Power pallet";
    }

    @Override
    public int getIconId() {
        return R.drawable.power_pallet;
    }

    @Override
    public String getDescription() {
        return "Power pallets allow pacman to eat ghosts for a short duration when collected.";
    }
}
