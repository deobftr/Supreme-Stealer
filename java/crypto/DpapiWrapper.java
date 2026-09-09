/*\n * ========================================\n * DEOBFUSCATED: DpapiWrapper.java\n * Original: c/t.java\n * ----------------------------------------\n * Windows DPAPI sarmalayıcı - Crypt32Util.cryptUnprotectData çağrısı\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public final class t {
    private static volatile Method cryptUnprotectMethod;
    private static volatile boolean dpapiInitialized;

    private t() {
    }

    private static String cn() {
        return "com" + '.' + "sun" + '.' + "jna" + '.' + "platform" + '.' + "win32" + '.' + "Crypt32Util";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Method getMethod() throws Exception {
        if (a != null) {
            return a;
        }
        Class<t> clazz = t.class;
        synchronized (t.class) {
            if (a != null) {
                // ** MonitorExit[var0] (shouldn't be in output)
                return a;
            }
            Class<?> clazz2 = Class.forName(t.cn());
            a = clazz2.getMethod("cryptUnprotectData", byte[].class);
            b = true;
            // ** MonitorExit[var0] (shouldn't be in output)
            return a;
        }
    }

    public static byte[] unprotect(byte[] byArray) {
        try {
            return (byte[])t.getMethod().invoke(null, new Object[]{byArray});
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    public static String unprotectStr(byte[] byArray) {
        if (byArray == null || byArray.length == 0) {
            return "";
        }
        try {
            byte[] byArray2 = t.unprotect(byArray);
            if (byArray2 == null) {
                return "";
            }
            return new String(byArray2, StandardCharsets.UTF_8);
        }
        catch (Throwable throwable) {
            return "";
        }
    }

    static {
        b = false;
    }
}

