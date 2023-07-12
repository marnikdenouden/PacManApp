package com.example.pacmanapp.storage;

import android.content.Context;

public class SavePlatform {
    private static SaveManager saveManager;

    /**
     * Set the save manager of the save platform.
     *
     * @param saveManager Save manager to set
     */
    static void setSaveManager(SaveManager saveManager) {
        SavePlatform.saveManager = saveManager;
    }

    /**
     * Check if the save platform has a save.
     *
     * @return
     */
    public static boolean hasSave() {
        return saveManager != null && saveManager.getCurrentSave() != null;
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
     * Load the current save.
     *
     * @pre save manager was set
     * @param context Context to load current save with
     */
    public static void load(Context context) {
        saveManager.getCurrentSave().load(context);
    }

    /**
     * Get the current save.
     *
     * @pre save manager was set
     * @return gameSave Game save currently active
     */
    public static GameSave getSave() {
        return saveManager.getCurrentSave();
    }
}
