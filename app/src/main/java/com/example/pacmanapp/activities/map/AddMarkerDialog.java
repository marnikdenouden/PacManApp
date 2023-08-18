package com.example.pacmanapp.activities.map;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;
import com.example.pacmanapp.location.LocationUpdater;
import com.example.pacmanapp.markers.MapMarkers;
import com.example.pacmanapp.markers.Marker;
import com.example.pacmanapp.markers.PacDot;
import com.example.pacmanapp.markers.PowerPellet;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AddMarkerDialog extends DialogFragment {
    private final AppCompatActivity activity;
    private final MapMarkers mapMarkers;
    private final LocationUpdater locationUpdater;
    private final int frameId;
    private static Marker marker;

    /**
     * Create add marker dialog that can create a new marker for a specified map frame.
     *
     * @param activity Activity to create dialog for
     * @param mapMarkers Map markers to add potential created marker to
     * @param locationUpdater Location updater to use for creating marker
     * @param frameId Frame id to use for potential created marker
     */
    public AddMarkerDialog(@NotNull AppCompatActivity activity, @NotNull MapMarkers mapMarkers,
                           @NotNull LocationUpdater locationUpdater, int frameId) {
        this.activity = activity;
        this.mapMarkers = mapMarkers;
        this.locationUpdater = locationUpdater;
        this.frameId = frameId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);

        builder.setTitle("Select marker to add");

        builder.setView(R.layout.dialog_add_marker)
                .setPositiveButton("Add", (dialogInterface, i) -> addMarker())
                .setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());

        return builder.create();
    }

    private void addMarker() {
        if (marker != null) {
            mapMarkers.addMarker(marker);
            dismiss();
        }
    }

    private double getLatitude() {
        return locationUpdater.getLastLocation().getLatitude();
    }

    private double getLongitude() {
        return locationUpdater.getLastLocation().getLongitude();
    }

    public void selectPacDot() {
        marker = new PacDot(frameId, getLatitude(), getLongitude(), activity);
        Objects.requireNonNull(getDialog()).setTitle("Add pac dot?");
    }

    public void selectPowerPellet() {
        marker = new PowerPellet(frameId, getLatitude(), getLongitude(), activity);
        Objects.requireNonNull(getDialog()).setTitle("Add power pellet?");
    }
}
