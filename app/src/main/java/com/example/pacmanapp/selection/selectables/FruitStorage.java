package com.example.pacmanapp.selection.selectables;

import android.util.Log;

import com.example.pacmanapp.R;
import com.example.pacmanapp.storage.GameSave;
import com.example.pacmanapp.storage.SaveObject;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class FruitStorage extends SaveObject implements Serializable {
    private final static String TAG = "FruitStorage";
    private static final long serialVersionUID = 1L;
    private static final int fruitStorageId = R.id.fruitStorage;
    private final Collection<Fruit> fruitCollection;

    /**
     * Create a fruit storage that is added to the save manager for the specified save name.
     *
     * @param gameSave Save to add the save object to the current save of.
     */
    public FruitStorage(GameSave gameSave) {
        super(fruitStorageId, gameSave);
        fruitCollection = new ArrayList<>();
    }

    /**
     * Add the specified fruit to the storage.
     *
     * @param fruit Fruit to add to the storage.
     */
    public void addFruit(@NotNull Fruit fruit) {
        fruitCollection.add(fruit);

        Log.i(TAG, "Added a fruit with type " + fruit.getFruitType().toString());
    }

    /**
     * Remove the specified fruit from the storage.
     *
     * @param fruit Fruit to remove from storage.
     */
    public void removeFruit(@NotNull Fruit fruit) {
        fruitCollection.remove(fruit);
    }

    /**
     * Clear all the fruits from the storage.
     */
    public void clearFruits() {
        if (fruitCollection.isEmpty()) {
            Log.i(TAG, "Fruit storage is already cleared");
            return;
        }

        Collection<Fruit> fruits = new HashSet<>(fruitCollection);
        for (Fruit fruit: fruits) {
            removeFruit(fruit);
        }

        Log.i(TAG, "Removed all fruits");
    }

    /**
     * Get a collection of the fruits in the storage.
     *
     * @return Collection of the fruits in the storage
     */
    public Collection<Fruit> getFruitCollection() {
        return new HashSet<>(fruitCollection);
    }

    /**
     * Gets or creates the map marker for the current save of the specified game save.
     *
     * @param gameSave Save to get fruit storage from
     * @return fruit storage from current save of the specified game save
     */
    public static FruitStorage getFromSave(GameSave gameSave) {
        if (gameSave.hasSaveObject(fruitStorageId)) {
            return (FruitStorage) gameSave.getSaveObject(fruitStorageId);
        }
        FruitStorage fruitStorage = new FruitStorage(gameSave);
        gameSave.addSaveObject(fruitStorage);
        return fruitStorage;
    }

}
