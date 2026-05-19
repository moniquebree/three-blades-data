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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale

private val Fur = Color(0xFF0D0D12)
private val FurHighlight = Color(0xFF1D1D28)
private val InnerEar = Color(0xFF3A2A35)
private val Muzzle = Color(0xFF1A1820)
private val EyeWhite = Color(0xFFEFE9D5)
private val EyeAmber = Color(0xFFD9A85A)
private val Pupil = Color(0xFF0A0A0D)
private val Shine = Color(0xE6FFFFFF)
private val Whisker = Color(0xFF8A8490)
private val Nose = Color(0xFFB89090)
private val MouthLine = Color(0xFF453D4A)
private val BrowLine = Color(0xFFBCB8AC)

@Composable
fun KuroCat(
    modifier: Modifier = Modifier,
    speaking: Boolean = false,
) {
    val transition = rememberInfiniteTransition(label = "kuro")

    val breathe by transition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.022f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "breathe",
    )

    val tailTipDrift by transition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "tail",
    )

    val blink by transition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 7000
                0f at 0
                0f at 6600
                1f at 6800
                0f at 7000
            },
        ),
        label = "blink",
    )

    val pupilDart by transition.animateFloat(
        initialValue = if (speaking) -1.2f else 0f,
        targetValue = if (speaking) 1.8f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pupilDart",
    )

    Canvas(modifier = modifier) {
        val side = minOf(size.width, size.height)
        val scaleFactor = side / 256f
        scale(scaleFactor, scaleFactor, pivot = Offset(0f, 0f)) {
            drawKuro(breathe, tailTipDrift, blink, pupilDart)
        }
    }
}

private fun DrawScope.drawKuro(
    breathe: Float,
    tailTipDrift: Float,
    blink: Float,
    pupilDart: Float,
) {
    // Tail — wraps from right side around the front-bottom of the feet,
    // tip curls back up on the left. Tip drifts subtly.
    drawPath(
        path = Path().apply {
            moveTo(178f, 220f)
            cubicTo(212f, 218f, 224f, 244f, 198f, 252f)
            cubicTo(170f, 258f, 124f, 256f, 96f + tailTipDrift, 246f)
            cubicTo(78f + tailTipDrift, 240f, 84f + tailTipDrift, 222f, 104f + tailTipDrift, 226f)
            cubicTo(132f, 234f, 158f, 232f, 178f, 224f)
            close()
        },
        color = Fur,
    )

    // Body breathes from a base pivot so he subtly inflates upward.
    scale(scaleX = 1.005f * breathe, scaleY = breathe, pivot = Offset(128f, 244f)) {
        // Slinky pear-shaped body — narrow at shoulders, wider at hips.
        drawPath(
            path = Path().apply {
                moveTo(108f, 132f)
                cubicTo(86f, 144f, 74f, 174f, 66f, 208f)
                cubicTo(58f, 240f, 70f, 250f, 96f, 250f)
                lineTo(160f, 250f)
                cubicTo(186f, 250f, 198f, 240f, 190f, 208f)
                cubicTo(182f, 174f, 170f, 144f, 148f, 132f)
                close()
            },
            color = Fur,
        )

        // Front paw hints — two small ovals where his feet meet the floor.
        drawOval(
            color = FurHighlight,
            topLeft = Offset(96f, 240f),
            size = Size(20f, 12f),
        )
        drawOval(
            color = FurHighlight,
            topLeft = Offset(140f, 240f),
            size = Size(20f, 12f),
        )

        // Head — smaller, more angular than before. Slight diamond/almond.
        drawPath(
            path = Path().apply {
                moveTo(128f, 48f)
                cubicTo(98f, 50f, 84f, 70f, 86f, 94f)
                cubicTo(88f, 116f, 102f, 134f, 128f, 138f)
                cubicTo(154f, 134f, 168f, 116f, 170f, 94f)
                cubicTo(172f, 70f, 158f, 50f, 128f, 48f)
                close()
            },
            color = Fur,
        )

        // Ears — tall, pointed, asymmetric tilt. Left ear is slightly more upright;
        // right ear leans outward, giving the head a knowing tilt.
        drawPath(
            path = Path().apply {
                moveTo(94f, 90f)
                lineTo(78f, 22f)
                lineTo(116f, 86f)
                close()
            },
            color = Fur,
        )
        drawPath(
            path = Path().apply {
                moveTo(96f, 84f)
                lineTo(86f, 40f)
                lineTo(108f, 82f)
                close()
            },
            color = InnerEar,
        )

        drawPath(
            path = Path().apply {
                moveTo(162f, 90f)
                lineTo(182f, 24f)
                lineTo(140f, 86f)
                close()
            },
            color = Fur,
        )
        drawPath(
            path = Path().apply {
                moveTo(160f, 84f)
                lineTo(174f, 42f)
                lineTo(148f, 82f)
                close()
            },
            color = InnerEar,
        )

        // Faint muzzle/chin patch — barely visible, just gives the face depth.
        drawOval(
            color = Muzzle,
            topLeft = Offset(108f, 110f),
            size = Size(40f, 22f),
        )

        // Eyes
        drawEye(Offset(113f, 98f), pupilDart, blink)
        drawEye(Offset(143f, 98f), pupilDart, blink)

        // Brows — single thin raised brow over the right eye only (the smug
        // "really?" look). Left eye has no brow; that asymmetry is the joke.
        rotate(-12f, pivot = Offset(143f, 80f)) {
            drawPath(
                path = Path().apply {
                    moveTo(130f, 80f)
                    cubicTo(138f, 76f, 152f, 76f, 158f, 80f)
                    lineTo(158f, 82.5f)
                    cubicTo(152f, 78.5f, 138f, 78.5f, 130f, 82.5f)
                    close()
                },
                color = BrowLine,
            )
        }

        // Nose — small, neat
        drawPath(
            path = Path().apply {
                moveTo(124f, 114f)
                lineTo(132f, 114f)
                lineTo(128f, 119f)
                close()
            },
            color = Nose,
        )

        // Mouth — a closed smirk: short flat line below the nose, with one
        // corner ticked up slightly. Reads as "mm-hm" not "boo-hoo".
        drawLine(
            color = MouthLine,
            start = Offset(128f, 121f),
            end = Offset(121f, 124f),
            strokeWidth = 2.0f,
        )
        drawLine(
            color = MouthLine,
            start = Offset(128f, 121f),
            end = Offset(135f, 123f),
            strokeWidth = 2.0f,
        )

        // Whiskers — thin, elegant, asymmetric. Salem-cat whiskers.
        drawLine(Whisker, Offset(106f, 116f), Offset(72f, 108f), strokeWidth = 1.2f)
        drawLine(Whisker, Offset(106f, 122f), Offset(70f, 124f), strokeWidth = 1.2f)
        drawLine(Whisker, Offset(106f, 128f), Offset(76f, 136f), strokeWidth = 1.0f)
        drawLine(Whisker, Offset(150f, 116f), Offset(184f, 110f), strokeWidth = 1.2f)
        drawLine(Whisker, Offset(150f, 122f), Offset(186f, 124f), strokeWidth = 1.2f)
        drawLine(Whisker, Offset(150f, 128f), Offset(180f, 136f), strokeWidth = 1.0f)
    }
}

private fun DrawScope.drawEye(center: Offset, pupilDart: Float, blink: Float) {
    val eyeRx = 12f
    val eyeRy = 9f

    // Almond outer eye (amber tint at the rim — Salem cat eyes)
    drawOval(
        color = EyeAmber,
        topLeft = Offset(center.x - eyeRx, center.y - eyeRy),
        size = Size(eyeRx * 2, eyeRy * 2),
    )

    // Inner eye white (smaller — leaves an amber rim)
    drawOval(
        color = EyeWhite,
        topLeft = Offset(center.x - eyeRx + 1.6f, center.y - eyeRy + 1.4f),
        size = Size((eyeRx - 1.6f) * 2, (eyeRy - 1.4f) * 2),
    )

    // Narrow vertical slit pupil — the iconic cat stare
    drawOval(
        color = Pupil,
        topLeft = Offset(center.x - 2.6f + pupilDart, center.y - eyeRy - 0.5f),
        size = Size(5.2f, eyeRy * 2 + 1f),
    )

    drawCircle(
        color = Shine,
        radius = 2.0f,
        center = Offset(center.x - 3.5f, center.y - 4f),
    )

    if (blink > 0f) {
        drawOval(
            color = Fur,
            topLeft = Offset(center.x - eyeRx - 1f, center.y - eyeRy - 1f),
            size = Size((eyeRx + 1f) * 2f, (eyeRy + 1f) * 2f * blink),
        )
    }
}
