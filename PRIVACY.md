# Passilius privacy policy

Last updated: 19 October 2026

Passilius is a password manager for Android and Windows. It keeps your vault on the device you are using. There is no Passilius account and no Passilius server.

## What stays on your device

- Service names, usernames, passwords, websites, notes, and categories are stored in a local database.
- Passwords, usernames, websites, and notes are encrypted on the device before they are written.
- The master password is not stored. It wraps a random key that stays in memory only while the vault is unlocked.
- On Android, the wrapped key is the file `vault.wrap`. If you turn on fingerprint or face unlock, a second wrapped copy is `biometric.wrap`, protected by the Android keystore.
- A settings file remembers whether biometric unlock and clipboard auto-clear are on. It does not contain your passwords.
- Android backup and device-to-device transfer are turned off, so Google backup does not receive the vault or the wrapped key.
- On Windows, the vault data and wrapped key stay under your user profile in `.passilius-vault`.

## What leaves your device

Nothing is sent to Passilius. The app does not include ads, analytics, or a cloud account.

If you choose **Sync devices**, the app can move an encrypted vault snapshot to another device you pair:

- You pair with a QR code.
- The transfer stays on your local network.
- The app connects only to a private local address from that QR code, over plain HTTP on that network.
- The snapshot itself is encrypted with the pairing token before it is sent.
- The camera is used only to scan that QR code, and only while you are on the sync screen.

Copying a username or password puts that value on the clipboard. You can turn on clipboard auto-clear in Settings.

## Permissions

- **Camera** — scan a sync QR code. Optional. The app works without it.
- **Biometrics** — optional unlock after you enable it in Settings.
- **Internet and network state** — local device sync. The app does not contact a Passilius service.

## Children

Passilius is not directed at children, and it does not ask for an account or a date of birth.

## Changes

If this policy changes, the date at the top of this file will change.

## Contact

Questions about this policy: [github.com/balius32/Passilius](https://github.com/balius32/Passilius).
