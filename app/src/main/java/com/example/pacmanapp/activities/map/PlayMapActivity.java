package com.example.pacmanapp.activities.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.selection.selectables.BlankInspect;
import com.example.pacmanapp.selection.selectables.InfoInspect;
import com.example.pacmanapp.displays.Clock;
import com.example.pacmanapp.displays.Score;
import com.example.pacmanapp.location.DynamicLocation;
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.location.LocationUpdater;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapType;
import com.example.pacmanapp.markers.Character;
import com.example.pacmanapp.markers.MapMarkers;
import com.example.pacmanapp.markers.Marker;
import com.example.pacmanapp.markers.PacMan;
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

public class PlayMapActivity extends AppCompatActivity
        implements DynamicLocation, Navigate.BaseActivity {
    private final static String TAG = "PlayMapActivity";
    private LocationUpdater locationUpdater;
    private MapMarkers mapMarkers;
    private Selector selector;
    private SelectableContent.Preview preview;
    private final Selector.SelectionListener selectionListener = PlayMapActivity.this::onSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button ghostInteractionButton = findViewById(R.id.ghostInteractionButton);

        NavigationBar.configure(this, PageType.MAP);

        // Get selector to make sure it gets relevant selections.
        selector = AcceptAllSelector.getAcceptAllSelector(R.id.inspectAllSelector,
                new BlankInspect(getResources()));

        preview = new SelectableContent.Preview(selector.getSelected());
        ViewGroup viewGroup = findViewById(R.id.selected_preview);
        preview.addView(this, viewGroup, false);

        if (!SavePlatform.hasSave()) {
            Navigate.navigate(this, SaveActivity.class);
            finish();
            return;
        }

        mapMarkers = MapMarkers.getFromSave(SavePlatform.getSave());

        ViewGroup mapFrame = findViewById(R.id.pacManMapFrame);
        MapArea.createMap(MapType.PacMan, mapFrame);


        Clock clock = new Clock(PlayMapActivity.this, PlayMapActivity.this,
                R.color.onPrimaryContainer);
        clock.setTime(Duration.ofSeconds(2678));

        Score score = new Score(5, R.id.scoreLayout,
                PlayMapActivity.this, PlayMapActivity.this,
                R.color.onPrimaryContainer);
        score.setValue(4678);

        locationUpdater = new LocationUpdater(PlayMapActivity.this);

        ghostInteractionButton.setOnClickListener(view -> {
            Toast.makeText(PlayMapActivity.this,
                    "Get captured by ghost or eat a ghost.", Toast.LENGTH_SHORT).show(); // TODO implement the button
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Load ghost and then load map to have old characters removed
        // and new one created on next location update.
        loadPacMan();
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
        MapArea.getMapArea(this, R.id.pacManMapFrame).removeMarkers();
        locationUpdater.stopLocationUpdates();
        selector.removeOnSelectionListener(selectionListener);
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
        preview.update(selectable);
    }

    /**
     * Load pacman character, which ensures one after the next location update.
     */
    private void loadPacMan() {
        // Remove all characters, except a single pacman, from the map markers collection
        Collection<Marker> currentCharacters =
                mapMarkers.getMarkersWithClass(R.id.pacManMapFrame, Character.class);
        boolean foundPacMan = false;
        for (Marker marker: currentCharacters) {
            if (marker instanceof PacMan && !foundPacMan) {
                foundPacMan = true;
            } else {
                mapMarkers.removeMarker(marker);
            }
        }

        // If a pacman was found to be part of the map markers collection,
        // then we do not need to create a new one.
        if (foundPacMan) {
            return;
        }

        locationUpdater.observeNextLocation(location -> {
            PacMan pacMan = new PacMan(R.id.pacManMapFrame,
                    location.getLatitude(), location.getLongitude(), PlayMapActivity.this);
            mapMarkers.addMarker(pacMan);
            Log.i(TAG, "Added new pacman to pac man map frame.");
        });

    }

}