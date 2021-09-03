package com.ryokenlabs.currencyconverter.repository

import androidx.lifecycle.LiveData
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItem
import com.ryokenlabs.util.Resource

interface CurrencyRepository {
    suspend fun getCurrencies() : Resource<Currencies>
    suspend fun getCurrenciesRates(currenciesQuery : String) : Resource<Rates>
    suspend fun insertCacheCurrencyRates(networkRates : Resource<Rates>)
    suspend fun deleteCacheCurrencyRates(ratesItem: RatesItem)
    fun getCacheCurrenciesRates() : LiveData<RatesItem>
    suspend fun setCurrencies(newCurrencies: Resource<Currencies>)
}