package com.example.pacmanapp.selection.selectables;

import android.content.res.Resources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Information;

import org.jetbrains.annotations.NotNull;

public class BlankEdit extends Blank {

    public BlankEdit(@NotNull Resources resources) {
        super(resources);
        String infoText = resources.getString(R.string.selectable_edit_information);
        addContent(new Information(infoText));
    }

    @Override
    public int getIconId() {
        return R.drawable.icon_edit;
    }

    @Override
    public String getDescription() {
        return resources.getString(R.string.selectable_edit_description);
    }

}
