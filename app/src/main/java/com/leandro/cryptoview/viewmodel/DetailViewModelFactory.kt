package com.leandro.cryptoview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leandro.cryptoview.model.repository.CoinRepository

class DetailViewModelFactory(private val repository: CoinRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}