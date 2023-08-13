package com.example.pacmanapp.contents;

import android.content.res.Resources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InfoEdit implements Selectable {
    private final Resources resources;

    public InfoEdit(@NotNull Resources resources) {
        this.resources = resources;
    }

    @Override
    public String getLabel() {
        return resources.getString(R.string.selectable_edit_label);
    }

    @Override
    public int getIconId() {
        return R.drawable.icon_edit;
    }

    @Override
    public String getDescription() {
        return resources.getString(R.string.selectable_edit_description);
    }

    @Override
    public List<Content> getContent() {
        List<Content> contentList = new ArrayList<>();
        String infoText = resources.getString(R.string.selectable_edit_information);
        Content content = new Information(infoText);
        contentList.add(content);
        return contentList;
    }

}
