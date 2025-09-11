package com.example.mnewsapp.domain

import SnackBartEvent
import com.example.mnewsapp.data.local.ArticleDao
import com.example.mnewsapp.data.local.ArticleEntity
import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import com.example.mnewsapp.data.remote.motelsDto.NewsObjectDto
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getPopularNews(query: String,from: String,to: String): NewsObjectDto

    suspend fun getRecentNews(query: String,from: String,): NewsObjectDto

    suspend fun getByCategoryNews(category: String): NewsObjectDto
    suspend fun getBySourceNews(source: String): NewsObjectDto


    fun getAllFavoriteArticles(): Flow<List<ArticleEntity>>

    fun getAllArticleUrls(): Flow<List<String>>

    suspend fun addArticle(article: ArticleEntity)

    suspend fun deleteArticle(article: ArticleEntity)

    suspend fun deletebyUrl(url: String)

    suspend fun deleteAllArticles()


    suspend fun toggleBookMarkStatus(articleDto: ArticleDto,_snackBarEvent: Channel<SnackBartEvent>)

    suspend fun saveUserPref(isFirstTime: Boolean)

    fun isFirstTime(): Flow<Boolean>


}