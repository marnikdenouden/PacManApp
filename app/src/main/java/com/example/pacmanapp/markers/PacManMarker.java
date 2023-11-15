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

public class PacManMarker extends Marker implements Selectable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "MarioKartMarker";
    private final PacManGhost pacManGhost;
    private final static int markerId = R.id.mariokart;
    private final ContentContainer contentContainer;

    /**
     * Pac-dot marker to display on the map and use.
     *
     * @param latitude  latitude that the pac-dot is placed at
     * @param longitude longitude that the pac-dot is placed at
     */
    public PacManMarker(double latitude, double longitude, PacManGhost pacManGhost) {
        super(latitude, longitude, pacManGhost.getDrawableId(), markerId);
        this.pacManGhost = pacManGhost;

        List<Content> contentList = new ArrayList<>();
        contentList.add(new Information("Hint to key"));
        contentList.add(new HintEdit(this));
        contentContainer = new ContentContainer(contentList);
    }

    @Override
    public String getLabel() {
        return pacManGhost.toString();
    }

    @Override
    public int getIconId() {
        return pacManGhost.getDrawableId();
    }

    @Override
    public String getDescription() {
        return "Here you can find a piece to restore the pac man arcade";
    }

    @Override
    protected PacManGhostView createView(@NotNull MapArea mapArea) {
        PacManGhostView pacManGhostView = new PacManGhostView(mapArea, this);
        pacManGhostView.createView();
        return pacManGhostView;
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
    public static class PacManGhostView extends MarkerView implements Character.CharacterView.Visitable {
        private final String TAG = "MarioKartView";
        private final PacManMarker pacManMarker;
        protected PacManGhostView(@NonNull MapArea mapArea, @NonNull PacManMarker pacManMarker) {
            super(mapArea, pacManMarker);
            this.pacManMarker = pacManMarker;
        }

        @Override
        public void visit(@NotNull Character character) {
            Log.d(TAG, "Pac Dot is being visited by character");
        }
    }

    public enum PacManGhost implements Option {
        INKY("Inky", R.drawable.ghost_inky),
        PINKY("PINKY", R.drawable.ghost_pinky),
        BLINKY("BLINKY", R.drawable.ghost_blinky),
        CLYDE("Clyde", R.drawable.ghost_clyde);

        private final String label;
        private final int drawableId;

        PacManGhost(String label, int drawableId) {
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
