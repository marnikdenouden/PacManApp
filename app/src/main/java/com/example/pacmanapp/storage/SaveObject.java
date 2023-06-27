package com.example.pacmanapp.storage;

import android.content.Context;

public abstract class SaveObject {
    private final String saveName;

    public SaveObject(String saveName, Context context) {
        this.saveName = saveName;
        SaveManager.getSaveManager(context).getGameSave(saveName).addSaveObject(this);
    }

    /**
     * Get the save name that this save object is saved at.
     *
     * @return SaveName name of the game save this save object is saved at
     */
    public String getSaveName() {
        return saveName;
    }

    /**
     * Load the objects that were stored.
     */
    public abstract SaveObject load(Context context);
}
