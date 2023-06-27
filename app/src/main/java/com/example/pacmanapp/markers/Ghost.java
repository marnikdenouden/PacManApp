package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.location.LocationObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("ViewConstructor")
public class Ghost extends Character implements Serializable, LocationObserver {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "Ghost";
    private transient Map<Direction, AnimationDrawable> animationDrawableMap;
    private final GhostType ghostType;
    private static final Direction startDirection = Direction.RIGHT;

    /**
     * Ghost marker to display on the map and control.
     *
     * @param ghostType GhostType that ghost displays as
     * @param frameId   FrameId reference to map area that the ghost is placed on
     * @param latitude  latitude that the ghost starts at
     * @param longitude longitude that the ghost start at
     * @param context   Context that the ghost is created in
     */
    public Ghost(GhostType ghostType, int frameId, double latitude, double longitude,
                 Context context) {
        super(frameId, latitude, longitude, ghostType.getId(), context);
        this.ghostType = ghostType;

        setColor(ghostType, context);

        setDrawable(getAnimationDrawable(startDirection));
    }

    /**
     * Set the color of the ghost.
     *
     * @param ghostType Type to set the ghost to
     * @param context Context for the theme
     */
    private void setColor(GhostType ghostType, Context context) {
        int color = ghostType.getColor(context);
        animationDrawableMap = createAnimationDrawables(color, context);
    }

    /**
     * Get marker size in pixels.
     *
     * @param activity Activity to get resources from
     * @return Marker size in pixels
     */
    private static int getMarkerSize(AppCompatActivity activity) {
        return activity.getResources().getDimensionPixelSize(R.dimen.ghostMarkerSize);
    }

    /**
     * Get the animation drawable for a specified direction.
     *
     * @param direction Direction to get animation drawable for
     * @return Animation drawable for the specified direction
     */
    private AnimationDrawable getAnimationDrawable(Direction direction) {
        return animationDrawableMap.get(direction);
    }

    /**
     * Create animation drawables map to use for different directions.
     *
     * @param color Color that all animation drawables get
     * @param context Context in which to create the drawables
     * @return Map of AnimationDrawables for each Direction
     */
    private Map<Direction, AnimationDrawable> createAnimationDrawables(int color, Context context) {
        int frameDuration = context.getResources().getInteger(R.integer.ghostFrameDuration);
        int frameCount = context.getResources().getInteger(R.integer.ghostFrameCount);

        HashMap<Direction, AnimationDrawable> drawableHashMap = new HashMap<>();

        for (Direction direction: Direction.values()) {
            AnimationDrawable animationDrawable = new AnimationDrawable();
            for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
                animationDrawable.addFrame(createFrame(direction, frameIndex, color, context),
                        frameDuration);
            }
            drawableHashMap.put(direction, animationDrawable);
        }

        return drawableHashMap;
    }

    /**
     * Create a ghost frame for a specific direction, frame index and color.
     *
     * @param direction Direction for the ghost
     * @param frameIndex Frame index of the ghost
     * @param color Color for the ghost to have
     * @param context Context in which to create the frame
     * @return LayerDrawable frame of ghost with specified properties
     */
    private LayerDrawable createFrame(Direction direction, int frameIndex, int color,
                                      Context context) {
        Drawable drawableBase = direction.getDrawableGhostBase(context, frameIndex);

        assert drawableBase != null;
        drawableBase.setTintMode(PorterDuff.Mode.SRC_ATOP);
        drawableBase.setTint(color);

        return new LayerDrawable(new Drawable[] {drawableBase, direction.getDrawableGhostEyes(context)});
    }

    @Override
    void setRotation(Direction direction) {
        // Replace the imageView with the new direction.
        setDrawable(getAnimationDrawable(direction));
    }

    @Override
    Ghost load(Context context) {
        return new Ghost(ghostType, frameId, latitude, longitude, context);
    }

}
