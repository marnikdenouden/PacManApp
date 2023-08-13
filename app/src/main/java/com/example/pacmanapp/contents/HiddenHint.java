package com.example.pacmanapp.contents;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

public class HiddenHint implements Content {

    private boolean locked = true;
    private final String key;
    private final Hint hint;
    private final Selectable cluePage;

    // TODO add java docs...
    public HiddenHint(Hint hint, Selectable cluePage, String key) {
        this.hint = hint;
        this.cluePage = cluePage;
        this.key = key;
    }

    @Override
    public void addView(@NonNull ViewGroup viewGroup, boolean editable) {
        if (editable) {
            hint.addEditView(viewGroup);
            return;
        }

        if (!locked) {
            hint.addInfoView(viewGroup);
            return;
        }

        addLockView(viewGroup);
    }

    void addLockView(@NotNull ViewGroup viewGroup) {
        View hintView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lock_content, viewGroup);

        EditText keyTextView = hintView.findViewById(R.id.key_input);
        keyTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (key.equals(editable.toString())) {
                    locked = false;
                    viewGroup.removeView(hintView);
                    hint.addInfoView(viewGroup);
                    keyTextView.setEnabled(false); // TODO could use some more juice
                }
            }
        });

        ImageView iconImageView = hintView.findViewById(R.id.lock_icon); // TODO icon should be clickable to where the clue for the lock is located.
        Drawable iconImage = ResourcesCompat.getDrawable(hintView.getResources(),
                cluePage.getIconId(), hintView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);
    }

}
