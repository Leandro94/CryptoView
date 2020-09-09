package com.leandro.cryptoview.service

import com.leandro.cryptoview.model.CryptoResult
import io.reactivex.Single
import retrofit2.http.GET

interface CoinsApi {
    @GET("/v1/cryptocurrency/listings/latest?limit=30&convert=BRL")
    fun getCoins(): Single<CryptoResult>
}