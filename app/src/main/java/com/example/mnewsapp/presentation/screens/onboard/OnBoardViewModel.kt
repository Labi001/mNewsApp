package com.example.mnewsapp.presentation.screens.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnewsapp.domain.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel()
{

    private val _isFirstTime = MutableStateFlow<Boolean?>(null)
    val isFirstTime: StateFlow<Boolean?> = _isFirstTime


    init {
        viewModelScope.launch {
            repository.isFirstTime().collect { result ->
                _isFirstTime.value = result
            }
        }
    }

    fun saveUserPref(isSaved: Boolean){

        viewModelScope.launch {

            try {

                repository.saveUserPref(isFirstTime = isSaved)

            } catch (e: Exception) {

                e.printStackTrace()
            }


        }


    }



}