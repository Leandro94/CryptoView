package com.leandro.cryptoview.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandro.cryptoview.model.entity.Coin
import com.leandro.cryptoview.service.CoinsApiService
import com.leandro.cryptoview.model.CryptoResult
import com.leandro.cryptoview.model.repository.CoinRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListViewModel(private val coinRepository: CoinRepository) : ViewModel() {

    private val coinsApiService = CoinsApiService()
    private val disposable = CompositeDisposable()

    val coins = MutableLiveData<List<Coin>>()

    val coinsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        getFromRemote()
    }

    private fun getFromRemote() {
        loading.value = true
        disposable.add(
            coinsApiService.getCoins()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CryptoResult>() {

                    override fun onSuccess(coinsList: CryptoResult) {
                        coinsList.data?.let { storeCoinsLocally(it) }
                            Log.d("XXX", "Recuperando dados na API...")
                    }

                    override fun onError(e: Throwable) {
                        coinsLoadError.value = true
                        loading.value = false
                        getFromDatabse()
                    }

                })
        )
    }

    private fun getFromDatabse(): Job = viewModelScope.launch {
        loading.value = true
        val list = coinRepository.getAll()
        coinsRetrieved(list)
        Log.d("XXX", "Recuperando dados no BD...")
    }

    private fun coinsRetrieved(coinsList: List<Coin>) {
        coins.value = coinsList
        coinsLoadError.value = false
        loading.value = false
    }

    private fun storeCoinsLocally(list: List<Coin>): Job = viewModelScope.launch {
        try {
            coinRepository.deleteAll()
            var j = 0

            while (j < list.size) {
                list[j].price = list[j].quote?.brl?.price
                list[j].percent_change_1h = list[j].quote?.brl?.percent_change_1h
                list[j].percent_change_24h = list[j].quote?.brl?.percent_change_24h
                list[j].percent_change_7d = list[j].quote?.brl?.percent_change_7d
                j++
            }

            val result = coinRepository.insertAll(*list.toTypedArray())

            var i = 0
            while (i < list.size) {

                list[i].coin_id = result[i].toInt()
                i++
            }
            coinsRetrieved(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}