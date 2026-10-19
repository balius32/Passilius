# Graph Report - vault  (2026-10-19)

## Corpus Check
- 167 files · ~46,751 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 35 file(s) not represented in the graph (top: .xml 21, .properties 3, (none) 2)

## Summary
- 1716 nodes · 3961 edges · 124 communities (53 shown, 71 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 129 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `9415e634`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- java/com/example/presentation/vault/VaultViewModel.kt
- VaultUiIntent
- CredentialDao
- VaultKeyStore
- CryptoManager
- SyncPairingTest.kt
- CategoryDao
- launch
- java/com/example/core/designsystem/Neumorphism.kt
- java/com/example/core/crypto/CryptoManager.kt
- GeneratorScreen
- java/com/example/core/components/BrandIconWell.kt
- SyncViewModel
- VaultScreen
- SyncTransport.desktop.kt
- rememberScreenContentPadding
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
- AppGraph
- CredentialDao
- java/com/example/MainActivity.kt
- BiometricResult
- java/com/example/presentation/category/ManageCategoriesViewModel.kt
- newSyncId
- ClipboardSettings
- VaultRepository
- VaultViewModel
- VaultUiIntent
- java/com/example/data/local/database/VaultDatabase.kt
- VaultRepository
- ByteArray
- ClipboardHelper.android.kt
- VaultSnapshot
- VaultRepositoryImpl
- kotlin/com/example/MainActivity.kt
- CredentialBottomSheet
- SyncPairingInfo
- .mergeSnapshot
- kotlin/com/example/presentation/generator/GeneratorViewModel.kt
- GeneratorUiIntent
- java/com/example/presentation/generator/GeneratorViewModel.kt
- VaultScreen
- GeneratorScreen
- GeneratorUiIntent
- SyncUiIntent
- QrImageBitmap.desktop.kt
- kotlin/com/example/data/local/database/VaultDatabase.kt
- ByteArray
- QrImage
- CryptoEngine
- ClipboardSettings
- App.kt
- SyncHostSession
- CryptoManager
- BiometricVaultKey
- SettingsScreen
- BiometricUnlockResult
- BiometricUnlock
- CategoryEntity
- OnboardingScreen
- SyncQrCamera.android.kt
- BiometricSettings
- SyncScreen
- VaultApp
- LegacyVaultCrypto
- AuthenticationCallback
- java/com/example/domain/model/Credential.kt
- BiometricUnlock
- QrImageBitmap.kt
- BiometricVaultKey
- Passilius privacy policy
- AGENTS.md
- currentTimeMillis
- currentTimeMillis
- BiometricSettings
- VaultRepositoryImpl
- AuthenticationCallback
- theme/Theme.kt
- SecureRandom.android.kt
- SyncEnvelope
- VaultKeyIo
- SecureRandom.desktop.kt
- SyncEnvelope
- LegacyVaultCrypto
- ChangeMasterPasswordScreen
- SyncQrCamera.desktop.kt
- BiometricVaultKey
- VaultDestination
- Play listing draft
- SettingsViewModel.kt
- SyncQrCamera
- ManageCategoriesScreen
- MasterUnlockScreen
- CredentialEntity
- SettingsScreen

## God Nodes (most connected - your core abstractions)
1. `SyncViewModel` - 27 edges
2. `VaultRepositoryImpl` - 26 edges
3. `AppGraph` - 24 edges
4. `VaultRepositoryImpl` - 23 edges
5. `VaultViewModel` - 23 edges
6. `VaultApp()` - 22 edges
7. `SyncPairingInfo` - 20 edges
8. `VaultUiIntent` - 20 edges
9. `VaultSnapshot` - 19 edges
10. `VaultViewModel` - 18 edges

## Surprising Connections (you probably didn't know these)
- `Credential` --semantically_similar_to--> `Credentials`  [INFERRED] [semantically similar]
  app/README.md → README.md
- `CryptoManager` --semantically_similar_to--> `CryptoManager`  [INFERRED] [semantically similar]
  app/README.md → README.md
- `MasterUnlockScreen` --semantically_similar_to--> `Master unlock`  [INFERRED] [semantically similar]
  app/README.md → README.md
- `PasswordGenerator` --semantically_similar_to--> `Password generator`  [INFERRED] [semantically similar]
  app/README.md → README.md
- `SecurityReport` --semantically_similar_to--> `Security insights`  [INFERRED] [semantically similar]
  app/README.md → README.md

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

## Communities (124 total, 71 thin omitted)

### Community 0 - "java/com/example/presentation/vault/VaultViewModel.kt"
Cohesion: 0.08
Nodes (10): AddCategoryUseCase, DeleteCategoryUseCase, DeleteCredentialUseCase, GeneratePasswordUseCase, GetVaultCredentialsUseCase, ObserveCategoriesUseCase, RenameCategoryUseCase, SaveCredentialUseCase (+2 more)

### Community 1 - "VaultUiIntent"
Cohesion: 0.12
Nodes (18): CloseBottomSheet, CloseCategorySheet, CopyPassword, CopyUsername, CreateCategory, DeleteCredential, LockVault, OpenCreate (+10 more)

### Community 3 - "VaultKeyStore"
Cohesion: 0.20
Nodes (6): base64Decode(), base64Encode(), RewrapResult, VaultKeyStore, WrapRecord, VaultKeyRewrapTest

### Community 6 - "CryptoManager"
Cohesion: 0.06
Nodes (42): Credential, CredentialBottomSheet, CryptoManager, MainActivity, MasterUnlockScreen, PasswordGenerator, SecurityReport, SettingsScreen (+34 more)

### Community 7 - "SyncPairingTest.kt"
Cohesion: 0.05
Nodes (10): VaultCoreUnitTest, base64UrlToBytes(), bytesToBase64Url(), SyncEnvelope, Credential, SecurityReport, VaultCategory, CryptoEngineTest (+2 more)

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
Nodes (4): isLocalSyncHost(), localLanAddresses(), SyncHost, toHex()

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

### Community 33 - "AppGraph"
Cohesion: 0.13
Nodes (11): AppGraph, BuildVaultSnapshotUseCase, MergeVaultSnapshotUseCase, AddCategoryUseCase, DeleteCategoryUseCase, DeleteCredentialUseCase, GeneratePasswordUseCase, GetVaultCredentialsUseCase (+3 more)

### Community 39 - "BiometricResult"
Cohesion: 0.22
Nodes (7): AuthenticationFailed, AuthenticationSuccess, BiometricPromptManager, BiometricResult, FeatureUnavailable, HardwareUnavailable, NoneEnrolled

### Community 40 - "java/com/example/presentation/category/ManageCategoriesViewModel.kt"
Cohesion: 0.05
Nodes (17): CloseSheet, DeleteCategory, ManageCategoriesUiIntent, ManageCategoriesUiState, ManageCategoriesViewModel, OpenCreate, OpenRename, SaveCategory (+9 more)

### Community 46 - "VaultUiIntent"
Cohesion: 0.10
Nodes (21): ChangeMasterPassword, ClearPasswordChangeResult, CloseBottomSheet, CloseCategorySheet, CopyPassword, CopyUsername, CreateCategory, CreateMasterPassword (+13 more)

### Community 47 - "java/com/example/data/local/database/VaultDatabase.kt"
Cohesion: 0.12
Nodes (4): VaultDatabase, bindVaultDatabaseContext(), createVaultDatabaseBuilder(), createVaultDatabaseBuilder()

### Community 51 - "VaultSnapshot"
Cohesion: 0.24
Nodes (7): SyncClient, SyncClient, SyncExchangeResult, CategorySnapshot, CredentialSnapshot, MergeResult, VaultSnapshot

### Community 53 - "kotlin/com/example/MainActivity.kt"
Cohesion: 0.13
Nodes (3): MainActivity, BiometricSettings, ClipboardSettings

### Community 54 - "CredentialBottomSheet"
Cohesion: 0.06
Nodes (23): GeneratedSecret, GeneratorConfig, PasswordGenerator, PasswordStrengthLevel, FAIR, STRONG, VERY_STRONG, WEAK (+15 more)

### Community 55 - "SyncPairingInfo"
Cohesion: 0.15
Nodes (6): exchangeSnapshot(), SyncRejectedException, toHex(), ClipboardHelper, SyncPairingInfo, SyncClient

### Community 56 - ".mergeSnapshot"
Cohesion: 0.17
Nodes (3): currentTimeMillis(), CategoryEntity, CredentialEntity

### Community 58 - "GeneratorUiIntent"
Cohesion: 0.20
Nodes (9): CopyPassword, GeneratorUiIntent, GeneratorUiState, Regenerate, ToggleLowercase, ToggleNumbers, ToggleSymbols, ToggleUppercase (+1 more)

### Community 60 - "VaultScreen"
Cohesion: 0.18
Nodes (4): CategoryPillStrip(), NeumorphicSearchBar(), PasswordCard(), VaultScreen()

### Community 61 - "GeneratorScreen"
Cohesion: 0.21
Nodes (5): NeumorphicButton(), TactileSlider(), TactileToggleSwitch(), AttributeRow(), GeneratorScreen()

### Community 62 - "GeneratorUiIntent"
Cohesion: 0.20
Nodes (9): CopyPassword, GeneratorUiIntent, GeneratorUiState, Regenerate, ToggleLowercase, ToggleNumbers, ToggleSymbols, ToggleUppercase (+1 more)

### Community 63 - "SyncUiIntent"
Cohesion: 0.14
Nodes (14): AuthCancelled, BackToIdle, ConfirmAuthAndSync, ConnectWithManualUri, DismissPinFallback, ManualUriChanged, PinChanged, QrScanned (+6 more)

### Community 65 - "kotlin/com/example/data/local/database/VaultDatabase.kt"
Cohesion: 0.20
Nodes (4): createVaultDatabase(), createVaultDatabaseBuilder(), VaultDatabase, VaultDatabaseConstructor

### Community 67 - "QrImage"
Cohesion: 0.15
Nodes (10): QrCodec, QrImage, AwaitingAuth, Done, Error, Hosting, Idle, SyncPhase (+2 more)

### Community 70 - "App.kt"
Cohesion: 0.11
Nodes (5): platformCapabilities(), App(), PlatformCapabilities, platformCapabilities(), main()

### Community 71 - "SyncHostSession"
Cohesion: 0.22
Nodes (3): SyncHost, SyncHost, SyncHostSession

### Community 73 - "BiometricVaultKey"
Cohesion: 0.06
Nodes (8): BiometricVaultKey, VaultFiles, VaultKeyIo, CryptoManager, VaultSession, LegacyVaultCrypto, VaultKeyIo, vaultDir()

### Community 75 - "BiometricUnlockResult"
Cohesion: 0.25
Nodes (5): BiometricUnlock, BiometricUnlockResult, Error, Success, Unavailable

### Community 79 - "OnboardingScreen"
Cohesion: 0.32
Nodes (9): Feature, FeatureCard(), HeroIcon(), IntroPage(), MasterKeyPage(), OnboardingButton(), OnboardingScreen(), PageBody() (+1 more)

### Community 80 - "SyncQrCamera.android.kt"
Cohesion: 0.08
Nodes (5): QrCodec, ScannerRef, SyncQrCamera(), BarcodeCallback, QrCodec

### Community 83 - "SyncScreen"
Cohesion: 0.50
Nodes (7): ActionButton(), HostContent(), ManualUriField(), PhaseBanner(), PinFallbackCard(), ScanContent(), SyncScreen()

### Community 84 - "VaultApp"
Cohesion: 0.20
Nodes (4): selectTopLevelTab(), stackDepth(), VaultApp(), VaultBottomNavigationBar()

### Community 87 - "java/com/example/domain/model/Credential.kt"
Cohesion: 0.50
Nodes (3): Credential, SecurityReport, VaultCategory

### Community 92 - "Passilius privacy policy"
Cohesion: 0.25
Nodes (7): Changes, Children, Contact, Passilius privacy policy, Permissions, What leaves your device, What stays on your device

### Community 116 - "VaultDestination"
Cohesion: 0.46
Nodes (7): CategoriesRoute, ChangeMasterPasswordRoute, GeneratorRoute, SettingsRoute, SyncRoute, VaultDestination, VaultRoute

### Community 117 - "Play listing draft"
Cohesion: 0.40
Nodes (4): Data safety form, Play listing draft, Release notes for 1.0, Store listing

### Community 123 - "MasterUnlockScreen"
Cohesion: 0.60
Nodes (3): MasterUnlockScreen(), PasswordField(), UnlockButton()

## Knowledge Gaps
- **141 isolated node(s):** `HardwareUnavailable`, `FeatureUnavailable`, `NoneEnrolled`, `AuthenticationSuccess`, `AuthenticationFailed` (+136 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 400 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **71 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `AppGraph` connect `AppGraph` to `kotlin/com/example/data/local/database/VaultDatabase.kt`, `VaultRepositoryImpl`, `java/com/example/MainActivity.kt`, `App.kt`, `java/com/example/presentation/category/ManageCategoriesViewModel.kt`, `VaultViewModel`, `SyncViewModel`, `kotlin/com/example/MainActivity.kt`, `SettingsViewModel.kt`, `kotlin/com/example/presentation/generator/GeneratorViewModel.kt`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **Why does `SyncViewModel` connect `SyncViewModel` to `AppGraph`, `QrImage`, `kotlin/com/example/presentation/navigation/VaultNavigation.kt`, `java/com/example/MainActivity.kt`, `SyncScreen`, `VaultApp`, `SyncPairingInfo`?**
  _High betweenness centrality (0.062) - this node is a cross-community bridge._
- **Why does `SyncPairingInfo` connect `SyncPairingInfo` to `SyncPairingTest.kt`, `SyncHostSession`, `newSyncId`, `SyncViewModel`, `SyncTransport.desktop.kt`, `VaultSnapshot`?**
  _High betweenness centrality (0.046) - this node is a cross-community bridge._
- **What connects `HardwareUnavailable`, `FeatureUnavailable`, `NoneEnrolled` to the rest of the system?**
  _141 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `java/com/example/presentation/vault/VaultViewModel.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.08019323671497584 - nodes in this community are weakly interconnected._
- **Should `VaultUiIntent` be split into smaller, more focused modules?**
  _Cohesion score 0.11578947368421053 - nodes in this community are weakly interconnected._
- **Should `kotlin/com/example/presentation/navigation/VaultNavigation.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.0594059405940594 - nodes in this community are weakly interconnected._