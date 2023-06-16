package com.example.pacmanapp.markers;

import android.content.Context;

import com.example.pacmanapp.R;

public enum GhostType {
    Blinky(R.color.Blinky, R.id.blinky),
    Pinky(R.color.Pinky, R.id.pinky),
    Inky(R.color.Inky, R.id.inky),
    Clyde(R.color.Clyde, R.id.clyde);

    private int colorId;
    private int markerId;

    GhostType(int colorId, int markerId) {
        this.colorId = colorId;
        this.markerId = markerId;
    }

    public int getColor(Context context) {
        return context.getResources().getColor(colorId, context.getTheme());
    }

    public int getId() {
        return markerId;
    }

}
