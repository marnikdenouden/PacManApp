package com.example.pacmanapp.displays;

import com.example.pacmanapp.R;
import com.example.pacmanapp.storage.GameSave;
import com.example.pacmanapp.storage.SaveObject;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashMap;
public class PlayValues extends SaveObject {
    private final static String TAG = "PlayValues";
    private static final long serialVersionUID = 1L;
    private final HashMap<Integer, Long> playValues;
    private static final int playValuesId = R.id.playValues;

    /**
     * Create a save object for play values that can be serialized.
     *
     * @param gameSave Save to add play values to
     */
    public PlayValues(@NotNull GameSave gameSave) {
        super(playValuesId, gameSave);
        playValues = new HashMap<>();
    }

    /**
     * Add value to play values map.
     *
     * @param key Integer id to identify the value to set
     * @param value long value to set for the specified key value
     */
    public void setValue(Integer key, long value) {
        playValues.put(key, value);
    }

    /**
     * Remove value from play values map.
     *
     * @param key Integer id to identify the value to remove
     */
    public void removeValue(int key) {
        playValues.remove(key);
    }

    /**
     * Get value from play values map.
     *
     * @param key Integer id to identify the value to get
     * @param defaultValue long to return if no value was set
     */
    public long getValue(int key, long defaultValue) {
        Long value = playValues.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * Gets or creates the play value save object from the specified game save.
     *
     * @param gameSave Save to get map marker from
     * @return Play values from the specified game save
     */
    public static PlayValues getFromSave(@NotNull GameSave gameSave) {
        if (gameSave.hasSaveObject(playValuesId)) {
            return (PlayValues) gameSave.getSaveObject(playValuesId);
        }
        PlayValues playValues = new PlayValues(gameSave);
        gameSave.addSaveObject(playValues);
        return playValues;
    }

    /**
     * Resets the game values of the specified game save.
     */
    public static void resetValues(GameSave gameSave) {
        Clock clock = new Clock(gameSave);
        clock.setTime(Duration.ofSeconds(9000));
        Score score = new Score(gameSave);
        score.setValue(0);
    }

}
