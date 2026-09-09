/*\n * ========================================\n * DEOBFUSCATED: RuntimeState.java\n * Original: d/r.java\n * ----------------------------------------\n * Çalışma zamanı durum yöneticisi - email/şifre çiftleri, token durumları\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package d;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class r {
    private final Map<String, String> a = new ConcurrentHashMap<String, String>();
    private final Map<String, String> b = new ConcurrentHashMap<String, String>();
    private final AtomicBoolean initComplete = new AtomicBoolean(false);
    private final Set<String> d = Collections.synchronizedSet(new HashSet());
    private String activeToken = "";
    private boolean injectionActive = false;

    public void storeCredential(String key, String value) {
        this.b.put("email", string);
        this.b.put("password", string2);
    }

    public void clearCredentials() {
        this.b.clear();
    }

    public String getEmail() {
        return this.b.getOrDefault("email", "");
    }

    public String getPassword() {
        return this.b.getOrDefault("password", "");
    }

    public void storeField(String key, String value) {
        if (string2 != null && !string2.isEmpty()) {
            this.a.put(string, string2);
        }
    }

    public String getTokenState(String token) {
        return this.a.get(string);
    }

    public void removeToken(String token) {
        this.a.remove(string);
    }

    public void addProcessedId(String id) {
        this.d.add(string);
    }

    public boolean isIdProcessed(String id) {
        return this.d.contains(string);
    }

    public void setCurrentToken(String token) {
        this.e = string;
    }

    public String getCurrentToken() {
        return this.e;
    }

    public boolean isInitDone() {
        return this.c.get();
    }

    public void setInitComplete(boolean done) {
        this.c.set(bl);
    }

    public boolean isInjectionRunning() {
        return this.f;
    }

    public void setInjectionRunning(boolean running) {
        this.f = bl;
    }
}

