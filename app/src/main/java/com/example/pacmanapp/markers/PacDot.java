package com.example.pacmanapp.markers;

import android.content.Context;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

public class PacDot extends Marker {

    /**
     * PacDot marker to display on the map and use.
     *
     * @param latitude latitude that the marker is placed at
     * @param longitude longitude that the marker is placed at
     * @param context Context that the marker is created in
     * @param activity Activity that the marker is placed in
     */
    public PacDot(double latitude, double longitude, Context context, AppCompatActivity activity) {
        create(latitude, longitude, context, activity);
    }

    /**
     * Create a pacdot marker for specified context, activity and alike.
     *
     * @param latitude Latitude used to position marker on map
     * @param longitude Longitude used to position marker on map
     * @param context Context in which the marker is created
     * @param activity Activity in which the marker is placed
     * @return ImageView of created pacdot marker
     */
    ImageView create(double latitude, double longitude,
                     Context context, AppCompatActivity activity) {
        int markerWidth = (int) (activity.getResources().getDimension(R.dimen.pacDotSize));
        int markerHeight = (int) (activity.getResources().getDimension(R.dimen.pacDotSize));

        return super.create(latitude, longitude, markerWidth, markerHeight,
                R.drawable.pac_dot_v1_1, R.id.pacdot, false, context, activity);
    }
}
