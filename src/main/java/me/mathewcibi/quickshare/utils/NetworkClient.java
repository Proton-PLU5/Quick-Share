package me.mathewcibi.quickshare.utils;

import me.mathewcibi.quickshare.Quickshare;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkClient {
    private Socket clientSocket;
    private DataOutputStream outputStream;

    public NetworkClient(String IP) throws IOException, InterruptedException {
        try {
            clientSocket = new Socket(IP, 8080);
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeMessages();
    }

    private void writeMessages() throws IOException, InterruptedException {
        while (true) {
            synchronized (Quickshare.DATA_TO_SEND) {
                byte[] fileName = Quickshare.DATA_TO_SEND.poll();
                byte[] data = Quickshare.DATA_TO_SEND.poll();
                if (data != null) {
                    outputStream.writeUTF(new String(fileName));
                    outputStream.writeInt(data.length);
                    outputStream.write(data);
                }
                Quickshare.DATA_TO_SEND.wait();
            }
        }
    }

    private void close() throws IOException {
        clientSocket.close();
        outputStream.close();
    }
}
