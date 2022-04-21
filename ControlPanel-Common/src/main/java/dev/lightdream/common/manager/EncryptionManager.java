package dev.lightdream.common.manager;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class EncryptionManager {

    private final KeyPair keyPair = getKeyPair();

    @SneakyThrows
    public String encrypt(String message) {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());   
        byte[] secretMessageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        return Base64.getEncoder().encodeToString(encryptedMessageBytes);
    }

    @SneakyThrows
    public String decrypt(String message) {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] secretMessageBytes = Base64.getDecoder().decode(message);
        byte[] decryptedMessageBytes = decryptCipher.doFinal(secretMessageBytes);
        return new String(decryptedMessageBytes, StandardCharsets.UTF_8);
    }

    //TODO make private
    @SneakyThrows
    public KeyPair getKeyPair() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.genKeyPair();
    }
}
