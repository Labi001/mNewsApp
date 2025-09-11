package com.example.mnewsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mnewsapp.domain.utils.Constants.ARTICLE_TABLE

@Entity(tableName = ARTICLE_TABLE)
data class ArticleEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sourceName: String?,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val url: String?,
    val imageUrl: String?,

)
