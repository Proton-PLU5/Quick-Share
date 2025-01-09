package me.mathewcibi.quickshare.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class NetworkClient {
    private Socket clientSocket;
    private DataOutputStream outputStream;
    private Scanner inputScanner;

    public NetworkClient(String IP) throws IOException {
        try {
            clientSocket = new Socket(IP, 8080);
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            inputScanner = new Scanner(System.in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeMessages();
    }

    private void writeMessages() throws IOException {
        String line = "";
        while (!line.equals(NetworkServer.STOP_STRING)) {
            try {
                line = inputScanner.nextLine();
                outputStream.writeUTF(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        close();
    }

    private void close() throws IOException {
        clientSocket.close();
        outputStream.close();
        inputScanner.close();
    }
}
