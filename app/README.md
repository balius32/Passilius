# Vault — Android app module

This module is the installable **Vault** password manager (`applicationId`: `com.aistudio.vault.qmxzld`).

## Responsibilities

| Feature | Main packages / files |
|---------|------------------------|
| App entry & DI wiring | `MainActivity.kt` — Room, repository, use cases, ViewModels |
| Vault list & search | `presentation/vault/` |
| Add / edit credential | `presentation/credential/CredentialBottomSheet.kt` |
| Password generator | `presentation/generator/` |
| PIN & biometrics unlock | `presentation/unlock/MasterUnlockScreen.kt`, `core/biometric/` |
| Settings & lock | `presentation/settings/SettingsScreen.kt` |
| Navigation & bottom bar | `presentation/navigation/VaultNavigation.kt` |
| Persistence | `data/local/` (Room), `data/repository/VaultRepositoryImpl.kt` |
| Encryption & TOTP | `core/crypto/CryptoManager.kt`, `TotpManager.kt`, `PasswordGenerator.kt` |
| Neumorphic UI | `core/designsystem/`, `core/components/` |

## Domain model

`domain/model/Credential.kt` defines each saved login: service, username, password, category, URL, encrypted TOTP secret, notes, favorites, and entropy metadata. `SecurityReport` aggregates vault health for the UI.

## Build & run

From the project root:

```bash
./gradlew :app:assembleDebug
```

On Windows:

```bat
gradlew.bat :app:assembleDebug
```

Install the debug APK from `app/build/outputs/apk/debug/` or use **Run** in Android Studio.

## Tests

- Unit tests: `app/src/test/`
- Instrumented tests: `app/src/androidTest/`

## Permissions

Declared in `AndroidManifest.xml`:

- `USE_BIOMETRIC` — fingerprint / face unlock
- `VIBRATE` — tactile feedback on supported controls

For full project overview and technology list, see the [root README](../README.md).
