package com.example.pacmanapp.storage;

import android.util.Log;

public class SavePlatform {
    private final static String TAG = "SavePlatform";
    private static SaveManager saveManager;

    /**
     * Set the save manager of the save platform.
     *
     * @param saveManager Save manager to set
     */
    public static void setSaveManager(SaveManager saveManager) {
        SavePlatform.saveManager = saveManager;
    }

    /**
     * Save the current save.
     *
     * @pre save manager was set
     */
    public static void save() {
        saveManager.saveCurrentSave();
    }

    /**
     * Check if the save platform has a save.
     *
     * @return Truth assignment, if save platform has a save loaded
     */
    public static boolean hasSave() {
        return saveManager != null && saveManager.hasCurrentSave();
    }

    /**
     * Get the current save.
     *
     * @pre has a save to give
     * @return gameSave Game save currently active
     */
    public static GameSave getSave() {
        if (!hasSave()) {
            Log.e(TAG, "There is no save set on the save platform");
            throw new NullPointerException("No save found");
        }
        return saveManager.getCurrentSave();
    }
}
