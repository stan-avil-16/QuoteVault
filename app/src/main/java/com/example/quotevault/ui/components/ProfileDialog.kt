package com.example.quotevault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.quotevault.data.repository.AuthRepository
import com.example.quotevault.ui.theme.ElectricViolet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDialog(
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {
    val authRepository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()
    val user = authRepository.currentUser
    
    var currentAvatar by remember { 
        mutableStateOf(user?.userMetadata?.get("avatar_url")?.toString()?.replace("\"", "") ?: "") 
    }
    
    var showPasswordSection by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isVerifying by remember { mutableStateOf(false) }
    var isPasswordVerified by remember { mutableStateOf(false) }
    
    // Using high-quality professional presets with neutral expressions
    // In C:/QuoteVault/app/src/main/java/com/example/quotevault/ui/components/ProfileDialog.kt

    val avatars = listOf(
        // Professional Males: Child, Boy, Adult, Uncle, Grandpa, Pro
        "https://api.dicebear.com/7.x/lorelei/png?seed=ChildMale",
        "https://api.dicebear.com/7.x/lorelei/png?seed=YoungMale",
        "https://api.dicebear.com/7.x/lorelei/png?seed=AdultMale",
        "https://api.dicebear.com/7.x/lorelei/png?seed=UncleMale",
        "https://api.dicebear.com/7.x/lorelei/png?seed=Grandpa",
        "https://api.dicebear.com/7.x/lorelei/png?seed=BusinessMale",

        // Professional Females: Child, Girl, Adult, Aunt, Grandma, Pro
        "https://api.dicebear.com/7.x/lorelei/png?seed=ChildFemale",
        "https://api.dicebear.com/7.x/lorelei/png?seed=YoungFemale",
        "https://api.dicebear.com/7.x/lorelei/png?seed=AdultFemale",
        "https://api.dicebear.com/7.x/lorelei/png?seed=AuntFemale",
        "https://api.dicebear.com/7.x/lorelei/png?seed=Grandma",
        "https://api.dicebear.com/7.x/lorelei/png?seed=BusinessFemale"
    )

    val snackbarHostState = remember { SnackbarHostState() }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF151515)
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { padding ->
                Column(
                    modifier = Modifier.padding(padding).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Profile", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, null, tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Current Selected Avatar
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(ElectricViolet.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentAvatar.isNotEmpty()) {
                            AsyncImage(
                                model = currentAvatar,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Person, null, tint = ElectricViolet, modifier = Modifier.size(50.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(user?.email ?: "No email found", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Choose Avatar", style = MaterialTheme.typography.labelLarge, color = Color.Gray, modifier = Modifier.align(Alignment.Start))
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Increased height and fixed grid to ensure all 12 professional avatars are visible
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.height(210.dp), // Increased height to accommodate 3 rows of 4
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(avatars) { avatarUrl ->
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.05f))
                                    .border(
                                        width = 2.dp,
                                        color = if (currentAvatar == avatarUrl) ElectricViolet else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        currentAvatar = avatarUrl
                                        scope.launch {
                                            authRepository.updateAvatar(avatarUrl)
                                            snackbarHostState.showSnackbar("Avatar updated!")
                                        }
                                    }
                            ) {
                                AsyncImage(
                                    model = avatarUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!showPasswordSection) {
                        OutlinedButton(
                            onClick = { showPasswordSection = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Text("Change Password")
                        }
                    } else {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            if (!isPasswordVerified) {
                                Text("Step 1: Verify Current Password", color = ElectricViolet, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = currentPassword,
                                    onValueChange = { currentPassword = it },
                                    label = { Text("Current Password") },
                                    modifier = Modifier.fillMaxWidth(),
                                    visualTransformation = PasswordVisualTransformation(),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = ElectricViolet,
                                        unfocusedBorderColor = Color.Gray
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        scope.launch {
                                            isVerifying = true
                                            val success = authRepository.verifyPassword(currentPassword)
                                            if (success) {
                                                isPasswordVerified = true
                                                snackbarHostState.showSnackbar("Verified! Now enter new password.")
                                            } else {
                                                snackbarHostState.showSnackbar("Error: Incorrect current password")
                                            }
                                            isVerifying = false
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !isVerifying && currentPassword.isNotEmpty(),
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
                                ) {
                                    if (isVerifying) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                                    else Text("Confirm Password")
                                }
                            } else {
                                Text("Step 2: Enter New Password", color = ElectricViolet, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = newPassword,
                                    onValueChange = { newPassword = it },
                                    label = { Text("New Password") },
                                    modifier = Modifier.fillMaxWidth(),
                                    visualTransformation = PasswordVisualTransformation(),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = ElectricViolet,
                                        unfocusedBorderColor = Color.Gray
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        scope.launch {
                                            try {
                                                authRepository.updatePassword(newPassword)
                                                snackbarHostState.showSnackbar("Success: Password updated!")
                                                kotlinx.coroutines.delay(1500)
                                                showPasswordSection = false
                                                isPasswordVerified = false
                                                currentPassword = ""
                                                newPassword = ""
                                            } catch (e: Exception) {
                                                snackbarHostState.showSnackbar("Update failed. Try again.")
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = newPassword.length >= 6,
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
                                ) {
                                    Text("Update Password")
                                }
                            }
                            
                            TextButton(
                                onClick = { 
                                    showPasswordSection = false 
                                    isPasswordVerified = false
                                    currentPassword = ""
                                    newPassword = ""
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Cancel", color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = {
                            scope.launch {
                                authRepository.signOut()
                                onLogout()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Logout, null, tint = Color.Red.copy(alpha = 0.7f))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logout", color = Color.Red.copy(alpha = 0.7f))
                    }
                }
            }
        }
    }
}
