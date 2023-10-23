package com.example.pacmanapp.activities.start;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.map.AdminMapActivity;
import com.example.pacmanapp.activities.map.PlayMapActivity;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.NavigationBarType;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.storage.GameSave;
import com.example.pacmanapp.storage.SavePlatform;

public class StartActivity extends AppCompatActivity implements Navigate.BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SavePlatform.setup(getApplicationContext());

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
        if (!SavePlatform.hasSave()) {
            Navigate.navigate(StartActivity.this, SaveActivity.class);
            return;
        } else if (SavePlatform.isPlaying()) {
            Navigate.navigate(StartActivity.this, PlayMapActivity.class);
        }

        GameSave gameSave = SavePlatform.getSave();
        Content previewSaveContent = new PreviewSaveContent(gameSave);

        ConstraintLayout constraintLayout = findViewById(R.id.SelectedSaveLayout);
        SelectableContent.setContent(this, constraintLayout, previewSaveContent, false);
    }

    /**
     * Call to play the current game save.
     */
    void play() {
        SavePlatform.play();
        Navigate.navigate(StartActivity.this, PlayMapActivity.class);
        finish();
    }

    /**
     * Change the game save.
     */
    void changeSave() {
        Navigate.navigate(StartActivity.this, SaveActivity.class);
    }

}
