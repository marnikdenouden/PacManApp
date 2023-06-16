package com.example.pacmanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Looper;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pacmanapp.displays.Clock;
import com.example.pacmanapp.displays.Score;
import com.example.pacmanapp.markers.Ghost;
import com.example.pacmanapp.markers.PacDot;
import com.example.pacmanapp.markers.PacMan;
import com.example.pacmanapp.markers.PowerPallet;
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

import java.time.Duration;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private TextView AddressText;
    private Button LocationButton;

    private View layout;

    private LocationRequest locationRequest;

    private LocationManager locationManager = null;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean requestingLocationUpdates = true;

    private PacMan pacman;
    private Ghost ghost;

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddressText = findViewById(R.id.addressText);
        LocationButton = findViewById(R.id.locationButton);
        layout = findViewById(R.id.layout);

        Clock clock = new Clock(MainActivity.this, MainActivity.this);
        clock.setTime(Duration.ofSeconds(10293));

        Score score = new Score(5, R.id.scoreLayout, MainActivity.this, MainActivity.this);
        score.setValue(10293);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(20000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation(); // Is this the same as get index - 1?

                AddressText.setText("Latitude " + location.getLatitude() + ", Longitude " + location.getLongitude());

                updateMarkerLocations(location);

                for (Location resultLocations : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            }
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestingLocationUpdates) {
                    stopLocationUpdates();
                    layout.setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
                    Log.i(TAG, "Stop location updates");
                    createMarkers(); // TODO move create markers so it is run at a good time.
                } else {
                    startLocationUpdates();
                    Log.i(TAG, "Start location updates");
                    layout.setBackgroundColor(getResources().getColor(R.color.black, getTheme()));
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void createMarkers() {
        ImageView map = findViewById(R.id.map_image);
        if (map.getHeight() > 0 && map.getWidth() > 0) {
            pacman = new PacMan(51.4198767, 5.485905, MainActivity.this, MainActivity.this);
            //ghost = new Ghost(GhostType.Blinky, 0, 0, MainActivity.this, MainActivity.this);
            PowerPallet powerPallet = new PowerPallet(51.4191983, 5.492802, MainActivity.this, MainActivity.this);
            PacDot pacDot = new PacDot(51.419331, 5.48632, MainActivity.this, MainActivity.this);
        }
    }

    private void updateMarkerLocations(Location location) {
        if (pacman != null) {
            pacman.move(location.getLatitude(), location.getLongitude(), MainActivity.this, MainActivity.this);
        }
        if (ghost != null) {
            //ghost.move(location.getLatitude() - 0.002, location.getLongitude() + 0.002, MainActivity.this, MainActivity.this);
        }

    }

    /**
     * Stop location updates.
     */
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        requestingLocationUpdates = false;
    }

    /**
     * Start location updates. after checking permissions and GPS.
     */
    private void startLocationUpdates() {
        if (!(ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            // Request location permission if it is not yet given.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0) {
            return;
        }

        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startLocationUpdates();
        }
    }

    /**
     * Turn on GPS by preparing settings and alike.
     */
    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();
                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            startLocationUpdates();
        }
    }

    /**
     * Check if GPS is enabled.
     *
     * @return Truth assignment, if GPS is enabled.
     */
    private boolean isGPSEnabled() {
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}