package com.example.mnewsapp.endToEnd


import com.example.mnewsapp.NewsRepositoryFake
import com.example.mnewsapp.data.di.AppModule
import com.example.mnewsapp.domain.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object FakeModules {

    @Provides
    @Singleton
    fun providefakeNewsRepository(): NewsRepository {


        return NewsRepositoryFake()

    }

}