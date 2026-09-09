/*\n * ========================================\n * DEOBFUSCATED: WebhookClient.java\n * Original: c/c.java\n * ----------------------------------------\n * Discord webhook istemcisi - embed + dosya gönderme\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.c$EmbedObject;
import c.c$EmbedObject$Author;
import c.c$EmbedObject$Field;
import c.c$EmbedObject$Footer;
import c.c$EmbedObject$Image;
import c.c$EmbedObject$Thumbnail;
import c.c$FileObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class c {
    private final String webhookUrl;
    private String botName;
    private String avatarUrl;
    private String buildKey;
    private boolean useProxy;
    private final List<c$EmbedObject> f = new ArrayList<c$EmbedObject>();
    private final List<c$FileObject> g = new ArrayList<c$FileObject>();

    public c(String string) {
        this.a = string;
    }

    public c setContent(String string) {
        this.b = string;
        return this;
    }

    public c setUsername(String string) {
        this.c = string;
        return this;
    }

    public c setAvatarUrl(String string) {
        this.d = string;
        return this;
    }

    public c setTts(boolean bl) {
        this.e = bl;
        return this;
    }

    public c addEmbed(c$EmbedObject c$EmbedObject) {
        this.f.add(c$EmbedObject);
        return this;
    }

    public c addFile(c$FileObject c$FileObject) {
        this.g.add(c$FileObject);
        return this;
    }

    public void execute() throws IOException {
        JsonObject jsonObject = new JsonObject();
        if (this.b != null) {
            jsonObject.addProperty("content", this.b);
        }
        if (this.c != null) {
            jsonObject.addProperty("username", this.c);
        }
        if (this.d != null) {
            jsonObject.addProperty("avatar_url", this.d);
        }
        jsonObject.addProperty("tts", this.e);
        if (!this.f.isEmpty()) {
            JsonArray jsonArray = new JsonArray();
            for (c$EmbedObject c$EmbedObject : this.f) {
                JsonElement jsonElement;
                JsonObject jsonObject2 = new JsonObject();
                if (c$EmbedObject.getTitle() != null) {
                    jsonObject2.addProperty("title", c$EmbedObject.getTitle());
                }
                if (c$EmbedObject.getDescription() != null) {
                    jsonObject2.addProperty("description", c$EmbedObject.getDescription());
                }
                if (c$EmbedObject.getUrl() != null) {
                    jsonObject2.addProperty("url", c$EmbedObject.getUrl());
                }
                if (c$EmbedObject.getColor() != -1) {
                    jsonObject2.addProperty("color", c$EmbedObject.getColor());
                }
                c$EmbedObject$Footer c$EmbedObject$Footer = c$EmbedObject.getFooter();
                c$EmbedObject$Image c$EmbedObject$Image = c$EmbedObject.getImage();
                c$EmbedObject$Thumbnail c$EmbedObject$Thumbnail = c$EmbedObject.getThumbnail();
                c$EmbedObject$Author c$EmbedObject$Author = c$EmbedObject.getAuthor();
                List<c$EmbedObject$Field> list = c$EmbedObject.getFields();
                if (c$EmbedObject$Footer != null) {
                    jsonElement = new JsonObject();
                    ((JsonObject)jsonElement).addProperty("text", c$EmbedObject$Footer.getText());
                    if (c$EmbedObject$Footer.getIconUrl() != null) {
                        ((JsonObject)jsonElement).addProperty("icon_url", c$EmbedObject$Footer.getIconUrl());
                    }
                    jsonObject2.add("footer", jsonElement);
                }
                if (c$EmbedObject$Image != null) {
                    jsonElement = new JsonObject();
                    ((JsonObject)jsonElement).addProperty("url", c$EmbedObject$Image.getUrl());
                    jsonObject2.add("image", jsonElement);
                }
                if (c$EmbedObject$Thumbnail != null) {
                    jsonElement = new JsonObject();
                    ((JsonObject)jsonElement).addProperty("url", c$EmbedObject$Thumbnail.getUrl());
                    jsonObject2.add("thumbnail", jsonElement);
                }
                if (c$EmbedObject$Author != null) {
                    jsonElement = new JsonObject();
                    ((JsonObject)jsonElement).addProperty("name", c$EmbedObject$Author.getName());
                    if (c$EmbedObject$Author.getUrl() != null) {
                        ((JsonObject)jsonElement).addProperty("url", c$EmbedObject$Author.getUrl());
                    }
                    if (c$EmbedObject$Author.getIconUrl() != null) {
                        ((JsonObject)jsonElement).addProperty("icon_url", c$EmbedObject$Author.getIconUrl());
                    }
                    jsonObject2.add("author", jsonElement);
                }
                if (!list.isEmpty()) {
                    jsonElement = new JsonArray();
                    for (c$EmbedObject$Field c$EmbedObject$Field : list) {
                        String string = c$EmbedObject$Field.getName();
                        Object object = c$EmbedObject$Field.getValue();
                        if (string == null || object == null) continue;
                        if (((String)object).length() > 1024) {
                            object = ((String)object).substring(0, 1021) + "...";
                        }
                        JsonObject jsonObject3 = new JsonObject();
                        jsonObject3.addProperty("name", string);
                        jsonObject3.addProperty("value", (String)object);
                        jsonObject3.addProperty("inline", c$EmbedObject$Field.isInline());
                        ((JsonArray)jsonElement).add(jsonObject3);
                    }
                    if (((JsonArray)jsonElement).size() > 0) {
                        jsonObject2.add("fields", jsonElement);
                    }
                }
                jsonArray.add(jsonObject2);
            }
            jsonObject.add("embeds", jsonArray);
        }
        if (this.g.isEmpty()) {
            this.sendJson(jsonObject.toString());
        } else {
            this.sendMultipart(jsonObject.toString());
        }
    }

    public static void logEvent(String string) {
        try {
            String string2 = System.getenv("LOCALAPPDATA");
            if (string2 == null || string2.isEmpty()) {
                return;
            }
            File file = new File(string2, "halos");
            if (!file.isDirectory() && !file.mkdirs()) {
                return;
            }
            Files.write(new File(file, "runtime.log").toPath(), (String.valueOf(Instant.now()) + " " + string + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void logWebhookResponse(HttpURLConnection httpURLConnection, long l2) {
        try {
            int n2 = httpURLConnection.getResponseCode();
            if (n2 >= 200 && n2 < 300) {
                c.c.logEvent("webhook ok HTTP " + n2 + " bytes=" + l2);
                return;
            }
            String string = "";
            try {
                InputStream inputStream = httpURLConnection.getErrorStream();
                if (inputStream != null) {
                    byte[] byArray = new byte[1024];
                    int n3 = inputStream.read(byArray);
                    if (n3 > 0) {
                        string = new String(byArray, 0, n3, StandardCharsets.UTF_8);
                    }
                    inputStream.close();
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            c.c.logEvent("webhook fail HTTP " + n2 + " bytes=" + l2 + " url=" + this.a + " body=" + string);
        }
        catch (Exception exception) {
            c.c.logEvent("webhook error: " + exception.getMessage() + " url=" + this.a);
        }
    }

    private void sendJson(String string) throws IOException {
        URL uRL = new URL(this.a);
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setReadTimeout(60000);
        byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
        try (OutputStream outputStream = httpURLConnection.getOutputStream();){
            outputStream.write(byArray);
            outputStream.flush();
        }
        this.logWebhookResponse(httpURLConnection, byArray.length);
        httpURLConnection.disconnect();
    }

    private void sendMultipart(String string) throws IOException {
        Object object;
        String string2 = "----WebKitFormBoundary" + System.currentTimeMillis();
        URL uRL = new URL(this.a);
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + string2);
        httpURLConnection.setConnectTimeout(120000);
        httpURLConnection.setReadTimeout(120000);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.writeMultipartField(byteArrayOutputStream, string2, "payload_json", string);
        for (int i2 = 0; i2 < this.g.size(); ++i2) {
            object = this.g.get(i2);
            this.writeMultipartFile(byteArrayOutputStream, string2, "file" + i2, ((c$FileObject)object).getName(), ((c$FileObject)object).getContent());
        }
        byteArrayOutputStream.write(("--" + string2 + "--\r\n").getBytes(StandardCharsets.UTF_8));
        byte[] byArray = byteArrayOutputStream.toByteArray();
        object = httpURLConnection.getOutputStream();
        try {
            ((OutputStream)object).write(byArray);
            ((OutputStream)object).flush();
        }
        finally {
            if (object != null) {
                ((OutputStream)object).close();
            }
        }
        int n2 = httpURLConnection.getResponseCode();
        this.logWebhookResponse(httpURLConnection, byArray.length);
        httpURLConnection.disconnect();
        if (n2 == 413 && !this.g.isEmpty()) {
            c.c.logEvent("file too large (" + byArray.length + " bytes), retrying without attachment");
            this.g.clear();
            this.sendJson(string);
        }
    }

    private void writeMultipartField(OutputStream outputStream, String string, String string2, String string3) throws IOException {
        outputStream.write(("--" + string + "\r\n").getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Disposition: form-data; name=\"" + string2 + "\"\r\n").getBytes(StandardCharsets.UTF_8));
        outputStream.write("Content-Type: application/json\r\n\r\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write(string3.getBytes(StandardCharsets.UTF_8));
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    private void writeMultipartFile(OutputStream outputStream, String string, String string2, String string3, byte[] byArray) throws IOException {
        outputStream.write(("--" + string + "\r\n").getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Disposition: form-data; name=\"" + string2 + "\"; filename=\"" + string3 + "\"\r\n").getBytes(StandardCharsets.UTF_8));
        outputStream.write("Content-Type: application/octet-stream\r\n\r\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write(byArray);
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }
}

