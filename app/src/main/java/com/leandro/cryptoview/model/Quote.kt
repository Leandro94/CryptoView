package com.leandro.cryptoview.model

import com.google.gson.annotations.SerializedName

data class QuoteModel (
    @SerializedName("BRL")
    val brl: BRL? = null
)
data class BRL(
    @SerializedName("price")
    var price: String? = null,

    @SerializedName("percent_change_1h")
    var percent_change_1h: String? = null,

    @SerializedName("volume_24h")
    var volume_24h: String? = null,

    @SerializedName("percent_change_24h")
    var percent_change_24h: String? = null,

    @SerializedName("percent_change_7d")
    var percent_change_7d: String? = null
)