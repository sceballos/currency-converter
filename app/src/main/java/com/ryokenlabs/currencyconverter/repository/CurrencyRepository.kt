package com.ryokenlabs.currencyconverter.repository

import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.util.Resource

interface CurrencyRepository {
    suspend fun getCurrenciesRate(currenciesQuery : String) : Resource<Rates>
}