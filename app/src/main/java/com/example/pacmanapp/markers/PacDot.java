package com.example.pacmanapp.markers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
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
import com.example.pacmanapp.map.MapPosition;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectionCrier;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class PacDot extends Marker implements Selectable, Character.Visitable {
    private final static String TAG = "PacDot";
    private static final long serialVersionUID = 1L;
    private final static int drawableId = R.drawable.pac_dot;
    private final static int markerId = R.id.pacdot;
    private final ContentContainer content;

    /**
     * Pac-dot marker to display on the map and use.
     *
     * @param frameId   FrameId reference to map area that the pac-dot is placed on
     * @param latitude  latitude that the pac-dot is placed at
     * @param longitude longitude that the pac-dot is placed at
     * @param context   Context that the pac-dot is created in
     */
    public PacDot(int frameId, double latitude, double longitude, @NotNull Context context) {
        super(frameId, latitude, longitude, drawableId, markerId, context);
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Information("Hint to key"));
        contentList.add(new HintEdit(this));
        content = new ContentContainer(contentList);
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
    public void visit(@NotNull Character character, @NotNull Location location) {
        float mapDistance = distanceTo(character);
        float realDistance = distanceTo(location.getLatitude(), location.getLongitude());

        // TODO Improve this method, and include a single time score bonus for when the real distance is close.
        MapPosition mapPositionPacDot = this.getMapPosition();
        MapPosition mapPositionCharacter = character.getMapPosition();
        int xDistance = Math.abs(mapPositionPacDot.getX() - mapPositionCharacter.getX());
        int yDistance = Math.abs(mapPositionPacDot.getY() - mapPositionCharacter.getY());
        if (xDistance < (character.getWidth() / 2) && yDistance < (character.getHeight() / 2)) {
            SelectionCrier.getInstance().select(this);
        }
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
    public void setContent(@Nullable List<Content> contentList) {
        content.setContent(contentList);
    }
}
