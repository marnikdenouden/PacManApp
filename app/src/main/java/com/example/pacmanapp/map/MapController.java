package com.example.pacmanapp.map;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.pacmanapp.R;

@SuppressLint("ViewConstructor")
public class MapController extends View {
    private final static String TAG = "MapController";
    private final static int mapMoverId = R.id.mapController;
    private final MapArea mapArea;

    public MapController(MapArea mapArea) {
        super(mapArea.getContext());
        this.mapArea = mapArea;

        setId(mapMoverId);
        setLayoutParams();
    }

    /**
     * Set the layout params for this map mover view.
     */
    private void setLayoutParams() {
        // Set map controller to match map frame in height and width
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
    }

    private VelocityTracker velocityTracker;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Stops scroll animation, which in a sense allows the user to catch the map
                mapArea.scroller.abortAnimation();

                // Obtain the velocity tracker to start tracking motion for the end fling
                velocityTracker = VelocityTracker.obtain();

                // Update velocity tracker and last touch position with event.
                velocityTracker.addMovement(event);
                setLastTouchPosition(event);
                return true;

            case MotionEvent.ACTION_MOVE:
                // Scroll the map area to the newly computed scroll x and y position
                mapArea.scrollTo(getScrollX(event), getScrollY(event));

                // Update velocity tracker and last touch position with event.
                velocityTracker.addMovement(event);
                setLastTouchPosition(event);
                return true;

            case MotionEvent.ACTION_UP:
                // Add movement of motion event to velocity tracker
                velocityTracker.addMovement(event);

                // Compute velocity of past second with a max map velocity
                int mapMaxVelocity = getResources().getInteger(R.integer.maxMapVelocity);
                velocityTracker.computeCurrentVelocity(1000, mapMaxVelocity);

                // Get the map overshoot value
                int maxOvershootValue = getResources().getInteger(R.integer.mapOvershootValue);

                // Get the velocity components from the velocity tracker
                int xVelocity = (int) velocityTracker.getXVelocity();
                int yVelocity = (int) velocityTracker.getYVelocity();

                // Fling the map area with the computed values
                mapArea.scroller.fling(
                        getScrollX(event), getScrollY(event),
                        -xVelocity, -yVelocity,
                        getLeftBound(), getRightBound(), getTopBound(), getBottomBound(),
                        maxOvershootValue, maxOvershootValue);

                // Invalidate the map area to ensure it gets updated
                mapArea.invalidate();
                return true;

            case MotionEvent.ACTION_CANCEL:
                // Recycle the velocity tracker so it is ready to be reused later
                velocityTracker.recycle();
                velocityTracker = null;
                return true;
            default:
                // Return super on touch event for those motion events that are not specified
                return super.onTouchEvent(event);
        }
    }

    private float lastRawX = 0;
    private float lastRawY = 0;

    /**
     * Set last touch position of the users finger.
     *
     * @param event Motion event to save touch position for
     */
    private void setLastTouchPosition(MotionEvent event) {
        lastRawX = event.getRawX();
        lastRawY = event.getRawY();
    }

    /**
     * Compute the scroll x position after the motion event.
     *
     * @return scrollX that has been computed with the motion event for hte map area
     */
    private int getScrollX(MotionEvent event) {
        float moveX = lastRawX - event.getRawX();
        return (int) Math.max(getLeftBound(), Math.min(mapArea.getScrollX() + moveX, getRightBound()));
    }

    /**
     * Compute the scroll y position after the motion event.
     *
     * @return scrollY that has been computed with the motion event for hte map area
     */
    private int getScrollY(MotionEvent event) {
        float moveY = lastRawY - event.getRawY();
        return (int) Math.max(getTopBound(),
                Math.min(mapArea.getScrollY() + moveY, getBottomBound()));
    }

    /**
     * Get the right bound for the current map area.
     *
     * @return rightBound value for the map area.
     */
    private int getRightBound() {
        int mapWidth = mapArea.getMapView().getWidth();
        return mapWidth - mapArea.getWidth();
    }

    /**
     * Get the bottom bound for the current map area.
     *
     * @return bottomBound value for the map area.
     */
    private int getBottomBound() {
        int mapHeight = mapArea.getMapView().getHeight();
        return mapHeight - mapArea.getHeight(); //+ 128; // 128 adjusts for overlap with bottom area
    }

    /**
     * Get the left bound for the current map area.
     *
     * @return leftBound value for the map area.
     */
    private int getLeftBound() {
        return 0;
    }

    /**
     * Get the top bound for the current map area.
     *
     * @return topBound value for the map area.
     */
    private int getTopBound() {
        return 0; //-128; // 128 adjusts for overlap with top area
    }
}
