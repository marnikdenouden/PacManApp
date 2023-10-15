package com.example.pacmanapp.storage;

import android.content.Context;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.example.pacmanapp.displays.Clock;
import com.example.pacmanapp.displays.Score;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class SaveManager {
    private static final String TAG = "SaveManager";
    private static SaveManager instance;
    private final FileManager fileManager;
    private final File imageDirectory;
    private GameSave currentSave;

    /**
     * Create a save manager for a specified context.
     *
     * @param context Context to get files directory with
     */
    private SaveManager(Context context) {
        File saveDirectory = new File(context.getFilesDir(), "saves");
        fileManager = new FileManager(saveDirectory);
        imageDirectory = new File(context.getFilesDir(), "images");
    }

// TODO on loading a new save the selection should be reset somehow? Maybe make what is selected part of the save.

    // TODO ensure that laod save and save save are called in worker threads, maybe it requires loading display in UI.


    /**
     * Load a game save with the specified save name and load it in the given context.
     *
     * @param saveName Save name to load game save for
     */
    @WorkerThread
    public void loadSave(@NotNull String saveName) {
        File file = fileManager.getFile(saveName);
        byte[] data = FileManager.loadFileData(file);
        try {
            currentSave = GameSave.getGameSaveFromData(data);
        } catch (IOException ioException) {
            Log.w(TAG, "Could not load game save for save name \"" + saveName +
                    "\" as IO exception occurred.");
            ioException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            Log.w(TAG, "Could not load game save for save name \"" + saveName +
                    "\" as class was not found.");
            classNotFoundException.printStackTrace();
        }
        Log.i(TAG, "Loaded save with save name " + saveName);
    }

    /**
     * Save the current save to its save file.
     *
     * @pre save manager has a current save set
     */
    @WorkerThread
    public void saveCurrentSave() {
        if (!hasCurrentSave()) {
            Log.e(TAG, "Could not save current save, because current save was not yet set.");
            return;
        }
        File saveFile = fileManager.getFile(currentSave.getSaveName());
        try {
            byte[] saveData = currentSave.getByteArray();
            FileManager.saveFileData(saveFile, saveData);
        } catch (IOException ioException) {
            Log.w(TAG, "Could not save current game save with save name \"" +
                    currentSave.getSaveName() + "\" as IO exception occurred.");
            ioException.printStackTrace();
        }
        Log.i(TAG, "Saved current save with name " + currentSave.getSaveName());
    }

    /**
     * Create a new save to the current save.
     *
     * @param saveName New save name to name the created save
     * @pre !hasSave(saveName)
     */
    @WorkerThread
    private void createSave(@NotNull String saveName) {
        if (fileManager.hasFile(saveName)) {
            Log.w(TAG, "Could not create save with name \"" + saveName +
                    "\" as a save with this name already exists.");
            return;
        }
        currentSave = new GameSave(saveName, imageDirectory);
        Log.i(TAG, "Created save with save name " + saveName);
    }

    /**
     * Checks if save name has a save stored.
     *
     * @param saveName Save name to check existence for
     * @return Truth assignment, if save is stored under save name
     */
    public boolean hasSave(@NotNull String saveName) {
        return fileManager.hasFile(saveName);
    }

    /**
     * Load or create the current save to the specified save name.
     *
     * @param saveName Save name to load or create current save for
     */
    @WorkerThread
    public void setCurrentSave(@NotNull String saveName) {
        if (hasSave(saveName)) {
            loadSave(saveName);
        } else {
            createSave(saveName);
        }
        saveCurrentSave();
        Log.i(TAG, "Set current save to be " + saveName);
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
            removeSave(saveFile.getName());
        }
        Log.i(TAG, "Removed all saves");
    }

    /**
     * Remove a save from the save directory.
     *
     * @param saveName Save name to remove from the save directory
     */
    public void removeSave(@NotNull String saveName) {
        ImageManager imageManager = new ImageManager(saveName, imageDirectory);
        imageManager.clearFiles();
        if (!imageManager.getDirectory().delete()) {
            Log.w(TAG, "Could not image manager directory file with name " +
                    imageManager.getDirectory().getName() + " for save " + saveName);
        }
        File saveFile = fileManager.getFile(saveName);
        fileManager.removeFile(saveFile);
        Log.i(TAG, "Removed save with save name " + saveName);
    }

    /**
     * Get the save names currently stored in the save directory.
     *
     * @return Array of save names currently stored
     */
    public String[] getSaveNames() {
        File[] files = fileManager.getFiles();
        String[] saveNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            saveNames[i] = files[i].getName();
        }
        return saveNames;
    }

    /**
     * Check if a current save is set.
     *
     * @return Truth assignment, if current save is set
     * A save can be set by either loading or creating a game save successfully
     */
    public boolean hasCurrentSave() {
        return currentSave != null;
    }

    /**
     * Get the current save of this save manager.
     *
     * @pre save manager has a current save
     * @return Game save currently loaded
     * @throws NullPointerException When pre condition is violated
     */
    public GameSave getCurrentSave() {
        if (!hasCurrentSave()) {
            throw new NullPointerException("Current save is null, before accessing " +
                    "the current save please load or create a save for the save manager.");
        }
        return currentSave;
    }

    /**
     * Get the save manager instance.
     *
     * @param context Context to get instance with
     * @return Save manager instance
     */
    public static SaveManager getInstance(Context context) {
        if (instance == null) {
            instance = new SaveManager(context);
        }
        return instance;
    }

}
