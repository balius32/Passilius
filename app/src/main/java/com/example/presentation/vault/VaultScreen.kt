package com.example.presentation.vault

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.components.CategoryPillStrip
import com.example.core.components.NeumorphicSearchBar
import com.example.core.components.PasswordCard
import com.example.core.components.TactileToastPill
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.OnSurfaceSecondary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.designsystem.neuFlat
import com.example.core.designsystem.neuPressed
import com.example.core.util.rememberScreenContentPadding
import kotlinx.coroutines.delay

@Composable
fun VaultScreen(
    viewModel: VaultViewModel,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentPadding = rememberScreenContentPadding()

    // Auto-clear toast after 2.5 seconds
    LaunchedEffect(uiState.toastMessage) {
        if (uiState.toastMessage != null) {
            delay(2500)
            viewModel.handleIntent(VaultUiIntent.ClearToast)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Vault",
                        style = VaultTypography.headlineLarge,
                        color = OnSurfacePrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Search Button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("search_btn")
                                .then(
                                    if (uiState.isSearchVisible) {
                                        Modifier.neuPressed(
                                            shape = CircleShape,
                                            cornerRadius = 20.dp,
                                            backgroundColor = Color(0xFFE5EDFC)
                                        )
                                    } else {
                                        Modifier.neuFlat(
                                            shape = CircleShape,
                                            cornerRadius = 20.dp,
                                            backgroundColor = SurfaceContainerLowest
                                        )
                                    }
                                )
                                .clickable { viewModel.handleIntent(VaultUiIntent.ToggleSearch) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = if (uiState.isSearchVisible) ElectricPrimaryBright else SecondarySlate,
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        // Profile / Security Lock Button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("profile_btn")
                                .neuFlat(
                                    shape = CircleShape,
                                    cornerRadius = 20.dp,
                                    backgroundColor = SurfaceContainerLowest
                                )
                                .clickable { onNavigateToSettings() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Security Settings",
                                tint = SecondarySlate,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Search Filter Recessed Bar (Toggled state)
            if (uiState.isSearchVisible) {
                item {
                    NeumorphicSearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = { viewModel.handleIntent(VaultUiIntent.UpdateSearchQuery(it)) }
                    )
                }
            }

            // Category Filter Strip
            item {
                CategoryPillStrip(
                    categories = uiState.categories,
                    selectedCategory = uiState.selectedCategory,
                    onSelectCategory = { viewModel.handleIntent(VaultUiIntent.SelectCategory(it)) }
                )
            }

            // Vault Summary Meta
            item {
                Column(modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)) {
                    Text(
                        text = "${uiState.credentials.size} Passwords",
                        style = VaultTypography.headlineSmall,
                        color = OnSurfacePrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "All accounts are securely stored",
                        style = VaultTypography.bodySmall,
                        color = OnSurfaceSecondary,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
            }

            // Password Cards List
            if (uiState.credentials.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No credentials found in this vault category.",
                            style = VaultTypography.bodyMedium,
                            color = SecondarySlate
                        )
                    }
                }
            } else {
                items(
                    items = uiState.credentials,
                    key = { it.id }
                ) { cred ->
                    PasswordCard(
                        credential = cred,
                        onCopyPassword = { viewModel.handleIntent(VaultUiIntent.CopyPassword(it, cred.service)) },
                        onCopyUsername = { viewModel.handleIntent(VaultUiIntent.CopyUsername(it)) },
                        onEdit = { viewModel.handleIntent(VaultUiIntent.OpenEdit(it)) },
                        onDelete = { viewModel.handleIntent(VaultUiIntent.DeleteCredential(it)) },
                        onToggleFavorite = { id, fav -> viewModel.handleIntent(VaultUiIntent.ToggleFavorite(id, fav)) }
                    )
                }
            }
        }

        // Floating Toast Pill Notification
        TactileToastPill(
            message = uiState.toastMessage ?: "",
            visible = uiState.toastMessage != null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 70.dp)
        )

    }
}
