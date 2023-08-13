package com.example.pacmanapp.contents;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.Marker;
import com.example.pacmanapp.selection.Selectable;

public class Hint implements Content {
    private static final String TAG = "ContentHint";
    private final int iconId;
    private final String label;
    private String hint;

    public Hint(HintBuilder hintBuilder) {
        iconId = hintBuilder.iconId;
        label = hintBuilder.label;
        hint = hintBuilder.hint;
    }

    @Override
    public void addView(@NonNull ViewGroup viewGroup, boolean editable) {
        if (editable) {
            addEditView(viewGroup);
        } else {
            addInfoView(viewGroup);
        }
    }

    /**
     * Set the hint text on the activity page.
     *
     * @param viewGroup ViewGroup to add edit hint view to
     */
    void addInfoView(ViewGroup viewGroup) {
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
    }

    /**
     * Set the hint text on the activity page.
     *
     * @param viewGroup ViewGroup to add edit hint view to
     */
    void addEditView(ViewGroup viewGroup) {
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
    }

    public static class HintBuilder {
        private String hint = "";
        private String label;
        private int iconId;

        public HintBuilder(String label, int iconId) {
            // Add any required values for hint in the hint builder parameter.
        }

        // TODO Should hints only be constructed with selectable, so icon can be clickable and take you to that page?

        public HintBuilder(Selectable selectable) {
            iconId = selectable.getIconId();
            label = selectable.getLabel();
        }

        public HintBuilder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public Hint build() {
            return new Hint(this);
        }
    }

}
