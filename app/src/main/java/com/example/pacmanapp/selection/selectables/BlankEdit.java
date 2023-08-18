package com.example.pacmanapp.selection.selectables;

import android.content.res.Resources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlankEdit extends Blank {

    public BlankEdit(@NotNull Resources resources) {
        super(resources);
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
