package com.leandro.cryptoview.service

import com.leandro.cryptoview.model.entity.CryptoResult
import com.leandro.cryptoview.utils.BASE_URL
import com.leandro.cryptoview.utils.KEY
import com.leandro.cryptoview.utils.KEY_VALUE
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class CoinsApiService {

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header(KEY, KEY_VALUE)
                return@Interceptor chain.proceed(builder.build())
            }
        )
    }.build()

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
        .create(CoinsApi::class.java)

    fun getCoins(): Single<CryptoResult> {
        return api.getCoins()
    }
}