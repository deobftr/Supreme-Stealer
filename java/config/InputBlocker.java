/*\n * ========================================\n * DEOBFUSCATED: Config.java\n * Original: a/c.java\n * ----------------------------------------\n * Yapılandırma sabitleri\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package a;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

interface c
extends StdCallLibrary {
    public static final c USER32 = Native.load("user32", c.class);

    public boolean onConfigChange(boolean var1);
}

