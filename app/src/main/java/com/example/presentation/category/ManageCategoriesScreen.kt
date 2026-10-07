package com.example.presentation.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.designsystem.neuFlat
import com.example.core.util.rememberScreenContentPadding

@Composable
fun ManageCategoriesScreen(
    viewModel: ManageCategoriesViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentPadding = rememberScreenContentPadding(bottom = 60.dp)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .neuFlat(
                                shape = CircleShape,
                                cornerRadius = 20.dp,
                                backgroundColor = SurfaceContainerLowest
                            )
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurfacePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Categories",
                        style = VaultTypography.headlineLarge,
                        color = OnSurfacePrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("manage_category_add")
                            .neuFlat(
                                shape = CircleShape,
                                cornerRadius = 20.dp,
                                backgroundColor = SurfaceContainerLowest
                            )
                            .clickable { viewModel.handleIntent(ManageCategoriesUiIntent.OpenCreate) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add category",
                            tint = ElectricPrimaryBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            items(uiState.categories, key = { it }) { category ->
                val canDelete = viewModel.canDelete(category)
                val canRename = viewModel.canRename(category)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp)
                        .neuFlat(
                            shape = RoundedCornerShape(20.dp),
                            cornerRadius = 20.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category,
                            style = VaultTypography.bodyMedium,
                            color = OnSurfacePrimary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )

                        if (canRename) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .neuFlat(
                                        shape = CircleShape,
                                        cornerRadius = 18.dp,
                                        backgroundColor = SurfaceCanvas
                                    )
                                    .clickable {
                                        viewModel.handleIntent(
                                            ManageCategoriesUiIntent.OpenRename(category)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Rename",
                                    tint = ElectricPrimaryBright,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        if (canDelete) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .neuFlat(
                                        shape = CircleShape,
                                        cornerRadius = 18.dp,
                                        backgroundColor = SurfaceCanvas
                                    )
                                    .clickable {
                                        viewModel.handleIntent(
                                            ManageCategoriesUiIntent.DeleteCategory(category)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFBA1A1A),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.categories.isEmpty()) {
                item {
                    Text(
                        text = "No categories yet.",
                        style = VaultTypography.bodySmall,
                        color = SecondarySlate,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }

        if (uiState.isSheetOpen) {
            key(uiState.sheetSessionId) {
                CategoryBottomSheet(
                    initialName = uiState.editingCategory,
                    onDismiss = { viewModel.handleIntent(ManageCategoriesUiIntent.CloseSheet) },
                    onSave = { viewModel.handleIntent(ManageCategoriesUiIntent.SaveCategory(it)) }
                )
            }
        }
    }
}
