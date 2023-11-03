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

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.map.MapMarkers;
import com.example.pacmanapp.map.MapSave;
import com.example.pacmanapp.map.MapStorage;
import com.example.pacmanapp.selection.selectables.Blank;
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
import com.example.pacmanapp.markers.PacMan;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.AcceptAllSelector;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.Selector;
import com.example.pacmanapp.storage.GameSave;
import com.example.pacmanapp.storage.SavePlatform;

import java.util.Collection;

public class PlayMapActivity extends AppCompatActivity
        implements DynamicLocation, Navigate.BaseActivity {
    private final static String TAG = "PlayMapActivity";
    private LocationUpdater locationUpdater;
    private MapSave mapSave;
    private MapArea mapArea;
    private Selector selector;
    private SelectableContent.Preview preview;
    private final Selector.SelectionListener selectionListener = PlayMapActivity.this::onSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // TODO add ghost interaction button
        //Button ghostInteractionButton = findViewById(R.id.ghostInteractionButton);

        NavigationBar.configure(this, PageType.MAP);

        // Get selector to make sure it gets relevant selections.
        selector = AcceptAllSelector.getSelector(R.id.inspectAllSelector,
                new InfoInspect(getResources()));

        preview = new SelectableContent.Preview(new BlankInspect(getResources()));
        ViewGroup viewGroup = findViewById(R.id.selected_preview);
        preview.configure(this, viewGroup, false);

        if (!SavePlatform.hasSave()) {
            Navigate.navigate(this, SaveActivity.class);
            finish();
            return;
        }

        GameSave gameSave = SavePlatform.getSave();
        MapStorage mapStorage = MapStorage.getFromSave(gameSave);
        mapSave = mapStorage.loadMapSave(R.id.pacManMapFrame, MapType.PACMAN_FRANSEBAAN);

        Clock clock = new Clock(gameSave);
        clock.updateDisplay(PlayMapActivity.this, R.color.onPrimaryContainer);

        Score score = new Score(gameSave);
        score.updateDisplay(5, R.id.scoreLayout, PlayMapActivity.this,
                R.color.onPrimaryContainer);

        locationUpdater = new LocationUpdater(PlayMapActivity.this);

//        ghostInteractionButton.setOnClickListener(view -> {
//            Toast.makeText(PlayMapActivity.this,
//                    "Get captured by ghost or eat a ghost.", Toast.LENGTH_SHORT).show(); // TODO implement the button
//        });

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
        ViewGroup mapFrame = findViewById(R.id.pacManMapFrame);
        mapArea = mapSave.loadMap(mapFrame);

        // Call on selection with currently selected to load in preview
        Selectable selectable = selector.getSelected();
        if (!(selectable instanceof Blank)) {
            onSelection(selectable);
        }

        // Add selection listener to selector to update selection preview
        selector.addOnSelectionListener(selectionListener);

        // TODO When going back to old map activity location updates do not work.
        if (locationUpdater.isRequestingLocationUpdates()) {
            locationUpdater.startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapSave.unloadMap(mapArea);
        selector.removeOnSelectionListener(selectionListener);
        locationUpdater.stopLocationUpdates();
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
        MapMarkers mapMarkers = mapSave.getMapMarkers();
        // Remove all characters, except a single pacman, from the map markers collection
        Collection<Character> currentCharacters = mapMarkers.getMarkersWithClass(Character.class);
        boolean foundPacMan = false;
        for (Character character: currentCharacters) {
            if (character instanceof PacMan && !foundPacMan) {
                foundPacMan = true;
            } else {
                mapMarkers.removeMarker(character);
            }
        }

        // If a pacman was found to be part of the map markers collection,
        // then we do not need to create a new one.
        if (foundPacMan) {
            return;
        }

        locationUpdater.observeNextLocation(location -> {
            PacMan pacMan = new PacMan(location.getLatitude(), location.getLongitude());
            mapMarkers.addMarker(pacMan);
            Log.i(TAG, "Added new pacman to pac man map frame.");
        });

    }

}