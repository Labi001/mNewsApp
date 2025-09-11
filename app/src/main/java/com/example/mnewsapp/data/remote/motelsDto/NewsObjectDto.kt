package com.example.mnewsapp.data.remote.motelsDto

import kotlinx.serialization.Serializable

@Serializable
data class NewsObjectDto(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
)