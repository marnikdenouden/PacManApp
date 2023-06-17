package com.example.pacmanapp.location;

import com.google.android.gms.location.LocationResult;

public interface locationObserver {

    /**
     * Utilize the new location result.
     *
     * @param locationResult Location result received
     */
    void onLocationResult(LocationResult locationResult);
}
