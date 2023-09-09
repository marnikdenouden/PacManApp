package com.example.pacmanapp.contents;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.displays.Score;
import com.example.pacmanapp.markers.PacDot;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.selectables.Fruit;
import com.example.pacmanapp.storage.SavePlatform;

import org.jetbrains.annotations.NotNull;

public class LockedContent implements Content {

    private boolean locked = true;
    private final String key;
    private final Content content;
    private final Selectable hintProvider;

    // Set in add lock view
    private EditText keyTextView;
    private AppCompatActivity activity;
    private ViewGroup viewGroup;
    private View lockView;
    private String keyInput = "";

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
        this.activity = activity;
        this.viewGroup = viewGroup;

        lockView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_lock, viewGroup, false);

        keyTextView = lockView.findViewById(R.id.key_input);
        Util.TextListener textListener = (String text) -> {
            keyInput = text;
        };
        Util.configureEditText(keyTextView, "", textListener);

        ImageView iconImageView = lockView.findViewById(R.id.lock_icon);
        Drawable iconImage = ResourcesCompat.getDrawable(lockView.getResources(),
                hintProvider.getIconId(), lockView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);
        iconImageView.setOnClickListener(view -> InspectActivity.open(activity, hintProvider));

        ImageView keyButton = lockView.findViewById(R.id.key_button);
        keyButton.setOnClickListener(view -> checkKey());

        viewGroup.addView(lockView);
        return lockView;
    }

    /**
     * Check the last key input for being the correct key.
     *
     * @pre add lock view was called
     */
    public void checkKey() {
        if (key.equals(keyInput)) {
            locked = false;
            viewGroup.removeView(lockView);
            content.addView(activity, viewGroup, false);
            keyTextView.setEnabled(false); // TODO could use some more juice
            int pointsToAdd = 0;
            if (hintProvider instanceof PacDot) {
                pointsToAdd = 50;
            }
            if (hintProvider instanceof Fruit) {
                Fruit fruit = (Fruit) hintProvider;
                pointsToAdd = fruit.getFruitType().getPoints();
            }
            new Score(SavePlatform.getSave()).addValue(pointsToAdd);
            keyTextView.getRootView().clearFocus();
            SavePlatform.save(); // TODO Check the places where save should be called.
        } else {
            new Score(SavePlatform.getSave()).addValue(-10);
            Toast.makeText(activity, "Incorrect key, reducing your score now", Toast.LENGTH_SHORT).show();
        }
    }

}
