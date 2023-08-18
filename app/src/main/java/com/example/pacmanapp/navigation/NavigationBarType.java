package com.example.pacmanapp.navigation;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

public enum NavigationBarType {
    ADMIN(R.layout.navigation_bar_admin, R.layout.navigation_bar_admin_inverse,
            R.id.admin_navigation_bar,
            new PageType[] {PageType.EDIT, PageType.ADMIN_MAP, PageType.ADMIN_SETTINGS}),
    PLAY(R.layout.navigation_bar_play, R.layout.navigation_bar_play_inverse,
            R.id.play_navigation_bar,
            new PageType[] {PageType.INSPECT, PageType.MAP, PageType.SETTINGS});

    private final static String TAG = "NavigationBarType";

    private final int navigationBarId;
    private final int layoutId;
    private final int inverseLayoutId;
    private final PageType[] pageTypes;
    NavigationBarType(int layoutId, int inverseLayoutId, int navigationBarId, PageType[] pageTypes) {
        this.layoutId = layoutId;
        this.inverseLayoutId = inverseLayoutId;
        this.navigationBarId = navigationBarId;
        this.pageTypes = pageTypes;
    }

    /**
     * Get the page types for this navigation bar.
     *
     * @return Page types of this navigation bar
     */
    public PageType[] getPageTypes() {
        return pageTypes;
    }

    public boolean hasNavigationBar(AppCompatActivity activity) {
        return activity.findViewById(navigationBarId) != null;
    }

    /**
     * Get the navigation bar layout id.
     *
     * @return Layout id for this navigation bar
     */
    public int getLayoutId() {
        return layoutId;
    }

    /**
     * Get the navigation bar inverse layout id.
     *
     * @return Inverse layout id for this navigation bar
     */
    public int getInverseLayoutId() {
        return inverseLayoutId;
    }

    public static NavigationBarType getNavigationBarType(AppCompatActivity activity) {
        for (NavigationBarType navigationBarType: values()) {
            if (navigationBarType.hasNavigationBar(activity)) {
                return navigationBarType;
            }
        }
        Log.e(TAG, "Could not find a valid navigation bar type in activity " + activity.getLocalClassName());
        return null;
    }
}
