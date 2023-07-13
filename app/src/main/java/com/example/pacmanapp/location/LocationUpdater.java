package com.example.pacmanapp.location;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Collection;
import java.util.HashSet;

public class LocationUpdater implements DynamicLocation {
    private final LocationRequest locationRequest;

    private LocationManager locationManager;
    private final FusedLocationProviderClient fusedLocationClient;
    private final LocationCallback locationCallback;
    private boolean requestingLocationUpdates = true;
    private final Context context;
    private final AppCompatActivity activity;
    private final Collection<LocationObserver> observers;

    /**
     * Location updater requests location and notifies of results to location observers.
     *
     * @param activity Activity that the location updater utilizes
     */
    public LocationUpdater(AppCompatActivity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
        observers = new HashSet<>();

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(10000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                notifyLocationResult(locationResult);
            }
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    /**
     * Notify location observers of location result.
     *
     * @param locationResult Location result received from update
     */
    private void notifyLocationResult(LocationResult locationResult) {
        for (LocationObserver locationObserver: observers) {
            locationObserver.onLocationResult(locationResult);
        }
    }

    /**
     * Add location observer to the location updater once.
     *
     * @param locationObserver Location observer to be notified of location results
     */
    public void addObserver(LocationObserver locationObserver) {
        if (observers.contains(locationObserver)) {
            return; // locationObserver is already added to the observers list.
        }
        observers.add(locationObserver);
    }

    /**
     * Stop location updates.
     */
    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        requestingLocationUpdates = false;
    }

    /**
     * Start location updates. after checking permissions and GPS.
     */
    public void startLocationUpdates() {
        if (!(ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            // Request location permission if it is not yet given.
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        if (!isGPSEnabled()) {
            turnOnGPS();
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());

        requestingLocationUpdates = true;
    }

    /**
     * Check if updater is requesting location updates.
     */
    public boolean isRequestingLocationUpdates() {
        return requestingLocationUpdates;
    }

    /**
     * Turn on GPS by preparing settings and alike.
     */
    public void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(activity.getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(context, "GPS is already turned on", Toast.LENGTH_SHORT).show();
                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(activity, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    /**
     * Check if GPS is enabled.
     *
     * @return Truth assignment, if GPS is enabled.
     */
    public boolean isGPSEnabled() {
        if (locationManager == null) {
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        }

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
