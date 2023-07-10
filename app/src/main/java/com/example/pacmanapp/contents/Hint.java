package com.example.pacmanapp.contents;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

public class Hint {

    private int iconId;
    private String label;
    private String hint;
    private int imageId;

    public Hint(HintBuilder hintBuilder) {
        // Have a builder construction? So static methods
    }

    /**
     * Add hint view to current activity attached to the provided view group.
     *
     * @param currentActivity Current activity to inflate view for
     * @param viewGroup View group to insert hint view in
     */
    public void addView(AppCompatActivity currentActivity, ViewGroup viewGroup) {
        View view = currentActivity.getLayoutInflater().inflate(R.layout.pacman_marker, viewGroup);
        // Configure hint view with hint data values.
    }
}
