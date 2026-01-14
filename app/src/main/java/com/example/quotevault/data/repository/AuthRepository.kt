package com.example.quotevault.data.repository

import com.example.quotevault.data.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.json.put
import kotlinx.serialization.json.buildJsonObject

@Singleton
class AuthRepository @Inject constructor() {

    suspend fun signUp(email: String, password: String) = withContext(Dispatchers.IO) {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signIn(email: String, password: String) = withContext(Dispatchers.IO) {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun verifyPassword(password: String): Boolean = withContext(Dispatchers.IO) {
        val email = currentUser?.email ?: return@withContext false
        try {
            // Re-authenticate to verify current password
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updatePassword(newPassword: String) = withContext(Dispatchers.IO) {
        supabase.auth.updateUser {
            password = newPassword
        }
    }

    suspend fun updateAvatar(avatarUrl: String) = withContext(Dispatchers.IO) {
        supabase.auth.updateUser {
            data = buildJsonObject {
                put("avatar_url", avatarUrl)
            }
        }
    }

    suspend fun signOut() = withContext(Dispatchers.IO) {
        supabase.auth.signOut()
    }

    val currentUser get() = supabase.auth.currentUserOrNull()
}