package com.example.pacmanapp.activities.map;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.BlankMarker;
import com.example.pacmanapp.markers.MapMarkers;
import com.example.pacmanapp.markers.Marker;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.TypeSelector;
import com.example.pacmanapp.storage.SavePlatform;

public class RemoveMarkerDialog extends DialogFragment {
    private final Marker marker;

    /**
     * Create a dialog to confirm removing the last selected marker.
     */
    public RemoveMarkerDialog() {
        TypeSelector typeSelector = TypeSelector.getSelector(R.id.markerSelector,
                new BlankMarker(getResources()), Marker.class);
        if (typeSelector.hasSelected()) {
            marker = (Marker) typeSelector.getSelected();
        } else {
            marker = null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.DialogTheme);

        builder.setTitle("Do you confirm to remove the selected marker?");

        builder.setPositiveButton("Remove", (dialogInterface, i) -> removeMarker())
                .setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (marker == null) {
            dismiss();
        }
    }

    /**
     * Remove the marker from the storage and selection.
     */
    private void removeMarker() {
        // Remove marker from the map markers game save storage.
        if (SavePlatform.hasSave()) {
            MapMarkers.getFromCurrentSave().removeMarker(marker);
        }

        // Remove marker from selector
        SelectionCrier.getInstance().removeSelected((Selectable) marker);
    }

}
