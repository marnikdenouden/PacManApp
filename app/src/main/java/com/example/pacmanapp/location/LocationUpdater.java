package com.example.pacmanapp.location;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;
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

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class LocationUpdater implements DynamicLocation {
    private static final String TAG = "LocationUpdater";

    private final LocationRequest locationRequest;
    private LocationManager locationManager;
    private final FusedLocationProviderClient fusedLocationClient;
    private final LocationCallback locationCallback;
    private boolean requestingLocationUpdates = true;
    private final Context context;
    private final AppCompatActivity activity;
    private final Collection<LocationObserver> observers;
    private final Collection<LocationObserver> singleObservers;
    private LocationResult locationResult;

    /**
     * Location updater requests location and notifies of results to location observers.
     *
     * @param activity Activity that the location updater utilizes
     */
    public LocationUpdater(AppCompatActivity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
        observers = new HashSet<>();
        singleObservers = new HashSet<>();

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(10000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                LocationUpdater.this.locationResult = locationResult;
                if (hasLocation()) {
                    notifyLocationUpdate(getLastLocation());
                }
            }
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    /**
     * Notify location observers of last location result.
     */
    private void notifyLocationUpdate(@NotNull Location location) {
        Log.i(TAG, "Starting to notify new location update to " + observers.size() +
                " observers and " + singleObservers.size() + " single observers");
        for (LocationObserver locationObserver: observers) {
            locationObserver.onLocationUpdate(location);
        }
        for (LocationObserver locationObserver: singleObservers) {
            locationObserver.onLocationUpdate(location);
        }
        Log.i(TAG, "Successfully notified the location update to " + observers.size() +
                " observers and " + singleObservers.size() + " single observers");
        singleObservers.clear();
    }

    /**
     * Checks if location updater has a location set.
     *
     * @return Truth assignment, if a location is set
     */
    public boolean hasLocation() {
        return hasLocationResult() && locationResult.getLastLocation() != null;
    }

    /**
     * Checks if location updater has location result set.
     *
     * @return Truth assignment, if location result is set
     */
    private boolean hasLocationResult() {
        return locationResult != null;
    }

    /**
     * Get the last location of the location updater.
     *
     * @pre location updater has a location
     * @return Location last location
     */
    public Location getLastLocation() {
        if (!hasLocationResult()) {
            Log.e(TAG, "Could not get last location");
            throw new NullPointerException("No location result was not set");
        }
        Location lastLocation = locationResult.getLastLocation();
        if (lastLocation == null) {
            Log.e(TAG, "Could not get last location");
            throw new NullPointerException("No location result was not set");
        }
        return lastLocation;
    }

    /**
     * Add location observer that receives only the next location result.
     *
     * @param locationObserver Location observer to receive only the next location result
     */
    public void observeNextLocation(@NotNull LocationObserver locationObserver) {
        singleObservers.add(locationObserver);
    }

    /**
     * Add location observer to the location updater once.
     *
     * @param locationObserver Location observer to be notified of location results
     */
    @Override
    public void addObserver(@NotNull LocationObserver locationObserver) {
        if (observers.contains(locationObserver)) {
            return; // locationObserver is already added to the observers list.
        }
        observers.add(locationObserver);
    }

    /**
     * Remove location observer.
     *
     * @param locationObserver Location observer to remove from listeners
     */
    @Override
    public void removeObserver(@NotNull LocationObserver locationObserver) {
        observers.remove(locationObserver);
    }

    /**
     * Get the location results currently stored.
     *
     * @return locationResults currently captured
     */
    public LocationResult getLocationResult() {
        return locationResult;
    }

    /**
     * Stop location updates.
     */
    public void stopLocationUpdates() {
        Log.i(TAG, "Stopping location updates");
        fusedLocationClient.removeLocationUpdates(locationCallback);
        requestingLocationUpdates = false;
    }

    /**
     * Start location updates. after checking permissions and GPS.
     */
    public void startLocationUpdates() {
        Log.i(TAG, "Starting location updates");
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
        Log.i(TAG, "Trying to turn on GPS");
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
