package com.example.pacmanapp.activities.map;

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
import android.widget.Toast;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.displays.Clock;
import com.example.pacmanapp.displays.Score;
import com.example.pacmanapp.location.DynamicLocation;
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.location.LocationUpdater;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapType;
import com.example.pacmanapp.markers.Ghost;
import com.example.pacmanapp.markers.GhostType;
import com.example.pacmanapp.markers.MapMarkers;
import com.example.pacmanapp.markers.PacDot;
import com.example.pacmanapp.markers.PowerPellet;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.AcceptAllSelector;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.Selector;
import com.example.pacmanapp.storage.SavePlatform;

import java.time.Duration;
import java.util.Random;

public class AdminMapActivity extends AppCompatActivity implements DynamicLocation {
    private final static String TAG = "AdminMapActivity";

    private LocationUpdater locationUpdater;
    private MapMarkers mapMarkers;
    private Selector selector;
    private Selector.SelectionListener selectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_edit);

        NavigationBar.configure(this, PageType.ADMIN_MAP);

        ViewGroup mapFrame = findViewById(R.id.pacManMapFrame);
        MapArea.createMap(MapType.PacMan, mapFrame);

        if (!SavePlatform.hasSave()) {
            Navigate.navigate(this, SaveActivity.class);
            finish();
            return;
        }

        mapMarkers = MapMarkers.getFromSave(SavePlatform.getSave());

        selector = AcceptAllSelector.getAcceptAllSelector(R.id.editAllSelector);
        selectionListener = AdminMapActivity.this::onSelection;

        Clock clock = new Clock(AdminMapActivity.this, AdminMapActivity.this);
        clock.setTime(Duration.ofSeconds(2678));

        Score score = new Score(5, R.id.scoreLayout,
                AdminMapActivity.this, AdminMapActivity.this);
        score.setValue(4678);
        locationUpdater = new LocationUpdater(AdminMapActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mapMarkers.loadMap(this, R.id.pacManMapFrame);

        addGhost();

        if (locationUpdater.isRequestingLocationUpdates()) {
            locationUpdater.startLocationUpdates();
        }

        selector = SelectionCrier.getInstance().getSelector(R.id.editAllSelector);
        onSelection(selector.getSelected()); // TODO figure out what the right place is for this selection code and how to simplify selection code.
        selector.addOnSelectionListener(selectionListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SavePlatform.save();
        locationUpdater.stopLocationUpdates();
        selector.removeOnSelectionListener(selectionListener);
        MapArea.getMapArea(this, R.id.pacManMapFrame).removeMarkers();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    private void createMarkers(int mapFrameId) {
//        // Create markers
//        mapMarkers.addMarker(new PacMan(mapFrameId,
//                51.4198767, 5.485905, AdminMapActivity.this));
//        mapMarkers.addMarker(new Ghost(GhostType.Blinky, mapFrameId,
//                51.4191783, 5.48632, AdminMapActivity.this));
//        mapMarkers.addMarker(new PowerPellet(mapFrameId,
//                51.4191983, 5.492802, AdminMapActivity.this));
//        mapMarkers.addMarker(new PacDot(mapFrameId,
//                51.419331, 5.48632, AdminMapActivity.this));
//    }

    // Location permission setup. //

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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

    @Override
    public void addObserver(LocationObserver locationObserver) {
        locationUpdater.addObserver(locationObserver);
    }

    /**
     * Update selectable content with new fetched selected.
     */
    private void onSelection(Selectable selectable) {
        SelectableContent.setContent(this, selectable);
    }

    public void createPacDot(View view) {
        if (!locationUpdater.hasLocation()) {
            Toast.makeText(getApplicationContext(),
                    "No location received yet, try again later", Toast.LENGTH_SHORT).show();
            return;
        }
        Location location = locationUpdater.getLastLocation();
        mapMarkers.addMarker(new PacDot(R.id.pacManMapFrame,
                location.getLatitude(), location.getLongitude(), this));
    }

    public void createPowerPellet(View view) {
        if (!locationUpdater.hasLocation()) {
            Toast.makeText(getApplicationContext(),
                    "No location received yet, try again later", Toast.LENGTH_SHORT).show();
            return;
        }
        Location location = locationUpdater.getLastLocation();
        mapMarkers.addMarker(new PowerPellet(R.id.pacManMapFrame,
                location.getLatitude(), location.getLongitude(), this));

    }

    public void addGhost() {
        // Get a random ghost type
        Random random = new Random();
        GhostType ghostType = GhostType.values()[random.nextInt(GhostType.values().length)];

        // Create a new ghost on the next location result
        locationUpdater.observeNextLocation(location -> {
            if (location == null) {
                Log.e(TAG, "Could not add ghost without valid location");
                throw new NullPointerException("Location could not be observed");
            }
            Ghost ghost = new Ghost(ghostType, R.id.pacManMapFrame,
                    location.getLatitude(), location.getLongitude(), AdminMapActivity.this);
            ghost.loadOnMapArea(getApplicationContext());
        });
    }

}