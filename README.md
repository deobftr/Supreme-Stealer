<div align="center">

# ðŸ›¡ï¸ Supreme Stealer â€” Exposed

### Fully Deobfuscated Source Code of a Dangerous Info-Stealer + RAT

[![Deobfuscation](https://img.shields.io/badge/Deobfuscation-100%25-brightgreen?style=for-the-badge&logo=shield)](.)
[![Methods](https://img.shields.io/badge/Methods_Renamed-580%2F580-blue?style=for-the-badge&logo=java)](.)
[![Strings](https://img.shields.io/badge/Strings_Decoded-213%2F213-blue?style=for-the-badge&logo=keycdn)](.)
[![Files](https://img.shields.io/badge/Files_Renamed-70%2F70-blue?style=for-the-badge&logo=files)](.)

<img src="https://img.shields.io/badge/âš ï¸_MALWARE_EXPOSURE_â€”_DO_NOT_RUN-red?style=for-the-badge&labelColor=darkred" alt="warning"/>

> *This repository contains the fully reverse-engineered source code of **Supreme Stealer**.*
> *Published for security research, threat intelligence, and public awareness.*
> *No malware was executed during this analysis â€” pure static reverse engineering.*

</div>

---

## ðŸ” What is Supreme Stealer?

Supreme Stealer is a commercial **info-stealer + RAT** (Remote Access Trojan) sold on **Telegram** (`t.me/supremest`). It is distributed as fake applications and performs the following:

<table>
<tr>
<td width="50%">

### ðŸ•µï¸ Stealer Capabilities
| | Target |
|---|---|
| ðŸ”‘ | **Saved passwords** from 53 browsers |
| ðŸª | **Cookies** â€” session hijacking |
| ðŸ’³ | **Credit cards** stored in browsers |
| ðŸ’¬ | **Discord tokens** from 30+ sources |
| ðŸ’° | **Crypto wallets** â€” 50+ extensions |
| ðŸ“ | **Recovery files** â€” backup, 2FA, seeds |
| ðŸ“‹ | **Autofill, bookmarks, history** |
| ðŸ–¥ï¸ | **System info** â€” OS, CPU, processes |

</td>
<td width="50%">

### ðŸŽ® RAT Capabilities
| | Command |
|---|---|
| âŒ¨ï¸ | **Remote CMD** execution |
| ðŸ“º | **Live screen** streaming |
| ðŸ“¸ | **Screenshot** capture |
| ðŸ’¬ | **Chat** with victim |
| ðŸ”’ | **Lock** keyboard & mouse |
| ðŸšª | **Discord logout** â€” force |
| ðŸ”„ | **Relog** â€” restart malware |
| â›” | **Shutdown** victim's PC |

</td>
</tr>
</table>

---

## âš¡ Attack Flow

```mermaid
graph TD
    A["ðŸŽ­ FakeApp.exe<br/>NSIS Installer â€” 128 MB"] --> B["ðŸ“¦ Electron App<br/>app.asar + runtime.dat"]
    B --> C["ðŸ”“ XOR Decrypt<br/>key: 0xA7"]
    C --> D["â˜• runtime.jar<br/>17.8 MB Java Payload"]
    D --> E["ðŸ”§ Setup"]
    D --> F["ðŸ•µï¸ Steal"]
    D --> G["ðŸŽ® RAT"]
    
    E --> E1["ðŸ” SSL Bypass"]
    E --> E2["ðŸ“Œ Persistence<br/>Registry + Startup"]
    E --> E3["ðŸ”„ Install halo.jar<br/>Rename javaw â†’ gangs.exe"]
    
    F --> F1["ðŸ”‘ Browser Data<br/>53 browsers"]
    F --> F2["ðŸ’¬ Discord<br/>Inject + Steal tokens"]
    F --> F3["ðŸ’° Crypto Wallets<br/>50+ extensions"]
    F --> F4["ðŸ“ Desktop Scan<br/>backup, 2FA, seeds"]
    
    G --> G1["ðŸ“¡ WebSocket C2<br/>Real-time control"]

    style A fill:#ff4444,color:#fff
    style D fill:#ff6600,color:#fff
    style F1 fill:#cc0000,color:#fff
    style F2 fill:#cc0000,color:#fff
    style F3 fill:#cc0000,color:#fff
    style F4 fill:#cc0000,color:#fff
    style G1 fill:#990099,color:#fff
```

---

## ðŸ“‚ Repository Structure

```
Supreme-Stealer/
â”‚
â”œâ”€â”€ ðŸ“„ README.md
â”‚
â”œâ”€â”€ ðŸŒ electron/                          Electron dropper (JavaScript)
â”‚   â”œâ”€â”€ main.js                            Deobfuscated â€” C2 URL & license key exposed
â”‚   â”œâ”€â”€ main_original.js                   Original with XOR-encoded strings
â”‚   â””â”€â”€ package.json
â”‚
â”œâ”€â”€ ðŸ’‰ discord_injection/
â”‚   â””â”€â”€ payload.js                         10KB JS injected into Discord client
â”‚                                          Captures: tokens, passwords, 2FA, credit cards
â”‚
â”œâ”€â”€ â˜• java/
â”‚   â”‚
â”‚   â”œâ”€â”€ ðŸŽ® rat/                            RAT & C2 Communication
â”‚   â”‚   â”œâ”€â”€ Main.java                      Entry point â€” persistence, C2 connect
â”‚   â”‚   â”œâ”€â”€ RatController.java             WebSocket C2 handler (1131 lines)
â”‚   â”‚   â”œâ”€â”€ DataCollector.java             Orchestrates data theft & exfiltration
â”‚   â”‚   â””â”€â”€ HaloInstaller.java             Installs persistence JAR + bundled JRE
â”‚   â”‚
â”‚   â”œâ”€â”€ ðŸ•µï¸ stealer/
â”‚   â”‚   â”‚
â”‚   â”‚   â”œâ”€â”€ ðŸŒ browser/                   Browser Data Theft (20 files)
â”‚   â”‚   â”‚   â”œâ”€â”€ PasswordStealer.java        Login Data â†’ usernames & passwords
â”‚   â”‚   â”‚   â”œâ”€â”€ CookieStealer.java          Cookies â†’ session tokens
â”‚   â”‚   â”‚   â”œâ”€â”€ CreditCardStealer.java      Web Data â†’ card numbers
â”‚   â”‚   â”‚   â”œâ”€â”€ AutofillStealer.java        Autofill â†’ addresses, phones
â”‚   â”‚   â”‚   â”œâ”€â”€ BookmarkStealer.java        Bookmarks
â”‚   â”‚   â”‚   â”œâ”€â”€ HistoryStealer.java         Browsing history
â”‚   â”‚   â”‚   â”œâ”€â”€ DownloadStealer.java        Download history
â”‚   â”‚   â”‚   â”œâ”€â”€ BrowserProfile.java         Discovers 53 browser profiles
â”‚   â”‚   â”‚   â””â”€â”€ DatabaseUtils.java          SQLite DB copy & decryption
â”‚   â”‚   â”‚
â”‚   â”‚   â”œâ”€â”€ ðŸ’¬ discord/                    Discord Modules (13 files)
â”‚   â”‚   â”‚   â”œâ”€â”€ DiscordInjector.java        Kills Discord â†’ patches index.js â†’ restarts
â”‚   â”‚   â”‚   â”œâ”€â”€ TokenStealer.java           Extracts tokens from 30+ sources
â”‚   â”‚   â”‚   â”œâ”€â”€ DiscordProfileCollector.java Profile, friends, guilds, badges
â”‚   â”‚   â”‚   â”œâ”€â”€ EventHandler.java           Real-time: login, 2FA, credit card capture
â”‚   â”‚   â”‚   â”œâ”€â”€ EmbedBuilder.java           Formats stolen data as Discord embeds
â”‚   â”‚   â”‚   â””â”€â”€ WebhookFormatter.java       Sends data via webhooks
â”‚   â”‚   â”‚
â”‚   â”‚   â””â”€â”€ ðŸ’° wallet/                    Crypto Wallet Theft (6 files)
â”‚   â”‚       â”œâ”€â”€ WalletStealer.java          Browser extensions + desktop wallets
â”‚   â”‚       â”œâ”€â”€ DesktopFileScanner.java      Scans for backup/recovery/2FA files
â”‚   â”‚       â””â”€â”€ MasterCollector.java         Aggregates all stolen data into ZIP
â”‚   â”‚
â”‚   â”œâ”€â”€ ðŸ” crypto/                        Encryption Bypass (9 files)
â”‚   â”‚   â”œâ”€â”€ CryptoDecryptor.java            AES-GCM + DPAPI master key extraction
â”‚   â”‚   â”œâ”€â”€ AbeProcessInjector.java         Chrome v20 App-Bound Encryption bypass
â”‚   â”‚   â”‚                                   (VirtualAllocEx â†’ WriteProcessMemory
â”‚   â”‚   â”‚                                    â†’ CreateRemoteThread â†’ IElevator COM)
â”‚   â”‚   â”œâ”€â”€ LockedFileBypass.java           Reads locked DBs via DuplicateHandle
â”‚   â”‚   â”œâ”€â”€ PeParser.java                  PE32+ export table parser
â”‚   â”‚   â””â”€â”€ DpapiWrapper.java              Windows DPAPI wrapper
â”‚   â”‚
â”‚   â”œâ”€â”€ âš™ï¸ config/                        Configuration (9 files)
â”‚   â”‚   â”œâ”€â”€ StringConstants.java            All 58 XOR-decoded string constants
â”‚   â”‚   â”œâ”€â”€ WebhookConfig.java              Webhook URL, bot name "Supreme"
â”‚   â”‚   â””â”€â”€ DiscordApiConstants.java        API endpoints, CDN, timeouts
â”‚   â”‚
â”‚   â”œâ”€â”€ ðŸ’» system/                        System Information (3 files)
â”‚   â”‚   â”œâ”€â”€ HwidGenerator.java              Hardware ID via WMI/registry
â”‚   â”‚   â””â”€â”€ SystemInfo.java                 OS, CPU, RAM, processes, programs
â”‚   â”‚
â”‚   â””â”€â”€ ðŸ”§ util/                          Utilities (7 files)
â”‚       â”œâ”€â”€ WebhookClient.java              Discord webhook HTTP client
â”‚       â”œâ”€â”€ SslBypass.java                  Disables all SSL cert validation
â”‚       â””â”€â”€ Log.java                        agent.log file writer
â”‚
â””â”€â”€ ðŸ–¼ï¸ installer/
    â””â”€â”€ icon.ico                            NSIS installer icon
```

---

## ðŸš¨ Indicators of Compromise (IOC)

<details>
<summary><b>ðŸŒ Network IOCs</b></summary>

| Type | Value |
|---|---|
| C2 Server | `http://veled.com.tr` |
| License Key | `H95S2-MJ56T-LJPQ2-ATZ6Z` |
| Webhook Bot | `Supreme` |
| Webhook Avatar | `https://i.imgur.com/YYumsB0.png` |
| Telegram | `t.me/supremest` |
| API Endpoints | `/v1/poll`, `/v1/screen_upload`, `/v1/stream`, `/v1/chat` |

</details>

<details>
<summary><b>ðŸ“ Filesystem IOCs</b></summary>

| Type | Value |
|---|---|
| Persistence JAR | `%LOCALAPPDATA%\halos\halo.jar` |
| Renamed JRE | `%LOCALAPPDATA%\halos\jdk\bin\gangs.exe` |
| Lock file | `%TEMP%\.sp_lock` |
| Init marker | `%TEMP%\.ira_init` |
| Temp payload | `%TEMP%\sp_*.jar` |
| Log file | `%LOCALAPPDATA%\halos\agent.log` |

</details>

<details>
<summary><b>ðŸ”‘ Registry IOCs</b></summary>

| Type | Value |
|---|---|
| Startup | `HKCU\Software\Microsoft\Windows\CurrentVersion\Run\halos` |

</details>

<details>
<summary><b>ðŸ’¬ Discord IOCs</b></summary>

| Type | Value |
|---|---|
| Injection marker | `/* __SP_INJECTED__ */` |
| Blocked WebSocket | `wss://remote-auth-gateway.discord.gg` |
| Targeted clients | Discord, DiscordCanary, DiscordPTB, DiscordDevelopment |

</details>

---

## ðŸŽ¯ Targeted Software

<details>
<summary><b>ðŸŒ 53 Browsers</b></summary>

Chrome, Chrome Beta, Chrome Dev, Chrome Canary, Chromium, Edge, Edge Beta, Edge Dev, Edge Canary, Brave, Brave Beta, Brave Nightly, Opera, Opera GX, Vivaldi, Yandex, CocCoc, Comodo Dragon, Epic Privacy, Iridium, Iron, Maxthon, Orbitum, QQ Browser, Sleipnir, Slimjet, Sputnik, Torch, UC Browser, Chedot, CentBrowser, 7Star, Amigo, Elements, Kometa, Firefox, Waterfox, Pale Moon, Librewolf, and more.

</details>

<details>
<summary><b>ðŸ’° 50+ Crypto Wallets</b></summary>

**Browser Extensions:** MetaMask, Phantom, Exodus, Coinbase Wallet, Trust Wallet, Brave Wallet, Ronin, TronLink, Solflare, Slope, Keplr, Terra Station, Coin98, BitPay, Guarda, Math Wallet, SafePal, XDEFI, Rabby, Backpack, OKX Wallet, and more.

**Desktop Wallets:** Exodus, Atomic, Electrum, Wasabi, Bitcoin Core, Ethereum Wallet, and more.

</details>

---

## ðŸ§¹ Removal Instructions

<details>
<summary><b>Click to expand removal steps</b></summary>

```powershell
# ====== STEP 1: Remove persistence ======
reg delete "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "halos" /f

# ====== STEP 2: Delete malware files ======
Remove-Item "$env:LOCALAPPDATA\halos" -Recurse -Force
Remove-Item "$env:TEMP\.sp_lock" -Force -ErrorAction SilentlyContinue
Remove-Item "$env:TEMP\.ira_init" -Force -ErrorAction SilentlyContinue
Remove-Item "$env:TEMP\sp_*.jar" -Force -ErrorAction SilentlyContinue

# ====== STEP 3: Fix Discord injection ======
# For each Discord installation, navigate to:
#   %APPDATA%\discord\<version>\modules\discord_desktop_core\
# Replace index.js with:
#   module.exports = require('./core.asar');

# ====== STEP 4: Post-infection actions ======
# âš ï¸ Change ALL passwords saved in browsers
# âš ï¸ Change Discord password and reset 2FA
# âš ï¸ Revoke and regenerate crypto wallet seed phrases
# âš ï¸ Contact your bank if credit cards were stored in browsers
# âš ï¸ Enable 2FA on all accounts
```

</details>

---

## ðŸ”¬ Technical Details

<details>
<summary><b>Encryption & Obfuscation</b></summary>

| Layer | Technique |
|---|---|
| Payload encryption | XOR with single byte key `0xA7` (167) |
| String obfuscation | Per-string XOR with varying int keys |
| Name obfuscation | Single-letter classes, methods, fields |
| Browser passwords | AES-256-GCM with DPAPI-protected master key |
| Chrome v20 ABE | Process injection + IElevator COM interface |
| Locked DB bypass | `NtQuerySystemInformation` + `DuplicateHandle` |

</details>

<details>
<summary><b>Architecture</b></summary>

| Stage | Technology |
|---|---|
| Installer | NSIS (128 MB) |
| Dropper | Electron 28.3.3 (JavaScript) |
| Payload delivery | XOR 0xA7 encrypted JAR |
| Core malware | Java 21 (stealer + RAT) |
| Discord injection | JavaScript (10KB payload) |
| ABE bypass | x64 native shellcode (PE) |
| C2 protocol | HTTP polling + WebSocket |
| Exfiltration | Multi-part HTTP POST to C2 |
| Persistence | Registry Run key + scheduled JAR |

</details>

<details>
<summary><b>Deobfuscation Process</b></summary>

1. **NSIS extraction** â†’ 7-Zip unpacking â†’ Electron app isolation
2. **Asar extraction** â†’ main.js XOR string decryption
3. **XOR 0xA7 decryption** â†’ runtime.dat â†’ valid JAR file
4. **CFR 0.152 decompilation** â€” 2 errors vs JADX's 1,781
5. **XOR 0xA7 decryption** â†’ abe/core.dat â†’ x64 shellcode PE
6. **6-pass symbol renaming** â€” 400+ methods, fields, files
7. **213 inline string constant decoding** with original comments
8. **Discord injection extraction** â€” 10KB standalone JS file
9. **Cross-reference updating** â€” package-level call resolution

</details>

---

## ðŸ“Š Deobfuscation Score

```
â•”â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•—
â•‘           DEOBFUSCATION RESULTS              â•‘
â• â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•£
â•‘  File names      â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ  100%  â•‘
â•‘  Methods         â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ   100%  â•‘
â•‘  Strings         â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ  100%  â•‘
â•‘  Fields          â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–‘â–‘â–‘â–‘   100%  â•‘
â•‘  Binaries        â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ  100%  â•‘
â•‘  Discord JS      â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ  100%  â•‘
â• â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•£
â•‘  OVERALL         â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–‘   100%  â•‘
â•šâ•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
```

---

## âš–ï¸ Disclaimer

This repository is published for **educational and security research purposes only**. The code is provided to help security researchers, antivirus vendors, and the general public understand how this malware operates.

**Do not use this code for malicious purposes.**

If you are a victim, follow the [removal instructions](#-removal-instructions) and report the C2 infrastructure to your local CERT or law enforcement.

---

## ðŸ› ï¸ Tools Used

| Tool | Purpose |
|---|---|
| [CFR](https://benf.org/other/cfr/) v0.152 | Java decompilation |
| [7-Zip](https://7-zip.org/) | NSIS/archive extraction |
| [Node.js](https://nodejs.org/) | Asar extraction & scripting |
| Static analysis only | **No malware was executed** |

---

<div align="center">

**If this helped you, â­ star the repo to increase visibility and protect more people.**

*Reverse engineered with patience and caffeine â˜•*

</div>
