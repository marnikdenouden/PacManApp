package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.map.MapArea;

@SuppressLint("ViewConstructor")
public class PowerPallet extends Marker {
    private final static int drawableId =  R.drawable.power_pallet_v1_1;
    private final static int markerId = R.id.powerpallet;

    /**
     * PowerPallet marker to display on the map and use.
     *
     * @param mapArea   MapArea that the power pallet is placed on
     * @param latitude  latitude that the power pallet is placed at
     * @param longitude longitude that the power pallet is placed at
     * @param context   Context that the power pallet is created in
     * @param activity  Activity that the power pallet is placed in
     */
    public PowerPallet(MapArea mapArea, double latitude, double longitude, Context context, AppCompatActivity activity) {
        super(mapArea, latitude, longitude, drawableId, markerId, false, context, activity);
    }

    private static int getPowerPalletSize(AppCompatActivity activity) {
        return (int) (activity.getResources().getDimension(R.dimen.powerPalletSize));
    }

    @Override
    int getPixelWidth(AppCompatActivity activity) {
        return getPowerPalletSize(activity);
    }

    @Override
    int getPixelHeight(AppCompatActivity activity) {
        return getPowerPalletSize(activity);
    }
}
