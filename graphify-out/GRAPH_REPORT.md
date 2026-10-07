# Graph Report - vault  (2026-10-07)

## Corpus Check
- Corpus is ~17,381 words - fits in a single context window. You may not need a graph.

## Summary
- 663 nodes · 1517 edges · 38 communities (22 shown, 16 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 30 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Use Cases Clipboard
- Vault Repository
- Category Room DAO
- Password Generator
- Biometric Unlock
- App Architecture Concepts
- Neumorphic Design System
- CryptoManager Encryption
- Tactile Controls
- Brand Icon Wells
- Vault List Components
- Notched Capsule Shape
- Screen Insets Helpers
- Vault App Shell
- Instrumented Tests
- Navigation Routes
- Xxxhdpi Round Icon
- Manage Categories UI
- Hdpi Launcher Icon
- Mdpi Launcher Icon
- Xhdpi Launcher Icon
- Xxhdpi Launcher Icon
- Xxhdpi Round Icon
- Xxxhdpi Launcher Icon
- Hdpi Round Icon
- Mdpi Round Icon
- Xhdpi Round Icon
- Generator Screen UI
- Settings Metrics UI
- Master Unlock Screen

## God Nodes (most connected - your core abstractions)
1. `Credential` - 27 edges
2. `VaultRepositoryImpl` - 26 edges
3. `VaultRepository` - 25 edges
4. `VaultViewModel` - 24 edges
5. `ManageCategoriesViewModel` - 20 edges
6. `VaultUiIntent` - 19 edges
7. `CredentialDao` - 18 edges
8. `CredentialEntity` - 17 edges
9. `VaultApp()` - 16 edges
10. `GeneratorViewModel` - 15 edges

## Surprising Connections (you probably didn't know these)
- `Vault app module` --semantically_similar_to--> `Vault`  [INFERRED] [semantically similar]
  app/README.md → README.md
- `MasterUnlockScreen` --semantically_similar_to--> `Master unlock`  [INFERRED] [semantically similar]
  app/README.md → README.md
- `PasswordGenerator` --semantically_similar_to--> `Password generator`  [INFERRED] [semantically similar]
  app/README.md → README.md
- `Credential` --semantically_similar_to--> `Credentials`  [INFERRED] [semantically similar]
  app/README.md → README.md
- `SecurityReport` --semantically_similar_to--> `Security insights`  [INFERRED] [semantically similar]
  app/README.md → README.md

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Vault security model** — readme_encryption, readme_cryptomanager, readme_android_keystore, readme_aes_gcm, readme_master_unlock, readme_biometrics [INFERRED 0.85]
- **Clean Architecture layers** — readme_clean_architecture, readme_data_layer, readme_domain_layer, readme_presentation_layer, readme_core_layer [EXTRACTED 1.00]
- **App module feature surface** — app_readme_mainactivity, app_readme_credentialbottomsheet, app_readme_passwordgenerator, app_readme_masterunlockscreen, app_readme_settingsscreen, app_readme_vaultnavigation [EXTRACTED 1.00]
- **Default Android Launcher Icon Composition** — app_src_main_res_mipmap_mdpi_ic_launcher, app_src_main_res_mipmap_mdpi_ic_launcher_android_robot_head, app_src_main_res_mipmap_mdpi_ic_launcher_long_shadow, app_src_main_res_mipmap_mdpi_ic_launcher_green_grid_background [EXTRACTED 1.00]
- **Round Launcher Icon Visual Composition** — app_src_main_res_mipmap_mdpi_ic_launcher_round, app_src_main_res_mipmap_mdpi_ic_launcher_round_android_bugdroid, app_src_main_res_mipmap_mdpi_ic_launcher_round_material_green_circle, app_src_main_res_mipmap_mdpi_ic_launcher_round_long_shadow [EXTRACTED 1.00]
- **Default Android Launcher Icon Composition** — app_src_main_res_mipmap_xhdpi_ic_launcher, app_src_main_res_mipmap_xhdpi_ic_launcher_android_robot_head, app_src_main_res_mipmap_xhdpi_ic_launcher_long_shadow, app_src_main_res_mipmap_xhdpi_ic_launcher_green_grid_background [EXTRACTED 1.00]
- **Round Launcher Visual System** — app_src_main_res_mipmap_xhdpi_ic_launcher_round_round_launcher_icon, app_src_main_res_mipmap_xhdpi_ic_launcher_round_android_robot_head, app_src_main_res_mipmap_xhdpi_ic_launcher_round_green_grid_background, app_src_main_res_mipmap_xhdpi_ic_launcher_round_material_long_shadow [INFERRED 0.85]
- **Default Android Studio Launcher Icon Composition** — app_src_main_res_mipmap_xxhdpi_ic_launcher_android_robot, app_src_main_res_mipmap_xxhdpi_ic_launcher_green_grid_background, app_src_main_res_mipmap_xxhdpi_ic_launcher_material_long_shadow, app_src_main_res_mipmap_xxhdpi_ic_launcher_squircle_mask [EXTRACTED 1.00]
- **Default Android Studio Round Launcher Icon Composition** — app_src_main_res_mipmap_xxhdpi_ic_launcher_round_android_robot, app_src_main_res_mipmap_xxhdpi_ic_launcher_round_green_grid_background, app_src_main_res_mipmap_xxhdpi_ic_launcher_round_material_long_shadow, app_src_main_res_mipmap_xxhdpi_ic_launcher_round_circular_mask [EXTRACTED 1.00]
- **Default Android App Icon Branding** — app_src_main_res_mipmap_xxxhdpi_ic_launcher_android_launcher_icon, app_src_main_res_mipmap_xxxhdpi_ic_launcher_bugdroid_head, app_src_main_res_mipmap_xxxhdpi_ic_launcher_green_grid_background, app_src_main_res_mipmap_xxxhdpi_ic_launcher_long_shadow_style [EXTRACTED 1.00]
- **Default Android Round App Icon Branding** — app_src_main_res_mipmap_xxxhdpi_ic_launcher_round_android_round_launcher_icon, app_src_main_res_mipmap_xxxhdpi_ic_launcher_round_bugdroid_head, app_src_main_res_mipmap_xxxhdpi_ic_launcher_round_green_grid_background, app_src_main_res_mipmap_xxxhdpi_ic_launcher_round_long_shadow_style [EXTRACTED 1.00]

## Communities (38 total, 16 thin omitted)

### Community 0 - "Use Cases Clipboard"
Cohesion: 0.05
Nodes (21): ClipboardHelper, AddCategoryUseCase, DeleteCategoryUseCase, DeleteCredentialUseCase, GeneratePasswordUseCase, GetVaultCredentialsUseCase, ObserveCategoriesUseCase, RenameCategoryUseCase (+13 more)

### Community 1 - "Vault Repository"
Cohesion: 0.06
Nodes (24): InitialSeed, VaultRepositoryImpl, Credential, SecurityReport, VaultCategory, VaultRepository, CloseBottomSheet, CloseCategorySheet (+16 more)

### Community 2 - "Category Room DAO"
Cohesion: 0.07
Nodes (5): CategoryDao, CredentialDao, VaultDatabase, CategoryEntity, CredentialEntity

### Community 3 - "Password Generator"
Cohesion: 0.06
Nodes (22): GeneratedSecret, GeneratorConfig, PasswordGenerator, PasswordStrengthLevel, FAIR, STRONG, VERY_STRONG, WEAK (+14 more)

### Community 4 - "Biometric Unlock"
Cohesion: 0.06
Nodes (11): AuthenticationError, AuthenticationFailed, AuthenticationSuccess, BiometricPromptManager, AuthenticationCallback, BiometricResult, FeatureUnavailable, HardwareUnavailable (+3 more)

### Community 6 - "App Architecture Concepts"
Cohesion: 0.07
Nodes (37): Credential, CredentialBottomSheet, CryptoManager, MainActivity, MasterUnlockScreen, PasswordGenerator, SecurityReport, SettingsScreen (+29 more)

### Community 10 - "Neumorphic Design System"
Cohesion: 0.16
Nodes (3): neuClickable(), neuFlat(), neuPressed()

### Community 12 - "Tactile Controls"
Cohesion: 0.14
Nodes (3): NeumorphicButton(), TactileSlider(), TactileToggleSwitch()

### Community 13 - "Brand Icon Wells"
Cohesion: 0.25
Nodes (7): AppleLogo(), BrandIconWell(), FigmaLogo(), GitHubLogo(), GoogleLogo(), SlackLogo(), SpotifyLogo()

### Community 15 - "Vault List Components"
Cohesion: 0.24
Nodes (4): CategoryPillStrip(), NeumorphicSearchBar(), PasswordCard(), VaultScreen()

### Community 18 - "Vault App Shell"
Cohesion: 0.32
Nodes (3): selectTopLevelTab(), VaultApp(), VaultBottomNavigationBar()

### Community 20 - "Navigation Routes"
Cohesion: 0.67
Nodes (4): CategoriesRoute, GeneratorRoute, SettingsRoute, VaultRoute

### Community 21 - "Xxxhdpi Round Icon"
Cohesion: 0.33
Nodes (7): Android Round Launcher Icon (ic_launcher_round), Android Bugdroid Head Silhouette, Circular Round Icon Mask, Default Android Studio Placeholder Icon, Green Grid Circular Background, Long Shadow Material Style, mipmap-xxxhdpi Density Bucket

### Community 23 - "Hdpi Launcher Icon"
Cohesion: 0.40
Nodes (6): Android App Launcher Icon (hdpi), Android Bugdroid Head Silhouette, Green Gridded Background, Home Screen Launcher Affordance, Material Design Drop Shadow, Squircle Icon Container

### Community 24 - "Mdpi Launcher Icon"
Cohesion: 0.40
Nodes (6): Android Launcher Icon (ic_launcher), Android Robot Head Silhouette, Green Grid Background, Long Shadow Design Effect, mipmap-mdpi Density Resource, Rounded Square Icon Mask

### Community 25 - "Xhdpi Launcher Icon"
Cohesion: 0.40
Nodes (6): Android Launcher Icon (ic_launcher), Android Robot Head Silhouette, Green Grid Background, Long Shadow Design Effect, mipmap-xhdpi Density Resource, Rounded Square Icon Mask

### Community 26 - "Xxhdpi Launcher Icon"
Cohesion: 0.40
Nodes (6): Android App Launcher Icon (xxhdpi), Android Robot (Bugdroid) Head, Default Android Studio Placeholder Branding, Mint Green Grid Background, Material Design Long Shadow, Squircle Icon Mask

### Community 27 - "Xxhdpi Round Icon"
Cohesion: 0.40
Nodes (6): Android Round App Launcher Icon (xxhdpi), Android Robot (Bugdroid) Head, Circular Round Icon Mask, Default Android Studio Placeholder Branding, Mint Green Grid Background, Material Design Long Shadow

### Community 28 - "Xxxhdpi Launcher Icon"
Cohesion: 0.40
Nodes (6): Android Launcher Icon (ic_launcher), Android Bugdroid Head Silhouette, Default Android Studio Placeholder Icon, Green Grid Background, Long Shadow Material Style, mipmap-xxxhdpi Density Bucket

### Community 29 - "Hdpi Round Icon"
Cohesion: 0.50
Nodes (5): Android Bugdroid Head Silhouette, Diagonal Drop Shadow, Green Circular Grid Background, Material Design Adaptive Icon Style, Round Android Launcher Icon

### Community 30 - "Mdpi Round Icon"
Cohesion: 0.60
Nodes (5): Round App Launcher Icon, Android Bugdroid Mascot Head, Long Shadow Effect, Material Design Green Circle Background, MDPI Mipmap Density Resource

### Community 31 - "Xhdpi Round Icon"
Cohesion: 0.50
Nodes (5): Android Robot Head, Green Grid Background, Material Design Long Shadow, mipmap-xhdpi Launcher Asset, Round Android Launcher Icon

## Knowledge Gaps
- **71 isolated node(s):** `HardwareUnavailable`, `FeatureUnavailable`, `NoneEnrolled`, `AuthenticationSuccess`, `AuthenticationFailed` (+66 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 207 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **16 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Credential` connect `Vault Repository` to `Use Cases Clipboard`, `Password Generator`, `Vault UI Screens`, `Credential Bottom Sheets`, `Vault List Components`?**
  _High betweenness centrality (0.093) - this node is a cross-community bridge._
- **Why does `VaultRepositoryImpl` connect `Vault Repository` to `Use Cases Clipboard`, `Category Room DAO`, `Password Generator`?**
  _High betweenness centrality (0.081) - this node is a cross-community bridge._
- **Why does `BiometricPromptManager` connect `Biometric Unlock` to `Unlock Settings UI`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **What connects `HardwareUnavailable`, `FeatureUnavailable`, `NoneEnrolled` to the rest of the system?**
  _71 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Use Cases Clipboard` be split into smaller, more focused modules?**
  _Cohesion score 0.05048766494549627 - nodes in this community are weakly interconnected._
- **Should `Vault Repository` be split into smaller, more focused modules?**
  _Cohesion score 0.055523085914669784 - nodes in this community are weakly interconnected._
- **Should `Category Room DAO` be split into smaller, more focused modules?**
  _Cohesion score 0.0726764500349406 - nodes in this community are weakly interconnected._