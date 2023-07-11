package com.example.pacmanapp.selection;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public abstract class Selector {
    private Selectable selected;
    Collection<SelectionListener> listeners;

    Selector() {
        listeners = new HashSet<>();
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
        for (SelectionListener listener: listeners) {
            listener.onSelection(selected);
        }
    }

    /**
     * Set selection listener for this selector.
     *
     * @param selectionListener Selection listener to set
     */
    public void addOnSelectionListener(@NotNull SelectionListener selectionListener) {
        listeners.add(selectionListener);
    }

    /**
     * Removes selection listener for this selector.
     *
     * @param selectionListener Selection listener to remove
     */
    public void removeOnSelectionListener(@NotNull SelectionListener selectionListener) {
        listeners.remove(selectionListener);
    }

    public interface SelectionListener {
        void onSelection(Selectable selectable);
    }
}

