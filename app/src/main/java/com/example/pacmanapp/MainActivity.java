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
import android.os.Bundle;

import android.os.Looper;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pacmanapp.displays.Clock;
import com.example.pacmanapp.displays.Score;
import com.example.pacmanapp.location.LocationUpdater;
import com.example.pacmanapp.location.locationObserver;
import com.example.pacmanapp.markers.Ghost;
import com.example.pacmanapp.markers.PacDot;
import com.example.pacmanapp.markers.PacMan;
import com.example.pacmanapp.markers.PowerPallet;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.time.Duration;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements locationObserver {

    private LocationUpdater locationUpdater;

    private TextView AddressText;
    private Button LocationButton;

    private View layout;



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



        locationUpdater = new LocationUpdater(MainActivity.this, MainActivity.this);

        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    layout.setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
                    Log.i(TAG, "Stop location updates");
                    createMarkers(); // TODO move create markers so it is run at a good time.
                } else {
                    locationUpdater.startLocationUpdates();
                    Log.i(TAG, "Start location updates");
                    layout.setBackgroundColor(getResources().getColor(R.color.black, getTheme()));
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationUpdater.stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationUpdater.isRequestingLocationUpdates()) {
            locationUpdater.startLocationUpdates();
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

    // Location result updating. //

    /**
     * Update marker locations with a new location.
     *
     * @param location Location to update markers with
     */
    public void updateMarkerLocations(Location location) {
        if (pacman != null) {
            pacman.move(location.getLatitude(), location.getLongitude(), MainActivity.this, MainActivity.this);
        }
        if (ghost != null) {
            //ghost.move(location.getLatitude() - 0.002, location.getLongitude() + 0.002, MainActivity.this, MainActivity.this);
        }

        }

    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        updateMarkerLocations(locationResult.getLastLocation());
    }

    // Location permission setup. //

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0) {
            return;
        }

        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            locationUpdater.startLocationUpdates();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            locationUpdater.startLocationUpdates();
        }
    }


}