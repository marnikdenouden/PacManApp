package com.example.pacmanapp.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.Marker;

@SuppressLint("ViewConstructor")
public class MapArea extends ConstraintLayout {
    private final static String TAG = "MapArea";
    private final static int mapAreaId = R.id.mapArea;

    // latitude range on the map drawable
    private final double latitudeStart = 51.424203344;
    private final double latitudeHeight = -0.0084914531;

    // longitude range on the map drawable
    private final double longitudeStart = 5.48382758497;
    private final double longitudeWidth = 0.013472188;

    private final MapView mapView; // Map view that determines the area of the map
    private final MarkerLayout markerLayout; // Relative layout to contain markers on top of the map

    //>>> Methods to create the map area layout <<<//

    /**
     * Create a map area layout for a context.
     *
     * @param context to create layout with
     */
    private MapArea(Context context) {
        super(context);

        mapView = new MapView(this); // Visual map that is behind the markers
        markerLayout = new MarkerLayout(this); // Contains the markers on top of the map

        setupMapArea();

        createOverScroller();
    }

    /**
     * Setup the map area to have a map image, marker layout and defined parameters.
     */
    private void setupMapArea() {
        setId(mapAreaId);
        setLayoutParams();
        setBackgroundColor(getResources().getColor(R.color.mapBackground, getContext().getTheme()));
    }

    /**
     * Set layout params of map area.
     */
    private void setLayoutParams() {
        // Set MapArea to match parent in height and width
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
    }

    //>>> Public methods to call for map area <<<//

    /**
     * Adds a marker to the map.
     *
     * @param marker Marker to add to the map
     */
    public void addMarker(Marker marker) {
        markerLayout.addView(marker);
    }

    /**
     * Adds a map to the given view.
     *
     * @param view View to add map to
     * @return MapArea map area added to the view
     */
    public static MapArea addMap(ViewGroup view) {
        MapArea mapArea = new MapArea(view.getContext());
        view.addView(mapArea);
        view.addView(new MapController(mapArea));
        return mapArea;
    }

    /**
     * Get the map position from a latitude and longitude location.
     *
     * @param latitude  latitude that is converted to map y position
     * @param longitude longitude that is converted to map x position
     * @return MapPosition that corresponds to the specified location
     */
    public MapPosition getMapLocation(double latitude, double longitude) {
        // Convert the latitude and longitude to X and Y values on the map
        double xPosition = (longitude - longitudeStart) * mapView.getWidth() / longitudeWidth;
        double yPosition = (latitude - latitudeStart) * mapView.getHeight() / latitudeHeight;
        return new MapPosition((int) xPosition, (int) yPosition);
    }

    //>>> Methods to call within package to orchestrate the map <<<//

    /**
     * Get the map view of this map area.
     *
     * @return Map view of this map area
     */
    MapView getMapView() {
        return mapView;
    }

    /**
     * Get the marker layout of this map area.
     *
     * @return markerLayout of this map area
     */
    MarkerLayout getMarkerLayout() {
        return markerLayout;
    }

    //>>> Map Area scroller code <<<//

    OverScroller scroller; // Allow map controller to use scroller

    private void createOverScroller() {
        scroller = new OverScroller(getContext(), new AccelerateDecelerateInterpolator());
        //scroller.setFriction(1.00f);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

}
