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
import android.widget.Button;
import android.widget.TextView;

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
import com.example.pacmanapp.markers.PacMan;
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

public class AdminMapActivity extends AppCompatActivity implements DynamicLocation {

    private LocationUpdater locationUpdater;

    private Button createMarkerButton;
    private Button saveGameButton;

    private ViewGroup layout;
    private MapMarkers mapMarkers;
    private Selector selector;
    private Selector.SelectionListener selectionListener;
    private final String TAG = "PlayMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_edit);

        createMarkerButton = findViewById(R.id.createMarkersButton);
        saveGameButton = findViewById(R.id.saveGameButton);
        layout = findViewById(R.id.layout);

        NavigationBar.configure(this, true, PageType.MAP);

        ViewGroup mapFrame = findViewById(R.id.pacManMapFrame);
        MapArea.createMap(MapType.PacMan, mapFrame);

        if (!SavePlatform.hasSave()) {
            Navigate.navigate(this, SaveActivity.class);
            finish();
            return;
        }

        mapMarkers = MapMarkers.getFromSave(SavePlatform.getSave());

        selector = new AcceptAllSelector(R.id.editAllSelector);
        selectionListener = AdminMapActivity.this::onSelection;

        Clock clock = new Clock(AdminMapActivity.this, AdminMapActivity.this);
        clock.setTime(Duration.ofSeconds(2678));

        Score score = new Score(5, R.id.scoreLayout,
                AdminMapActivity.this, AdminMapActivity.this);
        score.setValue(4678);
// TODO add interface that signifies that the activity updates locations, so when the map is loaded the markers can request to be added to the location updater.
        // TODO also passing the same thing twice in a method does not make sense.
        locationUpdater = new LocationUpdater(AdminMapActivity.this);

        createMarkerButton.setOnClickListener(view -> createMarkers(R.id.pacManMapFrame));

        saveGameButton.setOnClickListener(view -> SavePlatform.save());
    }

    @Override
    protected void onStart() {
        super.onStart();
        selector = SelectionCrier.getInstance().getSelector(R.id.editAllSelector);
        onSelection(selector.getSelected());
        selector.addOnSelectionListener(selectionListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        selector.removeOnSelectionListener(selectionListener);
        MapArea mapArea = MapArea.getMapArea(this, R.id.pacManMapFrame);
        mapArea.removeAllViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationUpdater.stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapMarkers.loadMap(this, R.id.pacManMapFrame);
        if (locationUpdater.isRequestingLocationUpdates()) {
            locationUpdater.startLocationUpdates();
        }
    }

    private void createMarkers(int mapFrameId) {
        // Create markers
        mapMarkers.addMarker(new PacMan(mapFrameId,
                51.4198767, 5.485905, AdminMapActivity.this));
        mapMarkers.addMarker(new Ghost(GhostType.Blinky, mapFrameId,
                51.4191783, 5.48632, AdminMapActivity.this));
        mapMarkers.addMarker(new PowerPellet(mapFrameId,
                51.4191983, 5.492802, AdminMapActivity.this));
        mapMarkers.addMarker(new PacDot(mapFrameId,
                51.419331, 5.48632, AdminMapActivity.this));
        mapMarkers.loadMap(this, mapFrameId);
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
    public void addObserver(LocationObserver locationObserver) {
        locationUpdater.addObserver(locationObserver);
    }

    /**
     * Update selectable content with new fetched selected.
     */
    private void onSelection(Selectable selectable) {
        SelectableContent.setContent(this, selectable);
    }
}