package com.example.pacmanapp.contents;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

public class SelectableButtonContent extends ButtonContent {
    private boolean selected = false;
    private View view;
    private int selectedColor;
    private int notSelectedColor;

    /**
     * Create a selectable button content.
     *
     * @param label String used on button as text
     * @param onButtonClick Runnable that is run on button click
     */
    public SelectableButtonContent(@NotNull String label, Runnable onButtonClick) {
        super(label, onButtonClick);
    }

    @Override
    public View addView(@NonNull AppCompatActivity activity, @NonNull ViewGroup viewGroup,
                        boolean editable) {
        view = super.addView(activity, viewGroup, editable);
        selectedColor = ResourcesCompat.getColor(activity.getResources(), R.color.cyan_base,
                activity.getTheme());
        notSelectedColor = ResourcesCompat.getColor(activity.getResources(), R.color.surface,
                activity.getTheme());
        updateDisplaySelected(selected);
        return view;
    }

    /**
     * Set the view to display as selected.
     *
     * @param selected Truth assignment, if view should display selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (view != null) {
            updateDisplaySelected(selected);
        }
    }

    /**
     * Update the display for the specified selected truth assignment.
     *
     * @param selected Truth assignment, if the display should show being selected.
     */
    private void updateDisplaySelected(boolean selected) {
        if (selected) {
            view.setBackgroundColor(selectedColor);
        } else {
            view.setBackgroundColor(notSelectedColor);
        }
    }
}
