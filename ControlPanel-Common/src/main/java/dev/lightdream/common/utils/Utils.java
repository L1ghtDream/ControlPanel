package dev.lightdream.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.data.Cookie;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;

import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Base64;

public class Utils {

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private static final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .create();
    public static int defaultTimeout = 2 * 1000;       // 2 seconds    (2000 milliseconds)
    public static int defaultWaitBeforeIteration = 50; // 0.05 seconds (50 milliseconds  )

    public static Server getServer(String id) {
        return CommonMain.instance.getServers().stream().filter(server -> server.id.equals(id)).findFirst().orElse(null);
    }

    public static Node getNode(String id) {
        return CommonMain.instance.getNodes().stream().filter(node -> node.id.equals(id)).findFirst().orElse(null);
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public static String base64Encode(String raw) {
        return Base64.getEncoder().encodeToString(raw.getBytes());
    }

    public static String base64Decode(String encoded) {
        return new String(Base64.getDecoder().decode(encoded));
    }


    public static String payloadToString(Message<?> message) {
        Object payload = message.getPayload();
        return (payload instanceof String ? (String) payload : new String((byte[]) payload, StandardCharsets.UTF_8));
    }

    @SuppressWarnings("unused")
    public static Cookie getCookieFromString(String cookie) {
        return fromJson(Utils.base64Decode(cookie), Cookie.class);
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    public static Double doubleOrNegative(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    public static Cookie getCookie(String cookie) {
        return Utils.fromJson(Utils.base64Decode(cookie), Cookie.class);
    }

    /**
     * @return Formats amount of memory initially in kb into KB, MB, GB, TB according to the size
     */
    @SuppressWarnings("unused")
    @NotNull
    private String formatMemory(Double usage) {
        if (usage < 1024) {
            return decimalFormat.format(usage) + "KB";
        }
        if (usage < 1024 * 1024) {
            return decimalFormat.format(usage / 1024) + "MB";
        }
        if (usage < 1024 * 1024 * 1024) {
            return decimalFormat.format(usage / 1024 / 1024) + "GB";
        }
        return decimalFormat.format(usage / 1024 / 1024 / 1024) + "TB";
    }

}
