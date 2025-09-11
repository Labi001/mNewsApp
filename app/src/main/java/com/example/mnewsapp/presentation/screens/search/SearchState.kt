

import com.example.mnewsapp.data.remote.motelsDto.ArticleDto

data class SearchState(
    val query: String = "",
    val from: String = "",
    val to: String = "",
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false,
    val searchedArticles: List<ArticleDto>? = emptyList()
)