package com.example.mnewsapp.presentation.screens.search

import SearchEvent
import SearchState
import SnackBartEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnewsapp.domain.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel()
{

    private val _state = MutableStateFlow(SearchState())
    val state = _state


    private val _uiEvent = Channel<SnackBartEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SearchEvent) {

        when(event){

            is SearchEvent.OnQueryChange -> {

                _state.value = _state.value.copy(query = event.query, from = event.from, to = event.to)

            }
            SearchEvent.OnSearch -> {
                executeSearch()
            }
        }


    }


    private fun executeSearch() {

        _state.value = _state.value.copy(isSearching = true,
            searchedArticles = emptyList())

        val currentQuery = _state.value.query.trim()
        val from = _state.value.from
        val to = _state.value.from

        if(currentQuery.isNotEmpty()){

            viewModelScope.launch {

                val serverResponse = repository.getPopularNews(query = currentQuery, from = from, to = to)

                _state.value = _state.value.copy(
                    isSearching = false,
                    searchedArticles = serverResponse.articles,
                    query = "",
                    hasSearched = true
                   )
            }



        }



    }



}