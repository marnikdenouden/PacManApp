package com.example.pacmanapp.selection;

import com.example.pacmanapp.activities.InfoSelectableActivity;

public class AcceptAllSelector extends Selector {
    private AcceptAllSelector(int id) {
        select(new InfoSelectableActivity());
        SelectionCrier.getInstance().addSelector(id, this);
    }

    public static AcceptAllSelector getAcceptAllSelector(int id) {
        if (SelectionCrier.getInstance().hasSelector(id)) {
            return (AcceptAllSelector) SelectionCrier.getInstance().getSelector(id);
        }
        return new AcceptAllSelector(id);
    }

}
