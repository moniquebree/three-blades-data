package com.threeblades.kuro.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.threeblades.kuro.AlarmScheduler
import com.threeblades.kuro.KuroPrefs
import com.threeblades.kuro.ReminderState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var persistence by remember { mutableIntStateOf(KuroPrefs.persistence(context)) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Night,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp).padding(bottom = 16.dp),
        ) {
            Text(
                text = "KURO  —  SETTINGS",
                color = Whisper,
                fontSize = 12.sp,
                letterSpacing = 4.sp,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Nag persistence", color = Whisper, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))

            PersistenceOption(
                label = "Gentle — says it once",
                selected = persistence == ReminderState.PERSIST_GENTLE,
            ) {
                persistence = ReminderState.PERSIST_GENTLE
                KuroPrefs.setPersistence(context, persistence)
            }
            PersistenceOption(
                label = "Firm — repeats up to 4 times",
                selected = persistence == ReminderState.PERSIST_FIRM,
            ) {
                persistence = ReminderState.PERSIST_FIRM
                KuroPrefs.setPersistence(context, persistence)
            }
            PersistenceOption(
                label = "Stubborn — keeps coming back",
                selected = persistence == ReminderState.PERSIST_STUBBORN,
            ) {
                persistence = ReminderState.PERSIST_STUBBORN
                KuroPrefs.setPersistence(context, persistence)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "DEBUG",
                color = Whisper,
                fontSize = 11.sp,
                letterSpacing = 3.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val id = "test-${System.currentTimeMillis()}"
                    ReminderState.start(context, id, "the test nag", persistence)
                    AlarmScheduler.scheduleAt(context, System.currentTimeMillis() + 30_000L)
                    scope.launch { sheetState.hide(); onDismiss() }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Mist,
                    contentColor = Moon,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Test nag — fires in 30s")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    ReminderState.acknowledge(context)
                    AlarmScheduler.cancel(context)
                    ReminderState.clear(context)
                    scope.launch { sheetState.hide(); onDismiss() }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Mist,
                    contentColor = Moon,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Clear any active nag")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { scope.launch { sheetState.hide(); onDismiss() } },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Ember,
                    contentColor = Ink,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PersistenceOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Ember,
                unselectedColor = Whisper,
            ),
        )
        Text(text = label, color = Moon, fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp))
    }
}
