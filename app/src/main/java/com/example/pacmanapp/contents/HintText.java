package com.example.pacmanapp.contents;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.edit.EditActivity;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

public class HintText implements Content {
    private static final String TAG = "HintText";
    private final int iconId;
    private final String label;
    private final Selectable hintTarget;
    private String hint;

    /**
     * Constructor of hint that is used by the hint builder.
     *
     * @param hintBuilder Hint builder that has hint data
     */
    HintText(@NotNull HintBuilder hintBuilder) {
        iconId = hintBuilder.hintTarget.getIconId();
        label = hintBuilder.hintTarget.getLabel();
        hintTarget = hintBuilder.hintTarget;
        hint = hintBuilder.hintText;
    }

    @Override
    public View addView(@NotNull AppCompatActivity activity,
                        @NotNull ViewGroup viewGroup, boolean editable) {
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
                .inflate(R.layout.content_hint_text, viewGroup, false);

        TextView hintInfoTextView = hintView.findViewById(R.id.hint_text);
        hintInfoTextView.setText(hint);

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
                .inflate(R.layout.content_hint_text_edit, viewGroup, false);

        EditText hintEditTextView = hintView.findViewById(R.id.hint_text);
        Util.TextListener textListener = (String text) -> hint = text;
        Util.configureEditText(hintEditTextView, hint, textListener);

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
