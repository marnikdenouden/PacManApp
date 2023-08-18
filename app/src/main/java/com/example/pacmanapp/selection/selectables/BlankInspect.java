package com.example.pacmanapp.selection.selectables;

import android.content.res.Resources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlankInspect implements Selectable {
    private final Resources resources;

    public BlankInspect(@NotNull Resources resources) {
        this.resources = resources;
    }

    @Override
    public String getLabel() {
        return resources.getString(R.string.selectable_inspect_label);
    }

    @Override
    public int getIconId() {
        return R.drawable.icon_inspect;
    }

    @Override
    public String getDescription() {
        return resources.getString(R.string.selectable_inspect_description);
    }

    @Override
    public List<Content> getContent() {
        List<Content> contentList = new ArrayList<>();
        String infoText = resources.getString(R.string.selectable_inspect_information);
        Content content = new Information(infoText);
        contentList.add(content);
        return contentList;
    }

}
