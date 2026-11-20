package com.example.presentation.onboarding

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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.core.crypto.VaultKeyStore
import com.example.core.designsystem.ElectricPrimaryBright
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.OutlineVariant
import com.example.core.designsystem.SecondarySlate
import com.example.core.designsystem.SurfaceCanvas
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.example.core.designsystem.neuFlat
import com.example.core.designsystem.neuPressed
import kotlinx.coroutines.launch

private const val PageCount = 2

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { PageCount })
    val scope = rememberCoroutineScope()

    BackHandler(enabled = pagerState.currentPage > 0) {
        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (pagerState.currentPage == 0) {
            OnboardingLanguageSelector()
            Spacer(Modifier.height(8.dp))
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> IntroPage()
                else -> MasterKeyPage()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PageDots(currentPage = pagerState.currentPage)
        Spacer(modifier = Modifier.height(20.dp))
        OnboardingButton(
            label = if (pagerState.currentPage == 0) tr(Res.string.`continue`) else tr(Res.string.create_master_password),
            onClick = {
                if (pagerState.currentPage == 0) {
                    scope.launch { pagerState.animateScrollToPage(1) }
                } else {
                    onFinished()
                }
            }
        )
    }
}

@Composable
private fun OnboardingLanguageSelector() {
    var showLanguageDialog by remember { mutableStateOf(false) }
    fun languageLabel(language: AppLanguage): String = tr(when (language) {
        AppLanguage.SYSTEM -> Res.string.system_default
        AppLanguage.ENGLISH -> Res.string.english
        AppLanguage.PERSIAN -> Res.string.persian
    })
    if (showLanguageDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(tr(Res.string.language)) },
            text = {
                Column {
                    AppLanguage.entries.forEach { language ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                LanguageController.select(language)
                                showLanguageDialog = false
                            }.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.RadioButton(
                                selected = LanguageController.selection == language,
                                onClick = null
                            )
                            Text(languageLabel(language))
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showLanguageDialog = false }) {
                    Text(tr(Res.string.cancel))
                }
            }
        )
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        androidx.compose.material3.TextButton(
            onClick = { showLanguageDialog = true },
            modifier = Modifier.testTag("onboarding_language")
        ) {
            Icon(Icons.Filled.Language, contentDescription = null,
                modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(languageLabel(LanguageController.selection))
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Filled.ArrowDropDown, contentDescription = tr(Res.string.language))
        }
    }
}

@Composable
private fun IntroPage() {
    PageBody {
        HeroIcon(icon = Icons.Filled.Lock, contentDescription = "Passilius")
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Passilius",
            style = VaultTypography.headlineLarge,
            color = OnSurfacePrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = tr(Res.string.passwords_stay_on_this_device_no_account_no_cloud),
            style = VaultTypography.bodySmall,
            color = SecondarySlate,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        FeatureCard(
            features = listOf(
                Feature(
                    icon = Icons.Filled.Shield,
                    title = tr(Res.string.encrypted_here),
                    body = tr(Res.string.credentials_are_encrypted_on_this_device_before_they_are_saved)
                ),
                Feature(
                    icon = Icons.Filled.ContentCopy,
                    title = tr(Res.string.search_and_copy),
                    body = tr(Res.string.find_a_login_then_copy_the_username_or_password)
                ),
                Feature(
                    icon = Icons.Filled.QrCode,
                    title = tr(Res.string.your_other_device),
                    body = tr(Res.string.pair_with_a_qr_code_and_move_the_vault_over_your_local_network)
                )
            )
        )
    }
}

@Composable
private fun MasterKeyPage() {
    PageBody {
        HeroIcon(icon = Icons.Filled.Key, contentDescription = tr(Res.string.master_password))
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = tr(Res.string.the_only_key),
            style = VaultTypography.headlineLarge,
            color = OnSurfacePrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = tr(Res.string.the_master_password_opens_this_vault_if_you_forget_it_the_vault_cannot_be_opened),
            style = VaultTypography.bodySmall,
            color = SecondarySlate,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        FeatureCard(
            features = listOf(
                Feature(
                    icon = Icons.Filled.Lock,
                    title = tr(Res.string.no_reset),
                    body = tr(Res.string.there_is_no_account_and_no_way_to_recover_a_forgotten_password)
                ),
                Feature(
                    icon = Icons.Filled.Key,
                    title = tr(Res.string.minimum_length, VaultKeyStore.MIN_PASSWORD_LENGTH),
                    body = tr(Res.string.you_will_choose_it_on_the_next_screen_write_it_down_first)
                )
            )
        )
    }
}

@Composable
private fun PageBody(content: @Composable () -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val viewportHeight = maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = viewportHeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                content()
            }
        }
    }
}

private data class Feature(
    val icon: ImageVector,
    val title: String,
    val body: String
)

@Composable
private fun FeatureCard(features: List<Feature>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neuFlat(
                shape = RoundedCornerShape(24.dp),
                cornerRadius = 24.dp,
                backgroundColor = SurfaceContainerLowest
            )
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        features.forEach { feature ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = ElectricPrimaryBright,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = feature.title,
                        style = VaultTypography.bodyMedium,
                        color = OnSurfacePrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = feature.body,
                        style = VaultTypography.bodySmall,
                        color = SecondarySlate
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroIcon(
    icon: ImageVector,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .neuPressed(
                shape = RoundedCornerShape(24.dp),
                cornerRadius = 24.dp,
                backgroundColor = SurfaceContainerLowest
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = ElectricPrimaryBright,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun PageDots(currentPage: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(PageCount) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (index == currentPage) ElectricPrimaryBright else OutlineVariant)
            )
        }
    }
}

@Composable
private fun OnboardingButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .testTag("onboarding_continue")
            .fillMaxWidth()
            .height(48.dp)
            .neuFlat(
                shape = RoundedCornerShape(18.dp),
                cornerRadius = 18.dp,
                backgroundColor = SurfaceContainerLowest
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = VaultTypography.bodyMedium,
            color = ElectricPrimaryBright,
            fontWeight = FontWeight.SemiBold
        )
    }
}
