package com.example.mnewsapp.presentation.screens.detail

import SnackBartEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnewsapp.data.maper.toArticleEntity
import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import com.example.mnewsapp.domain.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    private val _event = Channel<SnackBartEvent>()
    val event = _event.receiveAsFlow()

    fun addArticle(article: ArticleDto){

        viewModelScope.launch {

            try {
                // Calling the suspend function from the repository
                repository.addArticle(article = article.toArticleEntity())
              _event.send(SnackBartEvent.ShowToast(message = "The Article Saved in Bookmarks!"))
            } catch (e: Exception) {

                _event.send(SnackBartEvent.ShowToast(message = e.message?:"Unknown Error!"))
            }



        }



    }

    fun toggleBookMarkStatus(articleDto: ArticleDto){

        viewModelScope.launch {

            try {

                repository.toggleBookMarkStatus(articleDto,_event)

            } catch (e:Exception){

                _event.send(
                    SnackBartEvent.ShowToast(message = e.message?:"Unknown Error!")
                )

            }



        }


    }


    val allArticlesUrls: StateFlow<List<String>> = repository.getAllArticleUrls()
        .catch { exception ->

            _event.send(
                SnackBartEvent.ShowToast(message = "Something went wrong. ${exception.message}")
            )

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )




}