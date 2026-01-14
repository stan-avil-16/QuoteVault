package com.example.quotevault.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferenceManager(private val context: Context) {
    companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val FONT_SIZE = floatPreferencesKey("font_size")
        val ACCENT_COLOR = stringPreferencesKey("accent_color")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val NOTIFICATION_TIME = stringPreferencesKey("notification_time") // Format "HH:mm"
    }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: true
    }

    val fontSizeFlow: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[FONT_SIZE] ?: 18f
    }

    val notificationEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_ENABLED] ?: true
    }

    val notificationTimeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_TIME] ?: "09:00"
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = enabled
        }
    }

    suspend fun setFontSize(size: Float) {
        context.dataStore.edit { preferences ->
            preferences[FONT_SIZE] = size
        }
    }

    suspend fun setNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED] = enabled
        }
    }

    suspend fun setNotificationTime(time: String) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_TIME] = time
        }
    }
}
