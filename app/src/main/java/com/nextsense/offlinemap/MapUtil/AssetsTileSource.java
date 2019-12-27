package com.nextsense.offlinemap.MapUtil;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import org.osmdroid.tileprovider.util.StreamUtils;

import java.io.InputStream;

public class AssetsTileSource extends BitmapTileSourceBase {
    private static final String DEFAULT_MAP_TILE = "tiles/parchment.png";
    private final AssetManager mAssetManager;

    public AssetsTileSource(final AssetManager assetManager, final String aName, final int aZoomMinLevel, final int aZoomMaxLevel, final int aTileSizePixels, final String aImageFilenameEnding) {
        super(aName, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels, aImageFilenameEnding);
        mAssetManager = assetManager;
    }

    @Override
    public Drawable getDrawable(final String aFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = mAssetManager.open(aFilePath);
            if (inputStream != null) {
                final Drawable drawable = getDrawable(inputStream);
                return drawable;
            }
        } catch (final Throwable e) {
            try {
                return getDrawable(mAssetManager.open(DEFAULT_MAP_TILE));
            } catch (Exception ex) {
                return null;
            }
        } finally {
            if (inputStream != null) {
                StreamUtils.closeStream(inputStream);
            }
        }
        return null;
    }
}
