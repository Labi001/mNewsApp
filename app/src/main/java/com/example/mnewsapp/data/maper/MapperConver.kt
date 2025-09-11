package com.example.mnewsapp.data.maper

import com.example.mnewsapp.data.local.ArticleEntity
import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import com.example.mnewsapp.data.remote.motelsDto.SourceDto

fun ArticleDto.toArticleEntity() = ArticleEntity(

    sourceName = this.source?.name?:"No Source",
    author = this.author?:"No Author",
    content = this.content?:"No Content",
    description = this.description?:"No Description",
    publishedAt = this.publishedAt?:"No Date",
    title = this.title?:"No Title",
    url = this.url?:"No Url",
    imageUrl = this.urlToImage?:"No Image"


)


fun ArticleEntity.toArticleDto() = ArticleDto(

   source = SourceDto(name = this.sourceName),
   author = this.author?:"No Author",
   content = this.content?:"No Content",
   description = this.description?:"No Description",
   publishedAt = this.publishedAt?:"No Date",
   title = this.title?:"No Title",
   url = this.url?:"No Url",
   urlToImage = this.imageUrl?:"No Image"

)

