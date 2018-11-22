package com.example.dominyko.avl;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private double dLatitude = 0.0;
    private double dLongitude = 0.0;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(getExtrasLatitude() != 0 && getExtrasLongitude() !=  0){
            LatLng markerPosition = new LatLng(getExtrasLatitude(), getExtrasLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(markerPosition)
                    .title(getAddress(markerPosition.latitude,markerPosition.longitude)));
            marker.showInfoWindow();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(markerPosition)
                    .zoom(15)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }



//        LatLng latlng = new LatLng(dLatitude, dLongitude);
//        mMap.addMarker(new MarkerOptions().position(latlng).title("Current Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));


//        LatLng vikoeif = new LatLng(54.686010, 25.259900);
//        mMap.addMarker(new MarkerOptions().position(vikoeif).title("Vilnius, Jasinskio g.15"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vikoeif,5.0f));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
    }

    private float getExtrasLatitude(){
        float latidude = 0;
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().get(Constants.LAT_EXTRA_KEY) != null){
                latidude =  Float.valueOf(Objects.requireNonNull(getIntent().getExtras().get(Constants.LAT_EXTRA_KEY)).toString());
            }
        }
        return latidude;
    }

    private float getExtrasLongitude(){
        float longitude = 0;
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().get(Constants.LNG_EXTRA_KEY) != null){
                longitude =  Float.valueOf(Objects.requireNonNull(getIntent().getExtras().get(Constants.LNG_EXTRA_KEY)).toString());
            }
        }
        return longitude;
    }

    public String getAddress(double latitude, double longitude){
        String address = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
            address = "No network or phone off";
        }

        return address;
    }
}
