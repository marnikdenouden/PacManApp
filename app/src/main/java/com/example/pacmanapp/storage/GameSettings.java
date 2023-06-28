package com.example.pacmanapp.storage;

import com.example.pacmanapp.R;

public class GameSettings extends SaveObject {
    public static int gameSettingsId = R.id.gameSettings;

    /**
     * Create a group of settings that can be serialized.
     *
     * @param saveManager SaveManager to add game settings to current save of.
     */
    public GameSettings(SaveManager saveManager) {
        super(gameSettingsId, saveManager);
    }

}
