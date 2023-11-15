package com.example.pacmanapp.selection.selectables;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.HintEdit;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.selection.Selectable;

public class MarioKartTrack extends ContentContainer implements Selectable {

    /**
     * Construct the mario kart track selectable
     */
    public MarioKartTrack() {
        addContent(new Information("Complete the track and figure out the current track positions."));
        addContent(new HintEdit(this));
    }

    @Override
    public String getLabel() {
        return "Mario Kart";
    }

    @Override
    public int getIconId() {
        return R.drawable.mario_kart_logo;
    }

    @Override
    public String getDescription() {
        return "Just when all mario characters entered their last round the track got separated, " +
                "please restore the current standings";
    }

}
