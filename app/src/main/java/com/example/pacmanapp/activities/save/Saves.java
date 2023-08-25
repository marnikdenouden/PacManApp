package com.example.pacmanapp.activities.save;

import android.util.Log;

import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.SelectableButtonContent;
import com.example.pacmanapp.storage.SaveManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Saves implements ContentContainer {
    private final static String TAG = "SavesContentContainer";
    private final SaveManager saveManager;
    private final SaveActivity saveActivity;
    private final Map<String, SelectableButtonContent> saveButtonMap;
    private SelectableButtonContent selected;

    /**
     * Construct the content container for the saves using the save manager.
     *
     * @param saveManager Save manager to get updated list of save names from.
     * @param saveActivity Activity to open save dialog on button click in.
     */
    public Saves(@NotNull SaveManager saveManager, @NotNull SaveActivity saveActivity) {
        this.saveManager = saveManager;
        this.saveActivity = saveActivity;
        this.saveButtonMap = new HashMap<>();
    }

    @Override
    public List<Content> getContent() {
        List<Content> contentList = new ArrayList<>();
        for (String saveName: saveManager.getSaveNames()) {
            contentList.add(createSaveButton(saveName));
        }
        return contentList;
    }

    /**
     * Create a new save button for the specified save name.
     *
     * @param saveName Save name to create save button for
     * @return Selectable button content created for the specified save name
     */
    public SelectableButtonContent createSaveButton(String saveName) {
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
