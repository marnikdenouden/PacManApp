package com.example.pacmanapp.navigation;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Navigate {
    private static final String TAG = "Navigation";

    /**
     * Navigates to a different activity.
     *
     * @param intent Intent for navigation that can be extended with extra values
     * @param currentActivity activity user is currently at
     * @param nextActivityClass Activity class to navigate to
     */
    public static void navigate(Intent intent, AppCompatActivity currentActivity,
                                Class<? extends AppCompatActivity> nextActivityClass) {
        Log.i(TAG, "User tries to navigate to " + nextActivityClass.getSimpleName());
        currentActivity.startActivity(intent);
    }

    /**
     * Sets the on click listener for navigation.
     *
     * @param navigator View that can be clicked on to initiate navigation action
     * @param intent Intent for navigation that can be extended with extra values
     * @param currentActivity activity user is currently at
     * @param nextActivityClass Activity class to navigate to
     */
    private static void setNavigationListener(View navigator, Intent intent, AppCompatActivity currentActivity,
                                              Class<? extends AppCompatActivity> nextActivityClass) {
        navigator.setOnClickListener(
                view -> navigate(intent, currentActivity, nextActivityClass)
        );
    }

    /**
     * Navigates to a different activity.
     *
     * @param currentActivity activity user is currently at
     * @param nextActivityClass Activity class to navigate to
     */
    public static void navigate(AppCompatActivity currentActivity,
                                Class<? extends AppCompatActivity> nextActivityClass) {
        Intent intent = new Intent(currentActivity, nextActivityClass);
        navigate(intent, currentActivity, nextActivityClass);
    }

    /**
     * Configures navigation for a view without passing user.
     *
     * @param navigator View that can be clicked on to initiate navigation action
     * @param currentActivity activity user is currently at
     * @param nextActivityClass Activity class to navigate to
     */
    public static void configure(View navigator, AppCompatActivity currentActivity,
                                 Class<? extends AppCompatActivity> nextActivityClass) {
        Intent intent = new Intent(currentActivity, nextActivityClass);
        configure(navigator, intent, currentActivity, nextActivityClass);
    }

    /**
     * Configures navigation for a view with self created intent without passing user.
     *
     * @param navigator View that can be clicked on to initiate navigation action
     * @param intent Intent for navigation that can be extended with extra values
     * @param currentActivity Activity user is currently at
     * @param nextActivityClass Activity class to navigate to
     */
    public static void configure(View navigator, Intent intent, AppCompatActivity currentActivity,
                                 Class<? extends AppCompatActivity> nextActivityClass) {
        setNavigationListener(navigator, intent, currentActivity, nextActivityClass);
    }

}
