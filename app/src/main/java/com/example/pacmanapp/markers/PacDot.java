package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.Hint;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.selection.Selectable;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class PacDot extends Marker implements Selectable {
    private final static String TAG = "PacDot";
    private static final long serialVersionUID = 1L;
    private final static int drawableId = R.drawable.pac_dot;
    private final static int markerId = R.id.pacdot;
    private final List<Content> contentList;

    /**
     * Pac-dot marker to display on the map and use.
     *
     * @param frameId   FrameId reference to map area that the pac-dot is placed on
     * @param latitude  latitude that the pac-dot is placed at
     * @param longitude longitude that the pac-dot is placed at
     * @param context   Context that the pac-dot is created in
     */
    public PacDot(int frameId, double latitude, double longitude, Context context) {
        super(frameId, latitude, longitude, drawableId, markerId, context);
        contentList = new ArrayList<>();
        contentList.add(new Information("Location hints"));
        contentList.add(new Hint.HintBuilder(this).build());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    public String getLabel() {
        return "Pac-Dot";
    }

    @Override
    public int getIconId() {
        return R.drawable.pac_dot;
    }

    @Override
    public String getDescription() {
        return "Pac-Dot can be found using the hint they contain. " +
                "When found they provide a hint to the location of a fruit.";
    }

    @Override
    public List<Content> getContent() {
        return contentList;
    }
}
