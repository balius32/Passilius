package com.example.presentation.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.components.NotchedCapsuleShape
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnPrimary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SoftStroke
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.VaultTypography
import com.example.presentation.category.CategoryBottomSheet
import com.example.presentation.category.ManageCategoriesScreen
import com.example.presentation.category.ManageCategoriesViewModel
import com.example.presentation.credential.CredentialBottomSheet
import com.example.presentation.generator.GeneratorScreen
import com.example.presentation.generator.GeneratorViewModel
import com.example.presentation.settings.SettingsScreen
import com.example.presentation.unlock.MasterUnlockScreen
import com.example.presentation.vault.VaultScreen
import com.example.presentation.vault.VaultUiIntent
import com.example.presentation.vault.VaultViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

private const val ProfileTransitionDurationMs = 320

private val FabSize = 56.dp
private val FabHitSize = 80.dp

private fun selectTopLevelTab(
    backStack: MutableList<VaultDestination>,
    destination: VaultDestination
) {
    if (backStack.lastOrNull() == destination) return
    backStack.clear()
    backStack.add(destination)
}

@Composable
fun VaultApp(
    vaultViewModel: VaultViewModel,
    generatorViewModel: GeneratorViewModel,
    manageCategoriesViewModel: ManageCategoriesViewModel
) {
    val vaultState by vaultViewModel.uiState.collectAsStateWithLifecycle()
    val backStack = remember { mutableStateListOf<VaultDestination>(VaultRoute) }

    if (vaultState.isLocked) {
        MasterUnlockScreen(
            onUnlocked = { vaultViewModel.handleIntent(VaultUiIntent.UnlockVault) },
            masterPin = vaultState.masterPin
        )
    } else {
        val currentKey = backStack.lastOrNull()
        val showBottomBar = currentKey != SettingsRoute && currentKey != CategoriesRoute
        val hazeState = rememberHazeState()

        Scaffold(
            containerColor = SurfaceCanvas,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(state = hazeState)
                ) {
                    AnimatedContent(
                        targetState = currentKey,
                        transitionSpec = {
                            val forward = targetState == SettingsRoute ||
                                targetState == CategoriesRoute
                            if (forward) {
                                slideInHorizontally(
                                    initialOffsetX = { it },
                                    animationSpec = tween(ProfileTransitionDurationMs)
                                ) togetherWith slideOutHorizontally(
                                    targetOffsetX = { -it / 4 },
                                    animationSpec = tween(ProfileTransitionDurationMs)
                                )
                            } else {
                                fadeIn(animationSpec = tween(120)) togetherWith
                                    fadeOut(animationSpec = tween(120))
                            }
                        },
                        label = "vault_nav"
                    ) { destination ->
                        when (destination) {
                            VaultRoute, null -> VaultScreen(
                                viewModel = vaultViewModel,
                                onNavigateToSettings = { backStack.add(SettingsRoute) }
                            )
                            GeneratorRoute -> GeneratorScreen(
                                viewModel = generatorViewModel,
                                onNavigateToSettings = { backStack.add(SettingsRoute) }
                            )
                            SettingsRoute -> SettingsScreen(
                                onBack = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
                                },
                                onLockVault = {
                                    vaultViewModel.handleIntent(VaultUiIntent.LockVault)
                                },
                                onManageCategories = { backStack.add(CategoriesRoute) }
                            )
                            CategoriesRoute -> ManageCategoriesScreen(
                                viewModel = manageCategoriesViewModel,
                                onBack = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
                                }
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = showBottomBar,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    VaultBottomNavigationBar(
                        currentDestination = currentKey,
                        hazeState = hazeState,
                        onSelectDestination = { destination ->
                            selectTopLevelTab(backStack, destination)
                        },
                        onOpenCreate = {
                            vaultViewModel.handleIntent(VaultUiIntent.OpenCreate)
                        }
                    )
                }

                if (vaultState.isBottomSheetOpen) {
                    key(vaultState.bottomSheetSessionId) {
                        CredentialBottomSheet(
                            initialCredential = vaultState.editingCredential,
                            categories = vaultState.selectableCategories,
                            onDismiss = {
                                vaultViewModel.handleIntent(VaultUiIntent.CloseBottomSheet)
                            },
                            onSave = {
                                vaultViewModel.handleIntent(VaultUiIntent.SaveCredential(it))
                            }
                        )
                    }
                }

                if (vaultState.isCategorySheetOpen) {
                    key(vaultState.categorySheetSessionId) {
                        CategoryBottomSheet(
                            onDismiss = {
                                vaultViewModel.handleIntent(VaultUiIntent.CloseCategorySheet)
                            },
                            onSave = {
                                vaultViewModel.handleIntent(VaultUiIntent.CreateCategory(it))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VaultBottomNavigationBar(
    currentDestination: VaultDestination?,
    onSelectDestination: (VaultDestination) -> Unit,
    onOpenCreate: () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    val vaultTabInteraction = remember { MutableInteractionSource() }
    val generatorTabInteraction = remember { MutableInteractionSource() }
    val fabInteraction = remember { MutableInteractionSource() }

    val barShape = remember { NotchedCapsuleShape(fabRadius = FabSize / 2, notchGap = 6.dp) }
    val glassStyle = remember {
        HazeStyle(
            backgroundColor = SurfaceCanvas,
            tints = listOf(HazeTint(Color.White.copy(alpha = 0.45f))),
            blurRadius = 28.dp,
            noiseFactor = 0.04f,
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 20.dp, end = 20.dp, bottom = 14.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .padding(top = FabHitSize / 2)
                .fillMaxWidth()
                .height(64.dp)
                .clip(barShape)
                .hazeEffect(state = hazeState, style = glassStyle)
                .border(1.dp, SoftStroke, barShape)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isVault = currentDestination == VaultRoute
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("nav_tab_vault")
                        .clickable(
                            interactionSource = vaultTabInteraction,
                            indication = null
                        ) { onSelectDestination(VaultRoute) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isVault) Icons.Filled.Lock else Icons.Outlined.Lock,
                        contentDescription = "Vault",
                        tint = if (isVault) ElectricPrimaryBright else SecondarySlate,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Vault",
                        style = VaultTypography.labelSmall,
                        color = if (isVault) ElectricPrimaryBright else SecondarySlate,
                        fontWeight = if (isVault) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                val isGen = currentDestination == GeneratorRoute
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("nav_tab_generator")
                        .clickable(
                            interactionSource = generatorTabInteraction,
                            indication = null
                        ) { onSelectDestination(GeneratorRoute) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isGen) Icons.Filled.Key else Icons.Outlined.Key,
                        contentDescription = "Generator",
                        tint = if (isGen) ElectricPrimaryBright else SecondarySlate,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Generator",
                        style = VaultTypography.labelSmall,
                        color = if (isGen) ElectricPrimaryBright else SecondarySlate,
                        fontWeight = if (isGen) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .zIndex(2f)
                .size(FabHitSize)
                .testTag("open_generator_fab")
                .clickable(
                    interactionSource = fabInteraction,
                    indication = null
                ) { onOpenCreate() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(FabSize)
                    .clip(CircleShape)
                    .background(ElectricPrimaryBright, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Password",
                    tint = OnPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
