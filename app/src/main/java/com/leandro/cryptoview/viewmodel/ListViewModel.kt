package com.leandro.cryptoview.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandro.cryptoview.model.entity.Coin
import com.leandro.cryptoview.service.CoinsApiService
import com.leandro.cryptoview.model.entity.CryptoResult
import com.leandro.cryptoview.model.repository.CoinRepository
import com.leandro.cryptoview.utils.formatterToDecimal
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
                        coinsLoadError.let {
                            if(it.value==true){
                                coinsLoadError.value = false
                            }
                        }
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
    }

    private fun coinsRetrieved(coinsList: List<Coin>) {
        coins.value = coinsList
        loading.value = false
    }

    private fun storeCoinsLocally(list: List<Coin>): Job = viewModelScope.launch {
        try {
            coinRepository.deleteAll()
            var j = 0

            while (j < list.size) {
                list[j].price = formatterToDecimal(list[j].quote?.brl?.price.toString(),2).toDouble()
                list[j].percent_change_1h = formatterToDecimal(list[j].quote?.brl?.percent_change_1h.toString(),2).toDouble()
                list[j].percent_change_24h = formatterToDecimal(list[j].quote?.brl?.percent_change_24h.toString(),2).toDouble()
                list[j].percent_change_7d = formatterToDecimal(list[j].quote?.brl?.percent_change_7d.toString(),2).toDouble()
                list[j].volume_24h = formatterToDecimal(list[j].quote?.brl?.volume_24h.toString(),2).toString()
                list[j].market_cap = formatterToDecimal(list[j].quote?.brl?.market_cap.toString(),2).toString()
                list[j].circulating_supply = formatterToDecimal(list[j].circulating_supply.toString(), 2).toString()
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