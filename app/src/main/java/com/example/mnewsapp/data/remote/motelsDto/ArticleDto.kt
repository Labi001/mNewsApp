package com.example.mnewsapp.data.remote.motelsDto

import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    val source: SourceDto? = null,
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String ? = null
)


@Serializable
data class SourceDto(

    val id: String? = null,
    val name: String? = null,

)