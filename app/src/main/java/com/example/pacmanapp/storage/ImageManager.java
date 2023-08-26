package com.example.pacmanapp.storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.WorkerThread;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageManager extends FileManager {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "ImageManager";

    /**
     * Create a image manager for a specified directory.
     *
     * @param saveName String to create directory with
     * @param imageDirectory File parent directory
     * @pre directory exists as a directory or can be created without obstruction
     */
    public ImageManager(@NotNull String saveName, @NotNull File imageDirectory) {
        super(new File(imageDirectory, saveName));
    }

    /**
     * Get the image file for the specified file name.
     *
     * @param imageId String image id to get the image file for
     * @return File in the save directory with the save name
     */
    @Override
    public File getFile(String imageId) {
        return super.getFile(imageId + ".png");
    }

    /**
     * Save file image to a specified file.
     *
     * @param file File to store big map in
     * @param image Bitmap to store inside specified file
     */
    @WorkerThread
    public static void saveFileImage(File file, Bitmap image) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (IOException exception) {
            Log.w(TAG, "Exception while writing save data to file.");
            exception.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Load file image from a specified file.
     *
     * @param file File to get big map from
     * @return Bitmap image loaded from specified file
     */
    @WorkerThread
    public static Bitmap loadFileImage(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

}
