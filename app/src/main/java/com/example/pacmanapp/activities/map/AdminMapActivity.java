package com.example.pacmanapp.activities.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.general.Util;
import com.example.pacmanapp.map.MapMarkers;
import com.example.pacmanapp.map.MapSave;
import com.example.pacmanapp.map.MapStorage;
import com.example.pacmanapp.markers.BlankMarker;
import com.example.pacmanapp.markers.Marker;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.TypeSelector;
import com.example.pacmanapp.selection.selectables.Blank;
import com.example.pacmanapp.selection.selectables.BlankEdit;
import com.example.pacmanapp.selection.selectables.InfoEdit;
import com.example.pacmanapp.selection.selectables.InfoInspect;
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
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.AcceptAllSelector;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.Selector;
import com.example.pacmanapp.storage.GameSave;
import com.example.pacmanapp.storage.GameSettings;
import com.example.pacmanapp.storage.SavePlatform;

import java.time.Duration;
import java.util.Collection;
import java.util.Random;

public class AdminMapActivity extends AppCompatActivity
        implements DynamicLocation, Navigate.BaseActivity, RemoveMarkerDialog.Remove {
    private final static String TAG = "AdminMapActivity";

    private LocationUpdater locationUpdater;
    private MapSave mapSave;
    private MapArea mapArea;
    private Selector selector;
    private TypeSelector markerSelector;
    private SelectableContent.Preview preview;
    private final Selector.SelectionListener selectionListener = AdminMapActivity.this::onSelection;
    private AddMarkerDialog addMarkerDialog;
    private Score score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO dissect this method into more clear section, like configuring buttons and such.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_edit);

        NavigationBar.configure(this, PageType.ADMIN_MAP);

        if (!SavePlatform.hasSave()) {
            Navigate.navigate(this, SaveActivity.class);
            finish();
            return;
        }

        GameSave gameSave = SavePlatform.getSave();
        MapStorage mapStorage = MapStorage.getFromSave(gameSave);
        mapSave = mapStorage.loadMapSave(R.id.pacManMapFrame, MapType.SATELLITE_TUE_CAMPUS);

        // Get selectors to make sure they get relevant selections.
        AcceptAllSelector.getSelector(R.id.inspectAllSelector,
                new InfoInspect(getResources()));
        markerSelector = TypeSelector.getSelector(R.id.markerSelector,
                new BlankMarker(getResources()), Marker.class);
//        markerSelector.addOnSelectionListener(new Selector.SelectionListener() {
//            @Override
//            public void onSelect(Selectable selectable) {
//                if (selectable instanceof Marker) {
//                    Marker marker = (Marker) selectable;
//                    // TODO make marker look selected. Maybe in different place/class
//                }
//            }
//        });
        selector = AcceptAllSelector.getSelector(R.id.editAllSelector,
                new InfoEdit(getResources()));

        View addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(view -> openAddMarkerDialog());

        View removeButton = findViewById(R.id.remove_button);
        removeButton.setOnClickListener(view -> openRemoveMarkerDialog());

        preview = new SelectableContent.Preview(new BlankEdit(getResources()));
        ViewGroup selectableView = findViewById(R.id.selected_preview);
        preview.configure(this, selectableView, true);

        EditText editText = findViewById(R.id.editStartDuration);
        GameSettings gameSettings = GameSettings.getFromSave(gameSave);
        long gameDurationSeconds = gameSettings.getGameDuration().getSeconds();
        Util.configureEditText(editText, String.valueOf(gameDurationSeconds), text -> {
            if (text.equals("")) {
                return;
            }
            Duration newGameDuration = Duration.ofSeconds(Integer.parseInt(text));
            gameSettings.setGameDuration(newGameDuration);
        });

        score = new Score(gameSave);
        score.updateDisplay(5, R.id.scoreLayout, AdminMapActivity.this,
                R.color.onPrimaryContainer);

        locationUpdater = new LocationUpdater(AdminMapActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Load ghost and then load map to have old characters removed
        // and new one created on next location update.
        loadGhost();
        ViewGroup mapFrame = findViewById(R.id.pacManMapFrame);
        mapArea = mapSave.loadMap(mapFrame);

        // Call on selection with currently selected to load in preview
        Selectable selectable = selector.getSelected();
        if (!(selectable instanceof Blank)) {
            onSelection(selectable);
        }

        // Add selection listener to selector to update selection preview
        selector.addOnSelectionListener(selectionListener);

        addObserver(mapSave); // TODO probably a better way to do this than this
        if (locationUpdater.isRequestingLocationUpdates()) {
            locationUpdater.startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SavePlatform.save();
        mapSave.unloadMap(mapArea);
        selector.removeOnSelectionListener(selectionListener);
        removeObserver(mapSave);
        locationUpdater.stopLocationUpdates();
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
        if (!(selectable instanceof Blank)) {
            findViewById(R.id.remove_button).setVisibility(View.VISIBLE);
        }
        preview.update(selectable);
    }

    /**
     * Add marker to current map and location using the add marker dialog.
     */
    public void openAddMarkerDialog() {
        if (!locationUpdater.hasLocation()) {
            Toast.makeText(getApplicationContext(),
                    "Please wait on location", Toast.LENGTH_SHORT).show();
            return;
        }

        addMarkerDialog = new AddMarkerDialog(this, mapSave.getMapMarkers(), locationUpdater,
                R.id.pacManMapFrame);
        addMarkerDialog.show(getSupportFragmentManager(), "AddMarker");
    }

    /**
     * Open dialog to remove the currently selected marker from the map.
     */
    public void openRemoveMarkerDialog() {
        if (!markerSelector.hasSelected()) {
            Toast.makeText(this, "Nothing selected to remove", Toast.LENGTH_SHORT).show();
            return;
        }
        RemoveMarkerDialog removeMarkerDialog = new RemoveMarkerDialog();
        removeMarkerDialog.show(getSupportFragmentManager(), "RemoveMarker");
    }

    /**
     * Remove the selected marker from the map and selection.
     */
    public void removeMarker() {
        if (!markerSelector.hasSelected()) {
            return;
        }
        Selectable selectedMarker = markerSelector.getSelected();
        mapSave.getMapMarkers().removeMarker((Marker) selectedMarker);
        SelectionCrier.getInstance().removeSelected(selectedMarker);
    }

    /**
     * Select pac dot for the add marker dialog.
     *
     * @param view View that method can be called from
     */
    public void selectPacDot(View view) {
        addMarkerDialog.selectPacDot();
    }

    /**
     * Select power pellet for the add marker dialog.
     *
     * @param view View that method can be called from
     */
    public void selectPowerPellet(View view) {
        addMarkerDialog.selectPowerPellet();
    }

    /**
     * Load ghost character, which ensures one after the next location update.
     */
    private void loadGhost() {
        MapMarkers mapMarkers = mapSave.getMapMarkers();
        // Remove all characters, except a single ghost, from the map markers collection
        Collection<Character> currentCharacters = mapMarkers.getMarkersWithClass(Character.class);
        boolean foundGhost = false;
        for (Character character: currentCharacters) {
            if (character instanceof Ghost && !foundGhost) {
                foundGhost = true;
            } else {
                mapMarkers.removeMarker(character);
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
            Ghost ghost = new Ghost(ghostType, location.getLatitude(), location.getLongitude());
            mapMarkers.addMarker(ghost);
            Log.i(TAG, "Added new ghost to pac man map frame.");
        });
    }

}