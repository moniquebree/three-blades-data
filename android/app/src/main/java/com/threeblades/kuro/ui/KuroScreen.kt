package com.threeblades.kuro.ui

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.threeblades.kuro.AlarmScheduler
import com.threeblades.kuro.KuroSpeakService
import com.threeblades.kuro.ReminderState
import androidx.compose.material3.Text
import kotlinx.coroutines.delay

@Composable
fun KuroScreen() {
    val context = LocalContext.current
    var bubble by remember { mutableStateOf(KuroLines.IDLE_BUBBLE) }
    var bubbleDim by remember { mutableStateOf(true) }
    var speaking by remember { mutableStateOf(false) }
    var settingsOpen by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("") }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                statusText = if (ReminderState.isActive(context) && !ReminderState.isAcknowledged(context)) {
                    val label = ReminderState.label(context)
                    val nag = ReminderState.nagCount(context)
                    val max = ReminderState.maxNagsFor(ReminderState.persistence(context))
                    if (max == 0) "Active: \"$label\"" else "Active: \"$label\" — $nag of ${max + 1}"
                } else ""
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(speaking) {
        if (speaking) {
            delay(3500)
            speaking = false
            bubbleDim = true
            bubble = KuroLines.IDLE_BUBBLE
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column {
                    Text(
                        text = "KURO",
                        color = Moon,
                        fontSize = 18.sp,
                        letterSpacing = 7.sp,
                        fontWeight = FontWeight.Light,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "クロ",
                        color = ShuIro,
                        fontSize = 13.sp,
                        letterSpacing = 4.sp,
                    )
                }

                HankoSettingsButton(onClick = { settingsOpen = true })
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clickable {
                        val activeNag = ReminderState.isActive(context) && !ReminderState.isAcknowledged(context)
                        val line = if (activeNag) {
                            ReminderState.acknowledge(context)
                            AlarmScheduler.cancel(context)
                            ReminderState.clear(context)
                            statusText = ""
                            KuroLines.randomAck()
                        } else {
                            KuroLines.randomTap()
                        }
                        bubble = line
                        bubbleDim = false
                        speaking = true
                        speakNow(context, line)
                    },
                contentAlignment = Alignment.Center,
            ) {
                KuroCat(
                    modifier = Modifier.size(240.dp),
                    speaking = speaking,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = bubble,
                color = if (bubbleDim) Whisper else Moon,
                fontSize = 17.sp,
                fontStyle = if (bubbleDim) FontStyle.Italic else FontStyle.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            if (statusText.isNotEmpty()) {
                Text(
                    text = statusText,
                    color = Ember,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }
        }
    }

    if (settingsOpen) {
        SettingsSheet(onDismiss = { settingsOpen = false })
    }
}

@Composable
private fun HankoSettingsButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(3.dp))
            .background(ShuIro, RoundedCornerShape(3.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "印",
            color = Moon,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

internal fun speakNow(ctx: Context, line: String) {
    val intent = Intent(ctx, KuroSpeakService::class.java).apply {
        putExtra(KuroSpeakService.EXTRA_DIRECT_LINE, line)
    }
    ContextCompat.startForegroundService(ctx, intent)
}
