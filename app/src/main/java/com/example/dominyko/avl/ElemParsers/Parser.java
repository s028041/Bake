package com.example.dominyko.avl.ElemParsers;

import android.content.Context;
import android.util.Log;

import com.example.dominyko.avl.Models.GPS.AVL.AVLRecord;
import com.example.dominyko.avl.Models.GPS.AVL.AVLRecordCollection;
import com.example.dominyko.avl.Models.GPS.AVL.RecordHeader;
import com.example.dominyko.avl.Models.GPS.IO.RecordIO_Element;
import com.example.dominyko.avl.Models.GPS.IO.RecordIO_Property;
import com.example.dominyko.avl.Models.GPS.RecordGPS_Element;
import com.example.dominyko.avl.Primary.Converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <h1>Parser class for parsing data</h1>
 * <p>Parser class is responsible for all parsing of data from received TCP hex data to readable format</p>
 */
public class Parser implements Serializable {
    Context mContext;
    private String HEX;
    private int codec;
    private int recordC;
    private String ProtocolType;
    private Converter converter;
    private AVLRecordCollection avlRecordCollection;
    private short n;

    private List<AVLRecord> avlRecordArrayList = new ArrayList<>();

    public OnAvlJobDone onAvlJobDone;


    /**
     * <h1>Parser constructor requires only two parameters</h1>
     *
     * @param ProtocolType a protocol is required to know which parsing functions to use.
     */
    public Parser(String ProtocolType, Context context) {
        mContext = context;
        this.ProtocolType = ProtocolType;
        converter = new Converter();

        if(context instanceof OnAvlJobDone){
            onAvlJobDone = (OnAvlJobDone) context;
        }
    }



    public List<AVLRecord> avlRecords = new ArrayList<>();

    public AVLRecordCollection CreateCollection() { // private
        return new AVLRecordCollection(codec, recordC, avlRecords);
    }

    private void CreateRecord() {
        AVLRecord AVLRecord;
        RecordHeader recordHeader = GetRecord_Data();
        RecordGPS_Element recordGPS_element = GetRecord_GPS();
        RecordIO_Element recordIOElement = GetRecord_IO();
        AVLRecord = new AVLRecord(recordHeader, recordGPS_element, recordIOElement);
        avlRecords.add(AVLRecord);
    }

    private RecordHeader GetRecord_Data() {
        HeaderParser headerParser = new HeaderParser(HEX);
        RecordHeader recordHeader = headerParser.ConvertRecord_Data();
        HEX = headerParser.getHEX();
        return recordHeader;
    }

    public List<AVLRecord> getAvlRecordList(){
        return avlRecordArrayList;
    }

    private void setAvlRecordList(List<AVLRecord> avlRecord){
//        if(avlRecordArrayList != null){
//            avlRecordArrayList.add(avlRecord);
//        } else{
//            avlRecordArrayList = new ArrayList<>();
            avlRecordArrayList  = avlRecord;
       // }
    }

    public interface OnAvlJobDone{
        void avlJobWasDone(List<AVLRecord> avlRecords);
    }

    /**
     * <h1>Print Record to result Text Area</h1>
     * <p>A function that prints all results from Avl Record Collection list.</p>
     */

    private void PrintRecordToResultTA() {

        //ArrayList<AVLRecord> avlRecordsArray = new ArrayList<AVLRecord>();

        int i = 0;
        Log.d("Data","\nCodec: " + avlRecordCollection.getCodecID());
        Log.d("data","\nRecord Count: " + avlRecordCollection.getRecordCount());
        setAvlRecordList(avlRecordCollection.getRecordList());
        for (AVLRecord record : avlRecordCollection.getRecordList()) {
           // setAvlRecordList(record);
            i++;
            Log.d("data","\n" + i + " Record======================================================");
            Log.d("data","\nRecord Timestamp:      " + record.getRecordHeader().getTimestamp());
            Log.d("data","\nRecord Priority:       " + record.getRecordHeader().getRecordPriority());
            Log.d("data","\nRecord GPS longitude: " + record.getRecordGPS_elements().getLongitude());
            Log.d("data","\nRecord GPS latitude  : " + record.getRecordGPS_elements().getLatitude());
            Log.d("data","\nRecord GPS altitude  : " + record.getRecordGPS_elements().getAltitude());
            Log.d("data","\nRecord GPS angle     : " + record.getRecordGPS_elements().getAngle());
            Log.d("data","\nRecord GPS satellites: " + record.getRecordGPS_elements().getSatellites());
            Log.d("data","\nRecord GPS Kmh       : " + record.getRecordGPS_elements().getKmh());

            Log.d("data","\nEventID : " + record.getRecordIO_elements().getEventID());
            Log.d("data","\nElement count : " + record.getRecordIO_elements().getElementCount());

            Log.d("","\n\n1 byte elements ");
            for (RecordIO_Property data : record.getRecordIO_elements().getRecordIO_records().getByteList_1List()) {
                Log.d("","\nID      : " + data.getID());
                Log.d("data","\n\tValue   : " + data.getValue());
            }

            Log.d("data","\n\n2 byte elements ");
            for (RecordIO_Property data : record.getRecordIO_elements().getRecordIO_records().getByteList_2List()) {
                Log.d("data","\nID      : " + data.getID());
                Log.d("data","\n\tValue   : " + data.getValue());
            }

            Log.d("data","\n\n4 byte elements ");
            for (RecordIO_Property data : record.getRecordIO_elements().getRecordIO_records().getByteList_4List()) {
                Log.d("data","\nID      : " + data.getID());
                Log.d("data","\n\tValue   : " + data.getValue());
            }

            Log.d("data","\n\n8 byte elements ");
            for (RecordIO_Property data : record.getRecordIO_elements().getRecordIO_records().getByteList_8List()) {
                Log.d("data","\nID      : " + data.getID());
                Log.d("data","\n\tValue   : " + data.getValue());
            }

            Log.d("data","\n");

        }

        onAvlJobDone.avlJobWasDone(avlRecordCollection.getRecordList());
    }


    /**
     * <h1>Get Record GPS</h1>
     * <p>A function collects gps data from HEX</p>
     *
     * @return returns Record GPS element object.
     */
    private RecordGPS_Element GetRecord_GPS() {
        GPSParser gpsParser = new GPSParser(HEX);
        RecordGPS_Element recordGPS_element;
        recordGPS_element = gpsParser.ConvertRecord_GPS();
        HEX = gpsParser.getHEX();
        return recordGPS_element;
    }

    /**
     * <h1>Get Record IO</h1>
     * <p>A function collects IO data from HEX</p>
     *
     * @return returns Record IO element object.
     */
    private RecordIO_Element GetRecord_IO() {
        IOParser ioParser = new IOParser(HEX);
        ioParser.setTakeByte(n);
        RecordIO_Element recordIOElement;
        recordIOElement = ioParser.GetElement();
        HEX = ioParser.getHEX();
        return recordIOElement;
    }

    /**
     * <h1>TCP Calculate Records</h1>
     * <p>A function reads and header of given HEX to get number of records</p>
     */
    private void TCPCalcRecords() {
        if (HEX.length() >= 90) {
            String preamble = String.format("%d", converter.convertStringToIntHex(HEX.substring(0, 8)));
            HEX = HEX.substring(8, HEX.length());
            String avlDataLength = String.format("%d", converter.convertStringToIntHex(HEX.substring(0, 8)));
            HEX = HEX.substring(8, HEX.length());

            Log.d("data","\nPreamble       : " + preamble);
            Log.d("data","\nAvl Data Length       : " + avlDataLength);
            codec = converter.convertStringToIntHex(HEX.substring(0, 2));
            if (codec <= 142) {
                if (codec == 142){
                    n = 4;
                }
                else{
                    n = 2;
                }
                Log.d("codec","Codec: " + codec);
                recordC = converter.convertStringToIntHex(HEX.substring(2, 4));
                HEX = HEX.substring(4, HEX.length());
            }
            else {
                Log.e("Parse Error", "Data is corrupted or codec is wrong");
            }
        } else {
            Log.e("Parse Error", "Data is corrupted or codec is wrong");
        }
    }


    /**
     * <h1>TCP type</h1>
     * <p>Function calls to get number of records to create records and finally puts to Collections</p>
     */
    private void TCPType() {
        TCPCalcRecords();
        for (int i = 0; i < recordC; i++) {
            CreateRecord();
        }
        Log.d("records"," Records :" + recordC);
        AssignCollection();
    }


    /**
     * <h1>Clear Avl record Collection</h1>
     * <p>nullify avl record collection </p>
     */
    public void ClearAVLRecordCollection() {
        avlRecordCollection = null;
    }

    /**
     * <h1>Parse Data</h1>
     * <p>Function uses switch to check what protocol type is selected to start parsing</p>
     * @param hex parameter used for reading and parsing data
     */
    public void ParseData(String hex) {
        if (!empty(hex)) {
            HEX = hex;
            switch (ProtocolType) {
                case "TCP":
                    TCPType();
                    break;
            }
            if(avlRecordCollection != null)
                PrintRecordToResultTA();
        } else {
            System.err.println("HEX value is incorrect or null!");
        }
    }

    /**
     * <h1>Empty</h1>
     * <p>Checks if String is empty</p>
     * @param s parameter used for checking if String is null
     * @return returns true of false
     */
    private boolean empty(final String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * <h1>Assign Collection</h1>
     * <p>Assigns created collection to AVL record collection object</p>
     */
    private void AssignCollection() {
        avlRecordCollection = CreateCollection();
    }

}
