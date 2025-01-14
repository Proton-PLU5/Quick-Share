package me.mathewcibi.quickshare.utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkServer {
    private ServerSocket serverSocket;
    private DataInputStream inputStream;
    private static final int PORT = 8080;
    public static final String STOP_STRING = "##STOP##";

    private String fileName = "";
    private byte[] fileData;

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
        try {
            while (true) {
                String fileName = inputStream.readUTF();
                System.out.println("Received: " + fileName);
                int length = inputStream.readInt();
                byte[] data = new byte[length];
                inputStream.readFully(data, 0, data.length);
                File output = new File(fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(output);
                fileOutputStream.write(data);
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
