package com.nextsense.offlinemap.MapUtil;

import android.graphics.drawable.Drawable;

import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.modules.CantContinueException;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public class MapTileFileAssetsProvider extends MapTileModuleProviderBase {
    public static final int MAP_ZOOM_ZOOM_MIN = 18;
    public static final int MAP_ZOOM_ZOOM_MAX = 21;
    public static final String MAP_ASSETS_FOLDER_NAME = "tiles";
    public static final String PROVIDER_NAME = "assets";
    protected ITileSource mTileSource;

    public MapTileFileAssetsProvider(final ITileSource pTileSource) {
        super(8, 2);
        mTileSource = pTileSource;
    }

    @Override
    public boolean getUsesDataConnection() {
        return false;
    }

    @Override
    protected String getName() {
        return PROVIDER_NAME;
    }

    @Override
    protected String getThreadGroupName() {
        return MAP_ASSETS_FOLDER_NAME;
    }

    @Override
    public MapTileModuleProviderBase.TileLoader getTileLoader() {
        return new TileLoader();
    }

    @Override
    public int getMinimumZoomLevel() {
        return mTileSource != null ? mTileSource.getMinimumZoomLevel() : MAP_ZOOM_ZOOM_MAX;
    }

    @Override
    public int getMaximumZoomLevel() {
        return mTileSource != null ? mTileSource.getMaximumZoomLevel() :  MAP_ZOOM_ZOOM_MIN;
    }

    @Override
    public void setTileSource(final ITileSource pTileSource) {
        mTileSource = pTileSource;
    }

    private class TileLoader extends MapTileModuleProviderBase.TileLoader {

        @Override
        public Drawable loadTile(long pMapTileIndex) throws CantContinueException {
            if (mTileSource == null) {
                return null;
            }

            String path = mTileSource.getTileRelativeFilenameString(pMapTileIndex);
            Drawable drawable;
            try {
                drawable = mTileSource.getDrawable(path);
            } catch (final BitmapTileSourceBase.LowMemoryException e) {
                throw new CantContinueException(e);
            }

            return drawable;
        }
    }
}