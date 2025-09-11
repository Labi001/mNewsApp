package com.example.mnewsapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnewsapp.data.remote.motelsDto.NewsObjectDto
import com.example.mnewsapp.domain.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)
    val uiState = _uiState.asStateFlow()

    fun getPopularNews(query:String,from: String,fromM: String,to: String){

        viewModelScope.launch {

            _uiState.value = HomeScreenUIEvents.Loading
            delay(1000)
            try {
                val serverResponse = repository.getPopularNews(query = query, from = from, to = to)
                val recentResponse = repository.getRecentNews(query = query, from = fromM)
                _uiState.value = HomeScreenUIEvents.Success(newsResponse = serverResponse,
                    recentNews = recentResponse)


            }catch (e:Exception){

                _uiState.value = HomeScreenUIEvents.Error(message = "${e.message}")

            }



        }


    }



}



sealed class HomeScreenUIEvents {

    data object Loading : HomeScreenUIEvents()
    data class Success(val newsResponse: NewsObjectDto,val recentNews: NewsObjectDto) : HomeScreenUIEvents()
    data class Error(val message: String) : HomeScreenUIEvents()

}