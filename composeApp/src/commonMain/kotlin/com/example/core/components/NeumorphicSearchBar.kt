package com.example.core.components

import com.example.core.localization.*
import passilius.composeapp.generated.resources.*

import com.example.core.localization.tr

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnPrimary
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.OutlineColor
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.designsystem.neuFlat
import com.example.core.designsystem.neuPressed

@Composable
fun NeumorphicSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = tr(Res.string.search_accounts_emails)
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .neuPressed(
                shape = RoundedCornerShape(16.dp),
                cornerRadius = 16.dp,
                backgroundColor = Color(0xFFEFF2F7)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = tr(Res.string.search),
                tint = OutlineColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .testTag("search_input"),
                textStyle = VaultTypography.bodyMedium.copy(color = OnSurfacePrimary),
                cursorBrush = SolidColor(ElectricPrimaryBright),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = placeholderText,
                            style = VaultTypography.bodyMedium,
                            color = OutlineColor
                        )
                    }
                    innerTextField()
                }
            )

            if (query.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onQueryChange("") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = tr(Res.string.clear),
                        tint = SecondarySlate,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryPillStrip(
    categories: List<String>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    onAddCategory: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(categories) { category ->
            val isSelected = category.equals(selectedCategory, ignoreCase = true)

            Box(
                modifier = Modifier
                    .testTag("category_chip_${category.lowercase()}")
                    .then(
                        if (isSelected) {
                            Modifier.neuFlat(
                                shape = RoundedCornerShape(20.dp),
                                cornerRadius = 20.dp,
                                backgroundColor = ElectricPrimaryBright,
                                strokeColor = ElectricPrimaryBright
                            )
                        } else {
                            Modifier.neuFlat(
                                shape = RoundedCornerShape(20.dp),
                                cornerRadius = 20.dp,
                                backgroundColor = SurfaceContainerLowest
                            )
                        }
                    )
                    .clickable { onSelectCategory(category) }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(Color.White, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (category == "All Vaults" || category == "All") tr(Res.string.all_vaults) else categoryLabel(category),
                        style = VaultTypography.labelSmall,
                        color = if (isSelected) Color.White else SecondarySlate,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(Color.White, CircleShape)
                        )
                    }
                }
            }
        }

        if (onAddCategory != null) {
            item {
                Box(
                    modifier = Modifier
                        .testTag("category_chip_add")
                        .neuFlat(
                            shape = RoundedCornerShape(20.dp),
                            cornerRadius = 20.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .clickable(onClick = onAddCategory)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = tr(Res.string.add_category),
                            tint = ElectricPrimaryBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tr(Res.string.add),
                            style = VaultTypography.labelSmall,
                            color = ElectricPrimaryBright,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
