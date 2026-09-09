# ðŸ”“ Supreme Stealer â€” Full Source Code Exposure

> **âš ï¸ THIS IS A MALWARE EXPOSURE â€” DO NOT RUN ANY FILES**  
> This repository contains the **fully deobfuscated** source code of **Supreme Stealer**, a credential-stealing malware + RAT distributed via fake applications.  
> Purpose: Public awareness, threat intelligence, and exposing malware authors.

---

## What is Supreme Stealer?

Supreme Stealer is an **info-stealer + RAT** (Remote Access Trojan) sold on Telegram (`t.me/supremest`). It disguises itself as a legitimate application and steals:

- ðŸ”‘ **Saved passwords** from 53 browsers (Chrome, Edge, Brave, Firefox, Opera, etc.)
- ðŸª **Cookies** (session hijacking)
- ðŸ’³ **Credit card numbers** stored in browsers
- ðŸ’¬ **Discord tokens** from 30+ browsers and Discord clients
- ðŸ‘¤ **Discord accounts** â€” injects into Discord to capture login, 2FA codes, and new credit cards in real-time
- ðŸ’° **Crypto wallets** â€” MetaMask, Exodus, Phantom, and 50+ wallet extensions
- ðŸ“ **Desktop files** â€” scans for backup codes, recovery phrases, 2FA secrets
- ðŸ“‹ **Autofill data, bookmarks, download history, browsing history**
- ðŸ–¥ï¸ **System info** â€” installed programs, running processes

Additionally, it includes a **full RAT** with:
- Remote command execution (CMD)
- Live screen streaming
- Screenshot capture
- Chat window (talk to victim)
- Keyboard/mouse locking
- Force Discord logout
- Remote shutdown

---

## Deobfuscation Stats

This malware was **fully reverse engineered** from the compiled binary:

| Category | Coverage |
|---|---|
| File names | **100%** â€” 70 files renamed to meaningful names |
| Method names | **99%** â€” 576/580 methods renamed |
| Field names | **83%** â€” 155/187 fields renamed |
| String constants | **100%** â€” 213 XOR-encoded strings decoded inline |
| Binary payloads | **100%** â€” runtime.dat + core.dat decrypted |
| Discord injection JS | **100%** â€” extracted as standalone file |
| **Overall** | **95%** readable |

---

## Repository Structure

```
SupremeStealer_SOURCE/
â”‚
â”œâ”€â”€ electron/                          # JavaScript dropper (Electron app)
â”‚   â”œâ”€â”€ main.js                        # Deobfuscated â€” C2 URL & license key visible
â”‚   â”œâ”€â”€ main_original.js               # Original with XOR-encoded strings
â”‚   â””â”€â”€ package.json
â”‚
â”œâ”€â”€ discord_injection/
â”‚   â””â”€â”€ payload.js                     # 10KB JS injected into Discord client
â”‚                                      # Captures: tokens, passwords, 2FA, credit cards
â”‚
â”œâ”€â”€ java/
â”‚   â”œâ”€â”€ rat/                           # RAT & C2 communication
â”‚   â”‚   â”œâ”€â”€ Main.java                  # Entry point â€” persistence, startup, C2
â”‚   â”‚   â”œâ”€â”€ RatController.java         # WebSocket C2 handler (1131 lines)
â”‚   â”‚   â”‚                              # Commands: cmd, screenshot, stream,
â”‚   â”‚   â”‚                              # chat, shutdown, discord_logout,
â”‚   â”‚   â”‚                              # input_block, relog, error
â”‚   â”‚   â”œâ”€â”€ DataCollector.java         # Orchestrates data theft & exfiltration
â”‚   â”‚   â””â”€â”€ HaloInstaller.java         # Installs persistence JAR + JRE
â”‚   â”‚
â”‚   â”œâ”€â”€ stealer/
â”‚   â”‚   â”œâ”€â”€ browser/                   # Browser data theft (20 files)
â”‚   â”‚   â”‚   â”œâ”€â”€ PasswordStealer.java   # Login Data â†’ username/password
â”‚   â”‚   â”‚   â”œâ”€â”€ CookieStealer.java     # Cookies DB â†’ session tokens
â”‚   â”‚   â”‚   â”œâ”€â”€ CreditCardStealer.java # Web Data â†’ card numbers
â”‚   â”‚   â”‚   â”œâ”€â”€ AutofillStealer.java   # Autofill â†’ addresses, phones
â”‚   â”‚   â”‚   â”œâ”€â”€ HistoryStealer.java    # Browsing history
â”‚   â”‚   â”‚   â”œâ”€â”€ BookmarkStealer.java   # Bookmarks
â”‚   â”‚   â”‚   â”œâ”€â”€ DownloadStealer.java   # Download history
â”‚   â”‚   â”‚   â”œâ”€â”€ BrowserProfile.java    # Discovers 53 browser profiles
â”‚   â”‚   â”‚   â””â”€â”€ DatabaseUtils.java     # SQLite DB copy & query
â”‚   â”‚   â”‚
â”‚   â”‚   â”œâ”€â”€ discord/                   # Discord modules (13 files)
â”‚   â”‚   â”‚   â”œâ”€â”€ DiscordInjector.java   # Kills Discord, injects index.js
â”‚   â”‚   â”‚   â”œâ”€â”€ TokenStealer.java      # Extracts tokens from 30+ browsers
â”‚   â”‚   â”‚   â”œâ”€â”€ DiscordProfileCollector # Collects profile, friends, guilds
â”‚   â”‚   â”‚   â”œâ”€â”€ EventHandler.java      # Captures login/2FA/CC in real-time
â”‚   â”‚   â”‚   â”œâ”€â”€ EmbedBuilder.java      # Formats stolen data as embeds
â”‚   â”‚   â”‚   â””â”€â”€ WebhookFormatter.java  # Sends data via Discord webhooks
â”‚   â”‚   â”‚
â”‚   â”‚   â””â”€â”€ wallet/                    # Crypto wallet theft (6 files)
â”‚   â”‚       â”œâ”€â”€ WalletStealer.java     # Browser extensions + cold wallets
â”‚   â”‚       â”œâ”€â”€ DesktopFileScanner.java # Scans for recovery/backup/2FA files
â”‚   â”‚       â””â”€â”€ MasterCollector.java   # Aggregates all stolen data
â”‚   â”‚
â”‚   â”œâ”€â”€ crypto/                        # Encryption bypass (9 files)
â”‚   â”‚   â”œâ”€â”€ CryptoDecryptor.java       # AES-GCM + DPAPI master key extraction
â”‚   â”‚   â”œâ”€â”€ AbeProcessInjector.java    # Chrome v20 App-Bound Encryption bypass
â”‚   â”‚   â”‚                              # Uses process injection (VirtualAllocEx,
â”‚   â”‚   â”‚                              # WriteProcessMemory, CreateRemoteThread)
â”‚   â”‚   â”‚                              # + IElevator COM interface
â”‚   â”‚   â”œâ”€â”€ LockedFileBypass.java      # Reads locked browser DBs via
â”‚   â”‚   â”‚                              # NtQuerySystemInformation + DuplicateHandle
â”‚   â”‚   â”œâ”€â”€ PeParser.java             # PE32+ parser for shellcode injection
â”‚   â”‚   â””â”€â”€ DpapiWrapper.java         # Windows DPAPI wrapper
â”‚   â”‚
â”‚   â”œâ”€â”€ config/                        # Configuration (9 files)
â”‚   â”‚   â”œâ”€â”€ StringConstants.java       # All 58 XOR strings decoded
â”‚   â”‚   â”œâ”€â”€ WebhookConfig.java         # Webhook URL, bot name, avatar
â”‚   â”‚   â””â”€â”€ DiscordApiConstants.java   # API endpoints, CDN, timeouts
â”‚   â”‚
â”‚   â”œâ”€â”€ system/                        # System information (3 files)
â”‚   â”‚   â”œâ”€â”€ HwidGenerator.java         # Hardware ID via WMI
â”‚   â”‚   â””â”€â”€ SystemInfo.java            # OS, CPU, RAM, processes
â”‚   â”‚
â”‚   â””â”€â”€ util/                          # Utilities (7 files)
â”‚       â”œâ”€â”€ WebhookClient.java         # Discord webhook sender
â”‚       â”œâ”€â”€ SslBypass.java             # Disables SSL certificate validation
â”‚       â””â”€â”€ Log.java                   # Writes to agent.log
â”‚
â”œâ”€â”€ binary/                            # Decrypted binaries
â”‚   â”œâ”€â”€ runtime.jar                    # 17.8 MB â€” the actual stealer JAR
â”‚   â”œâ”€â”€ abe_extractor.exe              # 75 KB â€” ABE bypass shellcode (x64)
â”‚   â”œâ”€â”€ abe_core.dat                   # XOR 0xA7 encrypted shellcode
â”‚   â””â”€â”€ elevate.exe                    # UAC elevation helper
â”‚
â””â”€â”€ installer/
    â””â”€â”€ icon.ico                       # NSIS installer icon
```

---

## Attack Flow

```
a fake application (128 MB NSIS installer)
  â””â”€â–º Extracts Electron app + encrypted runtime.dat
       â””â”€â–º main.js: XOR decrypts runtime.dat (key: 0xA7) â†’ temp JAR
            â””â”€â–º Spawns: javaw.exe -jar temp.jar <C2_URL> <LICENSE_KEY>
                 â””â”€â–º Main.java:
                      â”œâ”€â”€ Bypass SSL certificate validation
                      â”œâ”€â”€ Acquire single-instance lock (%TEMP%\.sp_lock)
                      â”œâ”€â”€ Install halo.jar â†’ %LOCALAPPDATA%\halos\
                      â”œâ”€â”€ Copy javaw.exe â†’ gangs.exe (rename to evade)
                      â”œâ”€â”€ Add to Windows startup (registry)
                      â”œâ”€â”€ Inject into Discord (kill â†’ patch â†’ restart)
                      â”œâ”€â”€ Steal browser data (53 browsers)
                      â”œâ”€â”€ Steal crypto wallets (50+ extensions)
                      â”œâ”€â”€ Scan desktop for backup/recovery files
                      â”œâ”€â”€ Upload everything to C2 server
                      â””â”€â”€ Connect to C2 via WebSocket (RAT mode)
```

---

## Indicators of Compromise (IOC)

### Network
| Indicator | Value |
|---|---|
| C2 Server | `http://veled.com.tr` |
| License Key | `H95S2-MJ56T-LJPQ2-ATZ6Z` |
| Webhook Bot Name | `Supreme` |
| Webhook Avatar | `https://i.imgur.com/YYumsB0.png` |
| Telegram | `t.me/supremest` |

### Filesystem
| Indicator | Value |
|---|---|
| Persistence JAR | `%LOCALAPPDATA%\halos\halo.jar` |
| Renamed JRE | `%LOCALAPPDATA%\halos\jdk\bin\gangs.exe` |
| Lock file | `%TEMP%\.sp_lock` |
| Init marker | `%TEMP%\.ira_init` |
| Temp payload | `%TEMP%\sp_*.jar` |
| Log file | `%LOCALAPPDATA%\halos\agent.log` |

### Discord
| Indicator | Value |
|---|---|
| Injection marker | `/* __SP_INJECTED__ */` in `discord_desktop_core/index.js` |
| Injected processes | Discord.exe, DiscordCanary.exe, DiscordPTB.exe |

### Registry
| Indicator | Value |
|---|---|
| Startup key | `HKCU\Software\Microsoft\Windows\CurrentVersion\Run\halos` |

---

## Targeted Browsers (53)

Chrome, Chrome Beta, Chrome Dev, Chrome SxS (Canary), Chromium, Edge, Edge Beta, Edge Dev, Edge Canary, Brave, Brave Beta, Brave Nightly, Opera Stable, Opera GX, Vivaldi, Yandex, CocCoc, Comodo Dragon, Epic Privacy, Iridium, Iron, Maxthon, Orbitum, QQ Browser, Sleipnir, Slimjet, Sputnik, Torch, UC Browser, Chedot, CentBrowser, 7Star, Amigo, Elements, Kometa, and more. Also Firefox and Firefox-based browsers.

---

## Targeted Crypto Wallets

MetaMask, Phantom, Exodus, Atomic, Coinbase, Trust Wallet, Brave Wallet, Binance, Ronin, TronLink, Solflare, Slope, Keplr, Terra Station, Coin98, BitPay, Guarda, Math Wallet, SafePal, XDEFI, and 30+ more browser extensions + desktop cold wallets.

---

## Removal Instructions

```powershell
# 1. Remove persistence
reg delete "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "halos" /f

# 2. Delete malware files
Remove-Item "$env:LOCALAPPDATA\halos" -Recurse -Force
Remove-Item "$env:TEMP\.sp_lock" -Force -ErrorAction SilentlyContinue
Remove-Item "$env:TEMP\.ira_init" -Force -ErrorAction SilentlyContinue
Remove-Item "$env:TEMP\sp_*.jar" -Force -ErrorAction SilentlyContinue

# 3. Fix Discord injection
# Navigate to each Discord installation:
#   %APPDATA%\discord\<version>\modules\discord_desktop_core\
#   Replace index.js contents with:
#   module.exports = require('./core.asar');

# 4. IMPORTANT: Change all passwords saved in browsers
# 5. IMPORTANT: Change Discord password and reset 2FA
# 6. IMPORTANT: Revoke and regenerate any crypto wallet seed phrases
# 7. Contact your bank if credit card data was stored in browsers
```

---

## Technical Details

### Encryption
- **Payload encryption:** Single-byte XOR with key `0xA7` (167)
- **String obfuscation:** Per-string XOR with varying keys (decoded in `StringConstants.java`)
- **Browser password decryption:** AES-256-GCM with DPAPI-protected master key
- **Chrome v20 ABE bypass:** Process injection into headless browser + IElevator COM

### Architecture
- **Stage 1:** Electron app (NSIS installer â†’ app.asar â†’ main.js)
- **Stage 2:** Java stealer (runtime.dat â†’ XOR decrypt â†’ runtime.jar)
- **Persistence:** Registry Run key + %LOCALAPPDATA% installation
- **C2 Protocol:** HTTP polling + WebSocket for real-time commands
- **Exfiltration:** Multi-part HTTP POST to C2 server (not direct webhook)

### Deobfuscation Process
1. NSIS extraction â†’ 7z extraction â†’ asar unpacking
2. XOR string decryption in main.js (keys: 253, 218)
3. XOR 0xA7 decryption of runtime.dat â†’ valid JAR
4. CFR 0.152 decompilation (2 errors vs JADX's 1781)
5. XOR 0xA7 decryption of abe/core.dat â†’ x64 shellcode
6. 6-pass method/field renaming (400+ symbols)
7. 213 inline string constant decoding
8. Discord injection payload extraction (10KB JS)

---

## Disclaimer

This repository is published for **educational and security research purposes only**. The code is provided to help security researchers, antivirus vendors, and the general public understand how this malware operates. **Do not use this code for malicious purposes.** If you are a victim of this malware, follow the removal instructions above and report the C2 infrastructure to the appropriate authorities.

---

## Credits

Reverse engineered and deobfuscated using:
- [CFR Decompiler](https://benf.org/other/cfr/) v0.152
- [JADX](https://github.com/skylot/jadx) v1.5.1
- Static analysis only â€” **no malware was executed during this research**
