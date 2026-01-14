package com.example.quotevault.data.repository

import com.example.quotevault.data.model.Quote
import com.example.quotevault.data.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepository @Inject constructor() {

    suspend fun getQuotes(): List<Quote> = withContext(Dispatchers.IO) {
        supabase
            .from("quotes")
            .select()
            .decodeList<Quote>()
    }

    suspend fun getQuotesByCategory(category: String): List<Quote> =
        withContext(Dispatchers.IO) {
            supabase
                .from("quotes")
                .select {
                    filter {
                        eq("category", category)
                    }
                }
                .decodeList<Quote>()
        }

    suspend fun searchQuotes(query: String): List<Quote> =
        withContext(Dispatchers.IO) {
            supabase
                .from("quotes")
                .select {
                    filter {
                        or {
                            filter("text", FilterOperator.ILIKE, "%$query%")
                            filter("author", FilterOperator.ILIKE, "%$query%")
                        }
                    }
                }
                .decodeList<Quote>()
        }
}
