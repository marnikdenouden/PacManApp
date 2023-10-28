package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class PowerPellet extends Marker implements Selectable, Character.Visitable {
    private static final long serialVersionUID = 1L;
    private final static int drawableId =  R.drawable.power_pellet;
    private final static int markerId = R.id.powerpellet;
    private final ContentContainer content;

    /**
     * PowerPellet marker to display on the map and use.
     *
     * @param frameId   FrameId reference to map area that the marker is placed on
     * @param latitude  latitude that the power pellet is placed at
     * @param longitude longitude that the power pellet is placed at
     * @param context   Context that the power pellet is created in
     */
    public PowerPellet(int frameId, double latitude, double longitude, Context context) {
        super(frameId, latitude, longitude, drawableId, markerId, context);
        List<Content> contentList = new ArrayList<>();
        content = new ContentContainer(contentList);
    }

    /**
     * Get the power pellet size for width and height.
     *
     * @return Pixel size for the width and height of the power pellet
     */
    private int getPowerPelletSize() {
        return (int) getContext().getResources().getDimension(R.dimen.powerPelletSize);
    }

    @Override
    int getPixelWidth() {
        return getPowerPelletSize();
    }

    @Override
    int getPixelHeight() {
        return getPowerPelletSize();
    }

    @Override
    public String getLabel() {
        return "Power pellet";
    }

    @Override
    public int getIconId() {
        return R.drawable.power_pellet;
    }

    @Override
    public String getDescription() {
        return "Power pellets allow pacman to eat ghosts for a short duration when collected.";
    }

    @Override
    public View addView(@NonNull AppCompatActivity activity, @NonNull ViewGroup viewGroup, boolean editable) {
        return content.addView(activity, viewGroup, editable);
    }

    @Override
    public List<Content> getContent(@NonNull AppCompatActivity activity, boolean editable) {
        return content.getContent(activity, editable);
    }

    @Override
    public void visit(@NotNull Character character, @NotNull Location location) {
        // TODO implement method
    }
}
