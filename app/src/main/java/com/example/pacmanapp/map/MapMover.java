package com.example.pacmanapp.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pacmanapp.R;

@SuppressLint("ViewConstructor")
public class MapMover extends View {
    private final static String TAG = "MapMover";
    private final static int mapMoverId = R.id.mapMover;
    private final MapArea mapArea;

    public MapMover(MapArea mapArea) {
        super(mapArea.getContext());
        this.mapArea = mapArea;

        setId(mapMoverId);
        setLayoutParams();
    }

    /**
     * Set the layout params for this map mover view.
     */
    private void setLayoutParams() {
        // Set map mover to match map frame in height and width
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    float lastRawX = 0;
    float lastRawY = 0;
    VelocityTracker velocityTracker;

    public boolean handleTouchEvent(MotionEvent event) {
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

                lastRawX = event.getRawX();
                lastRawY = event.getRawY();

                velocityTracker = VelocityTracker.obtain();
                velocityTracker.addMovement(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                moveX = lastRawX - event.getRawX();
                moveY = lastRawY - event.getRawY();

                scrollX = Math.max(leftBound, Math.min(mapArea.getScrollX() + moveX, rightBound));
                scrollY = Math.max(topBound, Math.min(mapArea.getScrollY() + moveY, bottomBound));

                //moveX = scrollX - mapArea.getScrollX();
                //moveY = scrollY - mapArea.getScrollY();

                mapArea.scrollTo((int) scrollX, (int) scrollY);
                //mapArea.scroller.startScroll(mapArea.getScrollX(), mapArea.getScrollY(), (int) -moveX, (int) -moveY, 100);

                velocityTracker.addMovement(event);
                lastRawX = event.getRawX();
                lastRawY = event.getRawY();
                //mapArea.invalidate();
                return true;
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.recycle();
                velocityTracker = null;
                return true;
            case MotionEvent.ACTION_UP:
                moveX = lastRawX - event.getRawX();
                moveY = lastRawY - event.getRawY();

                scrollX = Math.max(leftBound, Math.min(mapArea.getScrollX() + moveX, rightBound));
                scrollY = Math.max(topBound, Math.min(mapArea.getScrollY() + moveY, bottomBound));

                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000, mapMaxVelocity);
                Log.d(TAG, "Flinging with velocity " + getXVelocity() + " " + getYVelocity()); // TODO remove debug statement
                mapArea.scroller.fling(
                        (int) scrollX, (int) scrollY,
                        -getXVelocity(), -getYVelocity(),
                        leftBound, rightBound, topBound, bottomBound,
                        maxOvershootValue, maxOvershootValue);

                //mapArea.scroller.springBack(mapArea.getScrollX(), mapArea.getScrollY(),
                //        leftBound, rightBound, topBound, bottomBound);
                /*
                velocityTracker.addMovement(event);
                int mapMaxVelocity = getResources().getInteger(R.integer.maxMapVelocity);
                velocityTracker.computeCurrentVelocity(1000); // TODO add max velocity

                mapArea.scroller.fling(
                        (int) event.getRawX(), (int) event.getRawY(),
                        -getXVelocity(), -getYVelocity(),
                        leftBound, rightBound, topBound, bottomBound,
                        maxOvershootValue, mapMaxVelocity);

                 */
                mapArea.invalidate();
                return true;
//
//            case MotionEvent.ACTION_UP:
//                velocityTracker.addMovement(event);
//                int mapMaxVelocity = getResources().getInteger(R.integer.maxMapVelocity);
//                velocityTracker.computeCurrentVelocity(1000, mapMaxVelocity);
//
//                scrollX = mapArea.getScrollX() + lastRawX - event.getRawX();
//                scrollY = mapArea.getScrollY() + lastRawY - event.getRawY();
//
//                Log.d(TAG, "scrollX " + scrollX + ", scrollY " + scrollY);
//                Log.d(TAG, "velocityX " + -getXVelocity() + ", velocityY " + -getYVelocity());
//
//                mapArea.scroller.fling((int) mapArea.getScrollX(), (int) mapArea.getScrollY(), -getXVelocity(), -getYVelocity(),
//                        leftBound, rightBound, topBound, bottomBound,
//                        maxOvershootValue, maxOvershootValue);
//
//            case MotionEvent.ACTION_CANCEL:
//                recycleVelocityTracker();
//                break;
//            case MotionEvent.ACTION_DOWN:
//                velocityTracker = VelocityTracker.obtain();
//                velocityTracker.addMovement(event);
//                lastRawX = event.getRawX();
//                lastRawY = event.getRawY();
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                velocityTracker.addMovement(event);
//
//                float moveX = lastRawX - event.getRawX();
//                float moveY = lastRawY - event.getRawY();
//
//                //float scrollX = mapArea.getScrollX() + moveX;
//                //float scrollY = mapArea.getScrollY() + moveY;
//
//                //Log.d(TAG, "scrollX " + scrollX + ", scrollY " + scrollY);
//                //Log.d(TAG, "velocityX " + getXVelocity(event) + ", velocityY " + getYVelocity(event));
//
//                scrollX = Math.max(leftBound, Math.min(mapArea.getScrollX() + moveX, rightBound));
//                scrollY = Math.max(topBound, Math.min(mapArea.getScrollY() + moveY, bottomBound));
//
//                // TODO should not fling when user is scrolling
//                mapArea.scrollTo((int) scrollX, (int) scrollY);
//
//                mapArea.invalidate();
//
//                //mapArea.scrollTo(scrollX, scrollY);
//
//                lastRawX = event.getRawX();
//                lastRawY = event.getRawY();
//                return true;
        }
        return super.onTouchEvent(event);
    }

//    MotionEvent.ACTION_CANCEL -> {
//        velocityTracker?.recycle()
//        velocityTracker = null
//    }
//    MotionEvent.ACTION_UP -> {
//        when (scrollerType) {
//            RadioScollerEnum.SPRING_BACK -> {
//                scroller.springBack(scrollX, scrollY, 0,0 ,0, 0)
//                invalidate()
//            }
//            RadioScollerEnum.SCROLL_TO -> {
//                scroller.startScroll(scrollX, scrollY, -scrollX, -scrollY, 1000)
//                invalidate()
//            }
//            RadioScollerEnum.FLING,
//                    RadioScollerEnum.FLING_OVER -> {
//                val overValue = if (scrollerType == RadioScollerEnum.FLING_OVER)
//                    resources.dpToPx(64) else 0
//
//                velocityTracker?.let {
//                    // Compute velocity within the last 1000ms
//                    it.addMovement(event)
//                    it.computeCurrentVelocity(1000)
//
//                    scroller.fling(
//                            scrollX, scrollY,
//                            -it.xVelocity.toInt(), -it.yVelocity.toInt(),
//                            leftLimit.toInt(), rightLimit.toInt(),
//                            topLimit.toInt(), bottomLimit.toInt(),
//                            overValue, overValue
//                    )
//                    invalidate()
//                }
//            }
//        }
//
//        velocityTracker?.recycle()
//        velocityTracker = null
//    }
//}
//        return super.onTouchEvent(event)


    private void recycleVelocityTracker() {
        velocityTracker.recycle();
        velocityTracker = null;
    }

    private int getXVelocity() {
        return (int) velocityTracker.getXVelocity();
    }
    private int getYVelocity() {
        return (int) velocityTracker.getYVelocity();
    }
}
