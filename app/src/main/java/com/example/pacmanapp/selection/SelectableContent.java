package com.example.pacmanapp.selection;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.edit.EditActivity;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.contents.Content;

import org.jetbrains.annotations.NotNull;

public class SelectableContent {
    private final static String TAG = "SelectableContent";

    /**
     * Add all data of selectable to the activity.
     *
     * @param activity Activity that is active
     * @param selectable Selectable to set content for
     */
    public static void setData(AppCompatActivity activity, Selectable selectable, boolean editable) {
        setIcon(activity, selectable, editable);
        setLabel(activity, selectable);
        setDescription(activity, selectable);
        setContent(activity, selectable, editable);
    }

    /**
     * Set icon for the selectable.
     *
     * @param activity Activity that is active
     * @param selectable Selectable to set icon for
     * @param editable Truth assignment, if selectable should be editable
     */
    private static void setIcon(AppCompatActivity activity, Selectable selectable, boolean editable) {
        ImageView iconImageView = activity.findViewById(R.id.selectable_icon);
        if (iconImageView == null) {
            Log.i(TAG, "No icon image found to set for content of selectable "
                    + selectable.getLabel() + " activity of class " + activity.getLocalClassName());
            return;
        }

        Drawable iconImage = ResourcesCompat.getDrawable(activity.getResources(),
                selectable.getIconId(), activity.getTheme());
        iconImageView.setImageDrawable(iconImage);
        if (editable) {
            iconImageView.setOnClickListener(view ->
                    EditActivity.open(activity, selectable));
        } else {
            iconImageView.setOnClickListener(view ->
                    InspectActivity.open(activity, selectable));
        }
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

    /**
     * Set content for selectable.
     *
     * @param activity Activity that is active
     * @param selectable Selectable to set content for
     * @param editable Truth assignment, if content should be editable
     */
    private static void setContent(AppCompatActivity activity, Selectable selectable, boolean editable) {
        LinearLayout linearLayoutContent = activity.findViewById(R.id.selectable_content);
        if (linearLayoutContent == null) {
            Log.i(TAG, "No content container found to add content to for selectable "
                    + selectable.getLabel() + " activity of class " + activity.getLocalClassName());
            return;
        }
        linearLayoutContent.removeAllViews();

        for (Content content: selectable.getContent()) {
            content.addView(activity, linearLayoutContent, editable);
        }
    }

    public static class Preview implements Content {
        private AppCompatActivity activity;
        private boolean editable;
        private final Selectable selectable;
        // TODO add java doc comments.
        public Preview(@NotNull Selectable selectable) {
            this.selectable = selectable;
        }

        @Override
        public View addView(@NonNull AppCompatActivity activity,
                            @NonNull ViewGroup viewGroup, boolean editable) {
            this.activity = activity;
            this.editable = editable;
            View view = LayoutInflater.from(activity).inflate(R.layout.selectable_preview, viewGroup);
            update(selectable);
            return view;
        }

        public void update(Selectable selectable) {
            setDescription(activity, selectable);
            setLabel(activity, selectable);
            setIcon(activity, selectable, editable);
        }
    }
}
