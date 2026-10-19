package com.example.presentation.onboarding

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
            label = if (pagerState.currentPage == 0) "Continue" else "Create master password",
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
            text = "Passwords stay on this device. No account. No cloud.",
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
                    title = "Encrypted here",
                    body = "Credentials are encrypted on this device before they are saved."
                ),
                Feature(
                    icon = Icons.Filled.ContentCopy,
                    title = "Search and copy",
                    body = "Find a login, then copy the username or password."
                ),
                Feature(
                    icon = Icons.Filled.QrCode,
                    title = "Your other device",
                    body = "Pair with a QR code and move the vault over your local network."
                )
            )
        )
    }
}

@Composable
private fun MasterKeyPage() {
    PageBody {
        HeroIcon(icon = Icons.Filled.Key, contentDescription = "Master password")
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "The only key",
            style = VaultTypography.headlineLarge,
            color = OnSurfacePrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The master password opens this vault. If you forget it, the vault cannot be opened.",
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
                    title = "No reset",
                    body = "There is no account and no way to recover a forgotten password."
                ),
                Feature(
                    icon = Icons.Filled.Key,
                    title = "At least ${VaultKeyStore.MIN_PASSWORD_LENGTH} characters",
                    body = "You will choose it on the next screen. Write it down first."
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
