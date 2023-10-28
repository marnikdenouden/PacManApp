package com.example.pacmanapp.selection.selectables;

import android.content.res.Resources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Information;

import org.jetbrains.annotations.NotNull;

public class BlankInspect extends Blank {
    public BlankInspect(@NotNull Resources resources) {
        super(resources);
        String infoText = resources.getString(R.string.selectable_inspect_information);
        addContent(new Information(infoText));
    }

    @Override
    public int getIconId() {
        return R.drawable.icon_inspect;
    }

    @Override
    public String getDescription() {
        return resources.getString(R.string.selectable_inspect_description);
    }

}
