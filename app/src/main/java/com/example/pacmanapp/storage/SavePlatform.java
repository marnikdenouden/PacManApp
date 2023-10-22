package com.example.pacmanapp.storage;

import android.content.Context;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.example.pacmanapp.general.Util;

import org.jetbrains.annotations.NotNull;

import java.io.File;


public class SavePlatform {
    private static final String TAG = "SavePlatform";
    private static final String PLATFORM_DIRECTORY = "platform";
    private static final String FILE_NAME = "current save";
    private static File currentSaveFile;
    private static SaveManager saveManager;
    private static GameSave currentSave;
    private static boolean isPlaying;
    private static boolean setup = false;

    /**
     * Sets up the save platform to be used.
     *
     * @param context Context to initialize platform with
     */
    public static SaveManager setup(Context context) {
        Log.i(TAG, "Setting up save platform with context");
        saveManager = SaveManager.getInstance(context);

        setup = true;

        File saveDirectory = new File(context.getFilesDir(), PLATFORM_DIRECTORY);
        FileManager fileManager = new FileManager(saveDirectory);
        currentSaveFile = fileManager.getFile(FILE_NAME);

        if (currentSaveFile.exists() && currentSaveFile.isFile()) {
            byte[] data = FileManager.loadFileData(currentSaveFile);
            currentSave = SaveManager.load(data, "current save");
            if (hasSave()) {
                updateIsPlaying();
                Log.i(TAG, "Loaded current save from file");
            } else {
                Log.w(TAG, "File with current save is null");
            }
        } else {
            Log.i(TAG, "No file stored for the current save");
        }

        return getSaveManager();
    }

    /**
     * Check if the save platform setup is invalid.
     *
     * @return Truth assignment, if the save platform setup is invalid.
     */
    private static boolean hasInvalidSetup() {
        if (!setup) {
            Log.e(TAG, "Save platform was not set up when it was used.");
        }
        return !setup;
    }

    /**
     * Set the save manager of the save platform.
     *
     * @pre Save platform is setup
     * @return saveManager Save manager being used.
     */
    public static SaveManager getSaveManager() {
        if (hasInvalidSetup()) {
            return null;
        }
        return saveManager;
    }

    /**
     * Update the saves of the current game save.
     *
     * @pre Save platform is setup
     * @pre platform has a save loaded
     */
    public static void save() {
        if (hasInvalidSetup()) {
            return;
        }

        if (!hasSave()) {
            Log.e(TAG, "Could not save current save, as no save is loaded.");
            return;
        }

        new Thread(() -> {
            if (!isPlaying) {
            saveManager.save(currentSave);
            }
            storeCurrentSave();
        }).start();

    }

    /**
     * Store the current save in the save platform files.
     */
    @WorkerThread
    private static void storeCurrentSave() {
        SaveManager.save(currentSave, currentSaveFile);
    }

    /**
     * Start the current game save to be playing.
     *
     * @pre Save platform is setup
     * @pre platform has a save loaded
     */
    public static void play() {
        if (hasInvalidSetup()) {
            return;
        }

        if (!hasSave()) {
            Log.e(TAG, "Could not play current save, as no save is loaded.");
            return;
        }

        new Thread(() -> {
            // Sanity save, so you can restore the save exactly from before you started to play it.
            save();

            currentSave.play();
            updateIsPlaying();
            storeCurrentSave();

            Log.i(TAG, "Started to play current save with name \"" +
                    currentSave.getSaveName() + "\"");
        }).start();
    }

    /**
     * Check if the current game save is playing.
     *
     * @pre Save platform is setup
     * @return Truth assignment, if the current game save is being played
     */
    public static boolean isPlaying() {
        if (hasInvalidSetup()) {
            return false;
        }

        return isPlaying;
    }

    /**
     * Stop the current game save from playing.
     *
     * @pre Save platform is setup
     */
    public static void quit() {
        if (hasInvalidSetup()) {
            return;
        }

        currentSave.quit();
        updateIsPlaying();

        // Replace the finished played save by a freshly loaded one, if it still exits
        if (saveManager.hasSave(currentSave.getSaveName())) {
            load(currentSave.getSaveName(), () -> {});
        } else {
            currentSave = null;
        }
    }

    /**
     * Update variable if the current save is playing.
     *
     * @pre Save platform is setup
     */
    private static void updateIsPlaying() {
        if (hasInvalidSetup()) {
            return;
        }

        isPlaying = currentSave.isPlaying();
    }

    /**
     * Checks if the platform has a save loaded.
     *
     * @pre Save platform is setup
     * @return Truth assignment, if the platform has a save loaded.
     */
    public static boolean hasSave() {
        if (hasInvalidSetup()) {
            return false;
        }

        return currentSave != null;
    }

    /**
     * Load a specified save name on to the platform.
     *
     * @pre Save platform is setup
     * @param saveName Save name to load on to the platform.
     */
    public static void load(@NotNull String saveName, Runnable onFinishLoading) {
        if (hasInvalidSetup()) {
            return;
        }

        new Thread(() -> {
            if (saveManager.hasSave(saveName)) {
                currentSave = saveManager.loadSave(saveName);
            } else {
                currentSave = saveManager.createSave(saveName);
            }

            updateIsPlaying();
            storeCurrentSave();
            Log.i(TAG, "Set current save to be " + saveName);

            if (onFinishLoading != null) {
                Util.runOnUiThread(onFinishLoading);
            }
        }).start();
    }

    /**
     * Get the save currently loaded on the platform.
     *
     * @pre Save platform is setup
     * @pre Platform has a current save loaded
     * @return GameSave currently loaded
     * @throws NullPointerException When pre condition is violated
     */
    public static GameSave getSave() {
        if (hasInvalidSetup()) {
            throw new NullPointerException("Save platform is not setup, before using " +
                    "the save platform please set up the save platform.");
        }

        if (!hasSave()) {
            throw new NullPointerException("Current save is null, before accessing " +
                    "the current save please load or create a save for the save manager.");
        }
        return currentSave;
    }

}

