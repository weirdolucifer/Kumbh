package com.example.kumbh;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlLineString;
import com.google.maps.android.data.kml.KmlPlacemark;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KmlDemoActivity extends BaseDemoActivity {

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
            KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.test, getApplicationContext());
            kmlLayer.addLayerToMap();
            moveCameraToKml(kmlLayer);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }



    private void moveCameraToKml(KmlLayer kmlLayer) {
        Intent it = getIntent();
        List<Road> myList = new ArrayList<Road>();
        myList = (List<Road>) it.getSerializableExtra("LIST1");



        KmlContainer container = kmlLayer.getContainers().iterator().next();
        //Retrieve a nested container within the first container
        container = container.getContainers().iterator().next();
        //Retrieve the first placemark in the nested container
        for ( KmlPlacemark placemark : container.getPlacemarks()) {
            //Retrieve a polygon object in a placemark
            KmlLineString poi = (KmlLineString) placemark.getGeometry();

            PolylineOptions polylineOptions = new PolylineOptions();
            String des = placemark.getProperty("name");
            ArrayList<LatLng> z = poi.getGeometryObject();

            int flag =0;
            for(int i=0;i<myList.size();i++) {
                Road temp = myList.get(i);
                String t_c = temp.Constraint;
                String t_r = temp.Road_name;

                if (des.matches(t_r)) {
                    Log.e("gd", t_r);
                    Log.e("gf", t_c);

                    flag = 1;

                    if(t_c.matches("Blocked")){
                        Log.e("gm", t_c);
                        polylineOptions.addAll(z);
                        polylineOptions
                                .width(8)
                                .color(Color.RED);
                    }
                    if(t_c.matches("Oneway")){
                        Log.e("gm", t_c);
                        polylineOptions.addAll(z);
                        polylineOptions
                                .width(9)
                                .color(Color.GRAY);
                    }


                }

            }
            if(flag == 0)
            {
                polylineOptions.addAll(z);
                polylineOptions
                        .width(7)
                        .color(Color.BLUE);

            }



// Adding multiple points in map using polyline and arraylist
            mMap.addPolyline(polylineOptions);

        }


    }






}
