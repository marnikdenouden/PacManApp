package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import androidx.annotation.NonNull;

import com.example.pacmanapp.R;
import com.example.pacmanapp.map.MapArea;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Ghost extends Character implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "Ghost";
    private static final Direction startDirection = Direction.RIGHT;
    private Direction direction;
    private final GhostType ghostType;

    /**
     * Ghost marker to display on the map and control.
     *
     * @param ghostType GhostType that ghost displays as
     * @param latitude  latitude that the ghost starts at
     * @param longitude longitude that the ghost start at
     */
    public Ghost(@NotNull GhostType ghostType, double latitude, double longitude) {
        super(latitude, longitude, R.drawable.ghost_icon, ghostType.getId());
        this.ghostType = ghostType;
        direction = startDirection;
    }

    /**
     * Get the ghost type.
     *
     * @return ghostType Ghost type of this ghost
     */
    public GhostType getGhostType() {
        return ghostType;
    }

    @Override
    protected GhostView createView(@NotNull MapArea mapArea) {
        GhostView ghostView = new GhostView(mapArea, this);
        ghostView.createView();
        return ghostView;
    }

    @SuppressLint("ViewConstructor")
    public static class GhostView extends CharacterView {
        private final String TAG = "GhostView";
        private final Ghost ghost;
        private final transient Map<Direction, AnimationDrawable> animationDrawableMap;

        protected GhostView(@NonNull MapArea mapArea, @NonNull Ghost ghost) {
            super(mapArea, ghost);
            this.ghost = ghost;
            animationDrawableMap = createAnimationDrawables();
            createView();
        }

        @Override
        protected void createView() {
            super.createView();
            setDrawable(getAnimationDrawable());
        }

        @Override
        void setRotation(Direction direction) {
            ghost.direction = direction;
            // Replace the imageView with the new direction.
            setDrawable(getAnimationDrawable());
        }

        /**
         * Get the animation drawable for a specified direction.
         *
         * @return Animation drawable for the specified direction
         */
        private AnimationDrawable getAnimationDrawable() {
            return animationDrawableMap.get(ghost.direction);
        }

        /**
         * Create animation drawables map to use for different directions.
         *
         * @return Map of AnimationDrawables for each Direction
         */
        private Map<Direction, AnimationDrawable> createAnimationDrawables() {
            int frameDuration = getContext().getResources().getInteger(R.integer.ghostFrameDuration);
            int frameCount = getContext().getResources().getInteger(R.integer.ghostFrameCount);

            HashMap<Direction, AnimationDrawable> drawableHashMap = new HashMap<>();

            int ghostColor = ghost.getGhostType().getColor(getContext());

            for (Direction direction: Direction.values()) {
                AnimationDrawable animationDrawable = new AnimationDrawable();
                for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
                    animationDrawable.addFrame(createFrame(direction, frameIndex,
                            ghostColor), frameDuration);
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
         * @return LayerDrawable frame of ghost with specified properties
         */
        private LayerDrawable createFrame(Direction direction, int frameIndex, int color) {
            Drawable drawable = direction.getDrawableGhostBase(getContext(), frameIndex);

            assert drawable != null;
            drawable.setFilterBitmap(false); // TODO do this for all the pixel art images and reduce the size of them.
            drawable.setTintMode(PorterDuff.Mode.SRC_ATOP);
            drawable.setTint(color);

            return new LayerDrawable(new Drawable[] {drawable,
                    direction.getDrawableGhostEyes(getContext())});
        }

    }

}
