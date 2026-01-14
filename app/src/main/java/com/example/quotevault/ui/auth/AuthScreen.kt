package com.example.quotevault.ui.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotevault.data.repository.AuthRepository
import com.example.quotevault.ui.theme.ElectricViolet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSignUp by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val repository = remember { AuthRepository() }
    val scrollState = rememberScrollState()

    // --- ANIMATIONS ---
    val infiniteTransition = rememberInfiniteTransition(label = "living_ui")
    val drift by infiniteTransition.animateFloat(
        initialValue = -50f, targetValue = 50f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing), RepeatMode.Reverse), label = "drift"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 0.4f,
        animationSpec = infiniteRepeatable(tween(4000, easing = EaseInOutSine), RepeatMode.Reverse), label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020202))
    ) {
        // --- MOVING & GLOWING BACKGROUND ---
        Box(
            modifier = Modifier
                .size(450.dp)
                .offset(x = drift.dp - 100.dp, y = (-100).dp)
                .background(ElectricViolet.copy(alpha = pulse), CircleShape)
                .blur(100.dp)
        )
        
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp - drift.dp, y = 100.dp)
                .background(ElectricViolet.copy(alpha = pulse * 0.8f), CircleShape)
                .blur(100.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Logo
            Surface(
                modifier = Modifier.size(85.dp),
                shape = RoundedCornerShape(24.dp),
                color = ElectricViolet,
                shadowElevation = 15.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.FormatQuote, null, tint = Color.White, modifier = Modifier.size(45.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "QuoteVault",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- AUTH CARD ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                color = Color(0xFF151515),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isSignUp) "Create Account" else "Welcome Back",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    // Email Box
                    TextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        placeholder = { Text("Email Address", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, if (email.isNotEmpty()) ElectricViolet else Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp)),
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = ElectricViolet) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF222222),
                            unfocusedContainerColor = Color(0xFF1A1A1A),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = ElectricViolet
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Box
                    TextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        placeholder = { Text("Password", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, if (password.isNotEmpty()) ElectricViolet else Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp)),
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = ElectricViolet) },
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(image, null, tint = Color.Gray)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF222222),
                            unfocusedContainerColor = Color(0xFF1A1A1A),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = ElectricViolet
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    if (errorMessage != null) {
                        Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.padding(top = 16.dp), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Main Button
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                errorMessage = null
                                try {
                                    if (isSignUp) repository.signUp(email, password)
                                    else repository.signIn(email, password)
                                    onAuthSuccess()
                                } catch (e: Exception) {
                                    errorMessage = e.message ?: "Authentication failed"
                                } finally { isLoading = false }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(if (isSignUp) "GET STARTED" else "SIGN IN", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // --- FIXED FOOTER ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(bottom = 60.dp)
            ) {
                Text(
                    text = if (isSignUp) "Already have an account?" else "New to QuoteVault?",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Solid button for footer ensures text is 100% visible
                Button(
                    onClick = { isSignUp = !isSignUp; errorMessage = null },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricViolet)
                ) {
                    Text(
                        text = if (isSignUp) "LOG IN HERE" else "CREATE ACCOUNT NOW",
                        color = Color.White, // FORCED WHITE
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
