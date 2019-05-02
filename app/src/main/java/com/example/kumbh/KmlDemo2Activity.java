package com.example.kumbh;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPoint;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class KmlDemo2Activity extends BaseDemoActivity {

    private GoogleMap mMap;

    protected int getLayoutId() {
        return R.layout.activity_kml_demo;
    }


    public void startDemo () {
        try {
            mMap = getMap();

            retrieveFileFromResource();


        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }





    private void retrieveFileFromResource() {
        try {
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.429955, 81.889256), 12));
            KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.kum2, getApplicationContext());
            kmlLayer.addLayerToMap();
            moveCameraToKml1(kmlLayer);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }



    private void moveCameraToKml1(KmlLayer kmlLayer) {

        KmlContainer container = kmlLayer.getContainers().iterator().next();
        //Retrieve a nested container within the first container
        container = container.getContainers().iterator().next();
        //Retrieve the first placemark in the nested container
        for ( KmlPlacemark placemark : container.getPlacemarks()) {
            //Retrieve a polygon object in a placemark
            KmlPoint poi = (KmlPoint) placemark.getGeometry();
            String des = placemark.getProperty("name");

            LatLng sydney = poi.getGeometryObject();
            mMap.addMarker(new MarkerOptions().position(sydney).title(des));

        }


    }





}
