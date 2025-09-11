package com.example.mnewsapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes{

    @Serializable
    data object HomeScreen : Routes()

    @Serializable
    data object SearchScreen : Routes()

    @Serializable
    data object BookmarksScreen : Routes()

    @Serializable
    data object DetailScreen : Routes()

    @Serializable
    data object OnboardingScreen : Routes()

    @Serializable
    data class SecondScreen(val query: String,val isSportCategory: Boolean,val isSource: Boolean,val sourceName : String = "") : Routes()


    @Serializable
    data class SportScreen(val title: String,val icon: Int) : Routes()


}