package com.example.pacmanapp.navigation;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

public enum NavigationBarType {
    ADMIN(R.layout.navigation_bar_admin, R.id.admin_navigation_bar, new PageType[] {PageType.EDIT, PageType.ADMIN_MAP, PageType.ADMIN_SETTINGS}),
    PLAY(R.layout.navigation_bar_play, R.id.play_navigation_bar, new PageType[] {PageType.INSPECT, PageType.MAP, PageType.SETTINGS});

    private final static String TAG = "NavigationBarType";

    private final int navigationBarId;
    private final int layoutId;
    private final PageType[] pageTypes;
    NavigationBarType(int layoutId, int navigationBarId, PageType[] pageTypes) {
        this.layoutId = layoutId;
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
