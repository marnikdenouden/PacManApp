package com.example.pacmanapp.activities.start;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.contents.ButtonContent;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.storage.GameSave;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PreviewSaveContent extends ContentContainer {
    private final GameSave gameSave;

    public PreviewSaveContent(@NotNull GameSave gameSave) {
        this.gameSave = gameSave;
    }

    @Override
    public List<Content> getContent(@NotNull AppCompatActivity activity, boolean editable) {
        ArrayList<Content> contentList = new ArrayList<>();
        if (activity instanceof StartActivity) {
            StartActivity startActivity = (StartActivity) activity;
            Runnable onSavePreviewClick = new Runnable() {
                @Override
                public void run() {
                    new PreviewSaveDialog(startActivity, gameSave)
                            .show(startActivity.getSupportFragmentManager(), "SavePreviewStart");
                }
            };
            contentList.add(new ButtonContent(gameSave.getSaveName(), onSavePreviewClick));
        }

        // TODO add content for when activity is not the start activity.

        return contentList;
    }

}
