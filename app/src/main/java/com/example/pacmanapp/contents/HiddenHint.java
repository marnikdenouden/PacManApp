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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

public class HiddenHint implements Content {

    private boolean locked = true;
    private final String key;
    private final Hint hint;
    private final Selectable hintProvider;

    /**
     * Constructs a hidden hint content that reveals hint after key is entered.
     *
     * @param hint Hint that will be revealed after entering the key
     * @param hintProvider Selectable that displays the clues for the key
     * @param key Key that will unlock the hint
     */
    public HiddenHint(Hint hint, Selectable hintProvider, String key) {
        this.hint = hint;
        this.hintProvider = hintProvider;
        this.key = key;
    }

    @Override
    public void addView(@NotNull AppCompatActivity activity,
                        @NonNull ViewGroup viewGroup, boolean editable) {
        if (editable) {
            hint.addEditView(activity, viewGroup);
            return;
        }

        if (!locked) {
            hint.addInfoView(activity, viewGroup);
            return;
        }

        addLockView(activity, viewGroup);
    }

    /**
     * Add the lock hint view on the activity page to the specified view group.
     *
     * @param activity Activity that the view is being added in
     * @param viewGroup ViewGroup to add edit hint view to
     */
    void addLockView(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup) {
        View hintView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lock_content, viewGroup, false);

        EditText keyTextView = hintView.findViewById(R.id.key_input);
        Util.TextListener textListener = (String text) -> {
            if (key.equals(text)) {
                locked = false;
                viewGroup.removeView(hintView);
                hint.addInfoView(activity, viewGroup);
                keyTextView.setEnabled(false); // TODO could use some more juice
                keyTextView.getRootView().clearFocus();
            }
        };
        Util.configureEditText(keyTextView, "", textListener);

        ImageView iconImageView = hintView.findViewById(R.id.lock_icon);
        Drawable iconImage = ResourcesCompat.getDrawable(hintView.getResources(),
                hintProvider.getIconId(), hintView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);
        iconImageView.setOnClickListener(view -> InspectActivity.open(activity, hintProvider));

        viewGroup.addView(hintView);
    }

}
