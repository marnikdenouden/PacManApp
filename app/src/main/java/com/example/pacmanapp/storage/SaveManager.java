package com.example.pacmanapp.storage;

import android.content.Context;
import android.util.Log;

import com.example.pacmanapp.location.LocationUpdater;
import com.google.android.gms.common.util.IOUtils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SaveManager {
    private static final String TAG = "SaveManager";
    private final File saveDirectory;
    private static SaveManager saveManager;

    // TODO check access modifiers of storage classes and related. Like do all constructors need to be public etc.
    Map<String, GameSave> saves;

    SaveManager(Context context) { // TODO should be singleton?
        //File directory = context.getFilesDir();
        saves = new HashMap<>();
        //saveDirectory = context.getDir(saveDir, Context.MODE_PRIVATE);
        //if (!saveDirectory.exists()) {
        //    saveDirectory.mkdirs();
        //}
        saveDirectory = new File(context.getFilesDir(), "saves");
        saveDirectory.mkdir();
        final File file = new File(saveDirectory, "test.txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write("Hello this is a test file in the saves folder".getBytes());
            Log.d("SaveManager", "Created test file");
        } catch (IOException exception) {
            // TODO remove this write in create, this is for testing
        }
    }

    public void saveSaves() {
        // Clear the saves directory
        File[] files = saveDirectory.listFiles();
        if (files != null) {
            for (File saveFile : files) {
                if (!saveFile.delete()) {
                    Log.d(TAG, "Could not delete save file with name " + saveFile.getName());
                    // TODO add problem implementation;
                }
            }
        }

        for (String fileName: saves.keySet()) {

            GameSave gameSave = saves.get(fileName);
            if (gameSave == null) {
                Log.d(TAG, "Game save is null for file name " + fileName);
                // TODO add problem implementation;
                return;
            }
            try {
                byte[] saveData = gameSave.getByteArray();
                saveFileData(fileName, saveDirectory, saveData);
            } catch (IOException exception) {
                Log.d(TAG, "Could not get byte array data from game save");
                // TODO add problem implementation;
            }
        }
    }

    public void loadSave(String saveName, Context context) {
        File saveFile = new File(saveDirectory.getPath(), saveName);
        try {
            byte[] saveData = loadFileData(saveFile);
            Log.d(TAG, "File data of save with name " + saveFile.getName() + ":" + Arrays.toString(saveData));
            GameSave gameSave = GameSave.getGameSaveFromData(saveData);
            if (gameSave == null) {
                Log.d(TAG, "Could not load game save from save file with name " + saveFile.getName());
                return;
            }
            saves.put(saveFile.getName(), gameSave);
            gameSave.load(context);
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getCause());
        }
    }

    public void loadSaves() {
        File[] files = saveDirectory.listFiles();

        if (files == null) {
            return; // TODO no files found to load in save directory
        }

        for (File saveFile : files) {
            try {
                byte[] saveData = loadFileData(saveFile);
                Log.d(TAG, "File data of save with name " + saveFile.getName() + ":" + Arrays.toString(saveData));
                GameSave gameSave = GameSave.getGameSaveFromData(saveData);
                if (gameSave == null) {
                    Log.d(TAG, "Could not load game save from save file with name " + saveFile.getName());
                }
                saves.put(saveFile.getName(), gameSave);
            } catch (IOException | ClassNotFoundException ignored) {

            }
            //catch (IOException | ClassNotFoundException exception) {
            //    Log.d(TAG, "Exception while loading data from file.");
            //    throw new RuntimeException(exception); // TODO add exception implementation;
            //}
        }

    }

    private static void saveFileData(String fileName, File saveDirectory, byte[] fileData) {
        File saveFile = new File(saveDirectory, fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(saveFile)) {
            Log.d(TAG, "Writing save data " + fileData + " to save file.");
            fileOutputStream.write(fileData);
        } catch (IOException exception) {
            Log.d(TAG, "Exception while writing save data to file.");
            throw new RuntimeException(exception); // TODO add exception implementation;
        }
    }

    private static byte[] loadFileData(File saveFile) {
        try (FileInputStream fileInputStream = new FileInputStream(saveFile)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int data = fileInputStream.read();
            while (data != -1) {
                byteArrayOutputStream.write(data);
                data = fileInputStream.read();
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException exception) {
            Log.d(TAG, "Exception while loading file with name " + saveFile.getName());
            throw new RuntimeException(exception); // TODO add exception implementation;
        }
    }

    // In static methods? Should do the following:
    // Save saves to a file
    // Load saves from a file
    // Send saves to other devices
    // Receive saves from other devices

    /**
     * Create a game save to the saves map.
     *
     * @param saveName Save name to reference the created game save
     */
    private void createSave(String saveName) {
        if (saves.containsKey(saveName)) {
            // TODO give error that save was already created?
            return;
        }
        saves.put(saveName, new GameSave());
    }

    /**
     * Remove a game save from the saves map.
     *
     * @param saveName Save name to remove from saves
     */
    public void removeSave(String saveName) {
        saves.remove(saveName);
    }

    /**
     * Get the game save for a save name.
     *
     * @param saveName Save name to get game save for
     * @return Game save for the save name
     */
    public GameSave getGameSave(String saveName) {
        if (!saves.containsKey(saveName)) {
            createSave(saveName);
            Log.d(TAG, "Created new game save with name " + saveName);
        }
        return saves.get(saveName);
    }

    /**
     * Get the save manager singleton instance.
     *
     * @param context Context to create save manger with, if not yet made
     * @return Save manager
     */
    public static @NotNull SaveManager getSaveManager(Context context) {
        if (saveManager == null) {
            saveManager = new SaveManager(context);
        }
        return saveManager;
    }
}
