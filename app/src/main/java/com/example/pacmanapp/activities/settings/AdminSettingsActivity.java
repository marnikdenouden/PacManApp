package com.example.pacmanapp.activities.settings;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.start.StartActivity;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.storage.SavePlatform;

public class AdminSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_admin);
        Button quitButton = findViewById(R.id.button_quit);
        quitButton.setOnClickListener(view -> quit());

        NavigationBar.configure(this, PageType.ADMIN_SETTINGS);
    }

    /**
     * Call to quit the current game save.
     */
    void quit() {
        SavePlatform.quit();
        Navigate.navigate(AdminSettingsActivity.this, StartActivity.class);
        finish();
    }

}
