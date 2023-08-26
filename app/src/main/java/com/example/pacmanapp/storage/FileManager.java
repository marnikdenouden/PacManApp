package com.example.pacmanapp.storage;

import android.util.Log;

import androidx.annotation.WorkerThread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class FileManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "FileManager";
    private final File directory;

    /**
     * Create a file manager for a specified directory.
     *
     * @param directory Directory to manage files for
     * @pre directory exists as a directory or can be created without obstruction
     */
    public FileManager(File directory) {
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Could not create directory required.");
                throw new RuntimeException();
            }
        } else {
            if (!directory.isDirectory()) {
                Log.e(TAG, "Given directory is not a directory.");
                throw new RuntimeException();
            }
        }
        this.directory = directory;
    }

    /**
     * Delete the specified file.
     *
     * @param file File to delete
     */
    public void removeFile(File file) {
        if (!file.delete()) {
            Log.w(TAG, "Could not delete file with name " + file.getName());
        }
    }

    /**
     * Delete all the files in the file manager directory.
     */
    public void clearFiles() {
        for (File file: getFiles()) {
            removeFile(file);
        }
    }

    /**
     * Check if a save exists with the specified file name.
     *
     * @param fileName File name to check file existence for
     * @return Truth assignment, if file exists for save name
     */
    public boolean hasFile(String fileName) {
        return getFile(fileName).exists();
    }

    /**
     * Get the file for the specified file name.
     *
     * @param fileName File name to get the file for
     * @return File in the save directory with the save name
     */
    public File getFile(String fileName) {
        return new File(directory, fileName);
    }

    /**
     * Get the files currently stored in the directory.
     *
     * @return File array of all files stored in the directory
     */
    public File[] getFiles() {
        return directory.listFiles();
    }

    /**
     * Save data to a file.
     *
     * @param file File to save data in
     * @param fileData File data to store in the file
     */
    @WorkerThread
    public static void saveFileData(File file, byte[] fileData) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(fileData);
        } catch (IOException exception) {
            Log.w(TAG, "Exception while writing save data to file.");
            exception.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Load byte array data of a file.
     *
     * @param file File to load the data from
     * @return Byte array data from the specified file
     */
    @WorkerThread
    public static byte[] loadFileData(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int data = fileInputStream.read();
            while (data != -1) {
                byteArrayOutputStream.write(data);
                data = fileInputStream.read();
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException exception) {
            Log.w(TAG, "Exception while loading file with name " + file.getName());
            exception.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Get the directory of the file manager.
     *
     * @return directory File of the file manager
     */
    public File getDirectory() {
        return directory;
    }
}
