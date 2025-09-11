package com.example.mnewsapp.data.maper

import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import com.example.mnewsapp.data.remote.motelsDto.SourceDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test


class MapperConverTest {

    @Test
    fun `maps all fields correctly when values present`() {

        val source = SourceDto(name = "BBC News")

        val dto = ArticleDto(
            source = source,
            author = "John Smith",
            content = "Some news content here.",
            description = "Breaking news description.",
            publishedAt = "2025-09-15T12:00:00Z",
            title = "Exciting News!",
            url = "https://bbc.com/news",
            urlToImage = "https://bbc.com/image.jpg"
        )

        val entity = dto.toArticleEntity()

        assertThat(entity.sourceName).isEqualTo("BBC News")
        assertThat(entity.author).isEqualTo("John Smith")
        assertThat(entity.content).isEqualTo("Some news content here.")
        assertThat(entity.description).isEqualTo("Breaking news description.")
        assertThat(entity.publishedAt).isEqualTo("2025-09-15T12:00:00Z")
        assertThat(entity.title).isEqualTo("Exciting News!")
        assertThat(entity.url).isEqualTo("https://bbc.com/news")
        assertThat(entity.imageUrl).isEqualTo("https://bbc.com/image.jpg")
    }




}