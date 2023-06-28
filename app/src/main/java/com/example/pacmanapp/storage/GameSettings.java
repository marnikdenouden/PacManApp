package com.example.pacmanapp.storage;

public class GameSettings extends SaveObject {

    /**
     * Create a group of settings that can be serialized.
     *
     * @param saveManager SaveManager to add game settings to current save of.
     */
    public GameSettings(SaveManager saveManager) {
        super(saveManager);
    }

}
