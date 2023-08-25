package com.example.pacmanapp.navigation;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.edit.EditActivity;
import com.example.pacmanapp.activities.general.FruitActivity;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.activities.map.AdminMapActivity;
import com.example.pacmanapp.activities.map.PlayMapActivity;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.activities.settings.AdminSettingsActivity;
import com.example.pacmanapp.activities.settings.PlaySettingsActivity;

public enum PageType {
    INSPECT(R.id.inspectOption, R.id.pageMarkerInspect, InspectActivity.class),
    MAP(R.id.mapOption, R.id.pageMarkerMap, PlayMapActivity.class),
    SETTINGS(R.id.settingsOption, R.id.pageMarkerSettings, PlaySettingsActivity.class),
    EDIT(R.id.editOption, R.id.pageMarkerEdit, EditActivity.class),
    ADMIN_MAP(R.id.mapOption, R.id.pageMarkerMap, AdminMapActivity.class),
    ADMIN_SETTINGS(R.id.settingsOption, R.id.pageMarkerSettings, AdminSettingsActivity.class),
    FRUITS(R.id.fruitsOption, R.id.pageMarkerFruits, FruitActivity.class);

    private final int buttonId;
    private final int markerId;
    private final Class<? extends AppCompatActivity> page;
    PageType(int buttonId, int markerId, Class<? extends AppCompatActivity> page) {
        this.buttonId = buttonId;
        this.markerId = markerId;
        this.page = page;
    }

    Class<? extends AppCompatActivity> getPage() {
        return page;
    }

    int getButtonId() {
        return buttonId;
    }

    int getMarkerId() {
        return markerId;
    }

}
