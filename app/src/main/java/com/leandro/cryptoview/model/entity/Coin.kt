package com.leandro.cryptoview.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

@Entity
data class Coin(

    @ColumnInfo(name = "id")
     @SerializedName("id")
     var id: String? = null,

    @ColumnInfo(name = "name")
     @SerializedName("name")
     var name: String? = null,

    @ColumnInfo(name = "symbol")
     @SerializedName("symbol")
     var symbol: String? = null,

    @ColumnInfo(name = "price")
     var price: Double? = null,

    @ColumnInfo(name = "percent_change_1h")
     var percent_change_1h: Double? = null,


    @ColumnInfo(name = "percent_change_24h")
     var percent_change_24h: Double? = null,

    @ColumnInfo(name = "percent_change_7d")
     var percent_change_7d: Double? = null,

    @ColumnInfo(name = "max_supply")
     @SerializedName("max_supply")
     var max_supply: String? = null,

    @ColumnInfo(name = "volume_24h")
    var volume_24h: String? = null,

    @ColumnInfo(name = "circulating_supply")
     @SerializedName("circulating_supply")
     var circulating_supply: String? = null,

    @ColumnInfo(name = "market_cap")
    @SerializedName("market_cap")
    var market_cap: String? = null,

    @Ignore
     @SerializedName("quote")
     var quote: QuoteModel? = null,


    ) {
         @PrimaryKey(autoGenerate = true)
         var coin_id: Int = 0
}