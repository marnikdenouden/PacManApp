package com.example.pacmanapp.markers;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.selection.Selectable;

import java.util.ArrayList;
import java.util.List;

public class BlankMarker implements Selectable {
    @Override
    public List<Content> getContent(@NonNull AppCompatActivity activity, boolean editable) {
        return new ArrayList<>();
    }

    @Override
    public View addView(@NonNull AppCompatActivity activity, @NonNull ViewGroup viewGroup, boolean editable) {
        return viewGroup;
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
