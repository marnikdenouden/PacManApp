package com.example.pacmanapp.selection;

import com.example.pacmanapp.activities.InfoSelectableActivity;

public class AcceptAllSelector extends Selector {
    public AcceptAllSelector(int id) {
        select(new InfoSelectableActivity());
        SelectionCrier.getInstance().addSelector(id, this);
    }

}
