package com.example.presentation.settings

import com.example.core.localization.*

import com.example.core.localization.tr
import passilius.composeapp.generated.resources.*

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.core.crypto.VaultKeyStore
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SecurityDanger
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.designsystem.neuFlat
import com.example.core.util.rememberScreenContentPadding

@Composable
fun ChangeMasterPasswordScreen(
    isChanging: Boolean,
    errorMessage: String?,
    succeeded: Boolean,
    onBack: () -> Unit,
    onSubmit: (current: String, newPassword: String, confirm: String) -> Unit,
    onFinished: () -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    var current by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    val contentPadding = rememberScreenContentPadding(bottom = 60.dp)

    LaunchedEffect(succeeded) {
        if (succeeded) onFinished()
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
                            contentDescription = tr(Res.string.back),
                            tint = OnSurfacePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = tr(Res.string.change_master_password),
                        style = VaultTypography.headlineLarge,
                        color = OnSurfacePrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Text(
                    text = tr(Res.string.password_warning, VaultKeyStore.MIN_PASSWORD_LENGTH),
                    style = VaultTypography.bodySmall,
                    color = SecondarySlate
                )
            }

            item {
                PasswordField(
                    value = current,
                    onValueChange = {
                        current = it
                        if (!errorMessage.isNullOrBlank()) onClearError()
                    },
                    hint = tr(Res.string.current_password)
                )
            }

            item {
                PasswordField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        if (!errorMessage.isNullOrBlank()) onClearError()
                    },
                    hint = tr(Res.string.new_password)
                )
            }

            item {
                PasswordField(
                    value = confirm,
                    onValueChange = {
                        confirm = it
                        if (!errorMessage.isNullOrBlank()) onClearError()
                    },
                    hint = tr(Res.string.confirm_password)
                )
            }

            if (!errorMessage.isNullOrBlank()) {
                item {
                    Text(
                        text = errorMessage,
                        style = VaultTypography.bodySmall,
                        color = SecurityDanger
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("change_master_password_btn")
                        .neuFlat(
                            shape = RoundedCornerShape(18.dp),
                            cornerRadius = 18.dp,
                            backgroundColor = SurfaceContainerLowest
                        )
                        .clickable(enabled = !isChanging) {
                            onSubmit(current, newPassword, confirm)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isChanging) tr(Res.string.working) else tr(Res.string.change_password),
                        style = VaultTypography.bodyMedium,
                        color = ElectricPrimaryBright,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        Text(text = hint, style = VaultTypography.labelSmall, color = SecondarySlate)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .neuFlat(
                    shape = RoundedCornerShape(18.dp),
                    cornerRadius = 18.dp,
                    backgroundColor = SurfaceContainerLowest
                )
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                textStyle = VaultTypography.bodyMedium.copy(color = OnSurfacePrimary, textDirection = androidx.compose.ui.text.style.TextDirection.ContentOrLtr),
                cursorBrush = SolidColor(ElectricPrimaryBright),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 6.dp)
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { visible = !visible },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) tr(Res.string.hide_field, hint) else tr(Res.string.show_field, hint),
                    tint = SecondarySlate,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
