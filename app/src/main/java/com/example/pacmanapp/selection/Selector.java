package com.example.pacmanapp.selection;

import org.jetbrains.annotations.NotNull;

public interface Selector {

    /**
     * Return the selected selectable.
     *
     * @return Selectable that is selected.
     */
    public @NotNull Selectable getSelected();

    /**
     * Select a selectable.
     *
     * @param selected Selectable to select
     */
    public void select(@NotNull Selectable selected);
}
