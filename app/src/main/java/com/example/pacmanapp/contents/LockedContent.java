package com.example.pacmanapp.contents;

import android.graphics.drawable.Drawable;
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

public class LockedContent implements Content {

    private boolean locked = true;
    private final String key;
    private final Content content;
    private final Selectable hintProvider;

    /**
     * Constructs a lock view that reveals content after key is entered.
     *
     * @param content Content that will be revealed after entering the key
     * @param hintProvider Selectable that displays the clues for the key
     * @param key Key that will unlock the hint
     */
    public LockedContent(Content content, Selectable hintProvider, String key) {
        this.content = content;
        this.hintProvider = hintProvider;
        this.key = key;
    }

    @Override
    public View addView(@NotNull AppCompatActivity activity,
                        @NonNull ViewGroup viewGroup, boolean editable) {
        if (editable) {
            return content.addView(activity, viewGroup, true);
        }

        if (!locked) {
            return content.addView(activity, viewGroup, false);
        }

        return addLockView(activity, viewGroup);
    }

    /**
     * Add the lock hint view on the activity page to the specified view group.
     *
     * @param activity Activity that the view is being added in
     * @param viewGroup ViewGroup to add edit hint view to
     *
     * @return View view that was added to the specified view group
     */
    View addLockView(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup) {
        View lockView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_lock, viewGroup, false);

        EditText keyTextView = lockView.findViewById(R.id.key_input);
        Util.TextListener textListener = (String text) -> {
            if (key.equals(text)) {
                locked = false;
                viewGroup.removeView(lockView);
                content.addView(activity, viewGroup, false);
                keyTextView.setEnabled(false); // TODO could use some more juice
                keyTextView.getRootView().clearFocus();
            }
        };
        Util.configureEditText(keyTextView, "", textListener);

        ImageView iconImageView = lockView.findViewById(R.id.lock_icon);
        Drawable iconImage = ResourcesCompat.getDrawable(lockView.getResources(),
                hintProvider.getIconId(), lockView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);
        iconImageView.setOnClickListener(view -> InspectActivity.open(activity, hintProvider));

        viewGroup.addView(lockView);
        return lockView;
    }

}
