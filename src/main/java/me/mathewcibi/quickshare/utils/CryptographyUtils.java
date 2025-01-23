package me.mathewcibi.quickshare.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class CryptographyUtils {
    private final PrivateKey privateKey;
    public final PublicKey publicKey;
    private SecretKey symmetricKey;

    public PublicKey recipientPublicKey;

    public CryptographyUtils() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public void generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // AES key size in bits
        this.symmetricKey = keyGenerator.generateKey();
    }

    public byte[] encryptDataWithSymmetricKey(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
        return cipher.doFinal(data);
    }

    public byte[] encryptSymmetricKeyWithRSA(PublicKey recipientPublicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, recipientPublicKey);
        return cipher.doFinal(symmetricKey.getEncoded());
    }

    public void decryptSymmetricKeyWithRSA(byte[] encryptedSymmetricKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decodedKey = cipher.doFinal(encryptedSymmetricKey);
        symmetricKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public byte[] decryptDataWithSymmetricKey(byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, symmetricKey);
        return cipher.doFinal(encryptedData);
    }
}