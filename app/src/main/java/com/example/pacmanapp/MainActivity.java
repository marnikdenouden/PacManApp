package com.example.pacmanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private TextView AddressText;
    private Button LocationButton;

    private LocationRequest locationRequest;

     private LocationManager locationManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddressText = findViewById(R.id.addressText);
        LocationButton = findViewById(R.id.locationButton);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (!isGPSEnabled()) {
                turnOnGPS();
                return;
            }
            getCurrentLocation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            getCurrentLocation();
        }
    }

    private void placeMarker(double latitude, double longitude, int markerWidth, int markerHeight, Context context, int drawableId, boolean animate) {
        // longitude range
        final double longitudeStart = 5.48382758497;
        final double longitudeWidth = 0.013472188;

        // latitude range
        final double latitudeStart = 51.424203344;
        final double latitudeHeight = -0.0084914531;

        System.out.println("longitude " + longitude + ", latitude " + latitude);

        // Get the map object to
        ImageView map = findViewById(R.id.map_image);

        // Convert the latitude and longitude to X and Y values on the map
        double rawX = (longitude - longitudeStart) * map.getWidth() / longitudeWidth;
        double rawY = (latitude - latitudeStart) * map.getHeight() / latitudeHeight;

        System.out.println("RawX " + rawX + ", RawY " + rawY);

        // Set the X and Y position of the marker to the center of the image
        int markerX = (int) (rawX) - (markerWidth / 2);
        int markerY = (int) (rawY)- (markerHeight / 2);

        System.out.println("MarkerX " + markerX + ", MarkerY " + markerY);

        // Limit the X and Y position to ensure the maker is completely within map bounds
        markerX = Math.max(0, Math.min(markerX, map.getWidth() - markerWidth));
        markerY = Math.max(0, Math.min(markerY, map.getHeight() - markerHeight));

        // Create the relative layout params with specified height and width
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(markerWidth, markerHeight);

        // set the margins of the layout params
        layoutParams.setMargins(markerX, markerY, 0, 0);

        // Create a new imageView for the marker
        ImageView imageView = new ImageView(context);

        // Gets the relative layout with markers id that marker will be added to.
        RelativeLayout layout = findViewById(R.id.markers);

        // Set layout params to imageView
        imageView.setLayoutParams(layoutParams);

        // Set drawable of marker to the created imageView
        Drawable drawable = AppCompatResources.
                getDrawable(context, drawableId);
        imageView.setImageDrawable(drawable);

        // Add the imageView to the layout
        layout.addView(imageView);

        if (animate) {
            // Cast the drawable of the animation resource to an animation drawable
            AnimationDrawable animation = (AnimationDrawable) drawable;

            if (animation == null) {
                // Do something when animation is null.
                return;
            }

            // Start the animation drawable
            animation.start();
        }
    }

    /**
     * Places a pacman marker.
     */
    private void placePacmanMarker(double latitude, double longitude) {

        int markerSize = getResources().getDimensionPixelSize(R.dimen.pacmanMarkerSize);
        placeMarker(latitude, longitude, markerSize, markerSize,
                MainActivity.this, R.drawable.pacman_marker_animation, true);

        //imageView.animate().x(x).y(y).setDuration(10000).withEndAction(() -> {}).start(); //Could be used for moving from one position to another.
    }

    /**
     * Gets the current location to update location used.
     */
    private void getCurrentLocation() {
        if (!(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        if (!isGPSEnabled()) {
            turnOnGPS();
        } else {
            LocationServices.getFusedLocationProviderClient(MainActivity.this)
                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);

                            LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                    .removeLocationUpdates(this);
                            // Remove request, without it would keep calling this function with new location updates.

                            if (locationResult.getLocations().size() > 0) {

                                Location location = locationResult.getLastLocation(); // Is this the same as get index - 1?
                                placePacmanMarker(location.getLatitude(), location.getLongitude());

                                // Old code for displaying latitude and longitude
                                int index = locationResult.getLocations().size() - 1;
                                //location = locationResult.getLocations().get(index);

                                double latitude = locationResult.getLocations().get(index).getLatitude();
                                double longitude = locationResult.getLocations().get(index).getLongitude();

                                AddressText.setText("Latitude: " + latitude + "\n" + "Longitude: " + longitude);

                            }
                        }
                    }, Looper.getMainLooper());
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