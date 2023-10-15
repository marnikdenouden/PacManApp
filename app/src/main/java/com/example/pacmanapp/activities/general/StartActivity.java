package com.example.pacmanapp.activities.general;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.map.AdminMapActivity;
import com.example.pacmanapp.activities.map.PlayMapActivity;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.displays.NumberSmall;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.NavigationBarType;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.storage.GameSave;
import com.example.pacmanapp.storage.SaveManager;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity implements Navigate.BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setAdminButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePreviewSave();
    }

    /**
     * Set the admin button backdoor of the start activity.
     */
    private void setAdminButton() {
        Button adminButton = findViewById(R.id.adminButton);
        adminButton.setOnClickListener(view -> {
            Navigate.navigate(StartActivity.this, AdminMapActivity.class);
            NavigationBar.setNavigationBarType(NavigationBarType.ADMIN);
        });
    }

    /**
     * Update the previewed save content with the current save.
     */
    private void updatePreviewSave() {
        SaveManager saveManager = SaveManager.getInstance(this);

        if (!saveManager.hasCurrentSave()) {
            saves();
            return;
        }

        GameSave gameSave = saveManager.getCurrentSave();
        ContentContainer previewSaveContent = new PreviewSaveContent(gameSave, this);

        ConstraintLayout constraintLayout = findViewById(R.id.SelectedSaveLayout);
        SelectableContent.setContent(this, constraintLayout, previewSaveContent, false);
    }

    /**
     * Method to use in save preview dialog to play the current save.
     */
    public void play() {
        Navigate.navigate(StartActivity.this, PlayMapActivity.class);
        NavigationBar.setNavigationBarType(NavigationBarType.PLAY);
    }

    /**
     * Method to use in save preview dialog to select a different save.
     */
    public void saves() {
        Navigate.navigate(StartActivity.this, SaveActivity.class);
    }
}
