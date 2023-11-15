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
import com.example.pacmanapp.contents.HintText;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.map.MapArea;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.selectables.CounterStrikeInferno;
import com.example.pacmanapp.selection.selectables.MarioKartTrack;
import com.example.pacmanapp.selection.selectables.PacManArcade;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GEPWNAGEMarker extends Marker implements Selectable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "GEPWNAGEMarker";
    private final static int drawableId = R.drawable.gepwnage_logo;
    private final static int markerId = R.id.GEPWNAGE;
    private final ContentContainer contentContainer;

    public GEPWNAGEMarker(double latitude, double longitude) {
        super(latitude, longitude, drawableId, markerId);
        List<Content> contentList = new ArrayList<>();
        contentList.add(new HintEdit(new MarioKartTrack()));
        contentList.add(new HintEdit(new PacManArcade()));
        contentList.add(new HintEdit(new CounterStrikeInferno()));
        contentContainer = new ContentContainer(contentList);
    }

    @Override
    public String getLabel() {
        return "GEWIS";
    }

    @Override
    public int getIconId() {
        return R.drawable.gepwnage_logo;
    }

    @Override
    public String getDescription() {
        return "Here you can find the game boards that need to be restored, " +
                "be quick as only one team can restore a game at a time.";
    }

    @Override
    protected GEPWNAGEMarker.GEPWNAGEView createView(@NotNull MapArea mapArea) {
        GEPWNAGEView pacDotView = new GEPWNAGEView(mapArea, this);
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
    public static class GEPWNAGEView extends MarkerView implements Character.CharacterView.Visitable {

        private final String TAG = "GEWPANGEView";
        private final GEPWNAGEMarker GEPWNAGEMarker;
        protected GEPWNAGEView(@NonNull MapArea mapArea, @NonNull GEPWNAGEMarker GEPWNAGEMarker) {
            super(mapArea, GEPWNAGEMarker);
            this.GEPWNAGEMarker = GEPWNAGEMarker;
        }

        @Override
        public void visit(@NotNull Character character) {
            Log.d(TAG, "Pac Dot is being visited by character");
        }
    }
}
