package com.example.pacmanapp.storage;

import android.util.Log;

import com.example.pacmanapp.displays.PlayValues;
import com.example.pacmanapp.selection.SelectionCrier;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class ResetManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "ResetManager";
    private final GameSave gameSave;
    private final Collection<ResetListener> resetCollection;

    public ResetManager(@NotNull GameSave gameSave) {
        this.gameSave = gameSave;
        resetCollection = new HashSet<>();
    }

    /**
     * Resets the game save to the starting values.
     */
    public void reset() {
        Log.i(TAG, "Resetting to start values for game save with name " +
                gameSave.getSaveName());
        PlayValues.resetValues(gameSave);
        SelectionCrier.getInstance().clearSelected();

        while(resetCollection.contains(null)) {
            resetCollection.remove(null);
        }

        for(ResetListener resetListener : resetCollection) {
            resetListener.reset();
        }
    }

    /**
     * Add a specified reset listener to the reset collection.
     *
     * @param resetListener Reset listener to add
     */
    public void addResetListener(@NotNull ResetListener resetListener) {
       resetCollection.add(resetListener);
    }

    /**
     * Remove a specified reset listener from the reset collection.
     *
     * @param resetListener Reset listener to remove
     */
    public void removeResetListener(@NotNull ResetListener resetListener) {
        resetCollection.remove(resetListener);
    }

    public interface ResetListener {
        /**
         * Called when the game save will be reset.
         */
        void reset();
    }
}
