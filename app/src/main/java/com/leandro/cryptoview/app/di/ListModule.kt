package com.leandro.cryptoview.app.di

import android.app.Application
import androidx.room.Room
import com.leandro.cryptoview.model.repository.CoinRepository
import com.leandro.cryptoview.model.AppDatabase
import com.leandro.cryptoview.model.dao.CoinDao
import com.leandro.cryptoview.utils.DATABASE_NAME
import com.leandro.cryptoview.viewmodel.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    //factory { ListViewModel(get()) }
    factory {
        repositoryModule
    }
    viewModel{
        ListViewModel(get())
    }
}

val repositoryModule = module {
    fun coinRepository(dao: CoinDao): CoinRepository {
        return CoinRepository(dao)
    }

    factory { coinRepository(get()) }
}

val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME)
            .build()
    }
}
