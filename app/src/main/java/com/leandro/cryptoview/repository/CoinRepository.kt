package com.leandro.cryptoview.repository

import com.leandro.cryptoview.service.CoinDao
import com.leandro.cryptoview.model.Coin

class CoinRepository(private val coinDao: CoinDao) {
    suspend fun getAll() = coinDao.getAll()

    suspend fun getOne(coin_id: Int) = coinDao.getOne(coin_id)

    suspend fun deleteAll() = coinDao.deleteAll()

    suspend fun insertAll(vararg coins: Coin): List<Long> =  coinDao.insetAll(*coins)

    companion object{
        @Volatile private var instance : CoinRepository? = null

        fun getInstance(coinDao: CoinDao) =
            instance ?: synchronized(this) {
                instance
                    ?: CoinRepository(coinDao).also { instance = it }
            }
    }

}