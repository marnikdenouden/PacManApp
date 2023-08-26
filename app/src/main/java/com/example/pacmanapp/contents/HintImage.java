package com.example.pacmanapp.contents;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.edit.EditActivity;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.storage.SavePlatform;

import org.jetbrains.annotations.NotNull;

public class HintImage implements Content {
    private static final String TAG = "HintImage";
    private final int iconId;
    private final String label;
    private final Selectable hintTarget;
    private final String imageId;
    private transient Bitmap image;

    /**
     * Constructor of hint that is used by the hint builder.
     *
     * @param hintBuilder Hint builder that has hint data
     */
    HintImage(@NotNull HintBuilder hintBuilder) {
        iconId = hintBuilder.hintTarget.getIconId();
        label = hintBuilder.hintTarget.getLabel();
        hintTarget = hintBuilder.hintTarget;
        imageId = hintBuilder.hintImageId;
    }

    @Override
    public View addView(@NotNull AppCompatActivity activity,
                        @NotNull ViewGroup viewGroup, boolean editable) {
        image = SavePlatform.getSave().getImageStorage().getImage(imageId);
        if (editable) {
            return addEditView(activity, viewGroup);
        } else {
            return addInfoView(activity, viewGroup);
        }
    }

    /**
     * Add the info hint view on the activity page to the specified view group.
     *
     * @param activity Activity that the view is being added in
     * @param viewGroup ViewGroup to add edit hint view to
     *
     * @return View view that was added to the specified view group
     */
    View addInfoView(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup) {
        View hintView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_hint_image, viewGroup, false);

        ImageView hintEditImageView = hintView.findViewById(R.id.hint_image);
        hintEditImageView.setImageBitmap(image);

        TextView labelTextView = hintView.findViewById(R.id.hint_label);
        labelTextView.setText(label);

        ImageView iconImageView = hintView.findViewById(R.id.hint_icon);
        Drawable iconImage = ResourcesCompat.getDrawable(hintView.getResources(), iconId,
                hintView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);
        iconImageView.setOnClickListener(view -> InspectActivity.open(activity, hintTarget));

        viewGroup.addView(hintView);
        return hintView;
    }

    /**
     * Add the edit hint view on the activity page to the specified view group.
     *
     * @param activity Activity that the view is being added in
     * @param viewGroup ViewGroup to add edit hint view to
     *
     * @return View view that was added to the specified view group
     */
    View addEditView(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup) {
        View hintView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_hint_image, viewGroup, false);

        ImageView hintEditImageView = hintView.findViewById(R.id.hint_image);
        hintEditImageView.setImageBitmap(image);

        TextView labelTextView = hintView.findViewById(R.id.hint_label);
        labelTextView.setText(label);

        ImageView iconImageView = hintView.findViewById(R.id.hint_icon);
        Drawable iconImage = ResourcesCompat.getDrawable(hintView.getResources(), iconId,
                hintView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);
        iconImageView.setOnClickListener(view -> EditActivity.open(activity, hintTarget));

        viewGroup.addView(hintView);
        return hintView;
    }
}
