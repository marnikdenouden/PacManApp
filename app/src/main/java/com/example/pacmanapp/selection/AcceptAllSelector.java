package com.example.pacmanapp.selection;

import androidx.annotation.NonNull;

import com.example.pacmanapp.activities.InfoSelectableActivity;

import org.jetbrains.annotations.NotNull;

public class AcceptAllSelector implements Selector {
    private static AcceptAllSelector acceptAllSelector;
    private Selectable selected;

    AcceptAllSelector() {
        selected = new InfoSelectableActivity();
    }

    @Override
    public @NonNull Selectable getSelected() {
        return selected;
    }

    @Override
    public void select(@NotNull Selectable selected) {
        this.selected = selected;
    }

    /**
     * Get selector.
     *
     * @return instance of the selector
     */
    public static AcceptAllSelector getSelector() {
        if (acceptAllSelector == null) {
            acceptAllSelector = new AcceptAllSelector();
        }
        return acceptAllSelector;
    }
}
