package com.example.dominyko.avl.TCP_Model;

import android.util.Log;

import com.example.dominyko.avl.MainActivity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class TCPServer extends Thread {
    MainActivity activity;
        int seconds;
        private int port;
        private ServerSocket ss;
        private Socket socket;
        private boolean running = true;
        private ArrayList<TCPConnection> tcpConnections;

        /**
         * <h1>TCP server construct</h1>
         * <p>The tcp server takes two parameters view model and port </p>

         * @param port is required for server to listen all incoming connections
         * */
        public TCPServer(int port, int seconds) {

            this.port = port;
            this.seconds = seconds;
        }
        /**
         * <h1>Run</h1>
         * <p>Runs the runnable thread to listen connections, it accepts a connection, if accept was successful,
         * the connection is added to tcpConnections list and runs the TCPConnection for further listening.
         * The server is running in while loop and stops when Running is set to false,
         * then break is called and shutdowns every connected client.</p>
         * */
       /* private class Task extends AsyncTask<Void, String, String>
        {

            @Override
            protected String doInBackground(Void... voids) {
                tcpConnections = new ArrayList<>();
                try {
                    ss = new ServerSocket(port);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        socket = ss.accept();
                        TCPConnection tcpConnection = new TCPConnection(socket);
                        tcpConnection.run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        ss.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }


                }

            }
        }
        */

        @Override
        public void run() {
            tcpConnections = new ArrayList<>();
            try {
                ss = new ServerSocket(port);
                Log.d("Listening","Listening on port : " + ss.getLocalPort());
                while (true) {
                    Log.d("Listening","Waiting for Client");
                    socket = ss.accept();
                    if (socket.isConnected())
                    {
                        Log.d("SocketAccept", "client connected from: %s" + socket.getRemoteSocketAddress().toString());
                        TCPConnection tcpConnection = new TCPConnection(socket);
                        tcpConnections.add(tcpConnection);
                        tcpConnection.run();
                    }
                    else {
                        StopConnections();
                        break;
                    }
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                Log.d("Socket","socket is closed");
            }
        }

    /**
     * <h1>Set Flag</h1>
     * <p>Function is being called when we want to interrupt server thread and stop it.</p>
     * @param flag the parameter sets whenever to true(run server) or false(stop server)
     * */
    public void setFlag(boolean flag) {
        running = flag;
        if (!running) {
            try {
                ss.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                Log.d("Socket","Socket is " + socket.isClosed());
            }
        }
    }



    /**
     * <h1>Stop Connections</h1>
     * <p>Function is being called when we are stopping server,
     * this function iterates through every connection and stops it.</p>
     * */
    private void StopConnections() {
        if (!tcpConnections.isEmpty()) {
            for (TCPConnection connections : tcpConnections) {
                connections.setRunning();
            }
            tcpConnections.clear();
        }
    }
}

