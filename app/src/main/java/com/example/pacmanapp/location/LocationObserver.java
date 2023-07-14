package com.example.pacmanapp.location;

import android.location.Location;

public interface LocationObserver {

    /**
     * Utilize the new location result.
     *
     * @param location Location update received
     */
    void onLocationUpdate(Location location);
}
