package com.nextsense.offlinemap.MapUtil;

import android.content.Context;
import android.util.AttributeSet;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

public class NsMapView extends MapView implements MapListener {

    public NsMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NsMapView(Context context) {
        super(context);
    }

    public void initMap(double zoom, double latitude, double longitude) {
        setUseDataConnection(false);
        AssetsTileSource tileSource = new AssetsTileSource(getContext().getAssets(), MapTileFileAssetsProvider.MAP_ASSETS_FOLDER_NAME, MapTileFileAssetsProvider.MAP_ZOOM_ZOOM_MIN, MapTileFileAssetsProvider.MAP_ZOOM_ZOOM_MAX, 256, ".png");
        MapTileModuleProviderBase moduleProvider = new MapTileFileAssetsProvider(tileSource);
        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(getContext());
        MapTileProviderArray tileProviderArray = new MapTileProviderArray(tileSource, simpleReceiver, new MapTileModuleProviderBase[] { moduleProvider });
        setTileProvider(tileProviderArray);
        addMapListener(this);
        setMultiTouchControls(true);
        getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        getController().setCenter(new GeoPoint(latitude, longitude));
        setMinZoomLevel(zoom);
        getController().setZoom(zoom);
        setMapOrientation(323.9f,true);
        GpxModel.getInstance().drawGpx(this);
    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        return false;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        invalidate();
        return false;
    }
}
