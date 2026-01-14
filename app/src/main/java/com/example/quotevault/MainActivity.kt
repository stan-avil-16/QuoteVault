package com.example.quotevault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.quotevault.data.preferences.PreferenceManager
import com.example.quotevault.data.supabase
import com.example.quotevault.ui.navigation.MainNavigation
import com.example.quotevault.ui.navigation.Screen
import com.example.quotevault.ui.theme.QuoteVaultTheme
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val preferenceManager = remember { PreferenceManager(applicationContext) }
            val darkMode by preferenceManager.darkModeFlow.collectAsState(initial = true)
            val fontSize by preferenceManager.fontSizeFlow.collectAsState(initial = 18f)
            
            var isCheckingSession by remember { mutableStateOf(true) }
            var startDestination by remember { mutableStateOf(Screen.Auth.route) }

            // Ensure we wait for the Supabase session to initialize
            LaunchedEffect(Unit) {
                // Give Supabase a tiny moment to load its internal cache
                delay(500) 
                val currentUser = supabase.auth.currentUserOrNull()
                startDestination = if (currentUser != null) Screen.Home.route else Screen.Auth.route
                isCheckingSession = false
            }

            QuoteVaultTheme(
                darkTheme = darkMode,
                fontSize = fontSize
            ) {
                if (isCheckingSession) {
                    // Modern splash/loader while checking login
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = androidx.compose.ui.graphics.Color(0xFF8B5CF6))
                    }
                } else {
                    MainNavigation(startDestination = startDestination)
                }
            }
        }
    }
}
