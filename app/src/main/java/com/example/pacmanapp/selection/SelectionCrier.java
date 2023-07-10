package com.example.pacmanapp.selection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SelectionCrier {
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
    public Selector getSelector(int id) {
        return selectors.get(id);
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
