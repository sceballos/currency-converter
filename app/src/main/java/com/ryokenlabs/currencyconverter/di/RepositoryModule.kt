package com.ryokenlabs.currencyconverter.di

import com.ryokenlabs.currencyconverter.data.local.rates.RatesDao
import com.ryokenlabs.currencyconverter.model.api.CurrencyInterface
import com.ryokenlabs.currencyconverter.repository.CurrencyRepository
import com.ryokenlabs.currencyconverter.repository.DefaultCurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideDefaultCurrencyRepository(
        dao : RatesDao,
        contentRetrofit : CurrencyInterface
    ) = DefaultCurrencyRepository(dao, contentRetrofit) as CurrencyRepository
}