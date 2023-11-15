package com.example.pacmanapp.selection.selectables;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.HintEdit;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.selection.Selectable;

public class PacManArcade extends ContentContainer implements Selectable {

    public PacManArcade() {
        addContent(new Information("Points for this arcade:\n" +
                                        "Pac-Dot = 10 Pts\n" +
                                        "Cherry = 100 Pts\n" +
                                        "Strawberry = 300 Pts\n" +
                                        "Orange = 500 Pts\n" +
                                        "Apple = 700 Pts\n" +
                                        "Melon = 1000 Pts\n" +
                                        "Galaxian = 2000 Pts\n" +
                                        "Bell = 3000 Pts\n" +
                                        "Key = 5000 Pts"));
        addContent(new HintEdit(this));
    }

    @Override
    public String getLabel() {
        return "Pac Man Arcade";
    }

    @Override
    public int getIconId() {
        return R.drawable.pacman_marker_frame_1;
    }

    @Override
    public String getDescription() {
        return "In this arcade Pac-Man can score points by collecting everything on its path.";
    }
}
