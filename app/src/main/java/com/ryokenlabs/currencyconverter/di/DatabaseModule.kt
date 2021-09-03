package com.ryokenlabs.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.ryokenlabs.currencyconverter.data.local.currencies.CurrenciesItemDatabase
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItemsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val RATES_DATABASE_NAME = "rates_db"
    private const val CURRENCIES_DATABASE_NAME = "currencies_db"

    @Singleton
    @Provides
    fun provideRatesDatabase(
        @ApplicationContext context : Context
    ) = Room.databaseBuilder(context, RatesItemsDatabase::class.java, RATES_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideRatesDao(
        database: RatesItemsDatabase
    ) = database.ratesDao()

    @Singleton
    @Provides
    fun provideCurrenciesDatabase(
        @ApplicationContext context : Context
    ) = Room.databaseBuilder(context, CurrenciesItemDatabase::class.java, CURRENCIES_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideCurrenciesDao(
        database: CurrenciesItemDatabase
    ) = database.currenciesDao()
}