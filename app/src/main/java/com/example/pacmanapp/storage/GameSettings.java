package com.example.pacmanapp.storage;

import com.example.pacmanapp.R;

import java.time.Duration;

public class GameSettings extends SaveObject {
    public static int gameSettingsId = R.id.gameSettings;
    public long gameDurationSeconds = 3600;

    /**
     * Create a group of settings that can be serialized.
     *
     * @param gameSave Save to add game settings to
     */
    private GameSettings(GameSave gameSave) {
        super(gameSettingsId, gameSave);
    }

    /**
     * Set the game duration to a specified duration value.
     *
     * @param gameDuration Duration that the game should take
     */
    public void setGameDuration(Duration gameDuration) {
        gameDurationSeconds = gameDuration.getSeconds();
    }

    /**
     * Get the game duration that the game should start with.
     *
     * @return game duration that the game should start with
     */
    public Duration getGameDuration() {
        return Duration.ofSeconds(gameDurationSeconds);
    }

    /**
     * Gets or creates the game settings for the current save of the specified game save.
     *
     * @param gameSave Save to get game settings from
     * @return game settings from current save of the specified game save
     */
    public static GameSettings getFromSave(GameSave gameSave) {
        if (gameSave.hasSaveObject(gameSettingsId)) {
            return (GameSettings) gameSave.getSaveObject(gameSettingsId);
        }
        GameSettings gameSettings = new GameSettings(gameSave);
        gameSave.addSaveObject(gameSettings);
        return gameSettings;
    }

}
