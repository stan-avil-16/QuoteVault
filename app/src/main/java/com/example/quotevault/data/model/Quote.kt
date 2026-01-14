package com.example.quotevault.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    val id: String? = null,
    val text: String,
    val author: String,
    val category: String,
    val user_id: String? = null,
    val created_at: String? = null
)
