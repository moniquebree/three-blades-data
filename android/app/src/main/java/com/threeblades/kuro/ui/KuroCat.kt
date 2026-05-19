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
private val FurHighlight = Color(0xFF22222E)
private val FurDeep = Color(0xFF050507)
private val InnerEar = Color(0xFF4A2A38)
private val InnerEarBright = Color(0xFF6A3A48)
private val Muzzle = Color(0xFF1A1820)
private val ChestTuft = Color(0xFF1A1A26)
private val EyeWhite = Color(0xFFF1ECDC)
private val EyeAmberRim = Color(0xFFE0AF60)
private val EyeAmberCore = Color(0xFFF4C878)
private val Pupil = Color(0xFF0A0A0D)
private val Shine = Color(0xE6FFFFFF)
private val ShineSoft = Color(0x99FFFFFF)
private val Whisker = Color(0xFF8E8896)
private val WhiskerPad = Color(0xFF2F2A34)
private val Nose = Color(0xFFB89090)
private val NoseShadow = Color(0xFF6E5A5A)
private val MouthLine = Color(0xFF4A4250)
private val BrowLine = Color(0xFFB8B5A6)

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

    val earTwitch by transition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 9000
                0f at 0
                0f at 8400
                -2f at 8600
                0f at 8800
                0f at 9000
            },
        ),
        label = "earTwitch",
    )

    Canvas(modifier = modifier) {
        val side = minOf(size.width, size.height)
        val scaleFactor = side / 256f
        scale(scaleFactor, scaleFactor, pivot = Offset(0f, 0f)) {
            drawKuro(breathe, tailTipDrift, blink, pupilDart, earTwitch)
        }
    }
}

private fun DrawScope.drawKuro(
    breathe: Float,
    tailTipDrift: Float,
    blink: Float,
    pupilDart: Float,
    earTwitch: Float,
) {
    // Tail — wraps from right side around the front-bottom of the feet
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
    // Tail subtle highlight along the upper curve (light from above)
    drawPath(
        path = Path().apply {
            moveTo(180f, 222f)
            cubicTo(206f, 222f, 218f, 240f, 210f, 248f)
        },
        color = FurHighlight,
        style = Stroke(width = 1.6f),
    )

    scale(scaleX = 1.005f * breathe, scaleY = breathe, pivot = Offset(128f, 244f)) {
        // Body — slinky pear silhouette
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

        // Body upper-right edge highlight (light direction: upper-right)
        drawPath(
            path = Path().apply {
                moveTo(150f, 134f)
                cubicTo(172f, 146f, 184f, 176f, 192f, 208f)
            },
            color = FurHighlight,
            style = Stroke(width = 2f),
        )
        // Body lower-left deeper shadow
        drawPath(
            path = Path().apply {
                moveTo(72f, 200f)
                cubicTo(64f, 232f, 76f, 248f, 96f, 250f)
            },
            color = FurDeep,
            style = Stroke(width = 2.4f),
        )

        // Chest tuft — slightly lighter inner shape suggesting fluffier chest fur
        drawPath(
            path = Path().apply {
                moveTo(116f, 142f)
                cubicTo(110f, 168f, 112f, 200f, 122f, 220f)
                cubicTo(132f, 220f, 144f, 200f, 142f, 168f)
                cubicTo(140f, 150f, 134f, 142f, 128f, 142f)
                close()
            },
            color = ChestTuft,
        )

        // Front paws with toe separations
        drawOval(
            color = FurHighlight,
            topLeft = Offset(94f, 240f),
            size = Size(22f, 12f),
        )
        drawLine(FurDeep, Offset(101f, 242f), Offset(101f, 252f), strokeWidth = 1.2f)
        drawLine(FurDeep, Offset(108f, 242f), Offset(108f, 252f), strokeWidth = 1.2f)

        drawOval(
            color = FurHighlight,
            topLeft = Offset(140f, 240f),
            size = Size(22f, 12f),
        )
        drawLine(FurDeep, Offset(148f, 242f), Offset(148f, 252f), strokeWidth = 1.2f)
        drawLine(FurDeep, Offset(155f, 242f), Offset(155f, 252f), strokeWidth = 1.2f)

        // Head
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

        // Head upper-right highlight
        drawPath(
            path = Path().apply {
                moveTo(128f, 50f)
                cubicTo(152f, 52f, 166f, 70f, 168f, 92f)
            },
            color = FurHighlight,
            style = Stroke(width = 1.8f),
        )

        // Ears
        rotate(earTwitch, pivot = Offset(96f, 86f)) {
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
                    moveTo(98f, 78f)
                    lineTo(91f, 52f)
                    lineTo(104f, 76f)
                    close()
                },
                color = InnerEarBright,
            )
        }

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
        drawPath(
            path = Path().apply {
                moveTo(158f, 78f)
                lineTo(169f, 54f)
                lineTo(152f, 76f)
                close()
            },
            color = InnerEarBright,
        )

        // Subtle muzzle/chin patch
        drawOval(
            color = Muzzle,
            topLeft = Offset(106f, 108f),
            size = Size(44f, 26f),
        )

        // Eyes
        drawEye(Offset(113f, 98f), pupilDart, blink)
        drawEye(Offset(143f, 98f), pupilDart, blink)

        // BOTH brows now. Asymmetric heights: left lower and flatter,
        // right higher and more arched — the raised-eyebrow smirk effect
        // but with both brows present.
        rotate(8f, pivot = Offset(113f, 84f)) {
            drawPath(
                path = Path().apply {
                    moveTo(100f, 84f)
                    cubicTo(106f, 81f, 120f, 81f, 126f, 84f)
                    lineTo(126f, 86.5f)
                    cubicTo(120f, 83.5f, 106f, 83.5f, 100f, 86.5f)
                    close()
                },
                color = BrowLine,
            )
        }
        rotate(-14f, pivot = Offset(143f, 78f)) {
            drawPath(
                path = Path().apply {
                    moveTo(130f, 78f)
                    cubicTo(138f, 74f, 152f, 74f, 158f, 78f)
                    lineTo(158f, 80.5f)
                    cubicTo(152f, 76.5f, 138f, 76.5f, 130f, 80.5f)
                    close()
                },
                color = BrowLine,
            )
        }

        // Nose
        drawPath(
            path = Path().apply {
                moveTo(124f, 114f)
                lineTo(132f, 114f)
                lineTo(128f, 119f)
                close()
            },
            color = Nose,
        )
        // Nose shadow underside
        drawLine(
            color = NoseShadow,
            start = Offset(126f, 118f),
            end = Offset(130f, 118f),
            strokeWidth = 1.2f,
        )

        // Philtrum (the vertical line from nose to mouth — cat anatomy detail)
        drawLine(
            color = MouthLine,
            start = Offset(128f, 119f),
            end = Offset(128f, 121f),
            strokeWidth = 1.4f,
        )

        // Mouth — closed smirk, one corner ticked up slightly
        drawLine(
            color = MouthLine,
            start = Offset(128f, 121f),
            end = Offset(121f, 124f),
            strokeWidth = 2.0f,
        )
        drawLine(
            color = MouthLine,
            start = Offset(128f, 121f),
            end = Offset(135f, 122.5f),
            strokeWidth = 2.0f,
        )

        // Whisker pad dots (where whiskers anatomically grow from)
        drawCircle(WhiskerPad, 1.0f, Offset(110f, 116f))
        drawCircle(WhiskerPad, 1.0f, Offset(112f, 120f))
        drawCircle(WhiskerPad, 1.0f, Offset(109f, 124f))
        drawCircle(WhiskerPad, 1.0f, Offset(146f, 116f))
        drawCircle(WhiskerPad, 1.0f, Offset(144f, 120f))
        drawCircle(WhiskerPad, 1.0f, Offset(147f, 124f))

        // Whiskers — thin, elegant, asymmetric
        drawLine(Whisker, Offset(106f, 116f), Offset(70f, 106f), strokeWidth = 1.2f)
        drawLine(Whisker, Offset(108f, 122f), Offset(68f, 124f), strokeWidth = 1.2f)
        drawLine(Whisker, Offset(106f, 128f), Offset(74f, 138f), strokeWidth = 1.0f)
        drawLine(Whisker, Offset(150f, 116f), Offset(186f, 108f), strokeWidth = 1.2f)
        drawLine(Whisker, Offset(148f, 122f), Offset(188f, 124f), strokeWidth = 1.2f)
        drawLine(Whisker, Offset(150f, 128f), Offset(182f, 138f), strokeWidth = 1.0f)
    }
}

private fun DrawScope.drawEye(center: Offset, pupilDart: Float, blink: Float) {
    val eyeRx = 13f
    val eyeRy = 10f

    // Outer amber rim
    drawOval(
        color = EyeAmberRim,
        topLeft = Offset(center.x - eyeRx, center.y - eyeRy),
        size = Size(eyeRx * 2, eyeRy * 2),
    )

    // Inner amber core (slightly brighter, smaller)
    drawOval(
        color = EyeAmberCore,
        topLeft = Offset(center.x - eyeRx + 1.4f, center.y - eyeRy + 1.2f),
        size = Size((eyeRx - 1.4f) * 2, (eyeRy - 1.2f) * 2),
    )

    // Eye white inner — small crescent suggesting the white of the eye
    drawOval(
        color = EyeWhite,
        topLeft = Offset(center.x - eyeRx + 3f, center.y - eyeRy + 2.5f),
        size = Size((eyeRx - 3f) * 2, (eyeRy - 2.5f) * 2),
    )

    // Narrow vertical slit pupil
    drawOval(
        color = Pupil,
        topLeft = Offset(center.x - 2.6f + pupilDart, center.y - eyeRy - 0.5f),
        size = Size(5.2f, eyeRy * 2 + 1f),
    )

    // Primary catchlight
    drawCircle(
        color = Shine,
        radius = 2.2f,
        center = Offset(center.x - 4f, center.y - 4.5f),
    )
    // Secondary softer catchlight (smaller, lower)
    drawCircle(
        color = ShineSoft,
        radius = 1.2f,
        center = Offset(center.x + 1.5f, center.y + 2f),
    )

    if (blink > 0f) {
        drawOval(
            color = Fur,
            topLeft = Offset(center.x - eyeRx - 1f, center.y - eyeRy - 1f),
            size = Size((eyeRx + 1f) * 2f, (eyeRy + 1f) * 2f * blink),
        )
    }
}
