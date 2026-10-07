# composeApp (Compose Multiplatform)

Shared Passilius UI and logic for **Android** and **Windows desktop**.

## Run

### Android

Open the project in Android Studio and run the `composeApp` configuration (application id `com.aistudio.vault.qmxzld`).

```bat
gradlew.bat :composeApp:installDebug
```

### Windows desktop

```bat
gradlew.bat :composeApp:run
```

Local desktop data/key files live under `%USERPROFILE%\.passilius-vault\`.

## Layout

- `src/commonMain` — domain, data, UI, ViewModels
- `src/androidMain` — Keystore crypto, biometrics, clipboard, Room builder, `MainActivity`
- `src/desktopMain` — file-based crypto key, AWT clipboard, Room builder, `main()`

Biometrics are Android-only; desktop unlock is PIN-only.
