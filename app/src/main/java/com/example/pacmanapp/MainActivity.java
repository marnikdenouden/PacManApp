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
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.map.MapType;
import com.example.pacmanapp.markers.Ghost;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.markers.GhostType;
import com.example.pacmanapp.markers.MapMarkers;
import com.example.pacmanapp.markers.PacDot;
import com.example.pacmanapp.markers.PacMan;
import com.example.pacmanapp.markers.PowerPallet;
import com.example.pacmanapp.storage.GameSave;
import com.example.pacmanapp.storage.SaveManager;
import com.google.android.gms.location.LocationResult;

import java.time.Duration;


public class MainActivity extends AppCompatActivity {

    private LocationUpdater locationUpdater;

    private TextView AddressText;
    private Button LocationButton;
    private Button createMarkerButton;
    private Button loadGameButton;
    private Button saveGameButton;

    private ViewGroup layout;

    private SaveManager saveManager;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddressText = findViewById(R.id.addressText);
        LocationButton = findViewById(R.id.locationButton);
        createMarkerButton = findViewById(R.id.createMarkersButton);
        loadGameButton = findViewById(R.id.loadGameButton);
        saveGameButton = findViewById(R.id.saveGameButton);
        layout = findViewById(R.id.layout);

        ViewGroup mapFrame = findViewById(R.id.pacManMapFrame);
        MapArea.addMap(MapType.PacMan, mapFrame);
        saveManager = new SaveManager(getApplicationContext());
        // Ensure that save test is loaded or created.
        if (saveManager.hasSave("Test")) {
            saveManager.loadSave("Test", getApplicationContext());
        } else {
            saveManager.createSave("Test");
        }

        Clock clock = new Clock(MainActivity.this, MainActivity.this);
        clock.setTime(Duration.ofSeconds(2678));

        Score score = new Score(5, R.id.scoreLayout, MainActivity.this, MainActivity.this);
        score.setValue(4678);

        locationUpdater = new LocationUpdater(MainActivity.this, MainActivity.this);
        saveManager.getCurrentSave().passLocationUpdater(locationUpdater);

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

        createMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMarkers(R.id.pacManMapFrame);
            }
        });

        loadGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveManager.loadCurrentSave(getApplicationContext());
                //saveManager.loadSave("Test", getApplicationContext());
            }
        });

        saveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveManager.saveCurrentSave();
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
        MapMarkers mapMarkers = new MapMarkers(saveManager);

        // Create markers
        mapMarkers.addMarker(new PacMan(mapFrameId, 51.4198767, 5.485905,
                MainActivity.this));
        mapMarkers.addMarker(new Ghost(GhostType.Blinky, mapFrameId, 51.4191783, 5.48632,
                MainActivity.this));
        mapMarkers.addMarker(new PowerPallet(mapFrameId,51.4191983, 5.492802,
                MainActivity.this));
        mapMarkers.addMarker(new PacDot(mapFrameId,51.419331, 5.48632,
                MainActivity.this));

        mapMarkers.passLocationUpdater(locationUpdater);
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