package me.mathewcibi.quickshare.utils;

import me.mathewcibi.quickshare.Quickshare;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

public class NetworkClient extends Thread {
    public volatile boolean running = true;
    private Socket clientSocket;
    private DataOutputStream outputStream;

    public NetworkClient(String IP) throws IOException, InterruptedException {
        super.run();
        try {
            clientSocket = new Socket(IP, 8080);
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            byte[] receivedPublicKey = new byte[294];
            inputStream.readFully(receivedPublicKey);

            Quickshare.CRYPTOGRAPHY_UTILS.recipientPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(receivedPublicKey));

            System.out.println("Public key received.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setName("NetworkClient");
    }

    @Override
    public void run() {
        try {
            writeMessages();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeMessages() throws Exception {
        while (running) {
            synchronized (Quickshare.DATA_TO_SEND) {
                byte[] isZip = Quickshare.DATA_TO_SEND.poll();
                byte[] fileName = Quickshare.DATA_TO_SEND.poll();
                byte[] data = Quickshare.DATA_TO_SEND.poll();

                if (data != null) {
                    Quickshare.CRYPTOGRAPHY_UTILS.generateSymmetricKey();
                    byte[] encryptedData = Quickshare.CRYPTOGRAPHY_UTILS.encryptDataWithSymmetricKey(data);
                    byte[] encryptedSymmetricKey = Quickshare.CRYPTOGRAPHY_UTILS.encryptSymmetricKeyWithRSA(Quickshare.CRYPTOGRAPHY_UTILS.recipientPublicKey);

                    outputStream.writeInt(encryptedSymmetricKey.length);
                    outputStream.write(encryptedSymmetricKey);
                    outputStream.writeUTF(new String(isZip));
                    outputStream.writeUTF(new String(fileName));
                    outputStream.writeInt(encryptedData.length);
                    outputStream.write(encryptedData);
                }
                Quickshare.DATA_TO_SEND.wait();
            }
        }
    }

    public void close() throws IOException {
        running = false;
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }
}