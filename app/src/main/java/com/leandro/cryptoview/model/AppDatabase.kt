package com.leandro.cryptoview.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leandro.cryptoview.model.dao.CoinDao
import com.leandro.cryptoview.model.entity.Coin
import com.leandro.cryptoview.utils.DATABASE_NAME

@Database(entities = arrayOf(Coin::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun coinDao(): CoinDao

    companion object{
        @Volatile private var instance: AppDatabase? =null
        private val LOCK = Any()

        fun getInstance(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, DATABASE_NAME
        ).build()
    }

}