package com.ryokenlabs.currencyconverter.repository

import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.util.Resource

interface CurrencyRepository {
    suspend fun getCurrencyList() : Resource<Currencies>
    suspend fun getCurrenciesRate(currenciesQuery : String) : Resource<Rates>

}