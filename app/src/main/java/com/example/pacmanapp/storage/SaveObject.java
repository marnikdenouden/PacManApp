package com.example.pacmanapp.storage;

import android.content.Context;

import java.io.Serializable;

public abstract class SaveObject implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Create a save object that is added to the save manager for the specified save name.
     *
     * @param context Context to get save manager with
     */
    public SaveObject(Context context) {
        SaveManager.getCurrentSave(context).addSaveObject(this);
    }

    /**
     * Load the objects that were stored.
     */
    public void load(Context context) {
        // Default behavior does not require anything to be loaded.
    }
}
