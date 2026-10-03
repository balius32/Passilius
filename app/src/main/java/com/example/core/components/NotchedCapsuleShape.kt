package com.example.core.components

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Capsule with a smooth concave cradle cut into the top-center edge, sized to hug a circular FAB
 * whose center sits on the capsule's top edge.
 */
class NotchedCapsuleShape(
    private val fabRadius: Dp,
    private val notchGap: Dp
) : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val w = size.width
        val h = size.height
        val r = h / 2f
        val cx = w / 2f
        val notchR = with(density) { (fabRadius + notchGap).toPx() }
        val shoulder = notchR * 0.75f

        val path = Path().apply {
            moveTo(r, 0f)
            lineTo(cx - notchR - shoulder, 0f)
            cubicTo(
                cx - notchR, 0f,
                cx - notchR, notchR,
                cx, notchR
            )
            cubicTo(
                cx + notchR, notchR,
                cx + notchR, 0f,
                cx + notchR + shoulder, 0f
            )
            lineTo(w - r, 0f)
            arcTo(Rect(w - 2 * r, 0f, w, 2 * r), -90f, 180f, false)
            lineTo(r, h)
            arcTo(Rect(0f, 0f, 2 * r, 2 * r), 90f, 180f, false)
            close()
        }
        return Outline.Generic(path)
    }
}
