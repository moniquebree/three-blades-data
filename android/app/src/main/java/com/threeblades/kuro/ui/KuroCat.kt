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
private val FurEdge = Color(0xFF1F1F2A)
private val InnerEar = Color(0xFF2A2230)
private val Muzzle = Color(0xFF2A2632)
private val EyeWhite = Color(0xFFF1ECDC)
private val Pupil = Color(0xFF0A0A0D)
private val Shine = Color(0xE6FFFFFF)
private val Whisker = Color(0xFF7A7480)
private val Nose = Color(0xFFCAA39A)
private val MouthLine = Color(0xFF3A3340)
private val BrowTuft = Color(0xFFD8D5C8)

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
        initialValue = if (speaking) -1.4f else 0f,
        targetValue = if (speaking) 2.0f else 0f,
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

        // Scruffy fur tufts between the ears (the "old man bedhead" look)
        drawPath(
            path = Path().apply {
                moveTo(112f, 80f)
                lineTo(118f, 64f)
                lineTo(124f, 80f)
                close()
            },
            color = Fur,
        )
        drawPath(
            path = Path().apply {
                moveTo(126f, 78f)
                lineTo(132f, 66f)
                lineTo(138f, 78f)
                close()
            },
            color = Fur,
        )
        drawPath(
            path = Path().apply {
                moveTo(140f, 80f)
                lineTo(146f, 70f)
                lineTo(150f, 80f)
                close()
            },
            color = Fur,
        )

        // Scruffy cheek fur tufts (asymmetric — one sticks out more)
        drawPath(
            path = Path().apply {
                moveTo(72f, 152f)
                lineTo(60f, 144f)
                lineTo(74f, 158f)
                close()
            },
            color = FurEdge,
        )
        drawPath(
            path = Path().apply {
                moveTo(184f, 156f)
                lineTo(198f, 158f)
                lineTo(184f, 164f)
                close()
            },
            color = FurEdge,
        )

        // Subtle aged grey muzzle patch
        drawOval(
            color = Muzzle,
            topLeft = Offset(102f, 154f),
            size = Size(52f, 26f),
        )

        drawEye(Offset(108f, 142f), pupilDart, blink)
        drawEye(Offset(148f, 142f), pupilDart, blink)

        // ANGRY V brows: inner edges down (toward nose), outer edges up.
        // Left brow tilts clockwise (+); right brow tilts counter-clockwise (-).
        // Right brow sits slightly higher than left = "raised eyebrow" judgmental asymmetry.
        rotate(18f, pivot = Offset(108f, 120f)) {
            drawPath(
                path = Path().apply {
                    moveTo(90f, 118f)
                    lineTo(124f, 121f)
                    lineTo(124f, 124f)
                    lineTo(92f, 122f)
                    close()
                },
                color = BrowTuft,
            )
        }
        rotate(-18f, pivot = Offset(148f, 116f)) {
            drawPath(
                path = Path().apply {
                    moveTo(132f, 117f)
                    lineTo(166f, 114f)
                    lineTo(166f, 117f)
                    lineTo(132f, 120f)
                    close()
                },
                color = BrowTuft,
            )
        }

        drawPath(
            path = Path().apply {
                moveTo(124f, 164f)
                lineTo(132f, 164f)
                lineTo(128f, 170f)
                close()
            },
            color = Nose,
        )

        // Tight grumpy mouth — short downturned dash, not a sad curve
        drawLine(
            color = MouthLine,
            start = Offset(128f, 170f),
            end = Offset(122f, 174f),
            strokeWidth = 2.2f,
        )
        drawLine(
            color = MouthLine,
            start = Offset(128f, 170f),
            end = Offset(134f, 174f),
            strokeWidth = 2.2f,
        )

        // Whiskers — asymmetric, a bit messy
        drawLine(Whisker, Offset(96f, 158f), Offset(58f, 148f), strokeWidth = 1.6f)
        drawLine(Whisker, Offset(96f, 166f), Offset(54f, 168f), strokeWidth = 1.6f)
        drawLine(Whisker, Offset(96f, 174f), Offset(62f, 184f), strokeWidth = 1.4f)
        drawLine(Whisker, Offset(160f, 158f), Offset(202f, 152f), strokeWidth = 1.6f)
        drawLine(Whisker, Offset(160f, 166f), Offset(204f, 168f), strokeWidth = 1.4f)
        drawLine(Whisker, Offset(160f, 174f), Offset(196f, 188f), strokeWidth = 1.6f)
    }
}

private fun DrawScope.drawEye(center: Offset, pupilDart: Float, blink: Float) {
    val eyeRx = 15f
    val eyeRy = 11f

    drawOval(
        color = EyeWhite,
        topLeft = Offset(center.x - eyeRx, center.y - eyeRy),
        size = Size(eyeRx * 2, eyeRy * 2),
    )

    drawOval(
        color = Pupil,
        topLeft = Offset(center.x - 4.5f + pupilDart, center.y - 8.5f),
        size = Size(9f, 17f),
    )

    drawCircle(
        color = Shine,
        radius = 2.6f,
        center = Offset(center.x - 3.5f, center.y - 4.5f),
    )

    if (blink > 0f) {
        drawOval(
            color = Fur,
            topLeft = Offset(center.x - eyeRx - 1f, center.y - eyeRy - 1f),
            size = Size((eyeRx + 1f) * 2f, (eyeRy + 1f) * 2f * blink),
        )
    }
}
