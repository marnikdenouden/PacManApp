package com.example.pacmanapp.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.Marker;

public class MapArea extends ConstraintLayout {
    private final static String TAG = "Map";
    private final static int mapDrawable = R.drawable.map; // Drawable for the map
    private final static int mapViewId = R.id.mapView;
    private final static int mapAreaId = R.id.mapArea;
    private final static int markerLayoutId = R.id.markerLayout;

    // latitude range on the map drawable
    private final double latitudeStart = 51.424203344;
    private final double latitudeHeight = -0.0084914531;

    // longitude range on the map drawable
    private final double longitudeStart = 5.48382758497;
    private final double longitudeWidth = 0.013472188;

    private ImageView mapView; // Map view that determines the area of the map
    private RelativeLayout markerLayout; // Relative layout to contain markers on top of the map

    // TODO Allow the map to be moved to be centered on a location,
    //  since the constraint layout map area provides a view section on the map.
    // TODO Additionally allow the map to be scaled.

    /**
     * Create a map area layout for a context.
     *
     * @param context Context for the layout
     */
    private MapArea(Context context) {
        super(context);
        setupMapArea();
    }

    /**
     * Setup the map area to have a map image, marker layout and defined parameters.
     */
    private void setupMapArea() {
        // Create a map view to use as the area
        createMapImageView();

        // Create a marker layout that covers the map image
        createMarkerLayout();

        setLayoutParams();
        setBackgroundColor(getResources().getColor(R.color.mapBackground, getContext().getTheme()));
    }

    /**
     * Set layout params of map area.
     */
    private void setLayoutParams() {
        setId(mapAreaId);

        // Set MapArea to match parent in height and width
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
    }

    /**
     * Create map image view to use for the map area.
     */
    private void createMapImageView() {
        // Create the imageView for the map
        mapView = new ImageView(getContext());
        mapView.setId(mapViewId);

        // Add the map image to the imageView
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),
                mapDrawable, getContext().getTheme());
        mapView.setImageDrawable(drawable);

        // Create layout params with default map size
        int mapSize = getResources().getDimensionPixelSize(R.dimen.defaultMapSize);
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(mapSize, mapSize);

        // Set constraint to match the map area
        layoutParams.bottomToBottom = getId();
        layoutParams.startToStart = getId();
        layoutParams.endToEnd = getId();
        layoutParams.topToTop = getId();

        // Center the image and adjust the bounds to the image size
        mapView.setScaleType(ImageView.ScaleType.CENTER);
        mapView.setAdjustViewBounds(true);

        // Set the layout parameters to the map view
        mapView.setLayoutParams(layoutParams);


        //mapView.setVisibility(INVISIBLE); // TODO remove invisible code on map view, only for debugging


        // Add map view to map area
        addView(mapView, 0);
    }

    /**
     * Create map image view to use for the map area.
     */
    private void createMarkerLayout() {
        // Create the relative layout for the marker layout
        markerLayout = new RelativeLayout(getContext());
        markerLayout.setId(markerLayoutId);

        // Create layout params with 0dp width and height
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(0,0);

        // Set constraint to match the map view
        layoutParams.bottomToBottom = mapView.getId();
        layoutParams.startToStart = mapView.getId();
        layoutParams.endToEnd = mapView.getId();
        layoutParams.topToTop = mapView.getId();

        // Set the layout parameters to the marker layout
        markerLayout.setLayoutParams(layoutParams);

        // Add marker layout to map area on top of map view
        addView(markerLayout);
    }

    /**
     * Center the map image in the map area.
     */
    private void centerMap() {
        setMapCenter(new MapPosition(mapView.getWidth() / 2, mapView.getHeight() / 2));
    }

    /**
     * Move the map image to place a map position in the center of the map area.
     *
     * @param mapPosition Map position to center in the map area
     */
    public void moveMapCenter(MapPosition mapPosition) {
        int targetX = getWidth() / 2 - mapPosition.getX();
        int targetY = getHeight() / 2 - mapPosition.getY();

        moveMap(targetX, targetY);
    }

    /**
     * Move the map image to place a marker in the center of the map area.
     *
     * @param marker Marker to center the map are on
     */
    public void moveMapCenter(Marker marker) {
        int targetX = getWidth() / 2 - ((int) marker.getX() + marker.getWidth() / 2);
        int targetY = getHeight() / 2 - ((int) marker.getY() + marker.getHeight() / 2);

        moveMap(targetX, targetY);
    }

    /**
     * Move map to the target x and target y position.
     *
     * @param targetX x position to move the map image to
     * @param targetY y position to move the map image to
     */
    private void moveMap(int targetX, int targetY) {
        // Animate the character to the target position
        Runnable moveMapCenter = () -> {
            placeMap(targetX, targetY);
        };
        mapView.animate().x(targetX).y(targetY).withEndAction(moveMapCenter)
                .setDuration(getContext().getResources().getInteger(R.integer.moveAnimationTime))
                .start();
    }

    /**
     * Set the map image to place a map position in the center of the map area.
     *
     * @param mapPosition Map position to center in the map area
     */
    private void setMapCenter(MapPosition mapPosition) {
        int x = getWidth() / 2 - mapPosition.getX();
        int y = getHeight() / 2 - mapPosition.getY();
        placeMap(x, y);
    }

    /**
     * Place the map image to an x and y pixel position.
     *
     * @param x X position to place the map at
     * @param y Y position to place the map at
     */
    private void placeMap(int x, int y) {
        mapView.setX(x);
        mapView.setY(y);
        markerLayout.setX(x);
        markerLayout.setY(y);
    }

    /**
     * Set the scale of the map.
     *
     * @param mapScale Scale to set map to
     */
    private void setMapScale(float mapScale) {
        mapView.setScaleX(mapScale);
        mapView.setScaleY(mapScale);
        // TODO Before making this public, make sure the markers are repositioned
        //  and that the mapScale parameter is sensible.
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        centerMap();
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
        MapArea mapArea = new MapArea(view.getContext());
        view.addView(mapArea);
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
}
