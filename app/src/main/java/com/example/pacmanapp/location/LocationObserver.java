package com.example.pacmanapp.location;

import android.location.Location;

import org.jetbrains.annotations.NotNull;

public interface LocationObserver {

    /**
     * Utilize the new location result.
     *
     * @param location Location update received
     */
    void onLocationUpdate(@NotNull Location location);
}
