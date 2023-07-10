package com.example.pacmanapp.selection;

import org.jetbrains.annotations.NotNull;

public abstract class Selector {
    private Selectable selected;

    Selector() {

    }

    /**
     * Return the selected selectable.
     *
     * @return Selectable that is selected.
     */
    public @NotNull Selectable getSelected() {
        return selected;
    }

    /**
     * Select a selectable.
     *
     * @param selected Selectable to select
     */
    void select(@NotNull Selectable selected) {
        this.selected = selected;
    }

}
