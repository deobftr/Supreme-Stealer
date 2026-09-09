/*\n * ========================================\n * DEOBFUSCATED: BrowserEnumSwitch.java\n * Original: d/m.java\n * ----------------------------------------\n * Browser enum switch tablosu (CFR artifact)\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package d;

import c.e$Info;

class m {
    static final int[] a = new int[e$Info.values().length];

    static {
        try {
            m.a[e$Info.GOOGLE_CHROME.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            m.a[e$Info.OPERA_GX.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            m.a[e$Info.FIREFOX.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            m.a[e$Info.MICROSOFT_EDGE.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            m.a[e$Info.OPERA.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            m.a[e$Info.BRAVE.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            m.a[e$Info.YANDEX.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            m.a[e$Info.VIVALDI.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}

