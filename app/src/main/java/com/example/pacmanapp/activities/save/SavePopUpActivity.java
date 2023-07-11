package com.example.pacmanapp.activities.save;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.popup.PopUpActivity;
import com.example.pacmanapp.storage.SaveManager;

public class SavePopUpActivity extends PopUpActivity {
    private static String TAG = "SavePopUpActivity";
    private String saveName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saveName = getIntent().getStringExtra("SaveName");
        if (saveName == null) {
            Log.e(TAG, "Did not get \"SaveName\" in intent for created save pop up activity.");
            finish();
            return;
        }

        setContentView(R.layout.popup_save);
        TextView saveNameTextView = findViewById(R.id.saveNameTitle);
        saveNameTextView.setText(saveName);
    }

    public void load(View view) {
        SaveManager saveManager = SaveManager.getInstance(getApplicationContext());
        saveManager.setCurrentSave(saveName, getApplicationContext());
        finish();
    }

    public void remove(View view) {
        SaveManager saveManager = SaveManager.getInstance(getApplicationContext());
        saveManager.removeSave(saveName);
        finish();
    }
}
