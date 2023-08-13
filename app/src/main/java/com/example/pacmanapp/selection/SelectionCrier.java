package com.example.pacmanapp.selection;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class SelectionCrier {
    private static final String TAG = "SelectionCrier";
    Map<Integer, Selector> selectors;
    private static SelectionCrier instance;

    private SelectionCrier() {
        selectors = new HashMap<>();
    }

    /**
     * Give a select call to the crier.
     *
     * @param selectable Selectable to notify selectors of
     */
    public void select(Selectable selectable) {
        Log.i(TAG, "User made selection with label " + selectable.getLabel());
        for(Selector selector: selectors.values()) {
            selector.select(selectable);
        }
    }

    /**
     * Add selector to the list of selectors to notify.
     *
     * @param selector selector to be notified
     */
    void addSelector(int id, Selector selector) {
        selectors.put(id, selector);
    }

    /**
     * Get the selector with the specified id.
     *
     * @param id Id of the selector to get
     * @return Selector that is mapped from the specified id
     */
    Selector getSelector(int id) {
        return selectors.get(id);
    }

    /**
     * Check if the selector with the specified id is stored.
     *
     * @param id Id of the selector to check
     * @return Truth assignment, if selector with id is stored
     */
    boolean hasSelector(int id) {
        return selectors.containsKey(id);
    }

    /**
     * Get selection crier instance.
     *
     * @return instance of the selection crier
     */
    public static SelectionCrier getInstance() {
        if (instance == null) {
            instance = new SelectionCrier();
        }
        return instance;
    }
}
