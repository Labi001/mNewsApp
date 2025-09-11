package com.example.mnewsapp.data


import SnackBartEvent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.mnewsapp.data.local.ArticleEntity
import com.example.mnewsapp.data.local.NewsDataBase
import com.example.mnewsapp.data.maper.toArticleEntity
import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import com.example.mnewsapp.data.remote.motelsDto.NewsObjectDto
import com.example.mnewsapp.data.remote.networkapi.NetworkApi
import com.example.mnewsapp.domain.NewsRepository
import com.example.mnewsapp.domain.utils.Constants.PREF_KEY
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class NewsRepositoryImpl(
    private val newsApi: NetworkApi,
    private val newsDatabase: NewsDataBase,
    private val prefs: DataStore<Preferences>
): NewsRepository
{

    companion object {

        private val USER_PREF_KEY = booleanPreferencesKey(PREF_KEY)
    }

    private val dao = newsDatabase.articleDao()

    override suspend fun getPopularNews(query: String,from: String,to: String): NewsObjectDto {

      return  newsApi.getPopularNews(query = query, from = from, to = to)
    }


    override suspend fun getRecentNews(query: String,from: String): NewsObjectDto {

        return newsApi.getRecentNews(query = query, from = from)
    }



    override suspend fun getByCategoryNews(category: String): NewsObjectDto {

        return newsApi.getByCategoryNews(category = category)
    }



    override suspend fun getBySourceNews(source: String): NewsObjectDto {

       return newsApi.getBySourcesNews(source = source)

    }

    override fun getAllFavoriteArticles(): Flow<List<ArticleEntity>> {

        return dao.selectAllArticles()
    }

    override fun getAllArticleUrls(): Flow<List<String>> {

      return dao.getAllUrls()
    }

    override suspend fun addArticle(article: ArticleEntity) {

        return dao.addArticle(article)
    }

    override suspend fun deleteArticle(article: ArticleEntity) {

        return dao.deleteArticle(article)

    }

    override suspend fun deletebyUrl(url: String) {

        return dao.deleteByUrl(url = url)

    }

    override suspend fun deleteAllArticles() {

        return dao.deleteAll()
    }

    override suspend fun toggleBookMarkStatus(
        articleDto: ArticleDto,
        _snackBarEvent: Channel<SnackBartEvent>
    ) {

        val url = articleDto.url ?: return

        val isSaved = dao.isArticleSaved(url = url)

        val articleEntity = articleDto.toArticleEntity()

       if(isSaved){

           dao.deleteByUrl(url = url)
           _snackBarEvent.send(

               SnackBartEvent.ShowToast(message = "This Article was deleted from Bookmarks")
           )


       }else{

           dao.addArticle(articleEntity)
           _snackBarEvent.send(

               SnackBartEvent.ShowToast(message = "This Article was added to Bookmarks")
           )



       }




    }

    override suspend fun saveUserPref(isFirstTime: Boolean) {


        prefs.edit { preferences ->
            preferences[USER_PREF_KEY] = isFirstTime
        }

    }

    override fun isFirstTime(): Flow<Boolean> {

        return prefs.data.map { preferences ->
            preferences[USER_PREF_KEY] ?: true
        }

    }


}