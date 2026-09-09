/* DEOBFUSCATED: SslBypass_TrustAllManager.java (inner class) */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import c.Net$1;
import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.time.Duration;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public final class Net {
    private static volatile boolean sslInstalled;
    private static volatile SSLContext trustAllSsl;

    private Net() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void installTrustAll() {
        if (a) {
            return;
        }
        Class<Net> clazz = Net.class;
        synchronized (Net.class) {
            if (a) {
                // ** MonitorExit[var0] (shouldn't be in output)
                return;
            }
            try {
                TrustManager[] trustManagerArray = new TrustManager[]{new Net$1()};
                SSLContext sSLContext = SSLContext.getInstance("TLS");
                sSLContext.init(null, trustManagerArray, new SecureRandom());
                b = sSLContext;
                HttpsURLConnection.setDefaultSSLSocketFactory(sSLContext.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(Net::lambda$installTrustAll$0);
                a = true;
            }
            catch (Exception exception) {
                // empty catch block
            }
            return;
        }
    }

    public static SSLContext sslContext() {
        Net.installTrustAll();
        return b;
    }

    public static HttpClient newHttpClient(Duration duration) {
        Net.installTrustAll();
        HttpClient.Builder builder = HttpClient.newBuilder().connectTimeout(duration);
        if (b != null) {
            builder.sslContext(b);
            SSLParameters sSLParameters = new SSLParameters();
            sSLParameters.setEndpointIdentificationAlgorithm(null);
            builder.sslParameters(sSLParameters);
        }
        return builder.build();
    }

    private static boolean lambda$installTrustAll$0(String string, SSLSession sSLSession) {
        return true;
    }
}

