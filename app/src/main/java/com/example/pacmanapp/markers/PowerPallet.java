package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

import java.io.Serializable;

@SuppressLint("ViewConstructor")
public class PowerPallet extends Marker implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static int drawableId =  R.drawable.power_pallet_v1_1;
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
}
