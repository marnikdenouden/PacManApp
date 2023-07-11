package com.example.pacmanapp.storage;

public class SavePlatform {
    private static SaveManager saveManager;
    private static GameSave gameSave;

    /**
     * Set the save manager of the save platform.
     *
     * @param saveManager Save manager to set
     */
    static void setSaveManager(SaveManager saveManager) {
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
     * Load the current save.
     *
     * @pre save manager was set
     */
    public static void load() {
        gameSave = saveManager.getCurrentSave();
    }

    /**
     * Get the current save.
     *
     * @pre save manager was set
     * @return gameSave Game save currently active
     */
    public static GameSave getSave() {
        return gameSave;
    }
}
