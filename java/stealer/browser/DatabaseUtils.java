/*\n * ========================================\n * DEOBFUSCATED: DatabaseUtils.java\n * Original: c/d.java\n * ----------------------------------------\n * SQLite veritabanı kopyalama ve şifre çözme yardımcıları\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.g;
import c.g$MasterKeys;
import c.r;
import c.s;
import com.sun.jna.Function;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class d {
    public static final String APPDATA = System.getenv("APPDATA");
    public static final String LOCALAPPDATA = System.getenv("LOCALAPPDATA");

    public static String formatDate(long l2) {
        return Instant.ofEpochMilli(-11641732431255L + TimeUnit.MICROSECONDS.toMillis(l2)).toString();
    }

    public static String getDate(ResultSet resultSet, String string) throws SQLException {
        return d.formatDate(resultSet.getLong(string));
    }

    public static String getEncrypted(ResultSet resultSet, String string, g$MasterKeys g$MasterKeys) throws SQLException {
        byte[] byArray = resultSet.getBytes(string);
        if (byArray == null || byArray.length == 0) {
            return "";
        }
        if (g$MasterKeys == null) {
            g$MasterKeys = new g$MasterKeys(null, null);
        }
        if (!g$MasterKeys.hasAny() && byArray.length >= 3 && byArray[0] == 118 && (byArray[1] == 49 && (byArray[2] == 48 || byArray[2] == 49) || byArray[1] == 50 && byArray[2] == 48)) {
            return "";
        }
        String string2 = g.decrypt(byArray, g$MasterKeys);
        if (string2 == null || string2.isEmpty()) {
            if (byArray.length >= 3 && byArray[0] == 118 && (byArray[1] == 49 && (byArray[2] == 48 || byArray[2] == 49) || byArray[1] == 50 && byArray[2] == 48)) {
                return "";
            }
            return new String(byArray, StandardCharsets.UTF_8);
        }
        return string2;
    }

    public static String getEncrypted(ResultSet resultSet, String string, byte[] byArray) throws SQLException {
        return d.getEncrypted(resultSet, string, new g$MasterKeys(byArray, byArray));
    }

    public static File copyToTemp(File file) throws IOException {
        Path path = Files.createTempFile("nf_", ".db", new FileAttribute[0]);
        File file2 = path.toFile();
        byte[] byArray = d.readShared(file.toPath());
        if (byArray == null || byArray.length == 0) {
            try {
                byArray = Files.readAllBytes(file.toPath());
            }
            catch (IOException iOException) {
                String string = System.getProperty("os.name", "").toLowerCase();
                if (!string.contains("win")) {
                    Files.deleteIfExists(path);
                    throw iOException;
                }
                try {
                    s.copyLocked(file, file2);
                    d.copySidecars(file, file2);
                    d.walCheckpoint(file2);
                    return file2;
                }
                catch (IOException iOException2) {
                    Files.deleteIfExists(path);
                    throw new IOException("copy failed for " + String.valueOf(file) + ": " + iOException.getMessage() + "; locked bypass: " + iOException2.getMessage(), iOException);
                }
            }
        }
        Files.write(path, byArray, new OpenOption[0]);
        d.copySidecars(file, file2);
        d.walCheckpoint(file2);
        return file2;
    }

    private static void copySidecars(File file, File file2) {
        for (String string : new String[]{"-wal", "-shm"}) {
            File file3 = new File(file.getAbsolutePath() + string);
            if (!file3.isFile()) continue;
            Path path = new File(file2.getAbsolutePath() + string).toPath();
            byte[] byArray = d.readShared(file3.toPath());
            try {
                if (byArray != null && byArray.length > 0) {
                    Files.write(path, byArray, new OpenOption[0]);
                    continue;
                }
                Files.copy(file3.toPath(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive exception aggregation
     */
    static byte[] readShared(Path path) {
        if (path == null || !Files.isRegularFile(path, new LinkOption[0])) {
            return null;
        }
        try {
            Function function = Function.getFunction("kernel32", "CreateFileW", 63);
            WinNT.HANDLE hANDLE = (WinNT.HANDLE)function.invoke(WinNT.HANDLE.class, new Object[]{new WString(path.toAbsolutePath().toString()), Integer.MIN_VALUE, 7, null, 3, 128, null});
            if (hANDLE == null || WinNT.INVALID_HANDLE_VALUE.equals(hANDLE)) {
                return null;
            }
            try {
                long l2 = Files.size(path);
                if (l2 <= 0L || l2 > 0x20000000L) {
                    byte[] byArray = null;
                    return byArray;
                }
                byte[] byArray = new byte[(int)l2];
                IntByReference intByReference = new IntByReference();
                if (!Kernel32.INSTANCE.ReadFile(hANDLE, byArray, byArray.length, intByReference, null) || intByReference.getValue() <= 0) {
                    byte[] byArray2 = null;
                    return byArray2;
                }
                int n2 = intByReference.getValue();
                if (n2 == byArray.length) {
                    byte[] byArray3 = byArray;
                    return byArray3;
                }
                byte[] byArray4 = new byte[n2];
                System.arraycopy(byArray, 0, byArray4, 0, n2);
                byte[] byArray5 = byArray4;
                return byArray5;
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(hANDLE);
            }
        }
        catch (Throwable throwable) {
            try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);){
                byte[] byArray;
                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();){
                    inputStream.transferTo(byteArrayOutputStream);
                    byArray = byteArrayOutputStream.toByteArray();
                }
                return byArray;
            }
            catch (IOException iOException) {
                return null;
            }
        }
    }

    private static void walCheckpoint(File file) {
        File file2 = new File(file.getAbsolutePath() + "-wal");
        if (!file2.isFile() || file2.length() == 0L) {
            return;
        }
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException classNotFoundException) {
            return;
        }
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" /* r.sq() */ + file.getAbsolutePath());
             Statement statement = connection.createStatement();){
            statement.execute("PRAGMA wal_checkpoint(TRUNCATE)");
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void writeZip(String string, String string2, ZipOutputStream zipOutputStream) {
        try {
            zipOutputStream.putNextEntry(new ZipEntry(string2));
            zipOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException iOException) {
        }
        finally {
            try {
                zipOutputStream.closeEntry();
            }
            catch (IOException iOException) {}
        }
    }

    public static String randomString(Random random, String string, int n2) {
        char[] cArray = new char[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            cArray[i2] = string.charAt(random.nextInt(string.length()));
        }
        return new String(cArray);
    }

    public static String randomNumericString(Random random, int n2) {
        return d.randomString(random, "0123456789", n2);
    }
}

