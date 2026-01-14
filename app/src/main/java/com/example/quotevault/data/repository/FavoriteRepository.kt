package com.example.quotevault.data.repository

import com.example.quotevault.data.model.Quote
import com.example.quotevault.data.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRow(val user_id: String, val quote_id: String)

@Singleton
class FavoriteRepository @Inject constructor() {

    suspend fun toggleFavorite(quoteId: String) = withContext(Dispatchers.IO) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return@withContext
        
        try {
            // Check if it already exists
            val existing = supabase.from("favorites").select {
                filter {
                    and {
                        eq("user_id", userId)
                        eq("quote_id", quoteId)
                    }
                }
            }.decodeSingleOrNull<FavoriteRow>()

            if (existing == null) {
                // If not found, insert it
                supabase.from("favorites").insert(FavoriteRow(userId, quoteId))
            } else {
                // If found, delete it (unlike)
                supabase.from("favorites").delete {
                    filter {
                        and {
                            eq("user_id", userId)
                            eq("quote_id", quoteId)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun isFavorite(quoteId: String): Boolean = withContext(Dispatchers.IO) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return@withContext false
        try {
            val existing = supabase.from("favorites").select {
                filter {
                    and {
                        eq("user_id", userId)
                        eq("quote_id", quoteId)
                    }
                }
            }.decodeSingleOrNull<FavoriteRow>()
            existing != null
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getFavoriteQuotes(): List<Quote> = withContext(Dispatchers.IO) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return@withContext emptyList()
        
        try {
            // 1. Get all favorite IDs for the current user
            val rows = supabase.from("favorites").select {
                filter {
                    eq("user_id", userId)
                }
            }.decodeList<FavoriteRow>()
            
            val ids = rows.map { it.quote_id }
            if (ids.isEmpty()) return@withContext emptyList()

            // 2. Fetch the full quote objects for those IDs
            // 'isIn' is the correct method for v3.0.2
            supabase.from("quotes").select {
                filter {
                    isIn("id", ids)
                }
            }.decodeList<Quote>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
