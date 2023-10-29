package com.example.pacmanapp.selection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public abstract class Selector {
    private Selectable selected;
    private final Selectable blankSelected;
    Collection<SelectionListener> listeners;

    Selector(@NotNull Selectable blankSelected) {
        this.blankSelected = blankSelected;
        selected = blankSelected;
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
     * @param selected Selectable to select or null to remove selection
     */
    void select(@Nullable Selectable selected) {
        if (selected == null) {
            this.selected = blankSelected;
        } else {
            this.selected = selected;
        }
        for (SelectionListener listener: listeners) {
            listener.onSelect(this.selected);
        }
    }

    /**
     * Clear selected from the selector, which will be replaced by default selected.
     */
    void clearSelected() {
        select(null);
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
        void onSelect(Selectable selectable);
    }
}

