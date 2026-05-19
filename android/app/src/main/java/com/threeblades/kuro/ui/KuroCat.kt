package com.threeblades.kuro.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale

private val Fur = Color(0xFF0D0D12)
private val InnerEar = Color(0xFF2A2230)
private val Muzzle = Color(0xFF1A1A22)
private val EyeWhite = Color(0xFFE8E4D8)
private val Pupil = Color(0xFF0A0A0D)
private val Shine = Color(0xE6FFFFFF)
private val Whisker = Color(0xFF4A4450)
private val Nose = Color(0xFFCAA39A)
private val MouthLine = Color(0xFF3A3340)
private val BrowTuft = Color(0xFFC8C5B8)

@Composable
fun KuroCat(
    modifier: Modifier = Modifier,
    speaking: Boolean = false,
) {
    val transition = rememberInfiniteTransition(label = "kuro")

    val breathe by transition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.018f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "breathe",
    )

    val tailRot by transition.animateFloat(
        initialValue = 0f,
        targetValue = -9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "tail",
    )

    val blink by transition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6400
                0f at 0
                0f at 6000
                1f at 6200
                0f at 6400
            },
        ),
        label = "blink",
    )

    val pupilDart by transition.animateFloat(
        initialValue = if (speaking) -1.6f else 0f,
        targetValue = if (speaking) 2.4f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pupilDart",
    )

    Canvas(modifier = modifier) {
        val side = minOf(size.width, size.height)
        val scaleFactor = side / 256f
        scale(scaleFactor, scaleFactor, pivot = Offset(0f, 0f)) {
            drawKuro(breathe, tailRot, blink, pupilDart)
        }
    }
}

private fun DrawScope.drawKuro(
    breathe: Float,
    tailRot: Float,
    blink: Float,
    pupilDart: Float,
) {
    rotate(tailRot, pivot = Offset(188f, 196f)) {
        drawPath(
            path = Path().apply {
                moveTo(186f, 198f)
                cubicTo(224f, 196f, 240f, 158f, 224f, 128f)
                cubicTo(216f, 150f, 200f, 168f, 182f, 174f)
                close()
            },
            color = Fur,
        )
    }

    scale(1f, breathe, pivot = Offset(128f, 200f)) {
        drawOval(
            color = Fur,
            topLeft = Offset(62f, 168f),
            size = Size(132f, 80f),
        )

        drawPath(
            path = Path().apply {
                moveTo(70f, 196f)
                cubicTo(60f, 120f, 92f, 78f, 128f, 78f)
                cubicTo(164f, 78f, 196f, 120f, 186f, 196f)
                close()
            },
            color = Fur,
        )

        drawPath(
            path = Path().apply {
                moveTo(84f, 96f)
                cubicTo(70f, 60f, 74f, 44f, 80f, 42f)
                cubicTo(96f, 52f, 108f, 74f, 112f, 92f)
                close()
            },
            color = Fur,
        )
        drawPath(
            path = Path().apply {
                moveTo(88f, 84f)
                cubicTo(82f, 66f, 84f, 56f, 88f, 54f)
                cubicTo(96f, 60f, 102f, 72f, 104f, 84f)
                close()
            },
            color = InnerEar,
        )

        drawPath(
            path = Path().apply {
                moveTo(172f, 96f)
                cubicTo(186f, 60f, 182f, 44f, 176f, 42f)
                cubicTo(160f, 52f, 148f, 74f, 144f, 92f)
                close()
            },
            color = Fur,
        )
        drawPath(
            path = Path().apply {
                moveTo(168f, 84f)
                cubicTo(174f, 66f, 172f, 56f, 168f, 54f)
                cubicTo(160f, 60f, 154f, 72f, 152f, 84f)
                close()
            },
            color = InnerEar,
        )

        drawOval(
            color = Muzzle,
            topLeft = Offset(106f, 156f),
            size = Size(44f, 22f),
        )

        drawEye(Offset(108f, 142f), pupilDart, blink)
        drawEye(Offset(148f, 142f), pupilDart, blink)

        rotate(-14f, pivot = Offset(108f, 122f)) {
            drawOval(
                color = BrowTuft,
                topLeft = Offset(92f, 119f),
                size = Size(28f, 6f),
            )
        }
        rotate(14f, pivot = Offset(148f, 122f)) {
            drawOval(
                color = BrowTuft,
                topLeft = Offset(136f, 119f),
                size = Size(28f, 6f),
            )
        }

        drawPath(
            path = Path().apply {
                moveTo(124f, 162f)
                lineTo(132f, 162f)
                lineTo(128f, 168f)
                close()
            },
            color = Nose,
        )

        drawPath(
            path = Path().apply {
                moveTo(128f, 168f)
                quadraticBezierTo(124f, 176f, 116f, 174f)
            },
            color = MouthLine,
            style = Stroke(width = 2f),
        )
        drawPath(
            path = Path().apply {
                moveTo(128f, 168f)
                quadraticBezierTo(132f, 176f, 140f, 174f)
            },
            color = MouthLine,
            style = Stroke(width = 2f),
        )

        drawLine(Whisker, Offset(96f, 160f), Offset(58f, 152f), strokeWidth = 1.6f)
        drawLine(Whisker, Offset(96f, 168f), Offset(56f, 168f), strokeWidth = 1.6f)
        drawLine(Whisker, Offset(96f, 176f), Offset(60f, 186f), strokeWidth = 1.6f)
        drawLine(Whisker, Offset(160f, 160f), Offset(198f, 152f), strokeWidth = 1.6f)
        drawLine(Whisker, Offset(160f, 168f), Offset(200f, 168f), strokeWidth = 1.6f)
        drawLine(Whisker, Offset(160f, 176f), Offset(196f, 186f), strokeWidth = 1.6f)
    }
}

private fun DrawScope.drawEye(center: Offset, pupilDart: Float, blink: Float) {
    val eyeRx = 16f
    val eyeRy = 13f

    drawOval(
        color = EyeWhite,
        topLeft = Offset(center.x - eyeRx, center.y - eyeRy),
        size = Size(eyeRx * 2, eyeRy * 2),
    )

    drawOval(
        color = Pupil,
        topLeft = Offset(center.x - 5.5f + pupilDart, center.y - eyeRy - 1f),
        size = Size(11f, eyeRy * 2 + 2f),
    )

    drawCircle(
        color = Shine,
        radius = 2.8f,
        center = Offset(center.x - 4f, center.y - 6f),
    )

    drawPath(
        path = Path().apply {
            moveTo(center.x - eyeRx, center.y - eyeRy + 1f)
            cubicTo(
                center.x - eyeRx / 2f, center.y - eyeRy + 7f,
                center.x + eyeRx / 2f, center.y - eyeRy + 7f,
                center.x + eyeRx, center.y - eyeRy + 1f,
            )
            lineTo(center.x + eyeRx, center.y - eyeRy)
            lineTo(center.x - eyeRx, center.y - eyeRy)
            close()
        },
        color = Fur,
    )

    if (blink > 0f) {
        drawOval(
            color = Fur,
            topLeft = Offset(center.x - eyeRx - 1f, center.y - eyeRy - 1f),
            size = Size((eyeRx + 1f) * 2f, (eyeRy + 1f) * 2f * blink),
        )
    }
}
