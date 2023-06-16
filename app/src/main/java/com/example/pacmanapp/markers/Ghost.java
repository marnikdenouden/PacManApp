package com.example.pacmanapp.markers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

import java.util.HashMap;
import java.util.Map;

public class Ghost extends Character {
    private final Map<Direction, AnimationDrawable> animationDrawableMap;
    private final GhostType ghostType;
    private static final Direction startDirection = Direction.RIGHT;

    /**
     * Ghost marker to display on the map and control.
     *
     * @param ghostType GhostType that ghost displays as
     * @param latitude latitude that the marker starts at
     * @param longitude longitude that the marker start at
     * @param context Context that the marker is created in
     * @param activity Activity that the marker is placed in
     */
    public Ghost(GhostType ghostType, double latitude, double longitude, Context context,
                 AppCompatActivity activity) {
        int color = ghostType.getColor(context);
        this.ghostType = ghostType;
        animationDrawableMap = createAnimationDrawables(color, context);
        create(latitude, longitude, context, activity);
    }

    /**
     * Create a ghost marker for specified context, activity and alike.
     *
     * @param latitude Latitude used to position marker on map
     * @param longitude Longitude used to position marker on map
     * @param context Context in which the marker is created
     * @param activity Activity in which the marker is placed
     * @return ImageView of created ghost marker
     */
    ImageView create(double latitude, double longitude, Context context,
                            AppCompatActivity activity) {
        int markerSize = activity.getResources().getDimensionPixelSize(R.dimen.ghostMarkerSize);
        return super.create(latitude, longitude, markerSize, markerSize,
                getAnimationDrawable(startDirection), ghostType.getId(), context, activity);
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

        HashMap<Direction, AnimationDrawable> map = new HashMap<>();

        for (Direction direction: Direction.values()) {
            AnimationDrawable animationDrawable = new AnimationDrawable();
            for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
                animationDrawable.addFrame(createFrame(direction, frameIndex, color, context),
                        frameDuration);
            }
            map.put(direction, animationDrawable);
        }

        return map;
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
        Drawable drawableBase = direction.getDrawableBase(context, frameIndex);

        assert drawableBase != null;
        drawableBase.setTintMode(PorterDuff.Mode.SRC_ATOP);
        drawableBase.setTint(color);

        return new LayerDrawable(new Drawable[] {drawableBase, direction.getDrawableEyes(context)});
    }

    @Override
    void setRotation(Direction direction) {
        // Replace the imageView with the new direction.
        imageView.setImageDrawable(getAnimationDrawable(direction));
    }

}
