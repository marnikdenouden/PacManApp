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

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.displays.Clock;
import com.example.pacmanapp.displays.Score;
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

public class PlayMapActivity extends AppCompatActivity {
    private LocationUpdater locationUpdater;
    private MapMarkers mapMarkers;
    private Selector selector;
    private Selector.SelectionListener selectionListener;
    private final String TAG = "PlayMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button createMarkerButton = findViewById(R.id.createMarkersButton);

        NavigationBar.configure(this, PageType.MAP);

        selector = new AcceptAllSelector(R.id.inspectAllSelector);
        selectionListener = PlayMapActivity.this::onSelection;

        if (!SavePlatform.hasSave()) {
            Navigate.navigate(this, SaveActivity.class);
            finish();
            return;
        }

        mapMarkers = MapMarkers.getFromSave(SavePlatform.getSave());

        ViewGroup mapFrame = findViewById(R.id.pacManMapFrame);
        MapArea.createMap(MapType.PacMan, mapFrame);


        Clock clock = new Clock(PlayMapActivity.this, PlayMapActivity.this);
        clock.setTime(Duration.ofSeconds(2678));

        Score score = new Score(5, R.id.scoreLayout,
                PlayMapActivity.this, PlayMapActivity.this);
        score.setValue(4678);

        locationUpdater = new LocationUpdater(PlayMapActivity.this);

        createMarkerButton.setOnClickListener(v -> createMarkers(R.id.pacManMapFrame));

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

    @Override
    protected void onStart() {
        super.onStart();
        selector = SelectionCrier.getInstance().getSelector(R.id.inspectAllSelector);
        onSelection(selector.getSelected());
        selector.addOnSelectionListener(selectionListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        selector.removeOnSelectionListener(selectionListener);
        MapArea.getMapArea(this, R.id.pacManMapFrame).removeMarkers();
    }

    /**
     * Create markers to add to the map.
     *
     * @param mapFrameId Map frame id to add markers to
     */
    private void createMarkers(int mapFrameId) {
        Log.i(TAG, "Created markers for the map");
        // Create markers
        mapMarkers.addMarker(new PacMan(mapFrameId,
                51.4198767, 5.485905, PlayMapActivity.this));
        mapMarkers.addMarker(new Ghost(GhostType.Blinky, mapFrameId,
                51.4191783, 5.48632, PlayMapActivity.this));
        mapMarkers.addMarker(new PowerPellet(mapFrameId,
                51.4191983, 5.492802, PlayMapActivity.this));
        mapMarkers.addMarker(new PacDot(mapFrameId,
                51.419331, 5.48632, PlayMapActivity.this));
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

    /**
     * Update selectable content with new fetched selected.
     */
    private void onSelection(Selectable selectable) {
        SelectableContent.setContent(this, selectable);
    }

}