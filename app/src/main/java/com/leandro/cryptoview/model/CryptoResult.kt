package com.leandro.cryptoview.model

import com.google.gson.annotations.SerializedName


data class CryptoResult (
    @SerializedName("data")
    val data: List<Coin>? = null
)
