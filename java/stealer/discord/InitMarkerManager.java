/*\n * ========================================\n * DEOBFUSCATED: InitMarkerManager.java\n * Original: d/c.java\n * ----------------------------------------\n * İlk çalıştırma işaretçisi - TEMP/.ira_init dosyası ile tekrar çalıştırmayı önler\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package d;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class c {
    private static final String MARKER_PATH = System.getProperty("java.io.tmpdir") + File.separator + ".ira_init";

    public static boolean exists() {
        try {
            return Files.exists(Paths.get(a, new String[0]), new LinkOption[0]);
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static void createMarker() {
        try {
            Files.write(Paths.get(a, new String[0]), String.valueOf(System.currentTimeMillis()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void deleteMarker() {
        try {
            Files.deleteIfExists(Paths.get(a, new String[0]));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

