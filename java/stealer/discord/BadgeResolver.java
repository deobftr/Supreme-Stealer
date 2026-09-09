/*\n * ========================================\n * DEOBFUSCATED: BadgeResolver.java\n * Original: d/o.java\n * ----------------------------------------\n * Discord rozet + Nitro tier çözücü - flag bitmask ten emoji ya dönüştürür\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package d;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class o {
    public static String resolveBadges(int n2, JsonObject jsonObject) {
        StringBuilder stringBuilder = new StringBuilder();
        if (jsonObject != null && jsonObject.has("badges") && jsonObject.get("badges").isJsonArray()) {
            JsonArray jsonArray = jsonObject.getAsJsonArray("badges");
            for (JsonElement jsonElement : jsonArray) {
                String string;
                JsonObject jsonObject2;
                if (!jsonElement.isJsonObject() || !(jsonObject2 = jsonElement.getAsJsonObject()).has("id")) continue;
                switch (string = jsonObject2.get("id").getAsString()) {
                    case "staff": {
                        stringBuilder.append("<:staff:1362105228719034679>").append(" ");
                        break;
                    }
                    case "partner": {
                        stringBuilder.append("<:partner:1362105185094336622>").append(" ");
                        break;
                    }
                    case "hypesquad": {
                        stringBuilder.append("<:hype:1475541995517903092>").append(" ");
                        break;
                    }
                    case "bug_hunter_level_1": {
                        stringBuilder.append("<:bughunter1:1362105034157981758>").append(" ");
                        break;
                    }
                    case "early_supporter": {
                        stringBuilder.append("<:pig:1362105166811103515>").append(" ");
                        break;
                    }
                    case "bug_hunter_level_2": {
                        stringBuilder.append("<:bughunter2:1362105047462314293>").append(" ");
                        break;
                    }
                    case "verified_developer": {
                        stringBuilder.append("<:dev:1362105068060676329>").append(" ");
                        break;
                    }
                    case "certified_moderator": {
                        stringBuilder.append("<:mod:1362105108170539229>").append(" ");
                        break;
                    }
                    case "active_developer": {
                        stringBuilder.append("<:activedev:1362104965065212074>").append(" ");
                    }
                }
            }
        } else {
            if ((n2 & 1) == 1) {
                stringBuilder.append("<:staff:1362105228719034679>").append(" ");
            }
            if ((n2 & 2) == 2) {
                stringBuilder.append("<:partner:1362105185094336622>").append(" ");
            }
            if ((n2 & 4) == 4) {
                stringBuilder.append("<:hype:1475541995517903092>").append(" ");
            }
            if ((n2 & 8) == 8) {
                stringBuilder.append("<:bughunter1:1362105034157981758>").append(" ");
            }
            if ((n2 & 0x200) == 512) {
                stringBuilder.append("<:pig:1362105166811103515>").append(" ");
            }
            if ((n2 & 0x4000) == 16384) {
                stringBuilder.append("<:bughunter2:1362105047462314293>").append(" ");
            }
            if ((n2 & 0x20000) == 131072) {
                stringBuilder.append("<:dev:1362105068060676329>").append(" ");
            }
            if ((n2 & 0x40000) == 262144) {
                stringBuilder.append("<:mod:1362105108170539229>").append(" ");
            }
            if ((n2 & 0x400000) == 0x400000) {
                stringBuilder.append("<:activedev:1362104965065212074>").append(" ");
            }
        }
        return stringBuilder.toString().trim();
    }

    public static boolean hasBadges(JsonObject jsonObject) {
        if (jsonObject != null && jsonObject.has("badges") && jsonObject.get("badges").isJsonArray()) {
            JsonArray jsonArray = jsonObject.getAsJsonArray("badges");
            for (JsonElement jsonElement : jsonArray) {
                String string;
                JsonObject jsonObject2;
                if (!jsonElement.isJsonObject() || !(jsonObject2 = jsonElement.getAsJsonObject()).has("id") || !"quest_completed".equals(string = jsonObject2.get("id").getAsString())) continue;
                return true;
            }
        }
        return false;
    }

    public static String resolveFlagBadges(int flags) {
        StringBuilder stringBuilder = new StringBuilder();
        if ((n2 & 1) == 1) {
            stringBuilder.append("<:staff:1362105228719034679>").append(" ");
        }
        if ((n2 & 2) == 2) {
            stringBuilder.append("<:partner:1362105185094336622>").append(" ");
        }
        if ((n2 & 4) == 4) {
            stringBuilder.append("<:hype:1475541995517903092>").append(" ");
        }
        if ((n2 & 8) == 8) {
            stringBuilder.append("<:bughunter1:1362105034157981758>").append(" ");
        }
        if ((n2 & 0x200) == 512) {
            stringBuilder.append("<:pig:1362105166811103515>").append(" ");
        }
        if ((n2 & 0x4000) == 16384) {
            stringBuilder.append("<:bughunter2:1362105047462314293>").append(" ");
        }
        if ((n2 & 0x20000) == 131072) {
            stringBuilder.append("<:dev:1362105068060676329>").append(" ");
        }
        if ((n2 & 0x40000) == 262144) {
            stringBuilder.append("<:mod:1362105108170539229>").append(" ");
        }
        if ((n2 & 0x400000) == 0x400000) {
            stringBuilder.append("<:activedev:1362104965065212074>").append(" ");
        }
        return stringBuilder.toString().trim();
    }

    public static String resolveNitroTier(String months) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneOffset.UTC);
            OffsetDateTime offsetDateTime2 = OffsetDateTime.parse(string);
            long l2 = ChronoUnit.MONTHS.between(offsetDateTime2, offsetDateTime);
            if (l2 < 1L) {
                return "<:klasiknitro:1501743844432154634>";
            }
            if (l2 < 3L) {
                return "<:bronze:1365454925357645994>";
            }
            if (l2 < 6L) {
                return "<:silver:1365454972962996254>";
            }
            if (l2 < 12L) {
                return "<:gold:1365454994337435739>";
            }
            if (l2 < 24L) {
                return "<:platinum:1365455020690243737>";
            }
            if (l2 < 36L) {
                return "<:diamond:1365455075937488967>";
            }
            if (l2 < 60L) {
                return "<:emerald:1365455096296509524>";
            }
            if (l2 < 72L) {
                return "<:ruby:1365455125187137536>";
            }
            return "<:opal:1365455150260551740>";
        }
        catch (Exception exception) {
            return "<:bronze:1365454925357645994>";
        }
    }

    public static String resolveNitroTier(String string) {
        if (string == null || string.isEmpty()) {
            return "";
        }
        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneOffset.UTC);
            OffsetDateTime offsetDateTime2 = OffsetDateTime.parse(string);
            long l2 = ChronoUnit.MONTHS.between(offsetDateTime2, offsetDateTime);
            if (l2 < 2L) {
                return "<:boost1:1362104840250986667>";
            }
            if (l2 < 3L) {
                return "<:boost2:1362104851575607636>";
            }
            if (l2 < 6L) {
                return "<:boost3:1362104863084904830>";
            }
            if (l2 < 9L) {
                return "<:boost4:1362104873600024857>";
            }
            if (l2 < 12L) {
                return "<:boost5:1362104892226928812>";
            }
            if (l2 < 15L) {
                return "<:boost6:1362104904348467431>";
            }
            if (l2 < 18L) {
                return "<:boost7:1362104916247707658>";
            }
            if (l2 < 24L) {
                return "<:boost8:1362104931745530197>";
            }
            return "<:boost9:1362104950938796164>";
        }
        catch (Exception exception) {
            return "";
        }
    }

    public static String resolveBoostTier(int months, String nitro, String boostSince) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (n2) {
            case 1: {
                stringBuilder.append("<:klasiknitro:1501743844432154634>");
                break;
            }
            case 2: {
                String string3;
                String string4 = o.a(string);
                if (string4 != null) {
                    stringBuilder.append(string4);
                }
                if (string2 == null || string2.isEmpty() || (string3 = o.b(string2)).isEmpty()) break;
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(string3);
                break;
            }
            case 3: {
                stringBuilder.append("<:klasiknitro:1501743844432154634>");
                break;
            }
            default: {
                return "";
            }
        }
        return stringBuilder.toString().trim();
    }
}

