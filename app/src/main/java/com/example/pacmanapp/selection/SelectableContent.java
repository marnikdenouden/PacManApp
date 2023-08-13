package com.example.pacmanapp.selection;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;

public class SelectableContent {
    private final static String TAG = "SelectableContent";

    /**
     * Add all data of selectable to the activity.
     *
     * @param activity Activity that is active
     * @param selectable Selectable to set content for
     */
    public static void setData(AppCompatActivity activity, Selectable selectable, boolean editable) {
        setIcon(activity, selectable);
        setLabel(activity, selectable);
        setDescription(activity, selectable);
        setContent(activity, selectable, editable);
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
            Log.i(TAG, "No icon image found to set for content of selectable "
                    + selectable.getLabel() + " activity of class " + activity.getLocalClassName());
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
            Log.i(TAG, "No label text found to set for content of selectable "
                    + selectable.getLabel() + " activity of class " + activity.getLocalClassName());
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
            Log.i(TAG, "No description text found to set for content of selectable "
                    + selectable.getLabel() + " activity of class " + activity.getLocalClassName());
            return;
        }

        descriptionTextView.setText(selectable.getDescription());
    }

    private static void setContent(AppCompatActivity activity, Selectable selectable, boolean editable) {
        LinearLayout linearLayoutContent = activity.findViewById(R.id.selectable_content);
        if (linearLayoutContent == null) {
            Log.i(TAG, "No content container found to add content to for selectable "
                    + selectable.getLabel() + " activity of class " + activity.getLocalClassName());
            return;
        }

        for (Content content: selectable.getContent()) {
            content.addView(linearLayoutContent, editable);
        }
    }
}
