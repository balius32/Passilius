# Graph Report - vault  (2026-11-22)

## Corpus Check
- 180 files · ~64,972 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 40 file(s) not represented in the graph (top: .xml 23, .properties 3, .mdc 2)

## Summary
- 1806 nodes · 4182 edges · 134 communities (62 shown, 72 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 160 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `7ece349f`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- java/com/example/presentation/vault/VaultViewModel.kt
- VaultUiIntent
- CredentialDao
- rememberScreenContentPadding
- BiometricPromptManager.kt
- CryptoManager
- ExampleRobolectricTest.kt
- CategoryDao
- launch
- java/com/example/core/designsystem/Neumorphism.kt
- java/com/example/core/crypto/CryptoManager.kt
- GeneratorScreen
- java/com/example/core/components/BrandIconWell.kt
- UiMessage
- VaultScreen
- SyncTransport.desktop.kt
- java/com/example/core/util/ScreenInsets.kt
- VaultApp
- ExampleInstrumentedTest.kt
- java/com/example/presentation/navigation/VaultRoutes.kt
- Android Round Launcher Icon (ic_launcher_round)
- ManageCategoriesScreen
- Android App Launcher Icon (hdpi)
- Android Launcher Icon (ic_launcher)
- Android Launcher Icon (ic_launcher)
- Android App Launcher Icon (xxhdpi)
- Android Round App Launcher Icon (xxhdpi)
- Android Launcher Icon (ic_launcher)
- Android Bugdroid Head Silhouette
- Round App Launcher Icon
- Round Android Launcher Icon
- java/com/example/core/designsystem/Theme.kt
- kotlin/com/example/domain/usecase/VaultUseCases.kt
- CredentialDao
- java/com/example/MainActivity.kt
- VaultViewModel
- kotlin/com/example/presentation/category/ManageCategoriesViewModel.kt
- CategoryEntity
- ClipboardSettings
- VaultRepository
- java/com/example/presentation/category/ManageCategoriesViewModel.kt
- QrCodec.android.kt
- VaultUiIntent
- java/com/example/data/local/database/VaultDatabase.kt
- VaultRepository
- ByteArray
- OnboardingScreen
- VaultSnapshot
- VaultRepositoryImpl
- kotlin/com/example/MainActivity.kt
- CredentialBottomSheet
- SyncTransport.kt
- .mergeSnapshot
- kotlin/com/example/presentation/generator/GeneratorViewModel.kt
- GeneratorUiIntent
- java/com/example/presentation/generator/GeneratorViewModel.kt
- SyncPairingInfo
- PlatformCapabilities.kt
- GeneratorUiIntent
- SettingsScreen
- QrImageBitmap.desktop.kt
- kotlin/com/example/data/local/database/VaultDatabase.kt
- ByteArray
- SyncUiIntent
- CryptoEngine
- LanguagePreferences
- App.kt
- VaultApp
- CryptoManager
- VaultSession
- SyncViewModel
- AppLanguage
- newSyncId
- SyncPhase
- NeumorphicButton
- SyncQrCamera.android.kt
- BiometricSettings
- composeApp/build.gradle.kts
- ClipboardHelper
- LegacyVaultCrypto
- VaultRepositoryImpl
- java/com/example/domain/model/Credential.kt
- BiometricUnlock
- QrImageBitmap.kt
- BiometricVaultKey
- Passilius privacy policy
- AGENTS.md
- VaultScreen
- currentTimeMillis
- currentTimeMillis
- BiometricSettings
- BiometricUnlockResult
- .start
- AppGraph
- SecureRandom.android.kt
- SyncEnvelope
- VaultKeyIo
- SecureRandom.desktop.kt
- SyncEnvelope
- LegacyVaultCrypto
- SyncQrCamera.desktop.kt
- .parseUri
- VaultFiles
- Play listing draft
- SettingsViewModel.kt
- tr
- SettingsScreen
- QrImage
- BiometricVaultKey
- ManageCategoriesScreen
- MasterUnlockScreen
- ManageCategoriesUiIntent
- CredentialEntity
- CredentialBottomSheet
- ManageCategoriesUiIntent
- SyncQrCamera
- VaultKeyIo
- LegacyVaultCrypto

## God Nodes (most connected - your core abstractions)
1. `tr()` - 51 edges
2. `SyncViewModel` - 27 edges
3. `VaultRepositoryImpl` - 26 edges
4. `AppGraph` - 24 edges
5. `VaultRepositoryImpl` - 23 edges
6. `VaultViewModel` - 23 edges
7. `VaultApp()` - 22 edges
8. `SyncPairingInfo` - 20 edges
9. `VaultUiIntent` - 20 edges
10. `UiMessage` - 19 edges

## Surprising Connections (you probably didn't know these)
- `Localization` --references--> `UiMessage`  [INFERRED]
  docs/LOCALIZATION.md → composeApp/src/commonMain/kotlin/com/example/core/localization/Strings.kt
- `Localization` --references--> `tr()`  [INFERRED]
  docs/LOCALIZATION.md → composeApp/src/commonMain/kotlin/com/example/core/localization/Strings.kt
- `Localization` --references--> `plural()`  [INFERRED]
  docs/LOCALIZATION.md → composeApp/src/commonMain/kotlin/com/example/core/localization/Strings.kt
- `Localization` --references--> `AppLanguage`  [INFERRED]
  docs/LOCALIZATION.md → composeApp/src/commonMain/kotlin/com/example/core/localization/AppLanguage.kt
- `Localization` --references--> `LanguageController`  [INFERRED]
  docs/LOCALIZATION.md → composeApp/src/commonMain/kotlin/com/example/core/localization/AppLanguage.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **App module feature surface** — app_readme_mainactivity, app_readme_credentialbottomsheet, app_readme_passwordgenerator, app_readme_masterunlockscreen, app_readme_settingsscreen, app_readme_vaultnavigation [EXTRACTED 1.00]
- **Default Android Launcher Icon Composition** — app_src_main_res_mipmap_mdpi_ic_launcher, app_src_main_res_mipmap_mdpi_ic_launcher_android_robot_head, app_src_main_res_mipmap_mdpi_ic_launcher_long_shadow, app_src_main_res_mipmap_mdpi_ic_launcher_green_grid_background [EXTRACTED 1.00]
- **Round Launcher Icon Visual Composition** — app_src_main_res_mipmap_mdpi_ic_launcher_round, app_src_main_res_mipmap_mdpi_ic_launcher_round_android_bugdroid, app_src_main_res_mipmap_mdpi_ic_launcher_round_material_green_circle, app_src_main_res_mipmap_mdpi_ic_launcher_round_long_shadow [EXTRACTED 1.00]
- **Default Android Launcher Icon Composition** — app_src_main_res_mipmap_xhdpi_ic_launcher, app_src_main_res_mipmap_xhdpi_ic_launcher_android_robot_head, app_src_main_res_mipmap_xhdpi_ic_launcher_long_shadow, app_src_main_res_mipmap_xhdpi_ic_launcher_green_grid_background [EXTRACTED 1.00]
- **Default Android Studio Launcher Icon Composition** — app_src_main_res_mipmap_xxhdpi_ic_launcher_android_robot, app_src_main_res_mipmap_xxhdpi_ic_launcher_green_grid_background, app_src_main_res_mipmap_xxhdpi_ic_launcher_material_long_shadow, app_src_main_res_mipmap_xxhdpi_ic_launcher_squircle_mask [EXTRACTED 1.00]
- **Default Android Studio Round Launcher Icon Composition** — app_src_main_res_mipmap_xxhdpi_ic_launcher_round_android_robot, app_src_main_res_mipmap_xxhdpi_ic_launcher_round_green_grid_background, app_src_main_res_mipmap_xxhdpi_ic_launcher_round_material_long_shadow, app_src_main_res_mipmap_xxhdpi_ic_launcher_round_circular_mask [EXTRACTED 1.00]
- **Default Android App Icon Branding** — app_src_main_res_mipmap_xxxhdpi_ic_launcher_android_launcher_icon, app_src_main_res_mipmap_xxxhdpi_ic_launcher_bugdroid_head, app_src_main_res_mipmap_xxxhdpi_ic_launcher_green_grid_background, app_src_main_res_mipmap_xxxhdpi_ic_launcher_long_shadow_style [EXTRACTED 1.00]
- **Default Android Round App Icon Branding** — app_src_main_res_mipmap_xxxhdpi_ic_launcher_round_android_round_launcher_icon, app_src_main_res_mipmap_xxxhdpi_ic_launcher_round_bugdroid_head, app_src_main_res_mipmap_xxxhdpi_ic_launcher_round_green_grid_background, app_src_main_res_mipmap_xxxhdpi_ic_launcher_round_long_shadow_style [EXTRACTED 1.00]
- **Clean Architecture layers** — readme_clean_architecture, readme_data_layer, readme_domain_layer, readme_presentation_layer, readme_core_layer [EXTRACTED 1.00]
- **Round Launcher Visual System** — app_src_main_res_mipmap_xhdpi_ic_launcher_round_round_launcher_icon, app_src_main_res_mipmap_xhdpi_ic_launcher_round_android_robot_head, app_src_main_res_mipmap_xhdpi_ic_launcher_round_green_grid_background, app_src_main_res_mipmap_xhdpi_ic_launcher_round_material_long_shadow [INFERRED 0.85]
- **Vault security model** — readme_encryption, readme_cryptomanager, readme_android_keystore, readme_aes_gcm, readme_master_unlock, readme_biometrics [INFERRED 0.85]

## Communities (134 total, 72 thin omitted)

### Community 0 - "java/com/example/presentation/vault/VaultViewModel.kt"
Cohesion: 0.08
Nodes (10): AddCategoryUseCase, DeleteCategoryUseCase, DeleteCredentialUseCase, GeneratePasswordUseCase, GetVaultCredentialsUseCase, ObserveCategoriesUseCase, RenameCategoryUseCase, SaveCredentialUseCase (+2 more)

### Community 1 - "VaultUiIntent"
Cohesion: 0.12
Nodes (18): CloseBottomSheet, CloseCategorySheet, CopyPassword, CopyUsername, CreateCategory, DeleteCredential, LockVault, OpenCreate (+10 more)

### Community 3 - "rememberScreenContentPadding"
Cohesion: 0.18
Nodes (5): rememberScreenContentPadding(), AttributeRow(), GeneratorScreen(), ChangeMasterPasswordScreen(), PasswordField()

### Community 4 - "BiometricPromptManager.kt"
Cohesion: 0.07
Nodes (11): AuthenticationError, AuthenticationFailed, AuthenticationSuccess, BiometricPromptManager, AuthenticationCallback, BiometricResult, FeatureUnavailable, HardwareUnavailable (+3 more)

### Community 6 - "CryptoManager"
Cohesion: 0.06
Nodes (42): Credential, CredentialBottomSheet, CryptoManager, MainActivity, MasterUnlockScreen, PasswordGenerator, SecurityReport, SettingsScreen (+34 more)

### Community 7 - "ExampleRobolectricTest.kt"
Cohesion: 0.05
Nodes (9): ExampleRobolectricTest, VaultCoreUnitTest, SyncEnvelope, Credential, SecurityReport, VaultCategory, CryptoEngineTest, TranslationTest (+1 more)

### Community 10 - "java/com/example/core/designsystem/Neumorphism.kt"
Cohesion: 0.24
Nodes (6): neuClickable(), neuFlat(), neuPressed(), neuClickable(), neuFlat(), neuPressed()

### Community 11 - "java/com/example/core/crypto/CryptoManager.kt"
Cohesion: 0.20
Nodes (4): base64Decode(), base64Encode(), base64Decode(), base64Encode()

### Community 12 - "GeneratorScreen"
Cohesion: 0.21
Nodes (5): NeumorphicButton(), TactileSlider(), TactileToggleSwitch(), AttributeRow(), GeneratorScreen()

### Community 13 - "java/com/example/core/components/BrandIconWell.kt"
Cohesion: 0.11
Nodes (16): AppleLogo(), BrandIconWell(), FigmaLogo(), GitHubLogo(), GoogleLogo(), SlackLogo(), SpotifyLogo(), NotchedCapsuleShape (+8 more)

### Community 15 - "VaultScreen"
Cohesion: 0.20
Nodes (4): CategoryPillStrip(), NeumorphicSearchBar(), PasswordCard(), VaultScreen()

### Community 16 - "SyncTransport.desktop.kt"
Cohesion: 0.09
Nodes (4): exchangeSnapshot(), SyncRejectedException, toHex(), SyncClient

### Community 18 - "VaultApp"
Cohesion: 0.16
Nodes (4): selectTopLevelTab(), VaultApp(), VaultBottomNavigationBar(), MasterUnlockScreen()

### Community 20 - "java/com/example/presentation/navigation/VaultRoutes.kt"
Cohesion: 0.67
Nodes (4): CategoriesRoute, GeneratorRoute, SettingsRoute, VaultRoute

### Community 21 - "Android Round Launcher Icon (ic_launcher_round)"
Cohesion: 0.33
Nodes (7): Android Round Launcher Icon (ic_launcher_round), Android Bugdroid Head Silhouette, Circular Round Icon Mask, Default Android Studio Placeholder Icon, Green Grid Circular Background, Long Shadow Material Style, mipmap-xxxhdpi Density Bucket

### Community 23 - "Android App Launcher Icon (hdpi)"
Cohesion: 0.40
Nodes (6): Android App Launcher Icon (hdpi), Android Bugdroid Head Silhouette, Green Gridded Background, Home Screen Launcher Affordance, Material Design Drop Shadow, Squircle Icon Container

### Community 24 - "Android Launcher Icon (ic_launcher)"
Cohesion: 0.40
Nodes (6): Android Launcher Icon (ic_launcher), Android Robot Head Silhouette, Green Grid Background, Long Shadow Design Effect, mipmap-mdpi Density Resource, Rounded Square Icon Mask

### Community 25 - "Android Launcher Icon (ic_launcher)"
Cohesion: 0.40
Nodes (6): Android Launcher Icon (ic_launcher), Android Robot Head Silhouette, Green Grid Background, Long Shadow Design Effect, mipmap-xhdpi Density Resource, Rounded Square Icon Mask

### Community 26 - "Android App Launcher Icon (xxhdpi)"
Cohesion: 0.40
Nodes (6): Android App Launcher Icon (xxhdpi), Android Robot (Bugdroid) Head, Default Android Studio Placeholder Branding, Mint Green Grid Background, Material Design Long Shadow, Squircle Icon Mask

### Community 27 - "Android Round App Launcher Icon (xxhdpi)"
Cohesion: 0.40
Nodes (6): Android Round App Launcher Icon (xxhdpi), Android Robot (Bugdroid) Head, Circular Round Icon Mask, Default Android Studio Placeholder Branding, Mint Green Grid Background, Material Design Long Shadow

### Community 28 - "Android Launcher Icon (ic_launcher)"
Cohesion: 0.40
Nodes (6): Android Launcher Icon (ic_launcher), Android Bugdroid Head Silhouette, Default Android Studio Placeholder Icon, Green Grid Background, Long Shadow Material Style, mipmap-xxxhdpi Density Bucket

### Community 29 - "Android Bugdroid Head Silhouette"
Cohesion: 0.50
Nodes (5): Android Bugdroid Head Silhouette, Diagonal Drop Shadow, Green Circular Grid Background, Material Design Adaptive Icon Style, Round Android Launcher Icon

### Community 30 - "Round App Launcher Icon"
Cohesion: 0.60
Nodes (5): Round App Launcher Icon, Android Bugdroid Mascot Head, Long Shadow Effect, Material Design Green Circle Background, MDPI Mipmap Density Resource

### Community 31 - "Round Android Launcher Icon"
Cohesion: 0.50
Nodes (5): Android Robot Head, Green Grid Background, Material Design Long Shadow, mipmap-xhdpi Launcher Asset, Round Android Launcher Icon

### Community 32 - "java/com/example/core/designsystem/Theme.kt"
Cohesion: 0.13
Nodes (3): VaultTheme(), MyApplicationTheme(), VaultTheme()

### Community 33 - "kotlin/com/example/domain/usecase/VaultUseCases.kt"
Cohesion: 0.14
Nodes (8): AddCategoryUseCase, DeleteCategoryUseCase, DeleteCredentialUseCase, GeneratePasswordUseCase, GetVaultCredentialsUseCase, ObserveCategoriesUseCase, RenameCategoryUseCase, SaveCredentialUseCase

### Community 39 - "VaultViewModel"
Cohesion: 0.07
Nodes (8): BiometricVaultKey, base64Decode(), base64Encode(), RewrapResult, VaultKeyStore, WrapRecord, VaultViewModel, VaultKeyRewrapTest

### Community 46 - "VaultUiIntent"
Cohesion: 0.10
Nodes (21): ChangeMasterPassword, ClearPasswordChangeResult, CloseBottomSheet, CloseCategorySheet, CopyPassword, CopyUsername, CreateCategory, CreateMasterPassword (+13 more)

### Community 47 - "java/com/example/data/local/database/VaultDatabase.kt"
Cohesion: 0.12
Nodes (4): VaultDatabase, bindVaultDatabaseContext(), createVaultDatabaseBuilder(), createVaultDatabaseBuilder()

### Community 50 - "OnboardingScreen"
Cohesion: 0.30
Nodes (10): Feature, FeatureCard(), HeroIcon(), IntroPage(), MasterKeyPage(), OnboardingButton(), OnboardingLanguageSelector(), OnboardingScreen() (+2 more)

### Community 51 - "VaultSnapshot"
Cohesion: 0.33
Nodes (5): SyncClient, CategorySnapshot, CredentialSnapshot, MergeResult, VaultSnapshot

### Community 53 - "kotlin/com/example/MainActivity.kt"
Cohesion: 0.12
Nodes (3): MainActivity, BiometricSettings, ClipboardSettings

### Community 54 - "CredentialBottomSheet"
Cohesion: 0.07
Nodes (20): GeneratedSecret, GeneratorConfig, PasswordGenerator, PasswordStrengthLevel, FAIR, STRONG, VERY_STRONG, WEAK (+12 more)

### Community 55 - "SyncTransport.kt"
Cohesion: 0.17
Nodes (5): SyncHost, SyncClient, SyncExchangeResult, SyncHost, SyncHostSession

### Community 56 - ".mergeSnapshot"
Cohesion: 0.18
Nodes (3): currentTimeMillis(), CategoryEntity, CredentialEntity

### Community 58 - "GeneratorUiIntent"
Cohesion: 0.20
Nodes (9): CopyPassword, GeneratorUiIntent, GeneratorUiState, Regenerate, ToggleLowercase, ToggleNumbers, ToggleSymbols, ToggleUppercase (+1 more)

### Community 61 - "PlatformCapabilities.kt"
Cohesion: 0.29
Nodes (3): platformCapabilities(), PlatformCapabilities, platformCapabilities()

### Community 62 - "GeneratorUiIntent"
Cohesion: 0.20
Nodes (9): CopyPassword, GeneratorUiIntent, GeneratorUiState, Regenerate, ToggleLowercase, ToggleNumbers, ToggleSymbols, ToggleUppercase (+1 more)

### Community 63 - "SettingsScreen"
Cohesion: 0.19
Nodes (6): displayNumber(), gradeText(), plural(), languageLabel(), MetricItem(), SettingsScreen()

### Community 65 - "kotlin/com/example/data/local/database/VaultDatabase.kt"
Cohesion: 0.20
Nodes (4): createVaultDatabase(), createVaultDatabaseBuilder(), VaultDatabase, VaultDatabaseConstructor

### Community 67 - "SyncUiIntent"
Cohesion: 0.14
Nodes (14): AuthCancelled, BackToIdle, ConfirmAuthAndSync, ConnectWithManualUri, DismissPinFallback, ManualUriChanged, PinChanged, QrScanned (+6 more)

### Community 69 - "LanguagePreferences"
Cohesion: 0.11
Nodes (3): LanguagePreferences, LanguagePreferences, ClipboardSettings

### Community 71 - "VaultApp"
Cohesion: 0.16
Nodes (11): selectTopLevelTab(), stackDepth(), VaultApp(), VaultBottomNavigationBar(), CategoriesRoute, ChangeMasterPasswordRoute, GeneratorRoute, SettingsRoute (+3 more)

### Community 75 - "AppLanguage"
Cohesion: 0.14
Nodes (9): AppLanguage, ENGLISH, PERSIAN, SYSTEM, LanguageController, LanguagePreferences, resolveLanguage(), LanguageTest (+1 more)

### Community 77 - "SyncPhase"
Cohesion: 0.29
Nodes (7): AwaitingAuth, Error, Hosting, Idle, SyncPhase, SyncUiState, Transferring

### Community 79 - "NeumorphicButton"
Cohesion: 0.32
Nodes (3): NeumorphicButton(), TactileSlider(), TactileToggleSwitch()

### Community 80 - "SyncQrCamera.android.kt"
Cohesion: 0.12
Nodes (3): ScannerRef, SyncQrCamera(), BarcodeCallback

### Community 87 - "java/com/example/domain/model/Credential.kt"
Cohesion: 0.50
Nodes (3): Credential, SecurityReport, VaultCategory

### Community 92 - "Passilius privacy policy"
Cohesion: 0.25
Nodes (7): Changes, Children, Contact, Passilius privacy policy, Permissions, What leaves your device, What stays on your device

### Community 94 - "VaultScreen"
Cohesion: 0.18
Nodes (4): CategoryPillStrip(), NeumorphicSearchBar(), PasswordCard(), VaultScreen()

### Community 98 - "BiometricUnlockResult"
Cohesion: 0.22
Nodes (5): BiometricUnlock, BiometricUnlockResult, Error, Success, Unavailable

### Community 99 - ".start"
Cohesion: 0.32
Nodes (4): isLocalSyncHost(), localLanAddresses(), SyncHost, toHex()

### Community 100 - "AppGraph"
Cohesion: 0.25
Nodes (4): AppGraph, BuildVaultSnapshotUseCase, MergeVaultSnapshotUseCase, main()

### Community 115 - ".parseUri"
Cohesion: 0.18
Nodes (3): base64UrlToBytes(), bytesToBase64Url(), SyncPairingTest

### Community 117 - "Play listing draft"
Cohesion: 0.40
Nodes (4): Data safety form, Play listing draft, Release notes for 1.0, Store listing

### Community 119 - "tr"
Cohesion: 0.53
Nodes (8): tr(), ActionButton(), HostContent(), ManualUriField(), PhaseBanner(), PinFallbackCard(), ScanContent(), SyncScreen()

### Community 121 - "QrImage"
Cohesion: 0.25
Nodes (3): QrCodec, QrImage, QrCodec

### Community 123 - "ManageCategoriesScreen"
Cohesion: 0.20
Nodes (3): categoryLabel(), CategoryBottomSheet(), ManageCategoriesScreen()

### Community 125 - "MasterUnlockScreen"
Cohesion: 0.60
Nodes (3): MasterUnlockScreen(), PasswordField(), UnlockButton()

### Community 130 - "ManageCategoriesUiIntent"
Cohesion: 0.33
Nodes (6): CloseSheet, DeleteCategory, ManageCategoriesUiIntent, OpenCreate, OpenRename, SaveCategory

### Community 132 - "CredentialBottomSheet"
Cohesion: 0.33
Nodes (3): CredentialBottomSheet(), sheetFieldWell(), sheetIconButton()

### Community 134 - "ManageCategoriesUiIntent"
Cohesion: 0.33
Nodes (6): CloseSheet, DeleteCategory, ManageCategoriesUiIntent, OpenCreate, OpenRename, SaveCategory

## Knowledge Gaps
- **142 isolated node(s):** `HardwareUnavailable`, `FeatureUnavailable`, `NoneEnrolled`, `AuthenticationSuccess`, `AuthenticationFailed` (+137 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 425 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **72 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `tr()` connect `tr` to `BiometricUnlockResult`, `rememberScreenContentPadding`, `BiometricPromptManager.kt`, `kotlin/com/example/presentation/navigation/VaultNavigation.kt`, `CredentialBottomSheet`, `VaultApp`, `ExampleRobolectricTest.kt`, `AppLanguage`, `UiMessage`, `SyncQrCamera.android.kt`, `OnboardingScreen`, `kotlin/com/example/presentation/generator/GeneratorViewModel.kt`, `ManageCategoriesScreen`, `MasterUnlockScreen`, `VaultScreen`, `SettingsScreen`?**
  _High betweenness centrality (0.073) - this node is a cross-community bridge._
- **Why does `AppGraph` connect `AppGraph` to `kotlin/com/example/data/local/database/VaultDatabase.kt`, `kotlin/com/example/domain/usecase/VaultUseCases.kt`, `java/com/example/MainActivity.kt`, `App.kt`, `kotlin/com/example/presentation/category/ManageCategoriesViewModel.kt`, `VaultViewModel`, `SyncViewModel`, `kotlin/com/example/MainActivity.kt`, `VaultRepositoryImpl`, `SettingsViewModel.kt`, `kotlin/com/example/presentation/generator/GeneratorViewModel.kt`?**
  _High betweenness centrality (0.070) - this node is a cross-community bridge._
- **Why does `SyncViewModel` connect `SyncViewModel` to `BiometricUnlockResult`, `AppGraph`, `kotlin/com/example/presentation/navigation/VaultNavigation.kt`, `java/com/example/MainActivity.kt`, `VaultApp`, `SyncPhase`, `UiMessage`, `tr`, `SyncPairingInfo`?**
  _High betweenness centrality (0.066) - this node is a cross-community bridge._
- **Are the 6 inferred relationships involving `tr()` (e.g. with `.authenticate()` and `categoryLabel()`) actually correct?**
  _`tr()` has 6 INFERRED edges - model-reasoned connections that need verification._
- **What connects `HardwareUnavailable`, `FeatureUnavailable`, `NoneEnrolled` to the rest of the system?**
  _142 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `java/com/example/presentation/vault/VaultViewModel.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.08019323671497584 - nodes in this community are weakly interconnected._
- **Should `VaultUiIntent` be split into smaller, more focused modules?**
  _Cohesion score 0.11578947368421053 - nodes in this community are weakly interconnected._