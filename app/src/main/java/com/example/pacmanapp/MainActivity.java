package com.example.pacmanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pacmanapp.displays.Clock;
import com.example.pacmanapp.displays.Score;
import com.example.pacmanapp.location.LocationUpdater;
import com.example.pacmanapp.location.locationObserver;
import com.example.pacmanapp.map.MapType;
import com.example.pacmanapp.markers.Ghost;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.markers.GhostType;
import com.example.pacmanapp.markers.PacDot;
import com.example.pacmanapp.markers.PacMan;
import com.example.pacmanapp.markers.PowerPallet;
import com.google.android.gms.location.LocationResult;

import java.time.Duration;


public class MainActivity extends AppCompatActivity implements locationObserver {

    private LocationUpdater locationUpdater;

    private TextView AddressText;
    private Button LocationButton;

    private ViewGroup layout;

    private PacMan pacman;
    private Ghost ghost;
    private PowerPallet powerPallet;
    private PacDot pacDot;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddressText = findViewById(R.id.addressText);
        LocationButton = findViewById(R.id.locationButton);
        layout = findViewById(R.id.layout);

        ViewGroup mapFrame = findViewById(R.id.pacManMapFrame);
        MapArea.addMap(MapType.PacMan, mapFrame);
        createMarkers(R.id.pacManMapFrame);

        Clock clock = new Clock(MainActivity.this, MainActivity.this);
        clock.setTime(Duration.ofSeconds(2678));

        Score score = new Score(5, R.id.scoreLayout, MainActivity.this, MainActivity.this);
        score.setValue(4678);

        locationUpdater = new LocationUpdater(MainActivity.this, MainActivity.this);

        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationUpdater.isRequestingLocationUpdates()) {
                    locationUpdater.stopLocationUpdates();
                    layout.setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
                    Log.i(TAG, "Stop location updates");
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

    private void createMarkers(int mapFrameId) {
        pacman = new PacMan(mapFrameId, 51.4198767, 5.485905, MainActivity.this, MainActivity.this);
        ghost = new Ghost(GhostType.Blinky, mapFrameId, 0, 0, MainActivity.this, MainActivity.this);
        powerPallet = new PowerPallet(mapFrameId,51.4191983, 5.492802, MainActivity.this, MainActivity.this);
        pacDot = new PacDot(mapFrameId,51.419331, 5.48632, MainActivity.this, MainActivity.this);
    }

    // Location result updating. //

    /**
     * Update marker locations with a new location.
     *
     * @param location Location to update markers with
     */
    public void updateMarkerLocations(Location location) {
        if (pacman != null) {
            pacman.move(location.getLatitude(), location.getLongitude());
            pacman.updatePlacement();
        }
        if (ghost != null) {
            //ghost.move(location.getLatitude() - 0.002, location.getLongitude() + 0.002, MainActivity.this, MainActivity.this);
        }

        if (pacDot != null) {
            pacDot.updatePlacement();
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