package com.example.pacmanapp.storage;

import android.content.Context;
import android.util.Log;

import com.example.pacmanapp.location.LocationUpdater;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GameSave implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String saveName;

    private static final String TAG = "GameSave";
    private final Collection<SaveObject> saveObjects;

    GameSave(String saveName) {
        this.saveName = saveName;
        saveObjects = new HashSet<>();
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
     * Get the save name of this game save.
     *
     * @return SaveName String of this game save
     */
    public String getSaveName() {
        return saveName;
    }

    /**
     * Load the all save objects that are part of this game save.
     */
    public void load(Context context) {
        for (SaveObject saveObject: saveObjects) {
            saveObject.load(context);
        }
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

}
