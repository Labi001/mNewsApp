package com.example.mnewsapp

import com.example.mnewsapp.data.NewsRepositoryImpl
import com.example.mnewsapp.data.local.ArticleDao
import com.example.mnewsapp.data.local.ArticleEntity
import com.example.mnewsapp.data.local.NewsDataBase
import com.example.mnewsapp.data.remote.networkapi.NetworkApi
import com.example.mnewsapp.domain.utils.responseBody
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import kotlinx.serialization.SerializationException

class NewsRepositoryImplTest {

    private lateinit var repository: NewsRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: NetworkApi
    private lateinit var dao: ArticleDao


    @Before
    fun setUp() {

        val contentType = "application/json".toMediaType()

        val json = Json {
            ignoreUnknownKeys = true
        }

        dao = mockk(relaxed = true)

        val mockDatabase = mockk<NewsDataBase> {
            every { articleDao() } returns dao
        }

            mockWebServer = MockWebServer()
            okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
            .create(NetworkApi::class.java)

        repository = NewsRepositoryImpl(
            newsApi = api,
            newsDatabase = mockDatabase,
            prefs = mockk(relaxed = true)
        )


    }

    @After
    fun tearDown(){

        mockWebServer.shutdown()


    }

    @Test
    fun `getPopularNews valid response returns success`() = runBlocking {


        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
        )

        val result = repository.getPopularNews("bitcoin", "2025-09-01", "2025-09-18")

        assertThat(result.status).isEqualTo("ok")
        assertThat(result.articles).hasSize(1)
        assertThat(result.articles.first().title).isEqualTo("Sample News Title")


    }

    @Test
    fun `getPopularNews malformed response throws exception`() = runBlocking {

        val malformedResponse = """
        {
          "status": "ok",
          "totalResults": "not_a_number"
        }
    """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(malformedResponse)
        )

        try {
            repository.getPopularNews("bitcoin", "2025-09-01", "2025-09-18")
            fail("Expected exception not thrown")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(SerializationException::class.java)
        }
    }


    @Test
    fun `getAllFavoriteArticles emits article list`() = runBlocking {

        val sampleArticles = listOf(
            ArticleEntity(
                id = 1,
                url = "https://news.com/article1",
                title = "Breaking News: Kotlin Takes Over",
                sourceName = "BBC News",
                author = "Anonymous",
                content = "Kotlin is now the most loved language.",
                description = "Developers worldwide are switching to Kotlin.",
                publishedAt = "2025-09-18",
                imageUrl = "https://news.com/image1.jpg"
            ),
            ArticleEntity(
                id = 2,
                url = "https://news.com/article2",
                title = "AI in 2025: What’s Next?",
                sourceName = "CNN",
                author = "Jane Doe",
                content = "Artificial intelligence is evolving faster than expected.",
                description = "Experts weigh in on AI's future.",
                publishedAt = "2025-09-17",
                imageUrl = "https://news.com/image2.jpg"
            ),
            ArticleEntity(
                id = 3,
                url = "https://news.com/article3",
                title = "Climate Change Report Released",
                sourceName = "The Guardian",
                author = "John Smith",
                content = "The latest climate data shows record temperatures.",
                description = "A new UN report warns of urgent action.",
                publishedAt = "2025-09-16",
                imageUrl = "https://news.com/image3.jpg"
            )
        )


        // Mock DAO to return a Flow of articles
        coEvery { dao.selectAllArticles() } returns flowOf(sampleArticles)

        val result = repository.getAllFavoriteArticles().first()

        assertThat(result).hasSize(3)
        assertThat(result[0].url).isEqualTo("https://news.com/article1")

        coVerify(exactly = 1) { dao.selectAllArticles() }
    }


    @Test
    fun `addArticle calls dao addArticle`() = runBlocking {

        val article = ArticleEntity(url = "url1",
            title = "Title 1", id = 1,
            sourceName = "BBC News",
            author = "Anonymos",
            content = "No Content",
            description = "No Description",
            publishedAt = "18-09-2025",
            imageUrl = "")

        coEvery { dao.addArticle(article) } just Runs

        repository.addArticle(article)

        coVerify(exactly = 1) { dao.addArticle(article) }
    }


    @Test
    fun `deleteArticle calls dao deleteArticle`() = runBlocking {

        val article = ArticleEntity(url = "url1",
            title = "Title 1", id = 1,
            sourceName = "BBC News",
            author = "Anonymos",
            content = "No Content",
            description = "No Description",
            publishedAt = "18-09-2025",
            imageUrl = "")

        coEvery { dao.deleteArticle(article) } just Runs

        repository.deleteArticle(article)

        coVerify(exactly = 1) { dao.deleteArticle(article) }
    }






}