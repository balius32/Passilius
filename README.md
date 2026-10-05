# Passilius (Vault)

**Passilius** is an Android password manager focused on local storage, strong encryption, and a tactile neumorphic UI. The app is branded **Vault** on device.

Credentials stay on the phone: passwords are encrypted before they are written to the database. Unlock the vault with a master PIN and optional biometrics.

Repository: [github.com/balius32/Passilius](https://github.com/balius32/Passilius)

## What the app does

- **Vault** — Browse, search, and filter saved logins by category; mark favorites; copy usernames and passwords to the clipboard.
- **Credentials** — Add and edit entries (service, username, password, website, notes, category) from a bottom sheet.
- **Password generator** — Create strong passwords with configurable length and character sets; entropy is calculated and stored with each credential.
- **Master unlock** — PIN pad to open the vault; **fingerprint / face** unlock when biometrics are available.
- **Settings** — Security overview, lock vault, and app preferences (neumorphic settings UI).
- **Security insights** — Tracks strength signals such as weak and reused entries for a vault security score.

## Technology stack

| Area | Technology |
|------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3, Material Icons |
| Architecture | Clean Architecture (`data` / `domain` / `presentation`), use cases, MVI-style ViewModels |
| Navigation | Navigation 3 (`androidx.navigation3`) |
| Local database | Room (SQLite) with KSP code generation |
| Async | Kotlin Coroutines, `Flow`, Lifecycle-aware collection |
| Encryption | Android Keystore, AES-GCM; PBKDF2 where applicable (`CryptoManager`) |
| Biometrics | AndroidX Biometric library |
| Build | Gradle Kotlin DSL, AGP 9.x, `compileSdk` / `targetSdk` 37, `minSdk` 24 |
| Testing | JUnit, Robolectric, AndroidX Test |

## Project layout

```
vault/
├── app/                    # Android application module (see app/README.md)
├── gradle/                 # Version catalog (libs.versions.toml)
├── build.gradle.kts
└── settings.gradle.kts
```

Inside `app/src/main/java/com/example/`:

- `core/` — Crypto, biometrics, design system (neumorphism), shared UI components
- `data/` — Room entities, DAO, repository implementations
- `domain/` — Models, repository contracts, use cases
- `presentation/` — Screens, navigation, ViewModels (vault, generator, unlock, settings)

## Getting started

**Prerequisites:** [Android Studio](https://developer.android.com/studio) with a recent JDK (project uses Java 11 bytecode).

1. Clone the repository and open the `vault` folder in Android Studio.
2. Let Gradle sync finish.
3. Run the **app** configuration on an emulator or device (API 24+).

### Release signing (optional)

Release builds expect keystore environment variables (see `app/build.gradle.kts`):

- `KEYSTORE_PATH` (defaults to `my-upload-key.jks` in the project root)
- `STORE_PASSWORD`, `KEY_PASSWORD`

Debug builds can use a local `debug.keystore` if present.

## Security notes

- Sensitive fields are encrypted at rest; decryption happens when data is loaded into the domain layer.
- The vault is designed for **local-first** use—there is no account sync in this codebase.
- Do not commit keystores, `.env` files with secrets, or `local.properties` to version control.

## Module documentation

Details specific to the Android app module (package map and run targets) are in [app/README.md](app/README.md).
