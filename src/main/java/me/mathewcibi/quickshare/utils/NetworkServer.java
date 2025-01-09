package me.mathewcibi.quickshare.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkServer {
    private ServerSocket serverSocket;
    private DataInputStream inputStream;
    private static final int PORT = 8080;
    public static final String STOP_STRING = "##STOP##";

    public NetworkServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            initConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initConnections() throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected: " + clientSocket.getInetAddress());
        inputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        readInputStream();
        closeConnections();
    }

    private void closeConnections() throws IOException {
        inputStream.close();
        serverSocket.close();
    }

    private void readInputStream() {
        String line = "";
        while (!line.equals(STOP_STRING)) {
            try {
                line = inputStream.readUTF();
                System.out.println(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
