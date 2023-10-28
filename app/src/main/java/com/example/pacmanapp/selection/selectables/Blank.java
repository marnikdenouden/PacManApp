package com.example.pacmanapp.selection.selectables;

import android.content.res.Resources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Blank extends ContentContainer implements Selectable {
    final Resources resources;

    public Blank(@NotNull Resources resources) {
        this.resources = resources;
    }

    @Override
    public String getLabel() {
        return resources.getString(R.string.selectable_blank_label);
    }

}
