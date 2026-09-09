/*\n * ========================================\n * DEOBFUSCATED: CookieUtils.java\n * Original: c/i.java\n * ----------------------------------------\n * Cookie formatlama - tarih çevirme, JSON/Netscape export\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.json.JSONArray;
import org.json.JSONObject;

public final class i {
    private static final long CHROME_EPOCH_OFFSET = 11644473600000000L;
    private static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ISO_INSTANT;
    static final long c = 730L;

    private i() {
    }

    static String exportBrowserName(String string) {
        if (string == null) {
            return "";
        }
        if ("Google Chrome".equals(string)) {
            return "Chrome";
        }
        return string;
    }

    static String ensureMinExpireIso(String string, long l2) {
        if (string == null || string.isEmpty() || l2 <= 0L) {
            return string;
        }
        try {
            Instant instant = Instant.parse(string);
            Instant instant2 = Instant.now().plus(l2, ChronoUnit.DAYS);
            if (instant.isBefore(instant2)) {
                return b.format(instant2);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return string;
    }

    static String chromiumMicrosToIso(long l2) {
        if (l2 <= 0L) {
            return "";
        }
        long l3 = l2 - 11644473600000000L;
        Instant instant = Instant.ofEpochSecond(l3 / 1000000L, l3 % 1000000L * 1000L);
        return b.format(instant);
    }

    static String chromiumMicrosToIsoFromUnixSeconds(long l2) {
        if (l2 <= 0L) {
            return "";
        }
        return b.format(Instant.ofEpochSecond(l2));
    }

    static String sanitizeCookieExportValue(String string) {
        if (string == null || string.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(string.length());
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 == '\t' || c2 == '\n' || c2 == '\r') {
                stringBuilder.append(' ');
                continue;
            }
            if (c2 < ' ' && c2 <= '\u007f') continue;
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }

    static byte[] stripCookieHash(byte[] byArray, String string) {
        if (byArray == null || byArray.length < 32 || string == null) {
            return byArray;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] byArray2 = messageDigest.digest(string.getBytes(StandardCharsets.UTF_8));
            boolean bl = true;
            for (int i2 = 0; i2 < 32; ++i2) {
                if (byArray[i2] == byArray2[i2]) continue;
                bl = false;
                break;
            }
            if (bl) {
                byte[] byArray3 = new byte[byArray.length - 32];
                System.arraycopy(byArray, 32, byArray3, 0, byArray3.length);
                return byArray3;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return byArray;
    }

    static void writeJsonArrayFile(File file, JSONArray jSONArray) throws Exception {
        File file2 = file.getParentFile();
        if (file2 != null && !file2.mkdirs() && !file2.isDirectory()) {
            throw new IllegalStateException("mkdir failed: " + String.valueOf(file2));
        }
        try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(file), StandardCharsets.UTF_8));){
            printWriter.print(jSONArray.toString(2));
        }
    }

    static JSONObject baseRow(String string, String string2) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("browser", i.exportBrowserName(string));
        jSONObject.put("profile", string2);
        return jSONObject;
    }

    public static String siteKey(String string) {
        if (string == null || string.isEmpty()) {
            return "unknown";
        }
        String string2 = string.startsWith(".") ? string.substring(1) : string;
        String[] stringArray = string2.split("\\.");
        if (stringArray.length <= 2) {
            return string2.toLowerCase();
        }
        return (stringArray[stringArray.length - 2] + "." + stringArray[stringArray.length - 1]).toLowerCase();
    }

    public static String safeFileName(String string) {
        if (string == null || string.isEmpty()) {
            return "unknown";
        }
        return string.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}

