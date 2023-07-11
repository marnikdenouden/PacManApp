package com.example.pacmanapp.activities.save;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.storage.SaveManager;

public class SaveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves);

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view ->
                Navigate.navigate(SaveActivity.this, CreateSavePopUpActivity.class));

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view ->
                Navigate.navigate(SaveActivity.this, ClearSavesPopupActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateSaveList();
    }

    /**
     * Update saves list.
     */
    public void updateSaveList() {
        LinearLayout savesList = findViewById(R.id.savesList);
        savesList.removeAllViews();
        SaveManager saveManager = SaveManager.getInstance(getApplicationContext());
        for (String saveName: saveManager.getSaveNames()) {
            savesList.addView(new SaveButton(saveName, this));
        }
    }

}
