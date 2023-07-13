package com.example.pacmanapp.selection;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;

public class SelectableContent {
    private final static String TAG = "SelectableContent";

    /**
     * Set all content of selectable.
     *
     * @param activity Activity that is active
     * @param selectable Selectable to set content for
     */
    public static void setContent(AppCompatActivity activity, Selectable selectable) {
        setIcon(activity, selectable);
        setLabel(activity, selectable);
        setDescription(activity, selectable);
    }

    /**
     * Set icon for the selectable.
     *
     * @param activity Activity that is active
     * @param selectable Selectable to set icon for
     */
    private static void setIcon(AppCompatActivity activity, Selectable selectable) {
        ImageView iconImageView = activity.findViewById(R.id.selectable_icon);
        if (iconImageView == null) {
            Log.w(TAG, "Could not set icon image for content of selectable "
                    + selectable.getLabel() + "activity of class " + activity.getLocalClassName());
            return;
        }
        Drawable iconImage = ResourcesCompat.getDrawable(activity.getResources(),
                selectable.getIconId(), activity.getTheme());
        iconImageView.setImageDrawable(iconImage);
    }

    /**
     * Set label for selectable.
     *
     * @param activity Activity that is active
     * @param selectable Selectable to set label for
     */
    private static void setLabel(AppCompatActivity activity, Selectable selectable) {
        TextView labelTextView = activity.findViewById(R.id.selectable_label);
        if (labelTextView == null) {
            Log.w(TAG, "Could not set label text for content of selectable "
                    + selectable.getLabel() + "activity of class " + activity.getLocalClassName());
            return;
        }
        labelTextView.setText(selectable.getLabel());
    }

    /**
     * Set description for selectable.
     *
     * @param activity Activity that is active
     * @param selectable Selectable to set description for
     */
    private static void setDescription(AppCompatActivity activity, Selectable selectable) {
        TextView descriptionTextView = activity.findViewById(R.id.selectable_description);
        if (descriptionTextView == null) {
            Log.w(TAG, "Could not set description text for content of selectable "
                    + selectable.getLabel() + "activity of class " + activity.getLocalClassName());
            return;
        }
        descriptionTextView.setText(selectable.getDescription());
    }
}
