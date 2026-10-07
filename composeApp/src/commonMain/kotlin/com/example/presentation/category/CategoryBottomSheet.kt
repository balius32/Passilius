package com.example.presentation.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnPrimary
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.OutlineColor
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.VaultTypography
import kotlinx.coroutines.launch

private val SheetHorizontalPadding = 24.dp
private val SheetFieldShape = RoundedCornerShape(16.dp)
private val SheetFieldBackground = Color(0xFFEFF2F7)
private val SheetFieldBorder = Color(0xFFD8DEE8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBottomSheet(
    initialName: String? = null,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    val isEdit = !initialName.isNullOrBlank()
    var name by remember { mutableStateOf(initialName.orEmpty()) }
    var isSaving by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun saveAndDismiss(value: String) {
        scope.launch {
            try {
                if (sheetState.isVisible) {
                    sheetState.hide()
                }
            } finally {
                onSave(value)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceCanvas,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 6.dp)
                    .size(width = 48.dp, height = 5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFCBD5E1))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .navigationBarsPadding()
                .imePadding()
        ) {
            Text(
                text = if (isEdit) "Rename Category" else "New Category",
                style = VaultTypography.headlineMedium,
                color = OnSurfacePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SheetHorizontalPadding)
                    .padding(bottom = 22.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SheetHorizontalPadding)
                    .height(52.dp)
                    .clip(SheetFieldShape)
                    .background(SheetFieldBackground, SheetFieldShape)
                    .border(1.dp, SheetFieldBorder, SheetFieldShape)
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("category_name_input"),
                    textStyle = VaultTypography.bodyMedium.copy(color = OnSurfacePrimary),
                    cursorBrush = SolidColor(ElectricPrimaryBright),
                    singleLine = true,
                    decorationBox = { inner ->
                        if (name.isEmpty()) {
                            Text(
                                text = "Category name",
                                style = VaultTypography.bodyMedium,
                                color = OutlineColor
                            )
                        }
                        inner()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SheetHorizontalPadding)
                    .height(56.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(ElectricPrimaryBright, RoundedCornerShape(20.dp))
                    .clickable(enabled = name.isNotBlank() && !isSaving) {
                        isSaving = true
                        saveAndDismiss(name.trim())
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Save",
                    style = VaultTypography.headlineSmall,
                    color = OnPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
