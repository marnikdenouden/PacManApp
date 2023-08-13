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
import com.example.pacmanapp.contents.InfoEdit;
import com.example.pacmanapp.contents.InfoInspect;
import com.example.pacmanapp.displays.Clock;
import com.example.pacmanapp.displays.Score;
import com.example.pacmanapp.location.DynamicLocation;
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.location.LocationUpdater;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapType;
import com.example.pacmanapp.markers.Character;
import com.example.pacmanapp.markers.Ghost;
import com.example.pacmanapp.markers.GhostType;
import com.example.pacmanapp.markers.MapMarkers;
import com.example.pacmanapp.markers.Marker;
import com.example.pacmanapp.markers.PacDot;
import com.example.pacmanapp.markers.PowerPellet;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.AcceptAllSelector;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.Selector;
import com.example.pacmanapp.storage.SavePlatform;

import java.time.Duration;
import java.util.Collection;
import java.util.Random;

public class AdminMapActivity extends AppCompatActivity implements DynamicLocation {
    private final static String TAG = "AdminMapActivity";

    private LocationUpdater locationUpdater;
    private MapMarkers mapMarkers;
    private Selector selector;
    private final Selector.SelectionListener selectionListener = AdminMapActivity.this::onSelection;

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

        // Get selectors to make sure they get relevant selections.
        AcceptAllSelector.getAcceptAllSelector(R.id.inspectAllSelector, new InfoInspect(getResources()));
        selector = AcceptAllSelector.getAcceptAllSelector(R.id.editAllSelector, new InfoEdit(getResources()));

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

        // Load ghost and then load map to have old characters removed
        // and new one created on next location update.
        loadGhost();
        mapMarkers.loadMap(this, R.id.pacManMapFrame);

        if (locationUpdater.isRequestingLocationUpdates()) {
            locationUpdater.startLocationUpdates();
        }

        // Call on selection with currently selected to load in preview
        onSelection(selector.getSelected());

        // Add selection listener to selector to update selection preview
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
    public void addObserver(@NonNull LocationObserver locationObserver) {
        locationUpdater.addObserver(locationObserver);
    }

    @Override
    public void removeObserver(@NonNull LocationObserver locationObserver) {
        locationUpdater.removeObserver(locationObserver);
    }

    /**
     * Update selectable content with new fetched selected.
     */
    private void onSelection(Selectable selectable) {
        SelectableContent.setData(this, selectable, false);
    }

    /**
     * Create pac dot on last location.
     *
     * @param view View that method can be called from.
     */
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

    /**
     * Create power pellet on last location.
     *
     * @param view View that method can be called from.
     */
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

    /**
     * Load ghost character, which ensures one after the next location update.
     */
    private void loadGhost() {
        // Remove all characters, except a single ghost, from the map markers collection
        Collection<Marker> currentCharacters =
                mapMarkers.getMarkersWithClass(R.id.pacManMapFrame, Character.class);
        boolean foundGhost = false;
        for (Marker marker: currentCharacters) {
            if (marker instanceof Ghost && !foundGhost) {
                foundGhost = true;
            } else {
                mapMarkers.removeMarker(marker);
            }
        }

        // If a ghost was found to be part of the map markers collection,
        // then we do not need to create a new one.
        if (foundGhost) {
            return;
        }

        // Get a random ghost type
        Random random = new Random();
        GhostType ghostType = GhostType.values()[random.nextInt(GhostType.values().length)];

        // Create a new ghost on the next location result
        locationUpdater.observeNextLocation(location -> {
            Ghost ghost = new Ghost(ghostType, R.id.pacManMapFrame,
                    location.getLatitude(), location.getLongitude(), AdminMapActivity.this);
            mapMarkers.addMarker(ghost);
            Log.i(TAG, "Added new ghost to pac man map frame.");
        });
    }

}