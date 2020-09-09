package com.leandro.cryptoview.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.leandro.cryptoview.model.Coin

@Dao
interface CoinDao {
    @Insert
    suspend fun insetAll(vararg coins: Coin): List<Long>

    @Query("SELECT * FROM Coin")
    suspend fun  getAll(): List<Coin>

    @Query("SELECT * FROM Coin WHERE coin_id = :idCoin")
    suspend fun getOne(idCoin: Int): Coin

    @Query("DELETE FROM Coin")
    suspend fun deleteAll()

}
