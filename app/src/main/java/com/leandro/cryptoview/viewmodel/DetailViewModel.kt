package com.leandro.cryptoview.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandro.cryptoview.model.entity.Coin
import com.leandro.cryptoview.model.repository.CoinRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(private val coinRepository: CoinRepository): ViewModel() {
    val coinLiveData = MutableLiveData<Coin>()

    fun getOne(id: Int): Job = viewModelScope.launch{
        val coin = coinRepository.getOne(id)

        coinLiveData.value = coin
    }
}