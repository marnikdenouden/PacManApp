package com.example.pacmanapp.map;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pacmanapp.R;
import com.example.pacmanapp.location.DynamicLocation;
import com.example.pacmanapp.location.LocationObserver;
import com.example.pacmanapp.markers.Marker;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

@SuppressLint("ViewConstructor")
public class MapArea extends ConstraintLayout {
    private final static String TAG = "MapArea";
    private final static int mapAreaId = R.id.mapArea;
    private final MapSave mapSave; // Map save that the map area was created from.
    private final MapType mapType; // Map type that specifies map specific values
    private final MapView mapView; // Map view that determines the area of the map
    private final MarkerLayout markerLayout; // Relative layout to contain markers on top of the map
    private final ViewGroup parent;

    //>>> Methods to create the map area layout <<<//

    /**
     * Create a map area layout for a context.
     *
     * @param viewGroup viewGroup to create map area in
     * @param mapSave MapSave to create map area for
     *
     */
    private MapArea(@NotNull ViewGroup viewGroup, @NotNull MapSave mapSave) {
        super(viewGroup.getContext());
        this.mapType = mapSave.getMapType();
        this.parent = viewGroup;
        this.mapSave = mapSave;

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
        setBackgroundColor(mapType.getBackgroundColor(getContext()));
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
     * Creates a map on the given view.
     *
     * @param viewGroup ViewGroup to create map on
     * @param mapSave MapSave to create map area for
     */
    protected static MapArea createMap(@NotNull ViewGroup viewGroup, @NotNull MapSave mapSave) {
        MapArea mapArea = new MapArea(viewGroup, mapSave);
        if (viewGroup.findViewById(mapAreaId) != null) {
            Log.w(TAG, "A map area was already added to this view group");
        }
        viewGroup.addView(mapArea);
        viewGroup.addView(new MapController(mapArea), 0);
        return mapArea;
    }

    /**
     * Get the map save of the map area.
     *
     * @return mapSave Map save that map area is created for
     */
    public MapSave getMapSave() {
        return mapSave;
    }

    /**
     * Remove the map area view from the view group it was added to.
     */
    protected void removeMap() {
        parent.removeView(this);
    }

    /**
     * Get the marker listener for the map area.
     *
     * @return markerListener that updates the markers in the map area
     */
    protected MapMarkers.MarkerListener getMarkerListener() {
        return markerLayout;
    }

    /**
     * Check if a map area exists in a frame layout with specified id in the specified view group.
     *
     * @param viewGroup ViewGroup to check frame layout for
     * @param frameId Frame id to check frame layout for
     * @return Truth assignment, if a map area exists in the specified activity
     */
    protected static boolean hasMapArea(@NotNull ViewGroup viewGroup, int frameId) {
        // Check if the frame layout for the frame id is null.
        FrameLayout frameLayout = viewGroup.findViewById(frameId);
        View mapAreaView = frameLayout.findViewById(mapAreaId);
        return mapAreaView != null;
    }

    /**
     * Get the map area from the specified frame id.
     *
     * @param viewGroup ViewGroup to get map area from
     * @param frameId Frame id to get map area for
     * @return Map area on the frame id and in the specified activity
     */
    protected static MapArea getMapArea(@NotNull ViewGroup viewGroup, int frameId) {
        // Get the map area from the frame id.
        FrameLayout frameLayout = viewGroup.findViewById(frameId);
        return frameLayout.findViewById(mapAreaId);
    }

    /**
     * Get the map area id.
     *
     * @return Id of the map area
     */
    public static int getMapAreaId() {
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

    /**
     * Update the map size of the map area.
     *
     * @param width int width size of the new map
     * @param height int height size of the new map
     */
    protected void updateMapSize(int width, int height) {
        ViewGroup.LayoutParams mapLayout = mapView.getLayoutParams();
        mapLayout.width = width;
        mapLayout.height = height;
        requestLayout();
    }

    /**
     * Get the map position from a latitude and longitude location.
     *
     * @param latitude  latitude that is converted to map y position
     * @param longitude longitude that is converted to map x position
     * @return MapPosition that corresponds to the specified location
     */
    public MapPosition getMapPosition(double latitude, double longitude) {
        // Convert the longitude to the X position on the map
        double xPosition = (longitude - mapType.getLongitudeStart())
                * mapView.getWidth() / mapType.getLongitudeWidth();

        // Convert the latitude to the Y position on the map
        double yPosition = (latitude - mapType.getLatitudeStart())
                * mapView.getHeight() / mapType.getLatitudeHeight();

        return new MapPosition((int) xPosition, (int) yPosition);
    }

    /* Marker layout methods that can be called from the map area. */

    /**
     * Adds a marker to the map.
     *
     * @param marker Marker to add to the map
     */
    protected void addMarker(Marker marker) {
        markerLayout.addMarker(marker);
    }

    /**
     * Remove marker view from this map area.
     */
    protected void removeMarker(@NotNull Marker marker) {
        markerLayout.removeMarker(marker);
    }

    /**
     * Remove all markers added to this map area.
     */
    protected void removeAllMarkers() {
        markerLayout.removeAllMarkers();
    }

    /**
     * Gets a collection of the map marker views.
     *
     * @return new Set of the map marker view collection
     */
    public Collection<Marker.MarkerView> getMapMarkerViews() {
        return markerLayout.getMapMarkerViews();
    }

    /**
     * Check if marker views contains a view with specified class.
     *
     * @param markerViewClass Class to check if a marker view from the collection is an instance of
     * @return Truth assignment, if there exists a view that is an instance of the specified class
     */
    public boolean hasMarkerViewWithClass(Class<?> markerViewClass) {
        return markerLayout.hasMarkerViewWithClass(markerViewClass);
    }

    /**
     * Get the collection of marker views with the specified class.
     *
     * @param markerViewClass Class of marker views to get
     * @return Collection of marker views with the specified class
     */
    public <MarkerType> Collection<MarkerType> getMarkerViewsWithClass(
            Class<MarkerType> markerViewClass) {
        return markerLayout.getMarkerViewsWithClass(markerViewClass);
    }

}
