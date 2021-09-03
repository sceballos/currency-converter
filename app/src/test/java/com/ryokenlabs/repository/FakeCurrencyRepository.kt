package com.ryokenlabs.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.data.local.RatesItem
import com.ryokenlabs.currencyconverter.repository.CurrencyRepository
import com.ryokenlabs.util.Resource

class FakeCurrencyRepository : CurrencyRepository {

    private var currencies : Currencies? = null
    private var rates : RatesItem? = null

    private var returnError = false

    private val observableCurrencies = MutableLiveData(currencies)
    private val observableCurrentRates = MutableLiveData<RatesItem>(rates)

    private fun setShouldReturnError(value : Boolean) {
        returnError = value
    }

    private fun refreshLiveData() {
        observableCurrentRates.postValue(rates)
        observableCurrencies.postValue(currencies)
    }

    override suspend fun getCurrencies(): Resource<Currencies> {
        return if (returnError) {
            Resource.error("Error", null)
        } else {
            Resource.success(Currencies(true,
                "https://currencylayer.com/terms",
                "https://currencylayer.com/privacy",
                mapOf<String, String>("AUS" to "Australian Dollar")))
        }
    }

    override suspend fun getCurrenciesRates(currenciesQuery: String): Resource<Rates> {
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

    override suspend fun insertCacheCurrencyRates(networkRates: Resource<Rates>) {
        networkRates.data?.let {
            val newValue = RatesItem(it.success, it.terms, it.privacy, it.timestamp, it.source, it.quotes, id = 777)
            rates = newValue
            refreshLiveData()
        }
    }

    override suspend fun deleteCacheCurrencyRates(ratesItem: RatesItem) {
        rates = null
    }

    override suspend fun getCacheCurrenciesRates(): LiveData<RatesItem> {
        return observableCurrentRates
    }

    override suspend fun setCurrencies(newCurrencies: Resource<Currencies>) {
        if (newCurrencies.data != null) {
            currencies = newCurrencies.data
            refreshLiveData()
        }
    }
}