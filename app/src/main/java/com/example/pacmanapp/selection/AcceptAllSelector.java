package com.example.pacmanapp.selection;

public class AcceptAllSelector extends Selector {
    private AcceptAllSelector(int id, Selectable selectable) {
        select(selectable);
        SelectionCrier.getInstance().addSelector(id, this);
    }

    /**
     * get the accept all selector for the specified id.
     *
     * @param id Id to get accept all selector for
     * @param selectable Selectable that will be selected as default
     * @return AcceptAllSelector for specified id
     */
    public static AcceptAllSelector getAcceptAllSelector(int id, Selectable selectable) {
        if (SelectionCrier.getInstance().hasSelector(id)) {
            return (AcceptAllSelector) SelectionCrier.getInstance().getSelector(id);
        }
        return new AcceptAllSelector(id, selectable);
    }

}
