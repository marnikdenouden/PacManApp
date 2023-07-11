package com.example.pacmanapp.activities.save;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.popup.PopUpActivity;
import com.example.pacmanapp.storage.SaveManager;

public class CreateSavePopUpActivity extends PopUpActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_create_save);
    }

    public void createSave(View view) {
        EditText saveNameText = findViewById(R.id.createSaveName);
        Editable editable = saveNameText.getText();
        if (editable.length() > 0) {
            SaveManager saveManager = SaveManager.getInstance(getApplicationContext());
            saveManager.setCurrentSave(editable.toString(), getApplicationContext());
        }
        finish();
    }

}
