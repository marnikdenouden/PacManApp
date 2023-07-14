package com.example.pacmanapp.activities.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.displays.Clock;
import com.example.pacmanapp.displays.Score;
import com.example.pacmanapp.location.LocationUpdater;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapType;
import com.example.pacmanapp.markers.MapMarkers;
import com.example.pacmanapp.markers.PacMan;
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
    private final static String TAG = "PlayMapActivity";
    private LocationUpdater locationUpdater;
    private MapMarkers mapMarkers;
    private Selector selector;
    private Selector.SelectionListener selectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button ghostInteractionButton = findViewById(R.id.ghostInteractionButton);

        NavigationBar.configure(this, PageType.MAP);

        selector = AcceptAllSelector.getAcceptAllSelector(R.id.inspectAllSelector);
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
        mapMarkers.loadMap(this, R.id.pacManMapFrame); // TODO figure out where to load and remove markers.

        locationUpdater.observeNextLocation(location -> {
            PacMan pacMan = new PacMan(R.id.pacManMapFrame,
                    location.getLatitude(), location.getLongitude(), PlayMapActivity.this);
            pacMan.loadOnMapArea(getApplicationContext());
        });

        if (locationUpdater.isRequestingLocationUpdates()) {
            locationUpdater.startLocationUpdates(); // TODO figure out where this should go
        }

        selector = SelectionCrier.getInstance().getSelector(R.id.inspectAllSelector);
        onSelection(selector.getSelected());
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

    /**
     * Update selectable content with new fetched selected.
     */
    private void onSelection(Selectable selectable) {
        SelectableContent.setContent(this, selectable);
    }

}