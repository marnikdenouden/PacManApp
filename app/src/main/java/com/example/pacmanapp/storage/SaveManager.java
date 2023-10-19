package com.example.pacmanapp.storage;

import android.content.Context;
import android.util.Log;

import androidx.annotation.WorkerThread;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class SaveManager {
    private static final String TAG = "SaveManager";
    private static SaveManager instance;
    private final FileManager fileManager;
    private final File imageDirectory;

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

    /**
     * Load a game save with the specified save name and load it in the given context.
     *
     * @param saveName Save name to load game save for
     */
    @WorkerThread
    GameSave loadSave(@NotNull String saveName) {
        File file = fileManager.getFile(saveName);
        byte[] data = FileManager.loadFileData(file);
        return load(data, saveName);
    }

    /**
     * Load the game save from file data.
     *
     * @param data Data to load game save from
     * @param saveName Name of the save for error handling
     * @return GameSave from the data, if successful
     */
    @WorkerThread
    public static GameSave load(@NotNull byte[] data, String saveName) {
        try {
            GameSave gameSave = GameSave.getGameSaveFromData(data);
            Log.i(TAG, "Loaded save with save name \"" + saveName + "\"");
            return gameSave;
        } catch (IOException ioException) {
            Log.w(TAG, "Could not load game save for save name \"" + saveName +
                    "\" as IO exception occurred.");
            ioException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            Log.w(TAG, "Could not load game save for save name \"" + saveName +
                    "\" as class was not found.");
            classNotFoundException.printStackTrace();
        }
        return null;
    }

    /**
     * Save the specified game save to the save files.
     *
     * @param gameSave Game save to save
     */
    @WorkerThread
    public void save(@NotNull GameSave gameSave) {
        File saveFile = fileManager.getFile(gameSave.getSaveName());
        save(gameSave, saveFile);
        Log.i(TAG, "Saved current save with name \"" + gameSave.getSaveName() + "\"");
    }

    /**
     * Tries to save the game save data to the specified save file.
     *
     * @param gameSave data to save in save file
     * @param saveFile file to save data in
     */
    @WorkerThread
    public static void save(@NotNull GameSave gameSave, @NotNull File saveFile) {
        try {
            byte[] saveData = gameSave.getByteArray();
            FileManager.saveFileData(saveFile, saveData);
        } catch (IOException ioException) {
            Log.e(TAG, "Could not save game data for game save \"" +
                    gameSave.getSaveName() + "\" in file \"" + saveFile.getName() +
                    "\" as IO exception occurred.");
            ioException.printStackTrace();
        }
    }

    /**
     * Create a new save to the current save.
     *
     * @param saveName New save name to name the created save
     * @pre !hasSave(saveName)
     *
     * @return gameSave save that has been created
     */
    @WorkerThread
    GameSave createSave(@NotNull String saveName) {
        if (hasSave(saveName)) {
            Log.w(TAG, "Could not create save with name \"" + saveName +
                    "\" as a save with this name already exists.");
            return null;
        }
        GameSave gameSave = new GameSave(saveName, imageDirectory);

        save(gameSave);

        Log.i(TAG, "Created save with save name \"" + saveName + "\"");
        return gameSave;
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
            Log.w(TAG, "Could not image manager directory file with name \"" +
                    imageManager.getDirectory().getName() + "\" for save \"" + saveName + "\"");
        }
        File saveFile = fileManager.getFile(saveName);
        fileManager.removeFile(saveFile);
        Log.i(TAG, "Removed save with save name \"" + saveName + "\"");
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
