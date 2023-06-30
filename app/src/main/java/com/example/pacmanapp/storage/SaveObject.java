package com.example.pacmanapp.storage;

import android.content.Context;

import com.example.pacmanapp.markers.MapMarkers;

import java.io.Serializable;

public abstract class SaveObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient final int saveObjectId;

    /**
     * Create a save object that is added to the save manager for the specified save name.
     *
     * @param saveManager SaveManager to add the save object to the current save of.
     */
    public SaveObject(int saveObjectId, SaveManager saveManager) {
        this.saveObjectId = saveObjectId;
        saveManager.getCurrentSave().addSaveObject(this);
    }

    /**
     * Get the save object id.
     *
     * @return id of save object
     */
    public int getSaveObjectId() {
        return saveObjectId;
    }

    /**
     * Load the objects that are stored.
     */
    public void load(Context context) {
        // Default behavior does not require anything to be loaded.
    }

}
