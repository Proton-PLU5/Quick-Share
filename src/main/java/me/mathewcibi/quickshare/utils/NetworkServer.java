package me.mathewcibi.quickshare.utils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import me.mathewcibi.quickshare.Quickshare;
import me.mathewcibi.quickshare.scenes.MainScene;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NetworkServer extends Thread {
    private final TextArea transferLogArea;
    private ServerSocket serverSocket;
    private DataInputStream inputStream;
    private static final int PORT = 8080;
    public volatile boolean running = true;

    public NetworkServer(TextArea transferLogArea) {
        this.transferLogArea = transferLogArea;
        this.setName("NetworkServer");
    }

    @Override
    public void run() {
        while (running) {
            startServer();
        }

    }

    private void startServer() {
        try {
            System.out.println("Server starting...");
            serverSocket = new ServerSocket(PORT);
            initConnections();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initConnections() throws IOException {
        while (running) {
            acceptConnections();
            System.out.println("client disconnected");
            ((MainScene) transferLogArea.getScene()).disconnectedFromReceiver();
        }

    }

    private void acceptConnections() throws IOException {
        while (running) {
            Socket clientSocket = serverSocket.accept();
            transferLogArea.setText(transferLogArea.getText() + "Connected to: " + clientSocket.getInetAddress().getHostAddress() + "\n");
            inputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            outputStream.write(Quickshare.CRYPTOGRAPHY_UTILS.publicKey.getEncoded());
            System.out.println("Public key sent.");
            readInputStream(clientSocket);
        }
    }

    private void closeConnections() throws IOException {
        inputStream.close();
        serverSocket.close();
    }

    private void readInputStream(Socket clientSocket) {
        try {
            while (running && !clientSocket.isClosed()) {
                int keyLength = inputStream.readInt();
                byte[] encryptedSymmetricKey = new byte[keyLength];
                inputStream.readFully(encryptedSymmetricKey);
                Quickshare.CRYPTOGRAPHY_UTILS.decryptSymmetricKeyWithRSA(encryptedSymmetricKey);

                String isZip = inputStream.readUTF();
                String fileName = inputStream.readUTF();
                transferLogArea.setText(transferLogArea.getText() + "Receiving: " + fileName + "\n");
                int length = inputStream.readInt();

                byte[] encryptedData = new byte[length];
                inputStream.readFully(encryptedData, 0, length);

                // Decrypt the data
                byte[] data = Quickshare.CRYPTOGRAPHY_UTILS.decryptDataWithSymmetricKey(encryptedData);

                // Ask user where to store the file
                Platform.runLater(() -> {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setTitle("Select a directory to save the file");
                    File selectedDirectory = directoryChooser.showDialog(transferLogArea.getScene().getWindow());

                    // Save the file.
                    File output = new File(fileName);
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "/" + output);
                        fileOutputStream.write(data);
                        fileOutputStream.close();
                    } catch (IOException e) {
                        transferLogArea.setText(transferLogArea.getText() + "Error saving file: " + fileName + "\n");
                    }
                    transferLogArea.setText(transferLogArea.getText() + "Received: " + fileName + "\n");

                    if (isZip.equals("true")) {
                        transferLogArea.setText(transferLogArea.getText() + "Unzipping: " + fileName + "\n");
                        try {
                            byte[] buffer = new byte[1024];
                            ZipInputStream zis = new ZipInputStream(new FileInputStream(selectedDirectory.getAbsolutePath() + "/" + output));
                            ZipEntry zipEntry = zis.getNextEntry();
                            while (zipEntry != null) {
                                File newFile = newFile(new File(selectedDirectory.getAbsolutePath() + "/" + fileName.replace(".zip", "/")), zipEntry);
                                if (zipEntry.isDirectory()) {
                                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                                        throw new IOException("Failed to create directory " + newFile);
                                    }
                                } else {
                                    // fix for Windows-created archives
                                    File parent = newFile.getParentFile();
                                    if (!parent.isDirectory() && !parent.mkdirs()) {
                                        throw new IOException("Failed to create directory " + parent);
                                    }

                                    // write file content
                                    FileOutputStream fos = new FileOutputStream(newFile);
                                    int len;
                                    while ((len = zis.read(buffer)) > 0) {
                                        fos.write(buffer, 0, len);
                                    }
                                    fos.close();
                                }
                                zipEntry = zis.getNextEntry();
                            }

                            zis.closeEntry();
                            zis.close();

                            Files.deleteIfExists(new File(selectedDirectory.getAbsolutePath() + "/" + fileName).toPath());
                            transferLogArea.setText(transferLogArea.getText() + "Unzipped: " + fileName + "\n");
                            transferLogArea.setText(transferLogArea.getText() + "Transfer complete\n");
                        } catch (IOException e) {
                            transferLogArea.setText(transferLogArea.getText() + "Error unzipping file: " + fileName + "\n");
                        }
                    } else {
                        transferLogArea.setText(transferLogArea.getText() + "Transfer complete\n");
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}