package com.example.pacmanapp.storage;

import com.example.pacmanapp.R;

public class GameSettings extends SaveObject {
    public static int gameSettingsId = R.id.gameSettings;

    /**
     * Create a group of settings that can be serialized.
     *
     * @param gameSave Save to add game settings to
     */
    public GameSettings(GameSave gameSave) {
        super(gameSettingsId, gameSave);
    }

}
