package com.example.pacmanapp.contents;

import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.location.LocationUpdater;
import com.example.pacmanapp.markers.Marker;

import org.jetbrains.annotations.NotNull;

public class RelocateButton extends ButtonContent {
    private final static String TAG = "RelocateButton";
    /**
     * Create a relocate button for the specified marker.
     */
    public RelocateButton(@NotNull Marker marker) {
        super("Relocate", () -> updateLocation(marker));
    }

    @Override
    public View addView(@NonNull AppCompatActivity activity, @NonNull ViewGroup viewGroup,
                        boolean editable) {
        // Relocate button should only be seen from edit view.
        if (editable) {
            return super.addView(activity, viewGroup, editable);
        } else {
            return viewGroup;
        }
    }

    /**
     * Upate the location of the marker to the latest location.
     *
     * @param marker Marker to relocate
     */
    private static void updateLocation(@NotNull Marker marker) {
//        if (!locationUpdater.hasLocation()) {
//            Log.i(TAG, "Could not get last location to relocate");
//            return;
//        }
//        Location location = locationUpdater.getLastLocation();
//        marker.setLocation(location.getLatitude(), location.getLongitude());
    }
}
