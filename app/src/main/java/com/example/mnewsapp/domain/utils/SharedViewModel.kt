package com.example.mnewsapp.domain.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mnewsapp.data.remote.motelsDto.ArticleDto
import dagger.hilt.android.lifecycle.HiltViewModel

class SharedViewModel: ViewModel() {

    var article by mutableStateOf<ArticleDto?>(null)
        private set

    fun addArticle(newArticle: ArticleDto){

        article = newArticle
    }



}