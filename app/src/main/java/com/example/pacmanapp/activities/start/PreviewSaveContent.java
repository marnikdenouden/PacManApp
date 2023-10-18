package com.example.pacmanapp.activities.start;

import com.example.pacmanapp.contents.ButtonContent;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.storage.GameSave;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PreviewSaveContent implements ContentContainer {
    private final Content content;

    public PreviewSaveContent(@NotNull GameSave gameSave, @NotNull StartActivity activity) {
        Runnable onSavePreviewClick = new Runnable() {
            @Override
            public void run() {
                new PreviewSaveDialog(activity, gameSave)
                        .show(activity.getSupportFragmentManager(), "SavePreviewStart");
            }
        };
        content = new ButtonContent(gameSave.getSaveName(), onSavePreviewClick);
    }

    @Override
    public List<Content> getContent() {
        ArrayList<Content> list = new ArrayList<>();
        list.add(content);
        return list;
    }
}
