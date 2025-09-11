package com.example.mnewsapp

import SnackBartEvent
import com.example.mnewsapp.data.local.ArticleEntity
import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import com.example.mnewsapp.data.remote.motelsDto.NewsObjectDto
import com.example.mnewsapp.domain.NewsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class NewsRepositoryFake: NewsRepository {

    private val favoriteArticlesFlow = MutableSharedFlow<List<ArticleEntity>>(replay = 1)

    private val favoriteArticles = mutableListOf<ArticleEntity>()

    private val isFirstTimeUser = MutableStateFlow(true)


    override suspend fun getPopularNews(
        query: String,
        from: String,
        to: String
    ): NewsObjectDto {

        return NewsObjectDto(
            articles = emptyList(),
            status = "ok",
            totalResults = 0
        )

    }

    override suspend fun getRecentNews(
        query: String,
        from: String
    ): NewsObjectDto {

        return NewsObjectDto(
            articles = emptyList(),
            status = "ok",
            totalResults = 0
        )

    }

    override suspend fun getByCategoryNews(category: String): NewsObjectDto {

        return NewsObjectDto(
            articles = emptyList(),
            status = "ok",
            totalResults = 0
        )
    }

    override suspend fun getBySourceNews(source: String): NewsObjectDto {
        return NewsObjectDto(
            articles = emptyList(),
            status = "ok",
            totalResults = 0
        )
    }

    override fun getAllFavoriteArticles(): Flow<List<ArticleEntity>> {

        return favoriteArticlesFlow
    }

    override fun getAllArticleUrls(): Flow<List<String>> {

        return favoriteArticlesFlow.map { articles ->
            articles.map { it.url ?: "" } // Replaces null with an empty string
        }



    }

    override suspend fun addArticle(article: ArticleEntity) {

        favoriteArticles.add(article)
        favoriteArticlesFlow.emit(favoriteArticles)

    }

    override suspend fun deleteArticle(article: ArticleEntity) {

        favoriteArticles.remove(article)
        favoriteArticlesFlow.emit(favoriteArticles)

    }

    override suspend fun deletebyUrl(url: String) {

        favoriteArticles.removeAll { it.url == url }
        favoriteArticlesFlow.emit(favoriteArticles)

    }

    override suspend fun deleteAllArticles() {

        favoriteArticles.clear()
        favoriteArticlesFlow.emit(favoriteArticles)

    }

    override suspend fun toggleBookMarkStatus(
        articleDto: ArticleDto,
        _snackBarEvent: Channel<SnackBartEvent>
    ) {
        _snackBarEvent.send(SnackBartEvent.ShowToast(message = "Article Saved"))
    }

    override suspend fun saveUserPref(isFirstTime: Boolean) {

        isFirstTimeUser.value = isFirstTime
    }

    override fun isFirstTime(): Flow<Boolean>  = isFirstTimeUser


}