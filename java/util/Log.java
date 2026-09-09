/*\n * ========================================\n * DEOBFUSCATED: Log.java\n * Original: c/Log.java\n * ----------------------------------------\n * Loglama - LOCALAPPDATA/halos/agent.log a yazar\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public final class Log {
    private Log() {
    }

    public static void agent(String string) {
        try {
            File file;
            String string2 = System.getenv("LOCALAPPDATA");
            if (string2 == null || string2.isEmpty()) {
                string2 = System.getProperty("java.io.tmpdir");
            }
            if (!(file = new File(string2, "halos")).isDirectory()) {
                file.mkdirs();
            }
            File file2 = new File(file, "agent.log");
            String string3 = System.currentTimeMillis() + " " + (string == null ? "" : string) + "\n";
            try (FileOutputStream fileOutputStream = new FileOutputStream(file2, true);){
                fileOutputStream.write(string3.getBytes(StandardCharsets.UTF_8));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

