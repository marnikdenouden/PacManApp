package com.example.pacmanapp.markers;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.selectables.Blank;

import java.util.ArrayList;
import java.util.List;

public class BlankMarker extends Blank implements Selectable {
    public BlankMarker(@NonNull Resources resources) {
        super(resources);
    }

    @Override
    public String getLabel() {
        return "Blank Marker";
    }

    @Override
    public int getIconId() {
        return R.drawable.icon_blank_marker;
    }

    @Override
    public String getDescription() {
        return "No marker selected";
    }
}
