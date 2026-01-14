package com.example.quotevault.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Collection(
    val id: String? = null,
    val name: String,
    val description: String? = null,
    val userId: String? = null,
    val createdAt: String? = null
)

@Serializable
data class CollectionQuote(
    val collection_id: String,
    val quote_id: String
)
