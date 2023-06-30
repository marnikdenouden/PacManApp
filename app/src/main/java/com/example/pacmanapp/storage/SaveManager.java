package com.example.pacmanapp.storage;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class SaveManager {
    private static final String TAG = "SaveManager";
    private final FileManager fileManager;
    private GameSave currentSave;

    /**
     * Create a save manager for a specified context.
     *
     * @param context Context to load current save in
     */
    public SaveManager(Context context) {
        File saveDirectory = new File(context.getFilesDir(), "saves");
        fileManager = new FileManager(saveDirectory);
    }

    /**
     * Load a game save with the specified save name and load it in the given context.
     *
     * @param saveName Save name to load game save for
     * @param context Context to load game save with
     */
    public void loadSave(String saveName, Context context) {
        File file = fileManager.getFile(saveName);
        byte[] data = FileManager.loadFileData(file);
        try {
            currentSave = GameSave.getGameSaveFromData(data);
            loadCurrentSave(context);
        } catch (IOException ioException) {
            Log.w(TAG, "Could not load game save for save name \"" + saveName +
                    "\" as IO exception occurred.");
            ioException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            Log.w(TAG, "Could not load game save for save name \"" + saveName +
                    "\" as class was not found.");
            classNotFoundException.printStackTrace();
        }
    }

    /**
     * Load the current save with the specified context.
     *
     * @param context Context to load the save with
     */
    public void loadCurrentSave(Context context) {
        currentSave.load(context);
    }

    /**
     * Save the current save to its save file.
     */
    public void saveCurrentSave() {
        File saveFile = fileManager.getFile(currentSave.getSaveName());
        try {
            byte[] saveData = currentSave.getByteArray();
            FileManager.saveFileData(saveFile, saveData);
        } catch (IOException ioException) {
            Log.w(TAG, "Could not save current game save with save name \"" +
                    currentSave.getSaveName() + "\" as IO exception occurred.");
            ioException.printStackTrace();
        }
    }

    /**
     * Create a new save to the current save.
     *
     * @param saveName New save name to name the created save
     * @pre !hasSave(saveName)
     */
    public void createSave(String saveName) {
        if (fileManager.hasFile(saveName)) {
            Log.w(TAG, "Could not create save with name \"" + saveName +
                    "\" as a save with this name already exists.");
            return;
        }
        currentSave = new GameSave(saveName);
    }

    /**
     * Checks if save name has a save stored.
     *
     * @param saveName Save name to check existence for
     * @return Truth assignment, if save is stored under save name
     */
    public boolean hasSave(String saveName) {
        return fileManager.hasFile(saveName);
    }

    /**
     * Load or create the current save to the specified save name.
     *
     * @param saveName Save name to load or create current save for
     */
    public void setCurrentSave(String saveName, Context context) {
        if (hasSave(saveName)) {
            loadSave(saveName, context);
        } else {
            createSave(saveName);
        }
    }

    /**
     * Clear all the files in the save directory.
     */
    public void clearSaves() {
        File[] files = fileManager.getFiles();
        if (files == null) {
            return; // Files is already empty.
        }
        for (File saveFile : files) {
            fileManager.removeFile(saveFile);
        }
    }

    /**
     * Remove a save from the save directory.
     *
     * @param saveName Save name to remove from the save directory
     */
    public void removeSave(String saveName) {
        File saveFile = fileManager.getFile(saveName);
        fileManager.removeFile(saveFile);
    }

    /**
     * Get the save names currently stored in the save directory.
     *
     * @return Array of save names currently stored
     */
    private String[] getSaveNames() {
        File[] files = fileManager.getFiles();
        String[] saveNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            saveNames[i] = files[i].getName();
        }
        return saveNames;
    }

    /**
     * Get the current save of this save manager.
     *
     * @pre Either a game save is successfully loaded or created for this save manager
     * @return Game save currently loaded
     * @throws NullPointerException When pre condition is violated
     */
    public GameSave getCurrentSave() {
        if (currentSave == null) {
            throw new NullPointerException("Current save is null, before accessing " +
                    "the current save please load or create a save for the save manager.");
        }
        return currentSave;
    }

}
