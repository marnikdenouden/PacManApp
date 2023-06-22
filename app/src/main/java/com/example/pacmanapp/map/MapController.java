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
    private final static int mapMoverId = R.id.mapMover;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int mapWidth = mapArea.getMapView().getWidth();
        int mapHeight = mapArea.getMapView().getHeight();

        int leftBound = 0;
        int rightBound = mapWidth - mapArea.getWidth();
        int topBound = 0;
        int bottomBound = mapHeight - mapArea.getHeight();

        int maxOvershootValue = getResources().getInteger(R.integer.mapOvershootValue);
        int mapMaxVelocity = getResources().getInteger(R.integer.maxMapVelocity);

        float scrollX;
        float scrollY;

        float moveX;
        float moveY;

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setLastTouchPosition(event);

                // Stops scroll animation, which in a sense allows the user to catch the map
                mapArea.scroller.abortAnimation();

                // Obtain the velocity tracker to start tracking motion for the end fling
                velocityTracker = VelocityTracker.obtain();
                velocityTracker.addMovement(event);
                return true;

            case MotionEvent.ACTION_MOVE:
                moveX = lastRawX - event.getRawX();
                moveY = lastRawY - event.getRawY();

                scrollX = Math.max(leftBound, Math.min(mapArea.getScrollX() + moveX, rightBound));
                scrollY = Math.max(topBound, Math.min(mapArea.getScrollY() + moveY, bottomBound));

                mapArea.scrollTo((int) scrollX, (int) scrollY);

                velocityTracker.addMovement(event);
                setLastTouchPosition(event);
                return true;

            case MotionEvent.ACTION_UP:
                moveX = lastRawX - event.getRawX();
                moveY = lastRawY - event.getRawY();

                scrollX = Math.max(leftBound, Math.min(mapArea.getScrollX() + moveX, rightBound));
                scrollY = Math.max(topBound, Math.min(mapArea.getScrollY() + moveY, bottomBound));

                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000, mapMaxVelocity);

                mapArea.scroller.fling(
                        (int) scrollX, (int) scrollY,
                        -getXVelocity(), -getYVelocity(),
                        leftBound, rightBound, topBound, bottomBound,
                        maxOvershootValue, maxOvershootValue);

                mapArea.invalidate();
                return true;

            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                return true;
        }
        return super.onTouchEvent(event);
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
     * Recycle velocity tracker, so it can be reused.
     */
    private void recycleVelocityTracker() {
        velocityTracker.recycle();
        velocityTracker = null;
    }

    /**
     * Get the X velocity of the velocity tracker.
     *
     * @pre compute the velocity on the velocity tracker
     * @return X velocity of the velocity tracker
     */
    private int getXVelocity() {
        return (int) velocityTracker.getXVelocity();
    }

    /**
     * Get the Y velocity of the velocity tracker.
     *
     * @pre compute the velocity on the velocity tracker
     * @return Y velocity of the velocity tracker
     */
    private int getYVelocity() {
        return (int) velocityTracker.getYVelocity();
    }
}
