package com.example.quotevault.data.repository

import com.example.quotevault.data.model.Collection
import com.example.quotevault.data.model.CollectionQuote
import com.example.quotevault.data.model.Quote
import com.example.quotevault.data.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class QuoteWrapper(val quotes: Quote)

@Singleton
class CollectionRepository @Inject constructor() {

    suspend fun getCollections(): List<Collection> = withContext(Dispatchers.IO) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return@withContext emptyList()
        try {
            supabase.from("collections").select {
                filter {
                    eq("user_id", userId)
                }
            }.decodeList<Collection>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun createCollection(name: String, description: String) = withContext(Dispatchers.IO) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return@withContext
        try {
            supabase.from("collections").insert(
                mapOf(
                    "name" to name,
                    "description" to description,
                    "user_id" to userId
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addQuoteToCollection(collectionId: String, quoteId: String) = withContext(Dispatchers.IO) {
        try {
            supabase.from("collection_quotes").insert(CollectionQuote(collectionId, quoteId))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeQuoteFromCollection(collectionId: String, quoteId: String) = withContext(Dispatchers.IO) {
        try {
            supabase.from("collection_quotes").delete {
                filter {
                    and {
                        eq("collection_id", collectionId)
                        eq("quote_id", quoteId)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getQuotesInCollection(collectionId: String): List<Quote> = withContext(Dispatchers.IO) {
        try {
            val response = supabase.from("collection_quotes").select(Columns.raw("quotes(*)")) {
                filter {
                    eq("collection_id", collectionId)
                }
            }
            response.decodeList<QuoteWrapper>().map { it.quotes }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun createCustomQuote(text: String, author: String, collectionId: String) = withContext(Dispatchers.IO) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return@withContext
        try {
            val newQuote = supabase.from("quotes").insert(
                Quote(text = text, author = author, category = "Custom", user_id = userId)
            ) { select() }.decodeSingle<Quote>()
            
            supabase.from("collection_quotes").insert(CollectionQuote(collectionId, newQuote.id!!))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateCustomQuote(quoteId: String, text: String, author: String) = withContext(Dispatchers.IO) {
        try {
            supabase.from("quotes").update({
                Quote::text setTo text
                Quote::author setTo author
            }) {
                filter { eq("id", quoteId) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteCustomQuote(quoteId: String) = withContext(Dispatchers.IO) {
        try {
            supabase.from("quotes").delete {
                filter { eq("id", quoteId) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
