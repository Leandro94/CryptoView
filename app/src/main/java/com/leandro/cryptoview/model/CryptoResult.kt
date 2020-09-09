package com.leandro.cryptoview.model

import com.google.gson.annotations.SerializedName
import com.leandro.cryptoview.model.entity.Coin


data class CryptoResult (
    @SerializedName("data")
    val data: List<Coin>? = null
)
