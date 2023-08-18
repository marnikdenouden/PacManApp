package com.example.pacmanapp.selection.selectables;

import android.content.res.Resources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.selection.Selectable;

public abstract class Blank implements Selectable {
    final Resources resources;

    public Blank(Resources resources) {
        this.resources = resources;
    }

    @Override
    public String getLabel() {
        return resources.getString(R.string.selectable_blank_label);
    }

}
