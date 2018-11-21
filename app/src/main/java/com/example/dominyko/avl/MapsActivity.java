package com.example.dominyko.avl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private double dLatitude = 0.0;
    private double dLongitude = 0.0;

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

               Bundle extras = getIntent().getExtras();
        dLatitude = extras.getDouble("latitude");
        dLongitude = extras.getDouble("longitude");
        if (extras != null)
        {
            dLatitude = Double.parseDouble(extras.getString("latitude"));
            dLongitude = Double.parseDouble(extras.getString("longitude"));
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(dLatitude, dLongitude))
                .title("CurrentLocation"));

//        LatLng latlng = new LatLng(dLatitude, dLongitude);
//        mMap.addMarker(new MarkerOptions().position(latlng).title("Current Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));


//        LatLng vikoeif = new LatLng(54.686010, 25.259900);
//        mMap.addMarker(new MarkerOptions().position(vikoeif).title("Vilnius, Jasinskio g.15"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vikoeif,5.0f));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
    }

}
