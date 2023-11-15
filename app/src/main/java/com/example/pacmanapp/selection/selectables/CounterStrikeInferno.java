package com.example.pacmanapp.selection.selectables;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.HintEdit;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.selection.Selectable;

public class CounterStrikeInferno extends ContentContainer implements Selectable {

    /**
     * Construct the counter strike inferno map selectable
     */
    public CounterStrikeInferno() {
        addContent(new Information("Figure out the result of the match"));
        addContent(new HintEdit(this));
    }

    @Override
    public String getLabel() {
        return "Mario Kart";
    }

    @Override
    public int getIconId() {
        return R.drawable.counterstrike_logo;
    }

    @Override
    public String getDescription() {
        return "";
    }

}
