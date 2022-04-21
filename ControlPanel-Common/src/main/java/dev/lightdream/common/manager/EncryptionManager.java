package dev.lightdream.common.manager;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class EncryptionManager {

    private final KeyPair keyPair = getKeyPair();

    @SneakyThrows
    public String encrypt(String message) {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] secretMessageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        return new String(encryptedMessageBytes, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public String decrypt(String message) {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] secretMessageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] decryptedMessageBytes = decryptCipher.doFinal(secretMessageBytes);
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        return decryptedMessage;
    }

    //TODO make private
    @SneakyThrows
    public KeyPair getKeyPair() {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        return kpg.genKeyPair();
    }
}
