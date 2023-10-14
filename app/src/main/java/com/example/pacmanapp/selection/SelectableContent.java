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
import com.example.pacmanapp.contents.ContentContainer;

import org.jetbrains.annotations.NotNull;

public class SelectableContent {
    private final static String TAG = "SelectableContent";

    /**
     * Add all data of selectable to the activity.
     *
     * @param activity Activity that is active
     * @param viewGroup ViewGroup that selectable data is contained in
     * @param selectable Selectable to set content for
     * @param editable Truth assignment, if selectable should be editable
     */
    public static void setData(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup,
                               @NotNull Selectable selectable, boolean editable) {
        setIcon(activity, viewGroup, selectable, editable);
        setLabel(activity, viewGroup, selectable);
        setDescription(activity, viewGroup, selectable);
        setContent(activity, viewGroup, selectable, editable);
    }

    /**
     * Set icon for the selectable.
     *
     * @param activity Activity that is active
     * @param viewGroup ViewGroup that selectable data is contained in
     * @param selectable Selectable to set icon for
     * @param editable Truth assignment, if selectable should be editable
     */
    private static void setIcon(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup,
                                @NotNull Selectable selectable, boolean editable) {
        ImageView iconImageView = viewGroup.findViewById(R.id.selectable_icon);
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
     * @param viewGroup ViewGroup that selectable data is contained in
     * @param selectable Selectable to set label for
     */
    private static void setLabel(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup,
                                 Selectable selectable) {
        TextView labelTextView = viewGroup.findViewById(R.id.selectable_label);
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
     * @param viewGroup ViewGroup that selectable data is contained in
     * @param selectable Selectable to set description for
     */
    private static void setDescription(@NotNull AppCompatActivity activity,
                                       @NotNull ViewGroup viewGroup,
                                       @NotNull Selectable selectable) {
        TextView descriptionTextView = viewGroup.findViewById(R.id.selectable_description);
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
     * Requires the content container to contain a linear layout with the selectable content id.
     *
     * @param activity Activity that is active
     * @param viewGroup ViewGroup that selectable data is contained in
     * @param contentContainer ContentContainer to set content from
     * @param editable Truth assignment, if content should be editable
     */
    public static void setContent(@NotNull AppCompatActivity activity,
                                   @NotNull ViewGroup viewGroup,
                                   @NotNull ContentContainer contentContainer, boolean editable) {
        LinearLayout linearLayoutContent = viewGroup.findViewById(R.id.selectable_content);
        if (linearLayoutContent == null) {
            Log.i(TAG, "No content layout found to add content to for content container "
                    + contentContainer.getClass().getSimpleName() + " activity of class "
                    + activity.getLocalClassName());
            return;
        }
        linearLayoutContent.removeAllViews();

        for (Content content: contentContainer.getContent()) {
            content.addView(activity, linearLayoutContent, editable);
        }
    }

    public static class Preview implements Content {
        private AppCompatActivity activity;
        private ViewGroup selectableView;
        private boolean editable;
        private final Selectable selectable;

        /**
         * Creates a preview for a selectable. Preview can be added as content view.
         *
         * @param selectable Selectable to use for the view data
         */
        public Preview(@NotNull Selectable selectable) {
            this.selectable = selectable;
        }

        @Override
        public View addView(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup,
                            boolean editable) {
            selectableView = (ViewGroup) LayoutInflater.from(activity)
                    .inflate(R.layout.selectable_preview, viewGroup, false);
            viewGroup.addView(selectableView);
            configure(activity, selectableView, editable);
            return selectableView;
        }

        /**
         * Configure the preview with data to use for updating.
         *
         * @param activity Activity that preview is placed in
         * @param selectableView GroupView that data fields are placed in
         * @param editable Truth assignment, if selectable may be edited
         */
        public void configure(@NotNull AppCompatActivity activity,
                              @NotNull ViewGroup selectableView, boolean editable) {
            this.activity = activity;
            this.selectableView = selectableView;
            this.editable = editable;
            update(selectable);
        }

        /**
         * Update the last view that was added with new selectable data.
         *
         * @pre Configure or addView has been called before
         * @param selectable Selectable to use for new data
         */
        public void update(@NotNull Selectable selectable) {
            if (selectableView == null || activity == null) {
                throw new IllegalStateException("Pre condition violated, could not update preview");
            }

            setDescription(activity, selectableView, selectable);
            setLabel(activity, selectableView, selectable);
            setIcon(activity, selectableView, selectable, editable);
        }

    }
}
