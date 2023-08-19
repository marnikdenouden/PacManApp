package com.example.pacmanapp.activities.save;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.storage.SaveManager;
import com.example.pacmanapp.storage.SavePlatform;

import java.util.HashMap;
import java.util.Map;

public class SaveActivity extends AppCompatActivity {
    private final static String TAG = "SaveActivity";
    private SaveManager saveManager;
    private Map<String, SaveButton> saveButtonMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves);

        saveManager = SaveManager.getInstance(getApplicationContext());

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> new CreateSaveDialog(this)
                .show(getSupportFragmentManager(), "CreateSave"));

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view -> new ClearSavesDialog(this)
                .show(getSupportFragmentManager(), "ClearSave"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateSaveList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SavePlatform.setSaveManager(saveManager);
    }

    private SaveManager getSaveManager() {
        return saveManager;
    }

    /**
     * Update saves list.
     */
    private void updateSaveList() {
        saveButtonMap = new HashMap<>();
        LinearLayout savesList = findViewById(R.id.savesList);
        savesList.removeAllViews();
        for (String saveName: getSaveManager().getSaveNames()) {
            saveButtonMap.put(saveName, addSaveButton(saveName, savesList));
        }
        markSelectedSaveButton(true);
    }

    /**
     * Mark the save button that is selected by calling set loaded.
     */
    private void markSelectedSaveButton(boolean loaded) {
        if (!getSaveManager().hasCurrentSave()) {
            return;
        }
        String loadedSaveName = getSaveManager().getCurrentSave().getSaveName();
        SaveButton saveButton = saveButtonMap.get(loadedSaveName);
        if (saveButton != null) {
            saveButton.setLoaded(loaded);
        }
    }

    /**
     * Add save button to saves list.
     *
     * @param saveName Save name of save button
     * @param savesList Save list to add new button to
     */
    SaveButton addSaveButton(String saveName, LinearLayout savesList) {
        SaveButton saveButton = new SaveButton(saveName, this);
        saveButton.setOnClickListener(view -> new SaveDialog(saveName, this)
                .show(getSupportFragmentManager(), "SaveDialog"));
        savesList.addView(saveButton);
        return saveButton;
    }

    // TODO add java doc comments
    void createSave(String saveName) {
        getSaveManager().setCurrentSave(saveName);
        updateSaveList();
    }

    void loadSave(String saveName) {
        // Mark currently selected to not be loaded
        markSelectedSaveButton(false);
        // Load the save name to be the new selected
        getSaveManager().loadSave(saveName);
        // Mark new selected to be loaded
        markSelectedSaveButton(true);
    }

    void removeSave(String saveName) {
        getSaveManager().removeSave(saveName);
        updateSaveList();
    }

    void clearSaves() {
        getSaveManager().clearSaves();
        updateSaveList();
    }

    @SuppressLint({"AppCompatCustomView", "ViewConstructor"})
    public static class SaveButton extends Button {

        public SaveButton(String saveName, AppCompatActivity currentActivity) {
            super(currentActivity.getApplicationContext());
            setLoaded(false);
            setText(saveName);
        }

        public void setLoaded(boolean loaded) {
            if (loaded) {
                setBackgroundColor(getResources().getColor(R.color.cyan_base,
                        getContext().getTheme()));
            } else {
                setBackgroundColor(getResources().getColor(R.color.white_base,
                        getContext().getTheme()));
            }
        }

    }

}

