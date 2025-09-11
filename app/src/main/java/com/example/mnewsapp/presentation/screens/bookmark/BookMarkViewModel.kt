package com.example.mnewsapp.presentation.screens.bookmark

import SnackBartEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnewsapp.data.local.ArticleEntity
import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import com.example.mnewsapp.domain.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel()
{

    private val _event = Channel<SnackBartEvent>()
    val event = _event.receiveAsFlow()

    @OptIn(FlowPreview::class)
    val uiState = repository.getAllFavoriteArticles()
        .debounce(300) // Wait for Room to stabilize
        .distinctUntilChanged()
        .map<List<ArticleEntity>, BookmarkScreenUIEvents> {
            BookmarkScreenUIEvents.Success(it)
        }
        .catch { emit(BookmarkScreenUIEvents.Error(it.message ?: "Unknown error")) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            BookmarkScreenUIEvents.Loading
        )


    fun addArticle(article: ArticleEntity){

        viewModelScope.launch {

            try {
                // Calling the suspend function from the repository
                repository.addArticle(article = article)
                _event.send(SnackBartEvent.ShowToast(message = "The Article Restored in Bookmarks!"))
            } catch (e: Exception) {

                _event.send(SnackBartEvent.ShowToast(message = e.message?:"Unknown Error!"))
            }



        }



    }

    fun deleteArticle(url: String){


        viewModelScope.launch {

            try {
                // Calling the suspend function from the repository
                repository.deletebyUrl(url = url)
                _event.send(SnackBartEvent.ShowToast(message = "The Article Deleted from Bookmarks!"))
            } catch (e: Exception) {

                _event.send(SnackBartEvent.ShowToast(message = e.message?:"Unknown Error!"))
            }



        }



    }


    fun deleteAllArticles(){

        viewModelScope.launch {

            try {

                repository.deleteAllArticles()
                _event.send(SnackBartEvent.ShowToast(message = "The Articles Deleted Successfully!"))
            } catch (e: Exception) {

                _event.send(SnackBartEvent.ShowToast(message = e.message?:"Unknown Error!"))
            }


        }




    }


}


sealed class BookmarkScreenUIEvents {

    data object Loading : BookmarkScreenUIEvents()
    data class Success(val allFavorites: List<ArticleEntity>) : BookmarkScreenUIEvents()
    data class Error(val message: String) : BookmarkScreenUIEvents()

}