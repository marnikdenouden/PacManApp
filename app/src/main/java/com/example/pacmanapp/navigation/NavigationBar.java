package com.example.pacmanapp.navigation;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

public class NavigationBar {
    // Log tag for NavigationBar
    private static final String TAG = "NavigationBar";
    private static final int navigationBarId = R.id.navigation_bar;
    private static final int inverseNavigationBarId = R.id.navigation_bar_inverse;
    private static NavigationBarType navigationBarType = NavigationBarType.PLAY;

    /**
     * Configures navigation bar.
     *
     * @param activity Activity that includes navigation bar
     * @param pageType Page type that activity represents
     */
    public static void configure(AppCompatActivity activity, PageType pageType) {
        // Add the navigation bar to the activity
        addNavigationBar(activity);

        // Robustness check to see if navigation bar is included in the activity
        NavigationBarType navigationBarType = NavigationBarType.getNavigationBarType(activity);
        if (navigationBarType == null) {
            Log.w(TAG,"Navigation bar to configure could not be found");
            throw new NullPointerException("Navigation bar to configure could not be found.");
        }

        // Configure navigation button for each of the page type buttons
        for (PageType navigationBarPageType: navigationBarType.getPageTypes()) {

            // Get navigation button and navigation marker from button page type ids
            ImageButton navigationButton = activity.findViewById(navigationBarPageType.getButtonId());
            ImageView navigationMarker = activity.findViewById(navigationBarPageType.getMarkerId());

            // Only set the navigation if both are found valid
            if (navigationButton == null || navigationMarker == null) {
                Log.w(TAG, "Navigation button or navigation marker " +
                        "was not found for page type " + navigationBarPageType);
                continue;
            }

            if (navigationBarPageType != pageType) {
                // Set button to navigate to other activities, be clickable and make marker invisible
                navigationButton.setClickable(true);
                Navigate.configure(navigationButton, activity, navigationBarPageType.getPage());
                navigationMarker.setVisibility(View.INVISIBLE);
            } else {
                // Set navigation button of current page to not clickable and make marker visible
                navigationButton.setClickable(false);
                navigationMarker.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Set the navigation bar type.
     *
     * @param navigationBarType Navigation bar type to continue with.
     */
    public static void setNavigationBarType(NavigationBarType navigationBarType) {
        NavigationBar.navigationBarType = navigationBarType;
    }

    /**
     * Add the current navigation bar to the activity.
     *
     * @param activity Activity to add navigation bar to
     */
    private static void addNavigationBar(AppCompatActivity activity) {
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(navigationBarId);
        ViewGroup viewGroupInverse = (ViewGroup) activity.findViewById(inverseNavigationBarId);
        if (viewGroup == null && viewGroupInverse == null) {
            Log.e(TAG, "Could not find navigation bar to set. " +
                    "Make sure the activity " + activity.getLocalClassName() +
                    " includes the navigation bar layout.");
            throw new NullPointerException("No view group found with valid navigation bar id");
        }

        if (viewGroup == null) {
            activity.getLayoutInflater()
                    .inflate(navigationBarType.getInverseLayoutId(), viewGroupInverse);
            return;
        }
        activity.getLayoutInflater().inflate(navigationBarType.getLayoutId(), viewGroup);
    }

}
