package com.example.pacmanapp.storage;

import com.example.pacmanapp.map.MapStorage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class SaveObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int saveObjectId;

    /**
     * Create a save object that is added to the save manager for the specified save name.
     *
     * @param gameSave Save to add the save object to
     */
    public SaveObject(int saveObjectId, GameSave gameSave) {
        this.saveObjectId = saveObjectId;
        gameSave.addSaveObject(this);
    }

    /**
     * Get the save object id.
     *
     * @return id of save object
     */
    public int getSaveObjectId() {
        return saveObjectId;
    }

}
