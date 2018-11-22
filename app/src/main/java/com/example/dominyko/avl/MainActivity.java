package com.example.dominyko.avl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dominyko.avl.ElemParsers.Parser;
import com.example.dominyko.avl.Models.GPS.AVL.AVLRecord;
import com.example.dominyko.avl.TCP_Model.Server;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Parser.OnAvlJobDone,
        RecyclerViewAdaper.LocationListRecyclerClickListener {

    Server server;
    public TextView msg;
    TextView infoip;
    public Button btn_Conn, btn_Map;



    public void initMap()
    {
        btn_Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleMap = new Intent(MainActivity.this,MapsActivity.class);
//                googleMap.putExtra("LATITUDE", 38.65452);
//                googleMap.putExtra("LONGITUDE", -90.18471);
                startActivity(googleMap);

            }
        });
    }

    private List<AVLRecord> mAvlRecordArrayList;

//vars
    private ArrayList<AVLRecord> mRecord = new ArrayList<>();
    Parser parse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //infoip = (TextView) findViewById(R.id.infoip);
       // msg = (TextView)findViewById(R.id.msg);
        server = new Server(this);
        btn_Conn = findViewById(R.id.btn_Conn);
        btn_Map = findViewById(R.id.btn_Map);
        parse = new Parser("TCP",MainActivity.this);
        //Maps
        initMap();


     //   infoip.setText(server.getIpAddress() + ":" + server.getPort());


        btn_Conn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Parser parse = new Parser("TCP",MainActivity.this);
                String text = "00000000000003c30810000001657b97ab30000f0da48d20959cee008d0000040000000c06ef01f00050001505c800450106b5000fb6000f423a681800004300004400000000000001657b978420000f0da48d20959cee008e0000040000000c06ef01f00050001505c800450106b5000db6000c423a5f1800004300004400000000000001657b975d10000f0da48d20959cee00000000030000000c06ef01f00050001505c800450206b50018b60018423a661800004300004400000000000001657b973600000f0da48d20959cee00000000030000000c06ef01f00050001505c800450206b50018b60018423a571800004300004400000000000001657b970ef0000f0da48d20959cee008d0000040000000c06ef01f00050001505c800450106b5000fb6000e423a5c1800004300004400000000000001657b96e7e0000f0da48d20959cee008c0000040000000c06ef01f00050001505c800450106b5000fb6000e423a6b1800004300004400000000000001657b96c0d0000f0da48d20959cee008a0000060000000c06ef01f00050001505c800450106b5000cb6000a423a7a1800004300004400000000000001657b9699c0000f0da48d20959cee008a0000050000000c06ef01f00050001505c800450106b5000db6000c423a641800004300004400000000000001657b9672b0000f0da48d20959cee008a0000040000000c06ef01f00050001505c800450106b50012b60012423a6a1800004300004400000000000001657b964ba0000f0da48d20959cee00000000030000000c06ef01f00050001505c800450206b50022b60022423a641800004300004400000000000001657b962490000f0da48d20959cee00000000030000000c06ef01f00050001505c800450206b50022b60022423a601800004300004400000000000001657b95fd80000f0da48d20959cee008b0000030000000c06ef01f00050001505c800450106b50021b60020423a621800004300004400000000000001657b95d670000f0da48d20959cee008b0000040000000c06ef01f00050001505c800450106b5001cb6001b423a601800004300004400000000000001657b95af60000f0da48d20959cee008b0000050000000c06ef01f00050001505c800450106b50014b60014423a621800004300004400000000000001657b958850000f0da48d20959cee008b0000040000000c06ef01f00050001505c800450106b5001cb6001c423a711800004300004400000000000001657b956140000f0da48d20959cee008b0000040000000c06ef01f00050001505c800450106b5001cb6001c423a6018000043000044000000001000008cf8\n";
                parse.ParseData(text);

            }
        });

        //mRecord.add();
       // initRecyclerView();
    }


    private void initRecyclerView(List<AVLRecord> avlRecordArrayList) {

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        RecyclerViewAdaper adaper = new RecyclerViewAdaper(this, avlRecordArrayList, this);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adaper);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.onDestroy();
    }

    @Override
    public void avlJobWasDone(List<AVLRecord> avlRecords) {
        mAvlRecordArrayList = parse.getAvlRecordList();
        Toast.makeText(MainActivity.this,"Parsing",Toast.LENGTH_LONG).show();
        initRecyclerView(avlRecords);
    }

    @Override
    public void onLocationClicked(LatLng markerLocation) {
       // Log.e("locationOnClck",String.valueOf(markerLocation.latitude) + "   " + String.valueOf(markerLocation.longitude));
        if(markerLocation != null){
            Intent mapActivityIntent = new Intent(this,MapsActivity.class);
            mapActivityIntent.putExtra(Constants.LAT_EXTRA_KEY,String.valueOf(markerLocation.latitude));
            mapActivityIntent.putExtra(Constants.LNG_EXTRA_KEY,String.valueOf(markerLocation.longitude));
            startActivity(mapActivityIntent);
        } else {
            Toast.makeText(this,"No location data",Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onLocationClicked(int position) {
//        Log.d("onLocationClicked","Longitude and Latitude");
//        Intent intent = new Intent(this, MapsActivity.class);
//        String latitude = intent.getExtras().getString("latitude");
//        intent.putExtra("Latitude",latitude);
//        String longitude = intent.getExtras().getString("longitude");
//        intent.putExtra("Longitude",longitude);
//        startActivity(intent);
//
//    }

}
