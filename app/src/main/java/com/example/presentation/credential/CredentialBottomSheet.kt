package com.example.presentation.credential

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.core.crypto.GeneratorConfig
import com.example.core.crypto.PasswordGenerator
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnPrimary
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.OutlineColor
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SecurityTertiaryBright
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.VaultTypography
import com.example.domain.model.Credential
import kotlinx.coroutines.launch

private val SheetHorizontalPadding = 24.dp
private val SheetFieldShape = RoundedCornerShape(16.dp)
private val SheetFieldBackground = Color(0xFFEFF2F7)
private val SheetFieldBorder = Color(0xFFD8DEE8)

private fun Modifier.sheetFieldWell(): Modifier = this
    .clip(SheetFieldShape)
    .background(SheetFieldBackground, SheetFieldShape)
    .border(1.dp, SheetFieldBorder, SheetFieldShape)

private fun Modifier.sheetIconButton(): Modifier = this
    .clip(CircleShape)
    .background(SurfaceCanvas, CircleShape)
    .border(1.dp, SheetFieldBorder, CircleShape)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CredentialBottomSheet(
    initialCredential: Credential?,
    categories: List<String>,
    onDismiss: () -> Unit,
    onSave: (Credential) -> Unit
) {
    var service by remember { mutableStateOf(initialCredential?.service ?: "") }
    var username by remember { mutableStateOf(initialCredential?.username ?: "") }
    var password by remember { mutableStateOf(initialCredential?.password ?: "") }
    var category by remember {
        mutableStateOf(
            initialCredential?.category
                ?: categories.firstOrNull()
                ?: "Personal"
        )
    }
    var websiteUrl by remember { mutableStateOf(initialCredential?.websiteUrl ?: "") }
    var notes by remember { mutableStateOf(initialCredential?.notes ?: "") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var entropyBits by remember { mutableIntStateOf(0) }

    LaunchedEffect(categories) {
        if (categories.isNotEmpty() && categories.none { it.equals(category, ignoreCase = true) }) {
            category = categories.first()
        }
    }

    LaunchedEffect(password) {
        entropyBits = PasswordGenerator.calculateEntropy(password)
    }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun saveAndDismiss(credential: Credential) {
        scope.launch {
            try {
                if (sheetState.isVisible) {
                    sheetState.hide()
                }
            } finally {
                onSave(credential)
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
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
        ) {
            Text(
                text = if (initialCredential == null) "New Credential" else "Edit Credential",
                style = VaultTypography.headlineMedium,
                color = OnSurfacePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SheetHorizontalPadding)
                    .padding(bottom = 22.dp)
            )

            Text(
                text = "Category",
                style = VaultTypography.labelMedium,
                color = SecondarySlate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SheetHorizontalPadding)
                    .padding(bottom = 10.dp)
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = SheetHorizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isCatSelected = cat.equals(category, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isCatSelected) ElectricPrimaryBright else SurfaceCanvas,
                                RoundedCornerShape(14.dp)
                            )
                            .then(
                                if (!isCatSelected) {
                                    Modifier.border(1.dp, SheetFieldBorder, RoundedCornerShape(14.dp))
                                } else {
                                    Modifier
                                }
                            )
                            .clickable { category = cat }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = cat,
                            style = VaultTypography.labelSmall,
                            color = if (isCatSelected) OnPrimary else SecondarySlate,
                            fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Field 1: Website or Service
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SheetHorizontalPadding)
                    .height(52.dp)
                    .sheetFieldWell()
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEBEEF3)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = SecondarySlate,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    BasicTextField(
                        value = service,
                        onValueChange = { service = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("service_input"),
                        textStyle = VaultTypography.bodyMedium.copy(color = OnSurfacePrimary),
                        cursorBrush = SolidColor(ElectricPrimaryBright),
                        singleLine = true,
                        decorationBox = { inner ->
                            if (service.isEmpty()) {
                                Text(
                                    text = "Website name",
                                    style = VaultTypography.bodyMedium,
                                    color = OutlineColor
                                )
                            }
                            inner()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Field 2: Username or Email
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SheetHorizontalPadding)
                    .height(52.dp)
                    .sheetFieldWell()
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEBEEF3)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AlternateEmail,
                            contentDescription = null,
                            tint = SecondarySlate,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    BasicTextField(
                        value = username,
                        onValueChange = { username = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("username_input"),
                        textStyle = VaultTypography.bodyMedium.copy(color = OnSurfacePrimary),
                        cursorBrush = SolidColor(ElectricPrimaryBright),
                        singleLine = true,
                        decorationBox = { inner ->
                            if (username.isEmpty()) {
                                Text(
                                    text = "Email or username",
                                    style = VaultTypography.bodyMedium,
                                    color = OutlineColor
                                )
                            }
                            inner()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Field 3: Password / Secret
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SheetHorizontalPadding)
                    .height(52.dp)
                    .sheetFieldWell()
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEBEEF3)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = SecondarySlate,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    BasicTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("password_input"),
                        textStyle = VaultTypography.labelMedium.copy(color = OnSurfacePrimary),
                        cursorBrush = SolidColor(ElectricPrimaryBright),
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        decorationBox = { inner ->
                            if (password.isEmpty()) {
                                Text(
                                    text = "Enter master password",
                                    style = VaultTypography.labelMedium,
                                    color = OutlineColor
                                )
                            }
                            inner()
                        }
                    )

                    // Eye visibility toggle
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .sheetIconButton()
                            .clickable { isPasswordVisible = !isPasswordVisible },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle Visibility",
                            tint = SecondarySlate,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Entropy Strength Track
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SheetHorizontalPadding)
                    .padding(top = 8.dp)
            ) {
                val fraction = (entropyBits / 128f).coerceIn(0.05f, 1f)
                val barColor = if (entropyBits >= 90) SecurityTertiaryBright else if (entropyBits >= 60) Color(0xFFF59E0B) else Color(0xFFBA1A1A)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFE2E8F0))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction)
                            .clip(RoundedCornerShape(3.dp))
                            .background(barColor)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${password.length} characters",
                        style = VaultTypography.labelSmall,
                        color = SecondarySlate
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFDBE1FF), RoundedCornerShape(12.dp))
                            .clickable {
                                val generated = PasswordGenerator.generate(GeneratorConfig(length = 20))
                                password = generated.password
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ElectricPrimaryBright,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Gen",
                                style = VaultTypography.labelSmall,
                                color = ElectricPrimaryBright,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Save
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SheetHorizontalPadding)
                    .height(56.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(ElectricPrimaryBright, RoundedCornerShape(20.dp))
                    .clickable(enabled = service.isNotBlank() && password.isNotBlank() && !isSaving) {
                        isSaving = true
                        val cred = Credential(
                            id = initialCredential?.id ?: 0L,
                            service = service.trim(),
                            username = username.trim(),
                            password = password,
                            category = category,
                            websiteUrl = websiteUrl.trim(),
                            notes = notes.trim(),
                            isFavorite = initialCredential?.isFavorite ?: false,
                            entropyBits = entropyBits
                        )
                        saveAndDismiss(cred)
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
