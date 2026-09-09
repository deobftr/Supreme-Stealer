/*\n * ========================================\n * DEOBFUSCATED: PeParser.java\n * Original: c/p.java\n * ----------------------------------------\n * PE32+ (x64) header ayrıştırıcı - export tablosundan Bootstrap fonksiyonunu bulur\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

final class p {
    static final int a = 40;
    static final int b = 41;
    static final int c = 1;
    static final int d = 42;
    static final int e = 44;
    static final int f = 48;
    static final int g = 64;
    static final int h = 32;
    static final int i = 32;
    static final int j = 64;
    static final int k = 72;
    static final int l = 80;
    static final int m = 88;
    static final int n = 96;

    private p() {
    }

    static int findExportFileOffset(byte[] byArray, String string) {
        int n2;
        int n3;
        ByteBuffer byteBuffer = ByteBuffer.wrap(byArray).order(ByteOrder.LITTLE_ENDIAN);
        if (byArray.length < 64 || byteBuffer.getShort(0) != 23117) {
            throw new IllegalArgumentException("invalid DOS header");
        }
        int n4 = byteBuffer.getInt(60);
        if (n4 + 24 > byArray.length || byteBuffer.getInt(n4) != 17744) {
            throw new IllegalArgumentException("invalid PE signature");
        }
        short s2 = byteBuffer.getShort(n4 + 24);
        if (s2 != 523) {
            throw new IllegalArgumentException("expected PE32+ image");
        }
        int n5 = n4 + 24;
        int n6 = byteBuffer.getInt(n5 + 112);
        int n7 = byteBuffer.getInt(n5 + 116);
        if (n6 == 0 || n7 == 0) {
            throw new IllegalArgumentException("no export directory");
        }
        short s3 = byteBuffer.getShort(n4 + 6);
        int n8 = p.rvaToFileOffset(byteBuffer, s3, n3 = n5 + byteBuffer.getShort(n4 + 20), n2 = p.findNamedExportRva(byArray, byteBuffer, s3, n3, n6, string));
        if (n8 < 0) {
            throw new IllegalArgumentException("export RVA has no file mapping");
        }
        return n8;
    }

    private static int findNamedExportRva(byte[] byArray, ByteBuffer byteBuffer, int n2, int n3, int n4, String string) {
        int n5 = p.rvaToFileOffset(byteBuffer, n2, n3, n4);
        if (n5 < 0) {
            throw new IllegalArgumentException("export directory not mapped");
        }
        int n6 = byteBuffer.getInt(n5 + 24);
        int n7 = byteBuffer.getInt(n5 + 28);
        int n8 = byteBuffer.getInt(n5 + 32);
        int n9 = byteBuffer.getInt(n5 + 36);
        int n10 = p.rvaToFileOffset(byteBuffer, n2, n3, n8);
        int n11 = p.rvaToFileOffset(byteBuffer, n2, n3, n7);
        int n12 = p.rvaToFileOffset(byteBuffer, n2, n3, n9);
        for (int i2 = 0; i2 < n6; ++i2) {
            String string2;
            int n13 = byteBuffer.getInt(n10 + i2 * 4);
            int n14 = p.rvaToFileOffset(byteBuffer, n2, n3, n13);
            if (n14 < 0 || !string.equals(string2 = p.readCString(byArray, n14))) continue;
            int n15 = byteBuffer.getShort(n12 + i2 * 2) & 0xFFFF;
            return byteBuffer.getInt(n11 + n15 * 4);
        }
        throw new IllegalArgumentException("export not found: " + string);
    }

    private static int rvaToFileOffset(ByteBuffer byteBuffer, int n2, int n3, int n4) {
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = n3 + i2 * 40;
            int n6 = byteBuffer.getInt(n5 + 8);
            int n7 = byteBuffer.getInt(n5 + 12);
            int n8 = byteBuffer.getInt(n5 + 16);
            int n9 = byteBuffer.getInt(n5 + 20);
            int n10 = Math.max(n6, n8);
            if (n4 < n7 || n4 >= n7 + n10) continue;
            return n4 - n7 + n9;
        }
        return -1;
    }

    private static String readCString(byte[] byArray, int n2) {
        int n3;
        for (n3 = n2; n3 < byArray.length && byArray[n3] != 0; ++n3) {
        }
        return new String(byArray, n2, n3 - n2, StandardCharsets.US_ASCII);
    }

    static void writeU64(byte[] byArray, int n2, long l2) {
        ByteBuffer.wrap(byArray, n2, 8).order(ByteOrder.LITTLE_ENDIAN).putLong(l2);
    }
}

