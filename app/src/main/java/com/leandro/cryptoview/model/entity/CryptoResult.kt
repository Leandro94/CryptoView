package com.leandro.cryptoview.model.entity

import com.google.gson.annotations.SerializedName


data class CryptoResult (
    @SerializedName("data")
    val data: List<Coin>? = null
)
