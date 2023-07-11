package com.example.pacmanapp.navigation;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

public class NavigationBar {

    // Log tag for NavigationBar
    private static final String TAG = "NavigationBar";

    /**
     * Configures navigation bar.
     *
     * @param activity Activity that includes navigation bar.
     * @param admin Truth assignment, if activity is for admin.
     * @param pageType Page type that activity represents.
     */
    public static void configure(AppCompatActivity activity, boolean admin,
                                 PageType pageType) {
        // Robustness check to see if navigation bar is included in the activity.
        if (activity.findViewById(R.id.navigationBar) == null) {
            Log.w(TAG,"Navigation bar to configure could not be found");
            throw new NullPointerException("Navigation bar to configure could not be found.");
        }

        // Configure navigation button for each of the page type buttons.
        for (PageType buttonPageType: PageType.values()) {

            // Get navigation button and navigation marker from button page type ids.
            ImageButton navigationButton = activity.findViewById(buttonPageType.getButtonId());
            ImageView navigationMarker = activity.findViewById(buttonPageType.getMarkerId());

            // Only set the navigation if both are found valid.
            if (navigationButton == null || navigationMarker == null) {
                Log.w(TAG, "Navigation button or navigation marker " +
                        "was not found for page type " + pageType.toString());
                continue;
            }

            if (buttonPageType != pageType) {
                // Set button to navigate to other activities, be clickable and make marker invisible.
                navigationButton.setClickable(true);
                Navigate.configure(navigationButton, activity, buttonPageType.getNextPage(admin));
                navigationMarker.setVisibility(View.INVISIBLE);
            } else {
                // Set navigation button of current page to not clickable and make marker visible.
                navigationButton.setClickable(false);
                navigationMarker.setVisibility(View.VISIBLE);
            }
        }
    }

}
