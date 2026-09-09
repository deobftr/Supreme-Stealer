/* DEOBFUSCATED: AbstractDbReader_Data.java (inner class) */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.d;
import c.f;
import c.j$Data;
import c.r;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class j {
    private final f profileData;
    private final String dbFileName;
    private final File tempCopy;

    public j(f f2, String string, String string2) {
        this.a = f2;
        this.b = string2;
        this.c = new File(f2.getDirectory(), string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void findResultSets() {
        block50: {
            if (!this.c.exists()) {
                return;
            }
            try {
                Class.forName("org.sqlite.JDBC");
            }
            catch (ClassNotFoundException classNotFoundException) {
                return;
            }
            File file = null;
            try {
                file = d.copyToTemp(this.c);
                if (file == null || !file.exists()) {
                    file = this.c;
                }
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" /* r.sq() */ + file.getAbsolutePath());
                     Statement statement = connection.createStatement();){
                    ResultSet resultSet;
                    boolean bl = false;
                    try {
                        resultSet = statement.executeQuery("SELECT * FROM " + this.b);
                        try {
                            bl = true;
                            while (resultSet.next()) {
                                try {
                                    this.onResultSetFound(resultSet);
                                }
                                catch (Exception exception) {}
                            }
                        }
                        finally {
                            if (resultSet != null) {
                                resultSet.close();
                            }
                        }
                    }
                    catch (SQLException sQLException) {
                        // empty catch block
                    }
                    if (bl || this.b == null) break block50;
                    try {
                        resultSet = statement.executeQuery("SELECT * FROM " + this.b.toUpperCase());
                        try {
                            while (resultSet.next()) {
                                try {
                                    this.onResultSetFound(resultSet);
                                }
                                catch (Exception exception) {}
                            }
                        }
                        finally {
                            if (resultSet != null) {
                                resultSet.close();
                            }
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
            catch (Exception exception) {
            }
            finally {
                if (file != null && file.exists() && !file.equals(this.c)) {
                    try {
                        file.delete();
                    }
                    catch (Exception exception) {}
                }
            }
        }
    }

    public final <T extends j$Data> String toCsvColumn(List<T> list) {
        String string = this.getProfile().getBrowser().getInfo().getBrowserName();
        StringBuilder stringBuilder = new StringBuilder();
        for (j$Data j$Data : list) {
            stringBuilder.append('\"').append(string).append("\",");
            stringBuilder.append(j$Data.toCsvColumn());
        }
        return stringBuilder.toString();
    }

    public abstract String toCsvColumn();

    public f getProfile() {
        return this.a;
    }

    public File getFile() {
        return this.c;
    }

    public abstract void onResultSetFound(ResultSet var1);
}

