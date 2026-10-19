# Play listing draft

Paste these into Play Console. The privacy policy URL should point at `PRIVACY.md` once that file is published, for example from the GitHub repository.

## Store listing

**App name:** Passilius

**Short description:**

Local password manager. Encrypted on your device. No account and no cloud.

**Full description:**

Passilius keeps your logins on your device. There is no account and no cloud.

Create a master password. It is the only key to the vault. If you forget it, the vault cannot be opened. On Android you can later turn on fingerprint or face unlock in Settings. That does not replace the master password.

Search saved logins, filter them by category, and copy a username or password. Add a service, username, password, website, notes, and category. A strength bar follows the password as you type. The password generator builds a password from length and character sets.

Pair two of your devices with a QR code and move the encrypted vault over your local network. This is not an online account.

Your passwords are encrypted on the device before they are saved. Android backup of the vault is turned off.

## Data safety form

Answer from the current app, not from a future cloud feature.

- **Data collected:** No. Passilius does not send vault contents, the master password, or usage data to a developer server.
- **Data shared:** No.
- **Data stored on the device:** Yes. Logins are encrypted in a local database. The wrapped vault key stays in app files. Settings store only biometric unlock and clipboard auto-clear.
- **Security practices:** Data is encrypted in transit during device sync (the snapshot is encrypted before it is sent on the local network). Data is encrypted at rest. Users can ask you to delete their data by uninstalling the app, because there is no account to erase on a server.
- **Camera:** Used only when the user scans a sync QR code. Photos are not saved or uploaded.
- **Account creation:** The app does not offer an account.

## Release notes for 1.0

First public release. Local encrypted vault, master password, optional biometric unlock, password generator, categories, and QR sync between your own devices.
