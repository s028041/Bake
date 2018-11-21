package com.example.dominyko.avl.TCP_Model;

import android.util.Log;

import com.example.dominyko.avl.Primary.Converter;
import com.example.dominyko.avl.Primary.Crc16;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;


/**
 * <h1>TCP Connection</h1>
 * <p>A tcp connection is a connected client to tcp server that listens for incoming imei and rest of data.
 * First received input reads as IMEI, second reads as data and from there it sends record length
 * until the tracker disconnects or sends nothing </p>
 * */
public class TCPConnection extends Thread implements Runnable{
    private volatile boolean flag = true;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private Converter converter;
    private Crc16 crc16;
    private String imei;

    /**
     * <h1>TCP Connection construct</h1>
     * <p>The tcp connection requires parameter socket. </p>
     * @param socket to establish connection.

     * */
    public TCPConnection(Socket socket) {
        super();
        this.socket = socket;
        converter = new Converter();
        crc16 = new Crc16();
    }

    /**
     * <h1>Run function to start listener</h1>
     * <p>Simply runs the runnable thread to listen everything from client</p>
     * */

        @Override
        public void run() {
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                Listen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    //public void run() {
      //  try {
        //    inputStream = new DataInputStream(socket.getInputStream());
          //  outputStream = new DataOutputStream(socket.getOutputStream());
            //Listen();
        //} catch (IOException e) {
         ///   e.printStackTrace();
        //}
        //Thread thread = new Thread(runnable);
        ///thread.start();
    //}

    /**
     * <h1>Listen</h1>
     * <p>Function for listening connected client</p>
     * @throws IOException throws exception if input stream is interrupted
     * */
    private void Listen() throws IOException {
        while (flag) {
            Log.d("listen","listening...");
            while (!socket.isClosed() && inputStream.available() == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            Communicate();
        }
        inputStream.close();
        outputStream.close();
        socket.close();
    }

    /**
     * <h1>Get Number Of Records</h1>
     * <p>Reads the number of records to send back to the sender</p>
     * @param data the parameter is a received hex data
     * @return String format number of records
     * */
    private String GetNumberOfRecords(String data) {
        return data.substring(18, 20);
    }
    /**
     * <h1>Communicate</h1>
     * <p>A reader and sender with client, first it reads imei, then sends back 01.
     * It receives data, as soon it receives it sends back number of records.
     * The while loop initializes and runs until it get interrupted or client disconnects.</p>
     * */
    private void Communicate()  {

        imei =  Objects.requireNonNull(ReadInput()).substring(4);
        imei = converter.ReadImei(imei);
        Log.d("Imei"," IMEI: " +imei);
        if(imei.length() < 15){
            SendOutput("00");
        }
        else{
            SendOutput("01");
            Log.d("Response","\tResponse: [0" + 1 + "]");
            String input = ReadInput();
            Log.d("Input",":" + input);
            while(flag){
                String recordsCount = GetNumberOfRecords(input);
                SendOutput("000000" + recordsCount);
               Log.d("Crc","\tCrc: " + Integer.toHexString(CRC(input)));
                Log.d("Response","\tResponse: [000000" + recordsCount + "]\n");
                input = ReadInput();
                Log.d("Input","NotNull:" + input);
            }
        }
    }

    /**
     * <h1>Send Output</h1>
     * <p>Sends output to the client</p>
     * @param message the parameter is a received hex data
     * */
    private void SendOutput(String message)  {
        try {
            outputStream.write(converter.StringToByteArray(message));
            outputStream.flush();
        } catch (IOException e) {
            Log.d("INTERRUPTED","Output stream was interrupted");
        }
    }

    /**
     * <h1>CRC</h1>
     * <p>Calculates CRC of received data</p>
     * @param str the parameter is a received hex data
     * @return int of crc16
     * */
    private int CRC(String str) {
        str = str.substring(16, str.length() - 8);
        byte[] bytes = converter.StringToByteArray(str);
        return crc16.getCRC(bytes);
    }

    /**
     * <h1>Read Input</h1>
     * <p>Reads the input from client. Currently maximum message byte is set up to 8192,
     * if message is bigger then message will not be properly readable and displayed.</p>
     * @return String of received data
     * */
    private String ReadInput() {
        byte[] messageByte = new byte[8192];
        int dataSize;
        try {
            dataSize = inputStream.read(messageByte);
            String finalInput = converter.BytesArrayToHex(messageByte, dataSize);
           Log.d("fInput","Final Input"+ finalInput);
            return finalInput;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <h1>Set Running</h1>
     * <p>Sets flag to run or stop while loop in order to interrupt the thread.</p>
     * */
    public void setRunning() {
        this.flag = false;
    }






}
