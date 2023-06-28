package com.example.pacmanapp.storage;

import android.content.Context;

public class GameSettings extends SaveObject {

    public GameSettings(String saveName, Context context) {
        super(saveName, context);
    }

    /**
     * Load settings ... TODO implement
     */
    @Override
    public void load(Context context) {

    }

}
