package com.example.pacmanapp.navigation;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.edit.EditActivity;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.activities.map.AdminMapActivity;
import com.example.pacmanapp.activities.map.PlayMapActivity;
import com.example.pacmanapp.activities.settings.AdminSettingsActivity;
import com.example.pacmanapp.activities.settings.PlaySettingsActivity;

public enum PageType {
    INSPECT(R.id.inspectOption, R.id.pageMarkerInspect,
            InspectActivity.class, EditActivity.class),
    MAP(R.id.mapOption, R.id.pageMarkerMap,
            PlayMapActivity.class, AdminMapActivity.class),
    SETTINGS(R.id.settingsOption, R.id.pageMarkerSettings,
            PlaySettingsActivity.class, AdminSettingsActivity.class);

    private final int buttonId;
    private final int markerId;
    private final Class<? extends AppCompatActivity> playPage;
    private final Class<? extends AppCompatActivity> adminPage;
    PageType(int buttonId, int markerId, Class<? extends AppCompatActivity> playPage,
             Class<? extends AppCompatActivity> adminPage) {
        this.buttonId = buttonId;
        this.markerId = markerId;
        this.playPage = playPage;
        this.adminPage = adminPage;
    }

    Class<? extends AppCompatActivity> getNextPage(boolean admin) {
        if (admin) {
            return adminPage;
        } else {
            return playPage;
        }
    }

    int getButtonId() {
        return buttonId;
    }

    int getMarkerId() {
        return markerId;
    }

}
