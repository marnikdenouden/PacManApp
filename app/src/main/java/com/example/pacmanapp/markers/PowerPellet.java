package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.HintEdit;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.map.MapPosition;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectionCrier;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PowerPellet extends Marker implements Selectable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "PowerPellet";
    private final static int drawableId =  R.drawable.power_pellet;
    private final static int markerId = R.id.powerpellet;
    private final ContentContainer contentContainer;

    /**
     * PowerPellet marker to display on the map and use.
     *
     * @param latitude  latitude that the power pellet is placed at
     * @param longitude longitude that the power pellet is placed at
     */
    public PowerPellet(double latitude, double longitude) {
        super(latitude, longitude, drawableId, markerId);
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Information("Start information"));
        contentList.add(new HintEdit(this));
        contentList.add(new Information("More Information"));
        contentList.add(new HintEdit(this));
        contentList.add(new Information("Just Information"));
        contentList.add(new HintEdit(this));
        contentList.add(new Information("Additional Information"));
        contentList.add(new HintEdit(this));
        contentList.add(new Information("Final Information"));
        contentContainer = new ContentContainer(contentList);
    }

    @Override
    protected PowerPelletView createView(@NotNull MapArea mapArea) {
        PowerPelletView powerPelletView = new PowerPelletView(mapArea, this);
        powerPelletView.createView();
        return powerPelletView;
    }

    /**
     * Get the power pellet size for width and height.
     *
     * @return Pixel size for the width and height of the power pellet
     */
    private int getPowerPelletSize(@NotNull Context context) {
        return (int) context.getResources().getDimension(R.dimen.powerPelletSize);
    }

    @Override
    int getPixelWidth(@NotNull Context context) {
        return getPowerPelletSize(context);
    }

    @Override
    int getPixelHeight(@NotNull Context context) {
        return getPowerPelletSize(context);
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
        return contentContainer.addView(activity, viewGroup, editable);
    }

    @Override
    public List<Content> getContent(@NonNull AppCompatActivity activity, boolean editable) {
        return contentContainer.getContent(activity, editable);
    }

    @Override
    public void setContent(@Nullable List<Content> contentList) {
        contentContainer.setContent(contentList);
    }

    @Override
    public void addContent(@NonNull Content content) {
        contentContainer.addContent(content);
    }

    @Override
    public void removeContent(@NonNull Content content) {
        contentContainer.removeContent(content);
    }

    @SuppressLint("ViewConstructor")
    public static class PowerPelletView extends MarkerView implements Character.CharacterView.Visitable {
        private final String TAG = "PacDotView";
        private final PowerPellet powerPellet;
        protected PowerPelletView(@NonNull MapArea mapArea, @NonNull PowerPellet powerPellet) {
            super(mapArea, powerPellet);
            this.powerPellet = powerPellet;
        }

        @Override
        public void visit(@NotNull Character character) {
            Log.d(TAG, "Power pellet is being visited by character");
        }
    }
}
