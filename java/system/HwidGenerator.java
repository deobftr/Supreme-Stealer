/*\n * ========================================\n * DEOBFUSCATED: HwidGenerator.java\n * Original: a/a.java\n * ----------------------------------------\n * Donanım kimliği (HWID) üretici - WMI/wmic kullanarak benzersiz makine ID si oluşturur\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package a;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.UUID;
import java.util.regex.Pattern;

public final class a {
    private static final Pattern UUID_PATTERN = Pattern.compile("(?i)^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    private static volatile String cachedHwid;

    private a() {
    }

    public static synchronized String getHwid() {
        if (b != null && !b.isEmpty()) {
            return b;
        }
        b = a.a.b();
        return b;
    }

    private static String generateHwid() {
        String string = a.a.d();
        if (a.a.a(string)) {
            return string.trim();
        }
        String string2 = a.a.e();
        if (!a.a.a(string2)) {
            string2 = a.a.f();
        }
        if (a.a.a(string2)) {
            a.a.b(string2.trim());
            return string2.trim();
        }
        if (string != null && !string.trim().isEmpty()) {
            return string.trim();
        }
        String string3 = "SP-" + UUID.nameUUIDFromBytes((a.a.d("COMPUTERNAME") + "|" + a.a.d("USERNAME") + "|" + a.a.e("user.name")).getBytes(StandardCharsets.UTF_8)).toString().toUpperCase();
        a.a.b(string3);
        return string3;
    }

    private static boolean isValidUuid(String uuid) {
        return string != null && a.matcher(string.trim()).matches();
    }

    private static File getHwidFile() {
        File file;
        String string = System.getenv("LOCALAPPDATA");
        if (string == null || string.isEmpty()) {
            string = System.getProperty("java.io.tmpdir");
        }
        if (!(file = new File(string, "halos")).isDirectory()) {
            file.mkdirs();
        }
        return new File(file, "hwid.txt");
    }

    private static String getFromWmic() {
        try {
            File file = a.a.c();
            if (!file.isFile()) {
                return null;
            }
            String string = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8).trim();
            return string.isEmpty() ? null : string;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static void saveToFile(String hwid) {
        try {
            Files.write(a.a.c().toPath(), string.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static String getFromRegistry() {
        try {
            Process process = new ProcessBuilder("powershell.exe", "-NoProfile", "-NonInteractive", "-Command", "(Get-CimInstance Win32_ComputerSystemProduct).UUID").redirectErrorStream(true).start();
            String string = a.a.a(process);
            process.waitFor();
            return a.a.c(string);
        }
        catch (Exception exception) {
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static String generateRandom() {
        try {
            Process process = new ProcessBuilder("wmic", "csproduct", "get", "uuid").redirectErrorStream(true).start();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));){
                String string;
                while ((string = bufferedReader.readLine()) != null) {
                    String string2;
                    if ((string = string.trim()).isEmpty() || string.equalsIgnoreCase("UUID") || (string2 = a.a.c(string)) == null) continue;
                    process.waitFor();
                    String string3 = string2;
                    return string3;
                }
            }
            process.waitFor();
            return null;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static String readFirstLine(String filePath) {
        if (string == null) {
            return null;
        }
        String string2 = string.trim();
        if (a.a.a(string2)) {
            return string2.toUpperCase();
        }
        return null;
    }

    private static String readProcessOutput(Process process) throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));){
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                if ((string = string.trim()).isEmpty()) continue;
                String string2 = string;
                return string2;
            }
        }
        return null;
    }

    private static String runWmicQuery(String query) {
        String string2 = System.getenv(string);
        return string2 == null ? "" : string2;
    }

    private static String readRegistryKey(String regPath) {
        String string2 = System.getProperty(string);
        return string2 == null ? "" : string2;
    }
}

