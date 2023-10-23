package com.example.pacmanapp.activities.save;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.SelectableButtonContent;
import com.example.pacmanapp.storage.SaveManager;
import com.example.pacmanapp.storage.SavePlatform;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Saves extends ContentContainer {
    private final static String TAG = "SavesContentContainer";
    private final SaveManager saveManager;
    private final Map<String, SelectableButtonContent> saveButtonMap;
    private SelectableButtonContent selected;

    /**
     * Construct the empty content container for the saves.
     *
     * @param saveManager Save manager to update content container with
     */
    public Saves(@NotNull SaveManager saveManager) {
        this.saveManager = saveManager;
        this.saveButtonMap = new HashMap<>();
    }

    @Override
    public List<Content> getContent(@NotNull AppCompatActivity activity, boolean editable) {
        List<Content> contentList = new ArrayList<>();

        if (activity instanceof SaveActivity) {
            SaveActivity saveActivity = (SaveActivity) activity;
            for (String saveName : saveManager.getSaveNames()) {
                contentList.add(createSaveButton(saveName, saveActivity));
            }
            if (SavePlatform.hasSave()) {
                markSelected(SavePlatform.getSave().getSaveName());
            }
        }

        // TODO add content for when activity is not the save activity.

        return contentList;
    }

    /**
     * Create a new save button for the specified save name.
     *
     * @param saveName Save name to create save button for
     * @param saveActivity Save activity to create save button for
     * @return Selectable button content created for the specified save name
     */
    private SelectableButtonContent createSaveButton(@NotNull String saveName,
                                                     @NotNull SaveActivity saveActivity) {
        Runnable onSaveButtonClick = () -> new SaveDialog(saveName, saveActivity)
                .show(saveActivity.getSupportFragmentManager(), "SaveDialog");
        SelectableButtonContent saveButton =
                new SelectableButtonContent(saveName, onSaveButtonClick);
        saveButtonMap.put(saveName, saveButton);
        return saveButton;
    }

    /**
     * Mark the content for the specified save name as selected,
     * previous selected will be unselected.
     *
     * @param saveName String save name to select
     */
    public void markSelected(String saveName) {
        // Check if save button map contains specified save name
        if (!saveButtonMap.containsKey(saveName)) {
            Log.w(TAG, "Could not get selectable button content for save name " + saveName);
            return;
        }

        // Get the content for the specified save name
        SelectableButtonContent newSelected = saveButtonMap.get(saveName);

        // Check that content is not null for specified save name
        if (newSelected == null) {
            Log.w(TAG, "Selectable button content for save name " + saveName + " is null");
            return;
        }

        // If selected is not null remove its selection
        if (selected != null) {
            selected.setSelected(false);
        }

        // Set the new selected to be selected
        newSelected.setSelected(true);
        selected = newSelected;
    }

}
