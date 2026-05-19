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
private val FurSheen = Color(0xFF2C2C3A)
private val FurDeep = Color(0xFF050507)
private val InnerEar = Color(0xFFEBA8B8)
private val InnerEarShadow = Color(0xFF8C5A66)
private val EyeOuter = Color(0xFFB8862E)
private val EyeMid = Color(0xFFE6B548)
private val EyeBright = Color(0xFFF8D470)
private val Pupil = Color(0xFF0A0A0D)
private val Shine = Color(0xFFFFFFFF)
private val Whisker = Color(0xFFC8C2D0)
private val Nose = Color(0xFFD8A0A8)
private val MouthLine = Color(0xFF5A5260)

@Composable
fun KuroCat(
    modifier: Modifier = Modifier,
    speaking: Boolean = false,
) {
    val transition = rememberInfiniteTransition(label = "kuro")

    val breathe by transition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.020f,
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
        initialValue = if (speaking) -1.0f else 0f,
        targetValue = if (speaking) 1.6f else 0f,
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
                durationMillis = 11000
                0f at 0
                0f at 10500
                -2f at 10700
                0f at 10900
                0f at 11000
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
    // Tail — long graceful curve wrapping front-bottom
    drawPath(
        path = Path().apply {
            moveTo(178f, 220f)
            cubicTo(212f, 216f, 226f, 244f, 198f, 252f)
            cubicTo(168f, 258f, 122f, 256f, 92f + tailTipDrift, 244f)
            cubicTo(74f + tailTipDrift, 236f, 82f + tailTipDrift, 220f, 104f + tailTipDrift, 226f)
            cubicTo(132f, 234f, 158f, 232f, 178f, 224f)
            close()
        },
        color = Fur,
    )
    // Tail glossy sheen
    drawPath(
        path = Path().apply {
            moveTo(182f, 222f)
            cubicTo(208f, 220f, 220f, 240f, 212f, 250f)
        },
        color = FurSheen,
        style = Stroke(width = 2.0f),
    )

    scale(scaleX = 1.005f * breathe, scaleY = breathe, pivot = Offset(128f, 244f)) {
        // Body — soft graceful pear silhouette
        drawPath(
            path = Path().apply {
                moveTo(110f, 134f)
                cubicTo(84f, 148f, 72f, 178f, 66f, 210f)
                cubicTo(60f, 242f, 74f, 252f, 100f, 252f)
                lineTo(156f, 252f)
                cubicTo(182f, 252f, 196f, 242f, 190f, 210f)
                cubicTo(184f, 178f, 172f, 148f, 146f, 134f)
                close()
            },
            color = Fur,
        )

        // Body glossy sheen along the upper-right (proper glossy black fur)
        drawPath(
            path = Path().apply {
                moveTo(150f, 138f)
                cubicTo(174f, 152f, 184f, 180f, 190f, 210f)
            },
            color = FurSheen,
            style = Stroke(width = 2.4f),
        )
        // Body soft shadow along lower-left
        drawPath(
            path = Path().apply {
                moveTo(72f, 204f)
                cubicTo(64f, 234f, 76f, 250f, 96f, 252f)
            },
            color = FurDeep,
            style = Stroke(width = 2.4f),
        )

        // Front paws — neat, no aggressive toe lines
        drawOval(
            color = FurSheen,
            topLeft = Offset(96f, 242f),
            size = Size(20f, 10f),
        )
        drawOval(
            color = FurSheen,
            topLeft = Offset(140f, 242f),
            size = Size(20f, 10f),
        )

        // Head — round, soft, gentle. Bigger than before to fit the big eyes.
        drawPath(
            path = Path().apply {
                moveTo(128f, 44f)
                cubicTo(92f, 46f, 76f, 74f, 80f, 102f)
                cubicTo(84f, 130f, 104f, 144f, 128f, 146f)
                cubicTo(152f, 144f, 172f, 130f, 176f, 102f)
                cubicTo(180f, 74f, 164f, 46f, 128f, 44f)
                close()
            },
            color = Fur,
        )

        // Head glossy sheen — upper-right
        drawPath(
            path = Path().apply {
                moveTo(132f, 46f)
                cubicTo(156f, 48f, 172f, 72f, 174f, 100f)
            },
            color = FurSheen,
            style = Stroke(width = 2.0f),
        )

        // Ears — symmetric, tall, graceful triangles
        rotate(earTwitch, pivot = Offset(96f, 86f)) {
            drawPath(
                path = Path().apply {
                    moveTo(90f, 92f)
                    lineTo(76f, 18f)
                    lineTo(116f, 84f)
                    close()
                },
                color = Fur,
            )
            drawPath(
                path = Path().apply {
                    moveTo(94f, 84f)
                    lineTo(84f, 36f)
                    lineTo(108f, 80f)
                    close()
                },
                color = InnerEarShadow,
            )
            drawPath(
                path = Path().apply {
                    moveTo(95f, 78f)
                    lineTo(88f, 46f)
                    lineTo(104f, 74f)
                    close()
                },
                color = InnerEar,
            )
        }

        drawPath(
            path = Path().apply {
                moveTo(166f, 92f)
                lineTo(180f, 18f)
                lineTo(140f, 84f)
                close()
            },
            color = Fur,
        )
        drawPath(
            path = Path().apply {
                moveTo(162f, 84f)
                lineTo(172f, 36f)
                lineTo(148f, 80f)
                close()
            },
            color = InnerEarShadow,
        )
        drawPath(
            path = Path().apply {
                moveTo(161f, 78f)
                lineTo(168f, 46f)
                lineTo(152f, 74f)
                close()
            },
            color = InnerEar,
        )

        // Eyes — big, round, sparkling. The pretty centrepiece.
        drawEye(Offset(108f, 102f), pupilDart, blink)
        drawEye(Offset(148f, 102f), pupilDart, blink)

        // Tiny dainty nose — small heart-ish shape
        drawPath(
            path = Path().apply {
                moveTo(124f, 122f)
                cubicTo(124f, 120f, 128f, 120f, 128f, 122f)
                cubicTo(128f, 120f, 132f, 120f, 132f, 122f)
                lineTo(128f, 126f)
                close()
            },
            color = Nose,
        )

        // Pretty cat mouth — small "ω" shape: two tiny curves below nose
        drawPath(
            path = Path().apply {
                moveTo(128f, 126f)
                quadraticBezierTo(125f, 130f, 122f, 128f)
            },
            color = MouthLine,
            style = Stroke(width = 1.6f),
        )
        drawPath(
            path = Path().apply {
                moveTo(128f, 126f)
                quadraticBezierTo(131f, 130f, 134f, 128f)
            },
            color = MouthLine,
            style = Stroke(width = 1.6f),
        )

        // Whiskers — long, thin, gracefully curved
        drawPath(
            path = Path().apply {
                moveTo(102f, 122f)
                quadraticBezierTo(82f, 116f, 60f, 112f)
            },
            color = Whisker,
            style = Stroke(width = 1.0f),
        )
        drawPath(
            path = Path().apply {
                moveTo(102f, 128f)
                quadraticBezierTo(80f, 128f, 58f, 130f)
            },
            color = Whisker,
            style = Stroke(width = 1.0f),
        )
        drawPath(
            path = Path().apply {
                moveTo(154f, 122f)
                quadraticBezierTo(174f, 116f, 196f, 112f)
            },
            color = Whisker,
            style = Stroke(width = 1.0f),
        )
        drawPath(
            path = Path().apply {
                moveTo(154f, 128f)
                quadraticBezierTo(176f, 128f, 198f, 130f)
            },
            color = Whisker,
            style = Stroke(width = 1.0f),
        )
    }
}

private fun DrawScope.drawEye(center: Offset, pupilDart: Float, blink: Float) {
    val eyeRx = 16f
    val eyeRy = 14f

    // Outer iris ring (deeper amber)
    drawOval(
        color = EyeOuter,
        topLeft = Offset(center.x - eyeRx, center.y - eyeRy),
        size = Size(eyeRx * 2, eyeRy * 2),
    )

    // Mid iris (warm amber)
    drawOval(
        color = EyeMid,
        topLeft = Offset(center.x - eyeRx + 1.5f, center.y - eyeRy + 1.3f),
        size = Size((eyeRx - 1.5f) * 2, (eyeRy - 1.3f) * 2),
    )

    // Inner bright glow (top half of iris — gives the eyes their lit-from-within look)
    drawPath(
        path = Path().apply {
            moveTo(center.x - eyeRx + 3f, center.y)
            cubicTo(
                center.x - eyeRx + 3f, center.y - eyeRy + 4f,
                center.x + eyeRx - 3f, center.y - eyeRy + 4f,
                center.x + eyeRx - 3f, center.y,
            )
            cubicTo(
                center.x + eyeRx - 5f, center.y - 1f,
                center.x - eyeRx + 5f, center.y - 1f,
                center.x - eyeRx + 3f, center.y,
            )
            close()
        },
        color = EyeBright,
    )

    // Narrow vertical slit pupil
    drawOval(
        color = Pupil,
        topLeft = Offset(center.x - 2.4f + pupilDart, center.y - eyeRy + 1f),
        size = Size(4.8f, eyeRy * 2 - 2f),
    )

    // Three sparkles per eye — big, medium, tiny — the anime "pretty" hallmark
    drawCircle(
        color = Shine,
        radius = 3.0f,
        center = Offset(center.x - 5f, center.y - 6f),
    )
    drawCircle(
        color = Shine.copy(alpha = 0.8f),
        radius = 1.6f,
        center = Offset(center.x + 4f, center.y + 4f),
    )
    drawCircle(
        color = Shine.copy(alpha = 0.65f),
        radius = 0.9f,
        center = Offset(center.x - 1.5f, center.y + 2f),
    )

    if (blink > 0f) {
        drawOval(
            color = Fur,
            topLeft = Offset(center.x - eyeRx - 1f, center.y - eyeRy - 1f),
            size = Size((eyeRx + 1f) * 2f, (eyeRy + 1f) * 2f * blink),
        )
    }
}
