package com.example.pacmanapp.storage;

import android.util.Log;

import androidx.annotation.WorkerThread;

import com.example.pacmanapp.displays.PlayValues;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class GameSave implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "GameSave";

    private final String saveName;
    private final ImageStorage imageStorage;
    private final ResetManager resetManager;
    private final Collection<SaveObject> saveObjects;
    private boolean isPlaying = false;

    /**
     * Create a game save for a specified save name.
     *
     * @param saveName Save name to identify save with
     * @param imageDirectory Image directory to save images in
     */
    @WorkerThread
    GameSave(@NotNull String saveName, @NotNull File imageDirectory) {
        this.saveName = saveName;
        this.imageStorage = new ImageStorage(saveName, imageDirectory);
        saveObjects = new HashSet<>();
        resetManager = new ResetManager(this);
    }

    /**
     * Start the game save to be playing.
     */
    void play() {
        isPlaying = true;
    }

    /**
     * Reset the game save values to prepare to start the game.
     */
    void reset() {
        resetManager.reset();
    }

    /**
     * Check if the game save is playing.
     *
     * @return Truth assignment, if the current game save is being played
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Stop the game save from playing.
     */
    void quit() {
        isPlaying = false;
    }

    /**
     * Add save object to the game save collection.
     *
     * @param saveObject Save object to add to the game save collection
     */
    public void addSaveObject(SaveObject saveObject) {
        saveObjects.add(saveObject);
    }

    /**
     * Removes a save object from the game save collection.
     *
     * @param saveObject Save object to remove from the game save collection
     */
    public void removeSaveObject(SaveObject saveObject) {
        saveObjects.remove(saveObject);
    }

    /**
     * Gets the first save object with matching save object id.
     *
     * @param saveObjectId Id of save object to get
     * @return Save object with specified id or null if none exist in the current save
     */
    public SaveObject getSaveObject(int saveObjectId) {
        for (SaveObject saveObject: saveObjects) {
            if (saveObject.getSaveObjectId() == saveObjectId) {
                return saveObject;
            }
        }
        return null;
    }

    /**
     * Check if the game save has a save object with matching save object id.
     *
     * @param saveObjectId Id to check save object for
     * @return Truth assignment, if game save has save object with matching id
     */
    public boolean hasSaveObject(int saveObjectId) {
        for (SaveObject saveObject: saveObjects) {
            if (saveObject.getSaveObjectId() == saveObjectId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the save name of this game save.
     *
     * @return SaveName String of this game save
     */
    @NotNull
    public String getSaveName() {
        return saveName;
    }

    /**
     * Get the byte array data of the game save.
     *
     * @return Byte array data of the game save
     * @throws IOException Thrown when output stream gives error
     */
    public byte[] getByteArray() throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(this);
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * Get the game save from the byte array data.
     *
     * @param gameSaveData Byte array data of the game save
     * @return Game save deserialized from the byte array data
     * @throws IOException Thrown when input stream gives error
     * @throws ClassNotFoundException Thrown when read object class is not found
     */
    public static GameSave getGameSaveFromData(byte[] gameSaveData) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(gameSaveData);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            GameSave gameSave = (GameSave) objectInputStream.readObject();
            Log.d(TAG, "Game save read from object input stream: \n" + gameSave);
            return gameSave;
        }
    }

    /**
     * Get the image storage of this save.
     *
     * @return Image storage of this save
     */
    public ImageStorage getImageStorage() {
        return imageStorage;
    }

    /**
     * Get the reset manager of the game save.
     */
    public ResetManager getResetManager() {
        return resetManager;
    }

}
