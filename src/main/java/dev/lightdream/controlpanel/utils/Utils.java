package dev.lightdream.controlpanel.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import dev.lightdream.controlpanel.Executor;
import dev.lightdream.controlpanel.database.Node;
import dev.lightdream.controlpanel.database.Server;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class Utils {

    public static Server getServer(String id) {
        return Executor.servers.stream().filter(server -> server.serverID.equals(id)).findFirst().orElse(null);
    }

    @SuppressWarnings("unused")
    public static Node getNode(String id) {
        return Executor.nodes.stream().filter(node -> node.nodeID.equals(id)).findFirst().orElse(null);
    }

    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    @SneakyThrows
    public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
        return "otpauth://totp/"
                + URLEncoder.encode(issuer + ":" + account, StandardCharsets.UTF_8).replace("+", "%20")
                + "?secret=" + URLEncoder.encode(secretKey, StandardCharsets.UTF_8).replace("+", "%20")
                + "&issuer=" + URLEncoder.encode(issuer, StandardCharsets.UTF_8).replace("+", "%20");
    }

    @SneakyThrows
    public static void createQRCode(String barCodeData, String filePath) {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, 0, 0);
        FileOutputStream out = new FileOutputStream(filePath);
        MatrixToImageWriter.writeToStream(matrix, "png", out);
        out.close();
    }

    @SuppressWarnings("unused")
    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static String base64Encode(String raw) {
        return Base64.getEncoder().encodeToString(raw.getBytes());
    }

    public static String base64Decode(String encoded) {
        return new String(Base64.getDecoder().decode(encoded));
    }
}
