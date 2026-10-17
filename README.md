# Passilius (Vault)

**Passilius** is a local password manager for **Android** and **Windows**. It uses Compose Multiplatform, keeps credentials on the device, and encrypts them before they are written to the database.

Unlock the vault with a master password of at least 8 characters. On Android you can also turn on fingerprint or face unlock. An empty or unfinished password stays on the unlock screen and shows an error. It does not open the vault.

Repository: [github.com/balius32/Passilius](https://github.com/balius32/Passilius)

## What the app does

- **Vault** — Search saved logins and filter them by category. Copy a username or password to the clipboard.
- **Credentials** — Add and edit a service, username, password, website, notes, and category from a bottom sheet. A strength bar follows the password as you type.
- **Password generator** — Build a password from length and character sets. Entropy is stored with the credential.
- **Categories** — Create and manage the categories used by the vault filter.
- **Master password** — First launch creates the vault key. Later launches ask for the same password. Settings can change it or lock the vault.
- **Biometrics** — Optional on Android, off until you enable them in Settings. Desktop has no biometric unlock.
- **Sync devices** — Pair two devices with a QR code and transfer the encrypted vault on the local network. This is not a cloud account.
- **Settings** — Biometric unlock, clipboard auto-clear, change master password, lock, and device sync.

## Technology stack

| Area | Technology |
|------|------------|
| Language | Kotlin |
| UI | Compose Multiplatform, Material 3, Material Icons |
| Architecture | Clean Architecture (`data` / `domain` / `presentation`), use cases, MVI-style ViewModels |
| Navigation | Sealed routes + in-memory back stack |
| Local database | Room KMP (SQLite) with KSP + bundled driver |
| Async | Kotlin Coroutines, `Flow`, Lifecycle-aware collection |
| Encryption | Random data key, AES-256-GCM. The master password wraps that key with PBKDF2-HMAC-SHA256 (600,000 iterations). Android Keystore holds the biometric copy. |
| Sync | Ktor client, QR pairing, encrypted snapshot exchange |
| Biometrics | AndroidX Biometric (Android only) |
| Build | Gradle Kotlin DSL, Compose Multiplatform 1.12, AGP 9.x, `compileSdk` / `targetSdk` 37, `minSdk` 24, JDK 17 |

## Project layout

```
vault/
├── composeApp/             # CMP module (Android + Windows desktop)
├── gradle/                 # Version catalog (libs.versions.toml)
├── build.gradle.kts
└── settings.gradle.kts
```

Inside `composeApp/src/commonMain/kotlin/com/example/`:

- `core/` — Crypto, design system, shared UI components
- `data/` — Room entities, DAO, repository implementations
- `domain/` — Models, repository contracts, use cases
- `presentation/` — Screens, navigation, ViewModels (vault, generator, unlock, categories, sync, settings)

## Getting started

**Prerequisites:** JDK 17 and [Android Studio](https://developer.android.com/studio) for Android. Desktop needs JDK 17.

1. Clone the repository and open the `vault` folder.
2. Let Gradle sync finish (`gradlew.bat` is included).
3. **Android:** run the `composeApp` configuration (API 24+).
4. **Windows:** `gradlew.bat :composeApp:run`

Desktop data and the wrapped vault key live under `%USERPROFILE%\.passilius-vault\`.

See [composeApp/README.md](composeApp/README.md) for module details.

### Release signing (optional)

Release builds expect keystore environment variables (see `composeApp/build.gradle.kts`):

- `KEYSTORE_PATH` (defaults to `my-upload-key.jks` in the project root)
- `STORE_PASSWORD`, `KEY_PASSWORD`

Debug builds can use a local `debug.keystore` if present.

## Security notes

- The master password never becomes the database key. It wraps a random 256-bit data key. That data key stays in memory only while the vault is unlocked.
- Password, username, website, and notes are encrypted with AES-256-GCM. The service name stays readable so the list can render.
- A blank unlock password is rejected before key derivation. A wrong password stays on the unlock screen.
- Device sync moves an encrypted snapshot between devices you pair. There is no hosted account.
- Do not commit keystores, `.env` files with secrets, or `local.properties`.

## Module documentation

Details for the Compose Multiplatform module are in [composeApp/README.md](composeApp/README.md).

> The legacy `:app` folder may still exist on disk but is **not** included in the Gradle build. Use `:composeApp`.
