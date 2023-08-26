package com.example.pacmanapp.storage;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.WorkerThread;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ImageStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient Map<String, Bitmap> imageMap;
    private final ImageManager imageManager;

    /**
     * Create the image manager that can store images.
     *
     * @param saveName String folder name to store images in
     * @param imageDirectory File directory of images
     */
    @WorkerThread
    public ImageStorage(@NotNull String saveName, @NotNull File imageDirectory) {
        this.imageManager = new ImageManager(saveName, imageDirectory);
        loadImages();
    }

    /**
     * Load the images stored in the files of the image manager.
     */
    @WorkerThread
    public void loadImages() {
        imageMap = new HashMap<>();
        for (File imageFile: imageManager.getFiles()) {
            Bitmap image = ImageManager.loadFileImage(imageFile);
            imageMap.put(imageFile.getName().split("\\.")[0], image);
        }
    }

    /**
     * Save image to the files and run time storage.
     *
     * @param imageId String identifier to store image with
     * @param image Bitmap image to store
     */
    @WorkerThread
    public void saveImage(String imageId, Bitmap image) {
        imageMap.put(imageId, image);
        File imageFile = imageManager.getFile(imageId);
        ImageManager.saveFileImage(imageFile, image);
    }

    /**
     * Remove image from files.
     *
     * @param imageId String identifier of image file to remove
     */
    public void removeImage(String imageId) {
        imageMap.remove(imageId);
        imageManager.removeFile(imageManager.getFile(imageId));
    }

    /**
     * Get the image for the specified string identifier.
     *
     * @param imageId String identifier of image
     * @return image Bitmap stored for string identifier
     */
    public Bitmap getImage(String imageId) {
        return imageMap.get(imageId);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        loadImages();
    }

}
