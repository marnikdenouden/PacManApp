package com.example.pacmanapp.selection.selectables;

import android.content.res.Resources;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InfoEdit extends BlankEdit {

    public InfoEdit(@NotNull Resources resources) {
        super(resources);
    }

    @Override
    public String getLabel() {
        return resources.getString(R.string.selectable_edit_title);
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
