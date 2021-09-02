package com.ryokenlabs.repository

import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.repository.CurrencyRepository
import com.ryokenlabs.util.Resource

class FakeCurrencyRepository : CurrencyRepository {
    override suspend fun getCurrencyList(): Resource<Currencies> {
        val returnError = false
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(Currencies(true,
                "https://currencylayer.com/terms",
                "https://currencylayer.com/privacy",
                mapOf<String, String>("AUS" to "Australian Dollar")))
        }
    }

    override suspend fun getCurrenciesRate(currenciesQuery: String): Resource<Rates> {
        val returnError = false
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(Rates(true,
                "https://currencylayer.com/terms",
                "https://currencylayer.com/privacy",
                1630579384,
                "USD",
                mapOf<String, Double>("AUS" to 13.4)))
        }
    }
}