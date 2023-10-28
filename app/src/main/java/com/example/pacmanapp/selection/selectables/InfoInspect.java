package com.example.pacmanapp.selection.selectables;

import android.content.res.Resources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Information;

import org.jetbrains.annotations.NotNull;

public class InfoInspect extends BlankInspect {

    public InfoInspect(@NotNull Resources resources) {
        super(resources);
    }

    @Override
    public String getLabel() {
        return resources.getString(R.string.selectable_inspect_title);
    }

}
