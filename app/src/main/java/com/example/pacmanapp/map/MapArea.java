package com.example.pacmanapp.map;

import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.Marker;

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
    private final MapMover mapMover; // View to detect touch input to move the map

    public Move move; // Used to move the map around
    OverScroller scroller;

    // TODO Allow the map to be moved to be centered on a location,
    //  since the constraint layout map area provides a view section on the map.
    // TODO Additionally allow the map to be scaled.

    /**
     * Create a map area layout for a context.
     *
     * @param view View that map area is added to
     */
    private MapArea(ViewGroup view) {
        super(view.getContext());
        view.addView(this);

        mapView = new MapView(this);
        markerLayout = new MarkerLayout(this);

        mapMover = new MapMover(this);
        view.addView(mapMover); // Detects touch to move the map, set on top of map area

        setupMapArea();

        move = new Move(this);
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh); // TODO update the use of this method
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b); // TODO update the use of this method
        //move.centerMap();
    }

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
     * @return Map map added to the view
     */
    public static MapArea addMap(ViewGroup view) {
        return new MapArea(view);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mapMover.handleTouchEvent(event); // TODO I am not sure what the right strategy is to get it to work well.
    }

    // Maybe map mover code can be moved here>
}
