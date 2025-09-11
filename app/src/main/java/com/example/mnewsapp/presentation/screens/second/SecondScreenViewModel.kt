package com.example.mnewsapp.presentation.screens.second

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnewsapp.data.remote.motelsDto.NewsObjectDto
import com.example.mnewsapp.domain.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SecondScreenViewModel @Inject constructor (
    private val repository: NewsRepository
): ViewModel(){

    private val _uiState = MutableStateFlow<SecondScreenUIEvents>(SecondScreenUIEvents.Loading)
    val uiState = _uiState.asStateFlow()

    fun getRecentNewsByCategory(category:String){

        viewModelScope.launch {

            _uiState.value = SecondScreenUIEvents.Loading
            delay(1000)
            try {

                val recentResponse = repository.getByCategoryNews(category = category)
                _uiState.value = SecondScreenUIEvents.Success(recentNews = recentResponse)


            }catch (e:Exception){

                _uiState.value = SecondScreenUIEvents.Error(message = "${e.message}")

            }



        }


    }


    @SuppressLint("SuspiciousIndentation")
    fun getRecentNewsBySource(sourceName:String){

        viewModelScope.launch {

            _uiState.value = SecondScreenUIEvents.Loading
              delay(1000)
            try {

                val recentResponse = repository.getBySourceNews(source = sourceName)
                _uiState.value = SecondScreenUIEvents.Success(recentNews = recentResponse)


            }catch (e:Exception){

                _uiState.value = SecondScreenUIEvents.Error(message = "${e.message}")

            }



        }


    }

    sealed class SecondScreenUIEvents {

        data object Loading : SecondScreenUIEvents()
        data class Success(val recentNews: NewsObjectDto) : SecondScreenUIEvents()
        data class Error(val message: String) : SecondScreenUIEvents()

    }



}


