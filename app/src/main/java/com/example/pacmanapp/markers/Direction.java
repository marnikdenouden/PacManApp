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

    private final int degrees;
    private final int[] drawableGhostBaseIds;
    private final int drawableGhostEyesId;

    Direction(int degrees, int[] drawableGhostBaseIds, int drawableGhostEyesId) {
        this.degrees = degrees;
        this.drawableGhostBaseIds = drawableGhostBaseIds;
        this.drawableGhostEyesId = drawableGhostEyesId;
    }

    /**
     * Get the degrees of the direction.
     *
     * @return Degrees of the direction
     */
    int getDegrees() {
        return degrees;
    }

    /**
     * Get the drawable base of a ghost for this direction.
     *
     * @param context Context to get drawable with
     * @param frameIndex Frame index to get drawable from
     * @pre Frame index needs to be >= 0 and < frameCount
     * @return Drawable for the frame index
     * @throws IllegalArgumentException if pre condition is violated
     */
    Drawable getDrawableGhostBase(Context context, int frameIndex) {
        if (frameIndex < 0 || frameIndex >= context.getResources().getInteger(R.integer.ghostFrameCount)) {
            throw new IllegalArgumentException("Frame index is not valid for the frame count.");
        }
        return AppCompatResources.getDrawable(context, drawableGhostBaseIds[frameIndex]);
    }

    /**
     * Get the ghost eyes drawable for this direction.
     *
     * @param context Context to get drawable with
     * @return Drawable of ghost eyes for this direction.
     */
    Drawable getDrawableGhostEyes(Context context) {
        return AppCompatResources.getDrawable(context, drawableGhostEyesId);
    }
}
