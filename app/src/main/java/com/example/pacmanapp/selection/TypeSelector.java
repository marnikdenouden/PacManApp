package com.example.pacmanapp.selection;

import android.util.Log;

import com.example.pacmanapp.markers.BlankMarker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TypeSelector extends Selector {
    private final String TAG = "TypeSelector";
    private final Class<?> type;

    private TypeSelector(int id, @NotNull Selectable selectable, @NotNull Class<?> type) {
        super(selectable);
        this.type = type;
        SelectionCrier.getInstance().addSelector(id, this);
    }

    /**
     * Check if type selector has a selector stored.
     *
     * @return Truth assignment, if selector has selectable stored.
     */
    public boolean hasSelected() {
        return (type.isInstance(getSelected()));
    }

    @Override
    void select(@Nullable Selectable selectable) {
        if (type.isInstance(selectable)) {
            Log.d(TAG, "Successfully selected a selectable with type " + type.getSimpleName());
            super.select(selectable);
        }
    }

    /**
     * get the type selector for the specified id.
     *
     * @param id Id to get accept all selector for
     * @param selectable Selectable that will be selected as default
     * @param type Type of the class to select, used if creating new selector for the specified id
     * @return TypeSelector for specified id
     */
    public static TypeSelector getSelector(int id, @NotNull Selectable selectable, @NotNull Class<?> type) {
        if (SelectionCrier.getInstance().hasSelector(id)) {
            return (TypeSelector) SelectionCrier.getInstance().getSelector(id);
        }
        return new TypeSelector(id, selectable, type);
    }

}
