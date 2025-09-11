package com.example.mnewsapp.data.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.mnewsapp.data.NewsRepositoryImpl
import com.example.mnewsapp.data.local.NewsDataBase
import com.example.mnewsapp.data.remote.networkapi.NetworkApi
import com.example.mnewsapp.domain.NewsRepository
import com.example.mnewsapp.domain.utils.Constants.BASE_URL
import com.example.mnewsapp.domain.utils.Constants.NEWS_DATABASE
import com.example.mnewsapp.domain.utils.Constants.PREF_KEY
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @SuppressLint("SuspiciousIndentation")
    @Provides
    @Singleton
    fun provideCalorieApi() : NetworkApi {

        val contentType = "application/json".toMediaType()

        val json = Json {
            ignoreUnknownKeys = true
        }

        val retrofit = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(NetworkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(
        @ApplicationContext context: Context
    ): NewsDataBase {

        return Room.databaseBuilder(
            context,
            NewsDataBase::class.java,
            NEWS_DATABASE
        )
            .fallbackToDestructiveMigration(false)
            .build()

    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(PREF_KEY )
        }
    }

    @Provides
    @Singleton
    fun provideRepository(
        api: NetworkApi,
        dataBase: NewsDataBase,
        prefs: DataStore<Preferences>
    ): NewsRepository{
        return NewsRepositoryImpl(newsApi = api, newsDatabase = dataBase, prefs = prefs)
    }


}


