package com.example.pacmanapp.selection;

import org.jetbrains.annotations.NotNull;

public class NextSelectionSelector extends Selector {
    private NextSelectionSelector(int id, Selectable selectable) {
        select(selectable);
        SelectionCrier.getInstance().addSelector(id, this);
    }

    @Override
    void select(@NotNull Selectable selected) {
        super.select(selected);
        listeners.clear();
    }

    /**
     * Get the next selection selector for the specified id.
     *
     * @param id Id to get next selection selector for
     * @param selectable Selectable that will be selected as default
     * @return NextSelectionSelector for specified id
     */
    public static NextSelectionSelector getNextSelectionSelector(int id, Selectable selectable) {
        if (SelectionCrier.getInstance().hasSelector(id)) {
            return (NextSelectionSelector) SelectionCrier.getInstance().getSelector(id);
        }
        return new NextSelectionSelector(id, selectable);
    }
}
