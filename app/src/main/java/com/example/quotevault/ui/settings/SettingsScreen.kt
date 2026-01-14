package com.example.quotevault.ui.settings

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.quotevault.data.preferences.PreferenceManager
import com.example.quotevault.data.repository.AuthRepository
import com.example.quotevault.worker.DailyQuoteWorker
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val scope = rememberCoroutineScope()
    val authRepository = remember { AuthRepository() }

    val darkMode by preferenceManager.darkModeFlow.collectAsState(initial = true)
    val fontSize by preferenceManager.fontSizeFlow.collectAsState(initial = 18f)
    val notificationsEnabled by preferenceManager.notificationEnabledFlow.collectAsState(initial = true)
    val notificationTime by preferenceManager.notificationTimeFlow.collectAsState(initial = "09:00")

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            scope.launch {
                preferenceManager.setNotificationEnabled(true)
                updateNotificationSchedule(context, true, notificationTime)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Settings", 
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color(0xFF050505)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Appearance Section
            SettingsSectionTitle("Appearance")
            
            SettingsRow {
                Text("Dark Mode", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Switch(
                    checked = darkMode,
                    onCheckedChange = { scope.launch { preferenceManager.setDarkMode(it) } },
                    colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Personalization Section
            SettingsSectionTitle("Personalization")
            
            Text("Font Size: ${fontSize.toInt()}sp", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Slider(
                value = fontSize,
                onValueChange = { scope.launch { preferenceManager.setFontSize(it) } },
                valueRange = 12f..30f,
                steps = 18,
                colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary, activeTrackColor = MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Notifications Section
            SettingsSectionTitle("Daily Inspiration")
            
            SettingsRow {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    Text("Daily Notifications", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                }
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                scope.launch {
                                    preferenceManager.setNotificationEnabled(true)
                                    updateNotificationSchedule(context, true, notificationTime)
                                }
                            }
                        } else {
                            scope.launch { 
                                preferenceManager.setNotificationEnabled(enabled)
                                updateNotificationSchedule(context, enabled, notificationTime)
                            }
                        }
                    }
                )
            }

            if (notificationsEnabled) {
                Spacer(Modifier.height(8.dp))
                SettingsRow(
                    modifier = Modifier.clickable {
                        val parts = notificationTime.split(":")
                        TimePickerDialog(
                            context,
                            { _, h, m ->
                                val newTime = String.format("%02d:%02d", h, m)
                                scope.launch {
                                    preferenceManager.setNotificationTime(newTime)
                                    updateNotificationSchedule(context, true, newTime)
                                }
                            },
                            parts[0].toInt(),
                            parts[1].toInt(),
                            true
                        ).show()
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, null, tint = Color.Gray)
                        Spacer(Modifier.width(12.dp))
                        Text("Notification Time", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                    }
                    Text(notificationTime, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            // Account Section
            Button(
                onClick = {
                    scope.launch {
                        authRepository.signOut()
                        onLogout()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red.copy(alpha = 0.1f),
                    contentColor = Color.Red
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.2f))
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun updateNotificationSchedule(context: android.content.Context, enabled: Boolean, time: String) {
    if (enabled) {
        val parts = time.split(":")
        DailyQuoteWorker.scheduleDailyNotification(context, parts[0].toInt(), parts[1].toInt())
    } else {
        DailyQuoteWorker.cancelNotification(context)
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun SettingsRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}
