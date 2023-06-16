package com.example.pacmanapp.markers;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.pacmanapp.R;

public enum Direction {
    LEFT(180, new int[] {R.drawable.ghosts_base_0, R.drawable.ghosts_base_1}, R.drawable.ghosts_eyes_left),
    RIGHT(0, new int[] {R.drawable.ghosts_base_0, R.drawable.ghosts_base_1}, R.drawable.ghosts_eyes_right),
    UP(270, new int[] {R.drawable.ghosts_base_up_0, R.drawable.ghosts_base_up_1}, R.drawable.ghosts_eyes_up),
    DOWN(90, new int[] {R.drawable.ghosts_base_0, R.drawable.ghosts_base_1}, R.drawable.ghosts_eyes_down);

    private int degrees;
    private int[] drawableBaseIds;
    private int drawableEyesId;
    Direction(int degrees, int[] drawableBaseIds, int drawableEyesId) {
        this.degrees = degrees;
        this.drawableBaseIds = drawableBaseIds;
        this.drawableEyesId = drawableEyesId;
    }

    int getDegrees() {
        return degrees;
    }

    Drawable getDrawableBase(Context context, int frameIndex) {
        return AppCompatResources.getDrawable(context, drawableBaseIds[frameIndex]);
    }

    Drawable getDrawableEyes(Context context) {
        return AppCompatResources.getDrawable(context, drawableEyesId);
    }
}
