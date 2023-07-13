package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.edit.EditPacDotActivity;
import com.example.pacmanapp.activities.inspect.InspectPacDotActivity;
import com.example.pacmanapp.selection.Selectable;

@SuppressLint("ViewConstructor")
public class PacDot extends Marker implements Selectable {
    private final static String TAG = "PacDot";
    private static final long serialVersionUID = 1L;
    private final static int drawableId = R.drawable.pac_dot;
    private final static int markerId = R.id.pacdot;
    private int debugOffset = 0;

    private String hint = "No hint is set to the location of this pac dot.";

    /**
     * Pac-dot marker to display on the map and use.
     *
     * @param frameId   FrameId reference to map area that the pac-dot is placed on
     * @param latitude  latitude that the pac-dot is placed at
     * @param longitude longitude that the pac-dot is placed at
     * @param context   Context that the pac-dot is created in
     */
    public PacDot(int frameId, double latitude, double longitude, Context context) {
        super(frameId, latitude, longitude, drawableId, markerId, false, context);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        getImageView().setX(getImageView().getX() + 50);
        debugOffset+= 40;  // TODO remove debugOffset
        getImageView().setX(100 + debugOffset);
        Log.d(TAG, hint);
    }

    @Override
    public Class<? extends AppCompatActivity> getInspectPage() {
        return InspectPacDotActivity.class;
    }

    @Override
    public Class<? extends AppCompatActivity> getEditPage() {
        return EditPacDotActivity.class;
    }

    @Override
    public String getLabel() {
        return "Pac-Dot";
    }

    @Override
    public int getIconId() {
        return R.drawable.pac_dot;
    }

    @Override
    public String getDescription() {
        return "Pac-Dot can be found using the hint they contain. " +
                "When found they provide a hint to the location of a fruit.";
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }
}
