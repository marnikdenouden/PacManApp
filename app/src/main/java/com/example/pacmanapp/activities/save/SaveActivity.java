package com.example.pacmanapp.activities.save;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.storage.SaveManager;
import com.example.pacmanapp.storage.SavePlatform;

public class SaveActivity extends AppCompatActivity {
    private final static String TAG = "SaveActivity";
    private SaveManager saveManager;
    private Saves saves;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves);

        saveManager = SavePlatform.setup(getApplicationContext());

        saves = new Saves(saveManager, this);
        updateSaveList();

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> new CreateSaveDialog(this)
                .show(getSupportFragmentManager(), "CreateSave"));

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view -> new ClearSavesDialog(this)
                .show(getSupportFragmentManager(), "ClearSave"));

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> {
            if (SavePlatform.hasSave()) {
                finish();
            } else {
                Toast.makeText(this, "No save selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateSaveList();
    }

    /**
     * Update saves list.
     */
    private void updateSaveList() {
        ViewGroup viewGroup = findViewById(R.id.content_scroll_view);
        SelectableContent.setContent(this, viewGroup, saves, true);
        if (SavePlatform.hasSave()) {
            saves.markSelected(SavePlatform.getSave().getSaveName());
        }
    }

    /**
     * Create a save with the specified save name.
     *
     * @param saveName Save name to create save for
     */
    void createSave(String saveName) {
        SavePlatform.load(saveName); // TODO what is difference between create and load
        updateSaveList();
    }

    /**
     * Load the save with the specified save name.
     *
     * @param saveName Save name to load save for
     */
    void loadSave(String saveName) {
        // Mark the content with save name as selected
        saves.markSelected(saveName);
        // Load the save name on to the save platform
        SavePlatform.load(saveName);
    }

    /**
     * Remove the save with the specified save name.
     *
     * @param saveName Save name to remove save for
     */
    void removeSave(String saveName) {
        saveManager.removeSave(saveName);
        updateSaveList();
    }

    /**
     * Clear all saves stored in the save manager.
     */
    void clearSaves() {
        saveManager.clearSaves();
        updateSaveList();
    }

}

