package com.example.mnewsapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article_table")
    fun selectAllArticles(): Flow<List<ArticleEntity>>

    @Upsert
    suspend fun addArticle(article: ArticleEntity)

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

    @Query("DELETE FROM article_table WHERE url = :url")
    suspend fun deleteByUrl(url: String)


    @Query("DELETE FROM article_table")
    suspend fun deleteAll()


    @Query("SELECT EXISTS(SELECT 1 FROM article_table WHERE url = :url)")
    suspend fun isArticleSaved(url: String): Boolean

    @Query("SELECT url FROM article_table")
    fun getAllUrls(): Flow<List<String>>


}