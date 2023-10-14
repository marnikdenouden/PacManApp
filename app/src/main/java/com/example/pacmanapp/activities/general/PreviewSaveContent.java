package com.example.pacmanapp.activities.general;

import com.example.pacmanapp.contents.ButtonContent;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;

import java.util.ArrayList;
import java.util.List;

public class PreviewSaveContent implements ContentContainer {
    private final Content content;

    public PreviewSaveContent(String saveName) {
        Runnable onSavePreviewClick = new Runnable() {
            @Override
            public void run() {
                // TODO Open save preview dialog.
            }
        };
        content = new ButtonContent(saveName, onSavePreviewClick);
    }

    @Override
    public List<Content> getContent() {
        ArrayList<Content> list = new ArrayList<>();
        list.add(content);
        return list;
    }
}
