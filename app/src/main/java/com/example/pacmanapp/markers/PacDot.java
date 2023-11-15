package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
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
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PacDot extends Marker implements Selectable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "PacDot";
    private final static int drawableId = R.drawable.pac_dot;
    private final static int markerId = R.id.pacdot;
    private final ContentContainer contentContainer;

    /**
     * Pac-dot marker to display on the map and use.
     *
     * @param latitude  latitude that the pac-dot is placed at
     * @param longitude longitude that the pac-dot is placed at
     */
    public PacDot(double latitude, double longitude) {
        super(latitude, longitude, drawableId, markerId);
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Information("Hint to key"));
        contentList.add(new HintEdit(this));
        contentContainer = new ContentContainer(contentList);
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
    protected PacDotView createView(@NotNull MapArea mapArea) {
        PacDotView pacDotView = new PacDotView(mapArea, this);
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
    public static class PacDotView extends MarkerView implements Character.CharacterView.Visitable {

        private final String TAG = "PacDotView";
        private final PacDot pacDot;
        protected PacDotView(@NonNull MapArea mapArea, @NonNull PacDot pacDot) {
            super(mapArea, pacDot);
            this.pacDot = pacDot;
        }

        @Override
        public void visit(@NotNull Character character) {
            Log.d(TAG, "Pac Dot is being visited by character");
        }
    }
}
