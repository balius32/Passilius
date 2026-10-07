# Passilius (Vault)

**Passilius** is a password manager focused on local storage, strong encryption, and a tactile neumorphic UI. It runs on **Android** and **Windows desktop** via Compose Multiplatform.

Credentials stay on the device: passwords are encrypted before they are written to the database. Unlock the vault with a master PIN (and optional biometrics on Android).

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
| UI | Compose Multiplatform, Material 3, Material Icons |
| Architecture | Clean Architecture (`data` / `domain` / `presentation`), use cases, MVI-style ViewModels |
| Navigation | Sealed routes + in-memory back stack |
| Local database | Room KMP (SQLite) with KSP + bundled driver |
| Async | Kotlin Coroutines, `Flow`, Lifecycle-aware collection |
| Encryption | AES-GCM (`CryptoManager`: Android Keystore / desktop file key) |
| Biometrics | AndroidX Biometric (Android only; desktop is PIN-only) |
| Build | Gradle Kotlin DSL, CMP 1.12, AGP 9.x, `compileSdk` / `targetSdk` 37, `minSdk` 24 |

## Project layout

```
vault/
├── composeApp/             # CMP module (Android + Windows desktop)
├── gradle/                 # Version catalog (libs.versions.toml)
├── build.gradle.kts
└── settings.gradle.kts
```

Inside `composeApp/src/commonMain/kotlin/com/example/`:

- `core/` — Crypto expect API, design system (neumorphism), shared UI components
- `data/` — Room entities, DAO, repository implementations
- `domain/` — Models, repository contracts, use cases
- `presentation/` — Screens, navigation, ViewModels (vault, generator, unlock, settings)

## Getting started

**Prerequisites:** JDK 17+ and [Android Studio](https://developer.android.com/studio) (for Android), or any JDK 17+ for desktop.

1. Clone the repository and open the `vault` folder.
2. Let Gradle sync finish (`gradlew.bat` is included).
3. **Android:** run the `composeApp` configuration (API 24+).
4. **Windows:** `gradlew.bat :composeApp:run`

See [composeApp/README.md](composeApp/README.md) for module details.

### Release signing (optional)

Release builds expect keystore environment variables (see `composeApp/build.gradle.kts`):

- `KEYSTORE_PATH` (defaults to `my-upload-key.jks` in the project root)
- `STORE_PASSWORD`, `KEY_PASSWORD`

Debug builds can use a local `debug.keystore` if present.

## Security notes

- Sensitive fields are encrypted at rest; decryption happens when data is loaded into the domain layer.
- The vault is designed for **local-first** use—there is no account sync in this codebase.
- Do not commit keystores, `.env` files with secrets, or `local.properties` to version control.

## Module documentation

Details for the Compose Multiplatform module are in [composeApp/README.md](composeApp/README.md).

> Note: the legacy `:app` folder may still exist on disk but is **not** included in the Gradle build; use `:composeApp`.
