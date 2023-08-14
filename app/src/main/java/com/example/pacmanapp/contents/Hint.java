package com.example.pacmanapp.contents;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
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

public class Hint implements Content {
    private static final String TAG = "ContentHint";
    private final int iconId;
    private final String label;
    private final Selectable selectable;
    private String hint;

    /**
     * Constructor of hint that is used by the hint builder.
     *
     * @param hintBuilder Hint builder that has hint data
     */
    private Hint(HintBuilder hintBuilder) {
        iconId = hintBuilder.selectable.getIconId();
        label = hintBuilder.selectable.getLabel();
        selectable = hintBuilder.selectable;
        hint = hintBuilder.hint;
    }

    @Override
    public void addView(@NotNull AppCompatActivity activity,
                        @NotNull ViewGroup viewGroup, boolean editable) {
        if (editable) {
            addEditView(activity, viewGroup);
        } else {
            addInfoView(activity, viewGroup);
        }
    }

    /**
     * Add the info hint view on the activity page to the specified view group.
     *
     * @param activity Activity that the view is being added in
     * @param viewGroup ViewGroup to add edit hint view to
     */
    void addInfoView(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup) {
        View hintView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hint_text, viewGroup);

        TextView hintInfoTextView = hintView.findViewById(R.id.hint_text);
        hintInfoTextView.setText(hint);

        TextView labelTextView = hintView.findViewById(R.id.hint_label);
        labelTextView.setText(label);

        ImageView iconImageView = hintView.findViewById(R.id.hint_icon);
        Drawable iconImage = ResourcesCompat.getDrawable(hintView.getResources(), iconId,
                hintView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);
        iconImageView.setOnClickListener(view -> InspectActivity.open(selectable, activity));
    }

    /**
     * Add the edit hint view on the activity page to the specified view group.
     *
     * @param activity Activity that the view is being added in
     * @param viewGroup ViewGroup to add edit hint view to
     */
    void addEditView(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup) {
        View hintView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hint_text_edit, viewGroup);

        EditText hintEditTextView = hintView.findViewById(R.id.hint_text);
        hintEditTextView.setText(hint);
        hintEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                hint = editable.toString();
            }
        });

        TextView labelTextView = hintView.findViewById(R.id.hint_label);
        labelTextView.setText(label);

        ImageView iconImageView = hintView.findViewById(R.id.hint_icon);
        Drawable iconImage = ResourcesCompat.getDrawable(hintView.getResources(), iconId,
                hintView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);
        iconImageView.setOnClickListener(view -> EditActivity.open(selectable, activity));
    }

    public static class HintBuilder {
        private String hint = "";
        private final Selectable selectable;

        // TODO Should hints only be constructed with selectable, so icon can be clickable and take you to that page?

        /**
         * Hint builder that can be created with a selectable.
         *
         * @param selectable Selectable to create hint for.
         */
        public HintBuilder(Selectable selectable) {
            this.selectable = selectable;
        }

        /**
         * Add a hint string to the hint builder.
         *
         * @param hint Hint that will be added to hint builder
         * @return HintBuilder that be continued
         */
        public HintBuilder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        /**
         * Finish hint building and get the created hint.
         *
         * @return Hint that was created by the hint builder
         */
        public Hint build() {
            return new Hint(this);
        }
    }

}
