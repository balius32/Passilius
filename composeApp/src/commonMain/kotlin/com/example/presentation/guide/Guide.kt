package com.example.presentation.guide

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.foundation.focusGroup
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.LocalLayoutDirection
import com.example.core.localization.tr
import org.jetbrains.compose.resources.StringResource
import passilius.composeapp.generated.resources.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester

internal const val GuideVersion = 1

internal enum class GuidePage { Vault, Form, Generator, Settings, Categories }

internal data class GuideStep(val id: String, val page: GuidePage, val message: StringResource)

private val add = GuideStep("add", GuidePage.Vault, Res.string.guide_add)
private val form_category = GuideStep("form_category", GuidePage.Form, Res.string.guide_form_category)
private val service = GuideStep("service", GuidePage.Form, Res.string.guide_service)
private val password = GuideStep("password", GuidePage.Form, Res.string.guide_password)
private val save = GuideStep("save", GuidePage.Form, Res.string.guide_save)
private val search = GuideStep("search", GuidePage.Vault, Res.string.guide_search)
private val filter = GuideStep("filter", GuidePage.Vault, Res.string.guide_filter)
private val card = GuideStep("card", GuidePage.Vault, Res.string.guide_card)
private val generator = GuideStep("generator", GuidePage.Vault, Res.string.guide_generator)
private val length = GuideStep("length", GuidePage.Generator, Res.string.guide_length)
private val characters = GuideStep("characters", GuidePage.Generator, Res.string.guide_characters)
private val regenerate = GuideStep("regenerate", GuidePage.Generator, Res.string.guide_regenerate)
private val copy = GuideStep("copy", GuidePage.Generator, Res.string.guide_copy)
private val categories = GuideStep("categories", GuidePage.Settings, Res.string.guide_categories)
private val category_add = GuideStep("category_add", GuidePage.Categories, Res.string.guide_category_add)
private val category_edit = GuideStep("category_edit", GuidePage.Categories, Res.string.guide_category_edit)
private val report = GuideStep("report", GuidePage.Settings, Res.string.guide_report)
private val lock = GuideStep("lock", GuidePage.Settings, Res.string.guide_lock)
private val master = GuideStep("master", GuidePage.Settings, Res.string.guide_master)
private val clipboard = GuideStep("clipboard", GuidePage.Settings, Res.string.guide_clipboard)
private val biometric = GuideStep("biometric", GuidePage.Settings, Res.string.guide_biometric)
private val sync = GuideStep("sync", GuidePage.Settings, Res.string.guide_sync)

internal enum class GuideTopic(val title: StringResource, val steps: List<GuideStep>) {
    Quick(Res.string.guide_quick, listOf(add, form_category, service, password, save, search, filter, card, generator, length, characters, regenerate, copy)),
    Categories(Res.string.guide_category_title, listOf(categories, category_add, category_edit)),
    Security(Res.string.guide_security, listOf(report, lock, master, clipboard, biometric)),
    Sync(Res.string.guide_sync_title, listOf(sync))
}

internal data class GuideHighlight(val bounds: Rect, val visibleBounds: Rect, val shape: Shape)

internal class GuideTarget(val step: GuideStep) {
    var highlight by mutableStateOf<GuideHighlight?>(null)
}

internal val LocalGuideTarget = staticCompositionLocalOf<GuideTarget?> { null }

@Composable
internal fun Modifier.guideTarget(id: String, shape: Shape = RectangleShape): Modifier {
    val target = LocalGuideTarget.current?.takeIf { it.step.id == id } ?: return this
    val requester = remember { BringIntoViewRequester() }
    LaunchedEffect(target) { requester.bringIntoView() }
    DisposableEffect(target) { onDispose { target.highlight = null } }
    return bringIntoViewRequester(requester).onPlaced { coordinates ->
        val visibleBounds = coordinates.boundsInWindow()
        target.highlight = if (visibleBounds.isEmpty) null else GuideHighlight(
            bounds = Rect(coordinates.positionInWindow(), coordinates.size.toSize()),
            visibleBounds = visibleBounds,
            shape = shape
        )
    }
}

@Composable
internal fun GuideScroll(state: LazyListState, index: Int?) {
    val target = LocalGuideTarget.current
    LaunchedEffect(target, state, index) {
        if (target != null && index != null) state.scrollToItem(index)
    }
}

@Suppress("DEPRECATION")
internal fun Modifier.guideBackground(active: Boolean): Modifier =
    if (active) clearAndSetSemantics { invisibleToUser() } else this

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuideOverlay(
    target: GuideTarget,
    index: Int,
    count: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onExit: () -> Unit
) {
    var origin by remember { mutableStateOf<Offset?>(null) }
    val focus = remember { FocusRequester() }
    LaunchedEffect(target) { focus.requestFocus() }
    BoxWithConstraints(
        Modifier.fillMaxSize()
            .onGloballyPositioned { origin = it.positionInWindow() }
            .onPreviewKeyEvent {
                if (it.key == Key.Escape && it.type == KeyEventType.KeyUp) {
                    onExit()
                    true
                } else false
            }
            .focusProperties { this.onExit = { cancelFocusChange() } }
            .focusGroup()
            .semantics { isTraversalGroup = true }
    ) {
        val windowOrigin = origin
        val highlight = target.highlight
        val density = LocalDensity.current
        val highlightPadding = with(density) { 5.dp.toPx() }
        val viewport = Rect(0f, 0f, constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())
        val visible = if (windowOrigin != null && highlight != null) {
            highlight.visibleBounds.translate(-windowOrigin).intersect(viewport)
                .takeUnless { it.isEmpty }
                ?.inflate(highlightPadding)
                ?.intersect(viewport)
        } else null
        val highlightColor = MaterialTheme.colorScheme.primary
        Canvas(Modifier.matchParentSize()) {
            val mask = Color.Black.copy(alpha = 0.72f)
            if (visible == null || highlight == null || windowOrigin == null) {
                drawRect(mask)
            } else {
                val bounds = highlight.bounds.translate(-windowOrigin).inflate(highlightPadding)
                val outline = highlight.shape.createOutline(bounds.size, layoutDirection, this)
                val hole = Path().apply {
                    addOutline(outline)
                    translate(bounds.topLeft)
                }
                val visibleHole = Path().apply {
                    op(hole, Path().apply { addRect(visible) }, androidx.compose.ui.graphics.PathOperation.Intersect)
                }
                clipPath(visibleHole, ClipOp.Difference) { drawRect(mask) }
                clipRect(visible.left, visible.top, visible.right, visible.bottom) {
                    drawPath(hole, highlightColor.copy(alpha = 0.18f))
                    drawPath(hole, highlightColor, style = androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx()))
                }
            }
        }
        // Intercept taps on the dimmed screen without consuming the guide card's button events.
        Box(
            Modifier.matchParentSize().clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
        )
        val onTop = visible != null && visible.center.y > constraints.maxHeight / 2f
        val available = with(density) {
            if (visible == null) maxHeight
            else (if (onTop) visible.top else constraints.maxHeight - visible.bottom).toDp()
        }
        val cardHeight = if (available >= 180.dp) available else maxHeight
        Surface(
            modifier = Modifier.align(if (onTop) Alignment.TopCenter else Alignment.BottomCenter)
                .safeDrawingPadding()
                .padding(12.dp)
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .heightIn(max = cardHeight),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp
        ) {
            Column(
                Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(tr(Res.string.guide_progress, index + 1, count), style = MaterialTheme.typography.labelLarge)
                Text(tr(target.step.message), Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onNext, modifier = Modifier.focusRequester(focus)) {
                        Text(tr(if (index == count - 1) Res.string.guide_finish else Res.string.guide_next))
                    }
                    if (index > 0) TextButton(onClick = onPrevious) { Text(tr(Res.string.guide_previous)) }
                    TextButton(onClick = onExit) { Text(tr(Res.string.guide_skip)) }
                }
            }
        }
    }
}

@Composable
internal fun GuideMenu(onClose: () -> Unit, onSelect: (GuideTopic) -> Unit) {
    val direction = LocalLayoutDirection.current
    Dialog(onDismissRequest = onClose, properties = DialogProperties()) {
        CompositionLocalProvider(LocalLayoutDirection provides direction) {
            Surface(shape = MaterialTheme.shapes.large) {
                Column(Modifier.widthIn(max = 380.dp).verticalScroll(rememberScrollState()).padding(20.dp)) {
                    Text(tr(Res.string.guide_menu), style = MaterialTheme.typography.titleLarge)
                    GuideTopic.entries.forEach { topic ->
                        TextButton(onClick = { onSelect(topic) }, modifier = Modifier.fillMaxWidth()) {
                            Text(tr(topic.title))
                        }
                    }
                    TextButton(onClick = onClose) { Text(tr(Res.string.guide_close)) }
                }
            }
        }
    }
}
