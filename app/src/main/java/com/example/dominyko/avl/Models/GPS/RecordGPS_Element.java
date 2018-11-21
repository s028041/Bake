package com.example.dominyko.avl.Models.GPS;

import java.io.Serializable;

public class RecordGPS_Element implements Serializable {

    private int Longitude;
    private int Latitude;
    private int Altitude;
    private int Angle;
    private int Satellites;
    private int Kmh;



    public RecordGPS_Element(int longitude, int latitude, int altitude, int angle, int satellites, int kmh)
    {
        Longitude = longitude;
        Latitude = latitude;
        Altitude = altitude;
        Angle = angle;
        Satellites = satellites;
        Kmh = kmh;
    }
    public int getLongitude()
    {
        return Longitude;
    }
    public int getLatitude()
    {
        return Latitude;
    }
    public int getAltitude()
    {
        return Altitude;
    }
    public int getAngle()
    {
        return Angle;
    }
    public int getSatellites()
    {
        return Satellites;
    }
    public int getKmh()
    {
        return Kmh;
    }
}
