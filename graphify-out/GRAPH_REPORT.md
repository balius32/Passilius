# Graph Report - vault  (2026-10-17)

## Corpus Check
- 159 files · ~44,269 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 34 file(s) not represented in the graph (top: .xml 20, .properties 3, (none) 2)

## Summary
- 1648 nodes · 3784 edges · 116 communities (54 shown, 62 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 129 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `85e2a6fd`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- java/com/example/presentation/vault/VaultViewModel.kt
- VaultUiIntent
- CredentialDao
- ExampleRobolectricTest.kt
- BiometricPromptManager
- CryptoManager
- java/com/example/core/designsystem/Theme.kt
- CategoryDao
- launch
- java/com/example/core/designsystem/Neumorphism.kt
- java/com/example/core/crypto/CryptoManager.kt
- NeumorphicButton
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
- VaultDatabase.android.kt
- kotlin/com/example/domain/usecase/VaultUseCases.kt
- CredentialDao
- java/com/example/MainActivity.kt
- java/com/example/presentation/category/ManageCategoriesViewModel.kt
- VaultRepositoryImpl
- BiometricResult
- VaultRepository
- kotlin/com/example/presentation/category/ManageCategoriesViewModel.kt
- VaultViewModel
- VaultUiIntent
- java/com/example/data/local/database/VaultDatabase.kt
- VaultRepository
- ByteArray
- kotlin/com/example/data/repository/VaultRepositoryImpl.kt
- VaultSnapshot
- VaultRepositoryImpl
- kotlin/com/example/MainActivity.kt
- CredentialBottomSheet
- SyncPairingInfo
- kotlin/com/example/presentation/generator/GeneratorViewModel.kt
- GeneratorUiIntent
- java/com/example/presentation/generator/GeneratorViewModel.kt
- VaultScreen
- SettingsScreen
- GeneratorUiIntent
- VaultApp
- QrImageBitmap.desktop.kt
- kotlin/com/example/data/local/database/VaultDatabase.kt
- ByteArray
- SyncUiIntent
- CryptoEngine
- CredentialBottomSheet
- AppGraph
- SyncHostSession
- CryptoManager
- BiometricVaultKey
- SettingsScreen
- BiometricUnlockResult
- ManageCategoriesScreen
- VaultDestination
- PlatformCapabilities.kt
- SyncQrCamera.android.kt
- BiometricSettings
- .start
- theme/Theme.kt
- LegacyVaultCrypto
- BiometricUnlock
- java/com/example/domain/model/Credential.kt
- BiometricUnlock
- QrImageBitmap.kt
- BiometricVaultKey
- GeneratorScreen
- AGENTS.md
- currentTimeMillis
- currentTimeMillis
- BiometricSettings
- AuthenticationCallback
- MasterUnlockScreen
- PasswordCard
- SecureRandom.android.kt
- SyncEnvelope
- VaultKeyIo
- SecureRandom.desktop.kt
- SyncEnvelope
- GeneratorScreen
- ChangeMasterPasswordScreen
- SyncQrCamera.desktop.kt

## God Nodes (most connected - your core abstractions)
1. `SyncViewModel` - 27 edges
2. `VaultRepositoryImpl` - 26 edges
3. `VaultRepositoryImpl` - 23 edges
4. `AppGraph` - 23 edges
5. `VaultViewModel` - 23 edges
6. `SyncPairingInfo` - 20 edges
7. `VaultApp()` - 20 edges
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

## Communities (116 total, 62 thin omitted)

### Community 0 - "java/com/example/presentation/vault/VaultViewModel.kt"
Cohesion: 0.08
Nodes (10): AddCategoryUseCase, DeleteCategoryUseCase, DeleteCredentialUseCase, GeneratePasswordUseCase, GetVaultCredentialsUseCase, ObserveCategoriesUseCase, RenameCategoryUseCase, SaveCredentialUseCase (+2 more)

### Community 1 - "VaultUiIntent"
Cohesion: 0.12
Nodes (18): CloseBottomSheet, CloseCategorySheet, CopyPassword, CopyUsername, CreateCategory, DeleteCredential, LockVault, OpenCreate (+10 more)

### Community 3 - "ExampleRobolectricTest.kt"
Cohesion: 0.07
Nodes (6): ExampleRobolectricTest, base64UrlToBytes(), bytesToBase64Url(), SyncEnvelope, CryptoEngineTest, SyncPairingTest

### Community 6 - "CryptoManager"
Cohesion: 0.06
Nodes (42): Credential, CredentialBottomSheet, CryptoManager, MainActivity, MasterUnlockScreen, PasswordGenerator, SecurityReport, SettingsScreen (+34 more)

### Community 9 - "launch"
Cohesion: 0.12
Nodes (3): ClipboardHelper, ClipboardHelper, ClipboardHelper

### Community 10 - "java/com/example/core/designsystem/Neumorphism.kt"
Cohesion: 0.24
Nodes (6): neuClickable(), neuFlat(), neuPressed(), neuClickable(), neuFlat(), neuPressed()

### Community 11 - "java/com/example/core/crypto/CryptoManager.kt"
Cohesion: 0.20
Nodes (4): base64Decode(), base64Encode(), base64Decode(), base64Encode()

### Community 12 - "NeumorphicButton"
Cohesion: 0.32
Nodes (3): NeumorphicButton(), TactileSlider(), TactileToggleSwitch()

### Community 13 - "java/com/example/core/components/BrandIconWell.kt"
Cohesion: 0.11
Nodes (16): AppleLogo(), BrandIconWell(), FigmaLogo(), GitHubLogo(), GoogleLogo(), SlackLogo(), SpotifyLogo(), NotchedCapsuleShape (+8 more)

### Community 15 - "VaultScreen"
Cohesion: 0.32
Nodes (3): CategoryPillStrip(), NeumorphicSearchBar(), VaultScreen()

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

### Community 32 - "VaultDatabase.android.kt"
Cohesion: 0.24
Nodes (3): bindVaultDatabaseContext(), createVaultDatabaseBuilder(), createVaultDatabaseBuilder()

### Community 33 - "kotlin/com/example/domain/usecase/VaultUseCases.kt"
Cohesion: 0.14
Nodes (8): AddCategoryUseCase, DeleteCategoryUseCase, DeleteCredentialUseCase, GeneratePasswordUseCase, GetVaultCredentialsUseCase, ObserveCategoriesUseCase, RenameCategoryUseCase, SaveCredentialUseCase

### Community 40 - "java/com/example/presentation/category/ManageCategoriesViewModel.kt"
Cohesion: 0.15
Nodes (8): CloseSheet, DeleteCategory, ManageCategoriesUiIntent, ManageCategoriesUiState, ManageCategoriesViewModel, OpenCreate, OpenRename, SaveCategory

### Community 41 - "VaultRepositoryImpl"
Cohesion: 0.15
Nodes (5): currentTimeMillis(), CategoryEntity, CredentialEntity, InitialSeed, VaultRepositoryImpl

### Community 42 - "BiometricResult"
Cohesion: 0.25
Nodes (7): AuthenticationError, AuthenticationFailed, AuthenticationSuccess, BiometricResult, FeatureUnavailable, HardwareUnavailable, NoneEnrolled

### Community 44 - "kotlin/com/example/presentation/category/ManageCategoriesViewModel.kt"
Cohesion: 0.15
Nodes (8): CloseSheet, DeleteCategory, ManageCategoriesUiIntent, ManageCategoriesUiState, ManageCategoriesViewModel, OpenCreate, OpenRename, SaveCategory

### Community 45 - "VaultViewModel"
Cohesion: 0.06
Nodes (8): BiometricVaultKey, base64Decode(), base64Encode(), RewrapResult, VaultKeyStore, WrapRecord, VaultViewModel, VaultKeyRewrapTest

### Community 46 - "VaultUiIntent"
Cohesion: 0.10
Nodes (21): ChangeMasterPassword, ClearPasswordChangeResult, CloseBottomSheet, CloseCategorySheet, CopyPassword, CopyUsername, CreateCategory, CreateMasterPassword (+13 more)

### Community 50 - "kotlin/com/example/data/repository/VaultRepositoryImpl.kt"
Cohesion: 0.09
Nodes (4): LegacyVaultCrypto, Credential, SecurityReport, VaultCategory

### Community 51 - "VaultSnapshot"
Cohesion: 0.22
Nodes (9): SyncClient, SyncClient, SyncExchangeResult, CategorySnapshot, CredentialSnapshot, MergeResult, VaultSnapshot, BuildVaultSnapshotUseCase (+1 more)

### Community 52 - "VaultRepositoryImpl"
Cohesion: 0.12
Nodes (4): CategoryEntity, CredentialEntity, InitialSeed, VaultRepositoryImpl

### Community 54 - "CredentialBottomSheet"
Cohesion: 0.06
Nodes (21): GeneratedSecret, GeneratorConfig, PasswordGenerator, PasswordStrengthLevel, FAIR, STRONG, VERY_STRONG, WEAK (+13 more)

### Community 55 - "SyncPairingInfo"
Cohesion: 0.19
Nodes (4): secureRandomBytes(), newSyncId(), ClipboardHelper, SyncPairingInfo

### Community 58 - "GeneratorUiIntent"
Cohesion: 0.20
Nodes (9): CopyPassword, GeneratorUiIntent, GeneratorUiState, Regenerate, ToggleLowercase, ToggleNumbers, ToggleSymbols, ToggleUppercase (+1 more)

### Community 60 - "VaultScreen"
Cohesion: 0.20
Nodes (4): CategoryPillStrip(), NeumorphicSearchBar(), PasswordCard(), VaultScreen()

### Community 61 - "SettingsScreen"
Cohesion: 0.21
Nodes (5): NeumorphicButton(), TactileSlider(), TactileToggleSwitch(), MetricItem(), SettingsScreen()

### Community 62 - "GeneratorUiIntent"
Cohesion: 0.20
Nodes (9): CopyPassword, GeneratorUiIntent, GeneratorUiState, Regenerate, ToggleLowercase, ToggleNumbers, ToggleSymbols, ToggleUppercase (+1 more)

### Community 63 - "VaultApp"
Cohesion: 0.20
Nodes (4): selectTopLevelTab(), stackDepth(), VaultApp(), VaultBottomNavigationBar()

### Community 65 - "kotlin/com/example/data/local/database/VaultDatabase.kt"
Cohesion: 0.20
Nodes (4): createVaultDatabase(), createVaultDatabaseBuilder(), VaultDatabase, VaultDatabaseConstructor

### Community 67 - "SyncUiIntent"
Cohesion: 0.05
Nodes (34): QrCodec, QrCodec, QrImage, AuthCancelled, AwaitingAuth, BackToIdle, ConfirmAuthAndSync, ConnectWithManualUri (+26 more)

### Community 69 - "CredentialBottomSheet"
Cohesion: 0.33
Nodes (3): CredentialBottomSheet(), sheetFieldWell(), sheetIconButton()

### Community 70 - "AppGraph"
Cohesion: 0.21
Nodes (3): App(), AppGraph, main()

### Community 71 - "SyncHostSession"
Cohesion: 0.22
Nodes (3): SyncHost, SyncHost, SyncHostSession

### Community 73 - "BiometricVaultKey"
Cohesion: 0.06
Nodes (8): BiometricVaultKey, VaultFiles, VaultKeyIo, CryptoManager, VaultSession, LegacyVaultCrypto, VaultKeyIo, vaultDir()

### Community 75 - "BiometricUnlockResult"
Cohesion: 0.25
Nodes (5): BiometricUnlock, BiometricUnlockResult, Error, Success, Unavailable

### Community 77 - "VaultDestination"
Cohesion: 0.46
Nodes (7): CategoriesRoute, ChangeMasterPasswordRoute, GeneratorRoute, SettingsRoute, SyncRoute, VaultDestination, VaultRoute

### Community 79 - "PlatformCapabilities.kt"
Cohesion: 0.29
Nodes (3): platformCapabilities(), PlatformCapabilities, platformCapabilities()

### Community 80 - "SyncQrCamera.android.kt"
Cohesion: 0.12
Nodes (3): ScannerRef, SyncQrCamera(), BarcodeCallback

### Community 83 - ".start"
Cohesion: 0.38
Nodes (3): localLanAddresses(), SyncHost, toHex()

### Community 87 - "java/com/example/domain/model/Credential.kt"
Cohesion: 0.50
Nodes (3): Credential, SecurityReport, VaultCategory

### Community 99 - "MasterUnlockScreen"
Cohesion: 0.60
Nodes (3): MasterUnlockScreen(), PasswordField(), UnlockButton()

## Knowledge Gaps
- **134 isolated node(s):** `HardwareUnavailable`, `FeatureUnavailable`, `NoneEnrolled`, `AuthenticationSuccess`, `AuthenticationFailed` (+129 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 381 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **62 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `AppGraph` connect `AppGraph` to `kotlin/com/example/data/local/database/VaultDatabase.kt`, `kotlin/com/example/domain/usecase/VaultUseCases.kt`, `java/com/example/MainActivity.kt`, `VaultRepositoryImpl`, `kotlin/com/example/presentation/category/ManageCategoriesViewModel.kt`, `VaultViewModel`, `SyncViewModel`, `VaultSnapshot`, `kotlin/com/example/MainActivity.kt`, `kotlin/com/example/presentation/generator/GeneratorViewModel.kt`?**
  _High betweenness centrality (0.088) - this node is a cross-community bridge._
- **Why does `main()` connect `AppGraph` to `CryptoManager`?**
  _High betweenness centrality (0.066) - this node is a cross-community bridge._
- **Why does `Layout` connect `CryptoManager` to `AppGraph`?**
  _High betweenness centrality (0.066) - this node is a cross-community bridge._
- **What connects `HardwareUnavailable`, `FeatureUnavailable`, `NoneEnrolled` to the rest of the system?**
  _134 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `java/com/example/presentation/vault/VaultViewModel.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.08019323671497584 - nodes in this community are weakly interconnected._
- **Should `VaultUiIntent` be split into smaller, more focused modules?**
  _Cohesion score 0.11578947368421053 - nodes in this community are weakly interconnected._
- **Should `CredentialDao` be split into smaller, more focused modules?**
  _Cohesion score 0.1111111111111111 - nodes in this community are weakly interconnected._