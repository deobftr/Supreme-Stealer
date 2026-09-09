/*\n * ========================================\n * DEOBFUSCATED: Helper.java\n * Original: a/h.java\n * ----------------------------------------\n * Yardımcı fonksiyonlar\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package a;

import a.g;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

class h
implements WebSocket.Listener {
    h() {
    }

    public void onOpen(WebSocket ws) {
        webSocket.request(1L);
    }

    public CompletionStage<?> onText(WebSocket ws, CharSequence text, boolean last) {
        webSocket.request(1L);
        return null;
    }

    public CompletionStage<?> onBinary(WebSocket ws, ByteBuffer data, boolean last) {
        webSocket.request(1L);
        return null;
    }

    public void onError(WebSocket ws, Throwable error) {
        if (g.k == webSocket) {
            g.k = null;
        }
    }

    public CompletionStage<?> onClose(WebSocket ws, int code, String reason) {
        if (g.k == webSocket) {
            g.k = null;
        }
        return null;
    }
}

