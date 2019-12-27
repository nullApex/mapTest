package com.nextsense.offlinemap.MapUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.nextsense.offlinemap.MapApp;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;
import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import io.ticofab.androidgpxparser.parser.domain.TrackSegment;

public class GpxModel {
    private static final String GPX_ASSET_LOCATION = "tiles/final.gpx";

    private static GpxModel instance;
    private Gpx gpx;
    private GeoPoint start = null;
    private GeoPoint end = null;

    public static GpxModel getInstance() {
        if(instance == null) {
            instance = new GpxModel(MapApp.getInstance());
        }
        return instance;
    }

    private GpxModel(Context context) {
        readGpxAsset(context);
    }

    private void readGpxAsset(Context context) {
        try {
            InputStream in = context.getAssets().open(GPX_ASSET_LOCATION);
            gpx = new GPXParser().parse(in);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public void drawGpx(MapView mapView) {
        Double firstLat, lastLat, firstLong, lastLong;
        firstLat = lastLat = firstLong = lastLong = -100.0;
        for (Track track : gpx.getTracks()) {
            for (TrackSegment trackSegment : track.getTrackSegments()) {
                Polyline line = new Polyline();
                ArrayList<GeoPoint> trajet = new ArrayList<>();
                for (TrackPoint trackPoint : trackSegment.getTrackPoints()) {
                    GeoPoint geoPoint = new GeoPoint(trackPoint.getLatitude(), trackPoint.getLongitude());
                    trajet.add(geoPoint);

                    if (firstLat == -100.0) {
                        firstLat = trackPoint.getLatitude();
                    }

                    if (lastLat == -100.0) {
                        lastLat = trackPoint.getLatitude();
                    }

                    if (firstLong == -100.0) {
                        firstLong = trackPoint.getLongitude();
                    }

                    if (lastLong == -100.0) {
                        lastLong = trackPoint.getLongitude();
                    }

                    if (trackPoint.getLatitude() < firstLat) {
                        firstLat = trackPoint.getLatitude();
                    }

                    if (trackPoint.getLatitude() > lastLat) {
                        lastLat = trackPoint.getLatitude();
                    }

                    if (trackPoint.getLongitude() < firstLong) {
                        firstLong = trackPoint.getLongitude();
                    }

                    if (trackPoint.getLongitude() > lastLong) {
                        lastLong = trackPoint.getLongitude();
                    }
                }

                line.setPoints(trajet);
                line.setInfoWindow(null);
                line.getOutlinePaint().setColor(Color.argb(255,132, 215, 0));
                line.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
                line.setOnClickListener(new Polyline.OnClickListener() {
                    @Override
                    public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
                        start = end;
                        end = eventPos;
                        drawRoute(mapView);
                        return false;
                    }
                });
                mapView.getOverlays().add(line);
                mapView.invalidate();
            }
        }
    }

    public void drawRoute(MapView mapView) {
        if(start != null && end != null) {
            TrackPoint startPoint = findClosestPoint(start);
            TrackPoint endPoint = findClosestPoint(end);
        }
    }

    private TrackPoint findClosestPoint(GeoPoint point) {
        TrackPoint closestPoint = null;
        double distance = 100000000;
        for (Track track : gpx.getTracks()) {
            for (TrackSegment trackSegment : track.getTrackSegments()) {
                for (TrackPoint trackPoint : trackSegment.getTrackPoints()) {
                    double currentDistance = calcDistance(point.getLatitude(), point.getLongitude(), trackPoint.getLatitude(), trackPoint.getLongitude());
                    if(currentDistance < distance) {
                        distance = currentDistance;
                        closestPoint = trackPoint;
                    }
                }
            }
        }
        return closestPoint;
    }

    public static double calcDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((Math.pow(x1, 2) - Math.pow(x2, 2)) + ((Math.pow(y1, 2) - Math.pow(y2, 2))));
    }
}
