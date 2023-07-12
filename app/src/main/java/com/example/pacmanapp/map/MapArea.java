package com.example.pacmanapp.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.Marker;

@SuppressLint("ViewConstructor")
public class MapArea extends ConstraintLayout {
    private final static String TAG = "MapArea";
    final static int mapAreaId = R.id.mapArea;
    private final MapType mapType; // Map type that specifies map specific values
    private final MapView mapView; // Map view that determines the area of the map
    private final MarkerLayout markerLayout; // Relative layout to contain markers on top of the map

    //>>> Methods to create the map area layout <<<//

    /**
     * Create a map area layout for a context.
     *
     * @param mapType Map to create map area for
     * @param context to create layout with
     */
    private MapArea(MapType mapType, Context context) {
        super(context);
        this.mapType = mapType;

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
        setBackgroundColor(getResources().getColor(mapType.getBackgroundColor(), getContext().getTheme()));
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
        markerLayout.addView(marker.getImageView());
        markerLayout.addOnLayoutChangeListener(
                (view, i, i1, i2, i3, i4, i5, i6, i7) -> marker.updatePlacement());
    }

    /**
     * Creates a map on the given view.
     *
     * @param view View to create map on
     * @return MapArea map area added to the view
     */
    public static void createMap(MapType mapType, ViewGroup view) {
        MapArea mapArea = new MapArea(mapType, view.getContext());
        if (view.findViewById(mapAreaId) != null) {
            Log.w(TAG, "A map area was already added to this view group");
        }
        view.addView(mapArea);
        view.addView(new MapController(mapArea), 0);
        MapManager.getMapManager().setMapArea(view.getId(), mapArea);
    }

    /**
     * Get the map area from the specified frame id.
     *
     * @param appCompatActivity Activity to get map from
     * @param frameId Frame id to get map for
     * @return Map area on the frame id and in the specified activity
     */
    public static MapArea getMapArea(AppCompatActivity appCompatActivity, int frameId) {
        // Get the map area from the frame id.
        FrameLayout frameLayout = appCompatActivity.findViewById(frameId);
        return frameLayout.findViewById(mapAreaId);
    }

    /**
     * Get the map position from a latitude and longitude location.
     *
     * @param latitude  latitude that is converted to map y position
     * @param longitude longitude that is converted to map x position
     * @return MapPosition that corresponds to the specified location
     */
    public MapPosition getMapLocation(double latitude, double longitude) {
        // Convert the longitude to the X position on the map
        double xPosition = (longitude - mapType.getLongitudeStart())
                * mapView.getWidth() / mapType.getLongitudeWidth();

        // Convert the latitude to the Y position on the map
        double yPosition = (latitude - mapType.getLatitudeStart())
                * mapView.getHeight() / mapType.getLatitudeHeight();

        return new MapPosition((int) xPosition, (int) yPosition);
    }

    /**
     * Get the map area id.
     *
     * @return Id of the map area
     */
    public int getMapAreaId() {
        return mapAreaId;
    }

    //>>> Methods to call within package to orchestrate the map <<<//

    /**
     * Get the map type of this map area.
     *
     * @return Map reference
     */
    MapType getMapType() {
        return mapType;
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
