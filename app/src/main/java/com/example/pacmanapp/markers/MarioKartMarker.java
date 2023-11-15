package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.general.Option;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.HintEdit;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MarioKartMarker extends Marker implements Selectable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "MarioKartMarker";
    private final static int drawableId = R.drawable.pac_dot;
    private final static int markerId = R.id.mariokart;
    private final ContentContainer contentContainer;

    /**
     * Pac-dot marker to display on the map and use.
     *
     * @param latitude  latitude that the pac-dot is placed at
     * @param longitude longitude that the pac-dot is placed at
     */
    public MarioKartMarker(double latitude, double longitude, MarioKartItem marioKartItem) {
        super(latitude, longitude, drawableId, markerId);
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Information("Hint to key"));
        contentList.add(new HintEdit(this));
        contentContainer = new ContentContainer(contentList);
    }

    @Override
    public String getLabel() {
        return "Mario-Kart";
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
    protected MarioKartView createView(@NotNull MapArea mapArea) {
        MarioKartView pacDotView = new MarioKartView(mapArea, this);
        pacDotView.createView();
        return pacDotView;
    }

    @Override
    public View addView(@NonNull AppCompatActivity activity, @NonNull ViewGroup viewGroup,
                        boolean editable) {
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
    public static class MarioKartView extends MarkerView implements Character.CharacterView.Visitable {
        private final String TAG = "MarioKartView";
        private final MarioKartMarker marioKartMarker;
        protected MarioKartView(@NonNull MapArea mapArea, @NonNull MarioKartMarker marioKartMarker) {
            super(mapArea, marioKartMarker);
            this.marioKartMarker = marioKartMarker;
        }

        @Override
        public void visit(@NotNull Character character) {
            Log.d(TAG, "Pac Dot is being visited by character");
        }
    }

    public enum MarioKartItem implements Option {
        SHELL("Shell", R.drawable.mk_shell),
        STAR("Star", R.drawable.mk_star),
        BANANA_PEEL("Banana Peel", R.drawable.mk_bananapeel),
        MUSHROOM("Mushroom", R.drawable.mk_mushroom),
        COIN("Coin", R.drawable.mk_coin),
        GHOST("Ghost", R.drawable.mk_ghost);

        private final String label;
        private final int drawableId;

        MarioKartItem(String label, int drawableId) {
            this.label = label;
            this.drawableId = drawableId;
        }

        @NonNull
        @Override
        public String toString() {
            return label;
        }

        /**
         * Get the drawable id of this fruit type.
         *
         * @return Drawable id of fruit type
         */
        public int getDrawableId() {
            return drawableId;
        }
    }
}
