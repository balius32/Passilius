# Localization

The active application is `composeApp`. English and Persian strings live in
`src/commonMain/composeResources/values/strings.xml` and `values-fa/strings.xml`.

Use generated `Res.string` identifiers with `tr`, and `Res.plurals` with `plural`.
Use positional placeholders instead of concatenating sentences. Store `UiMessage`
resource references in UI state; resolve them when rendering. Never translate user
input, stored credentials, protocol fields, routes, logs, or test identifiers.

`LanguageController` owns System/English/Persian selection. Platform preferences
persist it outside the encrypted vault. Initialization happens before the first
composition. The process locale selects Compose resources; Compose snapshot state
invalidates readers when selection changes. The root supplies RTL for Persian.
Changing language does not replace the navigation tree or recreate ViewModels.

System resolves Persian (`fa`, including regional variants) to Persian and all
other languages to English. Android observes locale configuration changes;
desktop captures the device language on launch. Existing saved category names
are user data and remain unchanged, including categories created by older versions.
New built-in categories use explicit `builtin:` identities and are translated only
while their canonical name remains unchanged. Renamed and user-created categories
retain their exact names. No existing rows are relabeled or migrated.

Run `python tools/check_localization.py` for resource parity, placeholder and
plural validation, and focused UI literal checks. The literal check is deliberately
limited to direct text/accessibility/hint assignments; review indirect strings too.
Brand names, glyphs, masked passwords, character-set examples, test tags, animation
labels, and protocol strings are intentional exclusions.

For a new language, add a values-language directory, translate every resource,
extend `AppLanguage` and platform selection, update the parity check, and verify
layout direction, font coverage, mixed-script input, plural rules, and formatting.

Manual acceptance checklist (Android and desktop):
- Fresh install in Persian and English; unsupported device language falls back to English.
- Switch languages in Profile; close/reopen app and confirm selection persists.
- Switch back to System and change Android device language while running.
- Verify onboarding, locked vault, forms, categories, generator, settings and sync.
- Verify errors in both languages and check narrow windows and large fonts.
- Copy mixed Persian/Latin passwords and usernames and verify exact round trips.
- Verify back navigation, QR instructions, and biometric prompts.
