package com.example.mnewsapp.data.remote.networkapi

import com.example.mnewsapp.data.remote.motelsDto.NewsObjectDto
import com.example.mnewsapp.domain.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET(value = "everything")
    suspend fun getPopularNews(

        @Query("q") query:String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("sortBy") sortBy: String = "popularity",
        @Query("apiKey") apiKey: String = Constants.API_KEY,

        ): NewsObjectDto

   // String = "2025-07-28"


    @GET(value = "everything")
    suspend fun getRecentNews(

        @Query("q") query:String,
        @Query("from") from: String,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = Constants.API_KEY,

        ): NewsObjectDto


    @GET(value = "top-headlines")
    suspend fun getByCategoryNews(

        @Query("country") sortBy: String = "us",
        @Query("category") category:String,
        @Query("apiKey") apiKey: String = Constants.API_KEY,

        ): NewsObjectDto


    @GET(value = "top-headlines")
    suspend fun getBySourcesNews(

        @Query("sources") source:String,
        @Query("apiKey") apiKey: String = Constants.API_KEY,

        ): NewsObjectDto



}