package com.example.pacmanapp.activities.save;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.popup.PopUpActivity;
import com.example.pacmanapp.storage.SaveManager;

public class ClearSavesPopupActivity extends PopUpActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_clear_saves);
    }

    public void clearSaves(View view) {
        SaveManager saveManager = SaveManager.getInstance(getApplicationContext());
        saveManager.clearSaves();
        finish();
    }
}
