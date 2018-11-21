package com.example.dominyko.avl.TCP_Model;

import android.util.Log;

import com.example.dominyko.avl.MainActivity;
import com.example.dominyko.avl.Primary.Converter;
import com.example.dominyko.avl.Primary.Crc16;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;

public class Server {
    MainActivity activity;
    ServerSocket serverSocket;
    private ArrayList<TCPConnection> tcpConnections;
    String message = "";
    static final int socketServerPORT = 7123;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private Converter converter;
    private Crc16 crc16;
    private String imei;

    public Server(MainActivity activity) {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();

    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            tcpConnections = new ArrayList<>();
            try {
                // create ServerSocket using specified port
                serverSocket = new ServerSocket(socketServerPORT);
                Log.d("Socket","Listening on port : " + socketServerPORT);

                while (true) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();
                    Log.d("SocketAccept", "client connected from: %s" + socket.getRemoteSocketAddress().toString());
                    Communicate();
                    count++;
                    message += "#" + count + " from "
                            + socket.getInetAddress() + ":"
                            + socket.getPort() + "\n";

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.msg.setText(message);
                        }
                    });

                    SocketServerReplyThread socketServerReplyThread =
                            new SocketServerReplyThread(socket, count);
                    socketServerReplyThread.run();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
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
            while(true){
                String recordsCount = GetNumberOfRecords(input);
                SendOutput("000000" + recordsCount);
                Log.d("Crc","\tCrc: " + Integer.toHexString(CRC(input)));
                Log.d("Response","\tResponse: [000000" + recordsCount + "]\n");
                input = ReadInput();
                Log.d("Input","NotNull:" + input);
            }
        }
    }

    private String GetNumberOfRecords(String data) {
        return data.substring(18, 20);
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

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            converter = new Converter();
            crc16 = new Crc16();
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            String msgReply = "Zinute:" + cnt;

            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();

                message += "replayed: " + msgReply + "\n";

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        activity.msg.setText(message);
                    }
                });

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });
        }

    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}
