package com.example.pacmanapp.activities.save;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.storage.SaveManager;

public class SaveActivity extends AppCompatActivity {
    private final static String TAG = "SaveActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves);

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

    private SaveManager getSaveManager() {
        return SaveManager.getInstance(getApplicationContext());
    }

    /**
     * Update saves list.
     */
    private void updateSaveList() {
        LinearLayout savesList = findViewById(R.id.savesList);
        savesList.removeAllViews();
        for (String saveName: getSaveManager().getSaveNames()) {
            addSaveButton(saveName, savesList);
        }
    }

    /**
     * Add save button to saves list.
     *
     * @param saveName Save name of save button
     * @param savesList Save list to add new button to
     */
    void addSaveButton(String saveName, LinearLayout savesList) {
        Button saveButton = new SaveButton(saveName, this);
        saveButton.setOnClickListener(view -> new SaveDialog(saveName, this)
                .show(getSupportFragmentManager(), "SaveDialog"));
        savesList.addView(saveButton);
    }

    void createSave(String saveName) {
        getSaveManager().setCurrentSave(saveName, getApplicationContext());
        updateSaveList();
    }

    void loadSave(String saveName) {
        getSaveManager().loadSave(saveName, getApplicationContext());
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
            setText(saveName);
        }

    }

}

