package com.ryokenlabs.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.data.local.currencies.CurrenciesItem
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItem
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItemsDBConstants.SINGLE_RATES_ID
import com.ryokenlabs.currencyconverter.repository.CurrencyRepository
import com.ryokenlabs.util.Resource

class FakeCurrencyRepository : CurrencyRepository {

    private var currencies : CurrenciesItem? = null
    private var rates : RatesItem? = null

    private var returnError = false

    private val observableCurrencies = MutableLiveData<CurrenciesItem>(currencies)
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

    override suspend fun insertCacheCurrencies(newCurrencies: Resource<Currencies>) {
        newCurrencies.data?.let {
            val newValue = CurrenciesItem(it.success, it.terms, it.privacy, it.currencies, id = SINGLE_RATES_ID)
            currencies = newValue
            refreshLiveData()
        }
    }

    override suspend fun deleteCacheCurrencies(currenciesItem: CurrenciesItem) {
        currencies = null
    }

    override fun getCacheCurrencies(): LiveData<CurrenciesItem> {
        return observableCurrencies
    }

    override suspend fun getCurrenciesRates(currenciesQuery: String, delayTime: Long): Resource<Rates> {
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
            val newValue = RatesItem(it.success, it.terms, it.privacy, it.timestamp, it.source, it.quotes, id = SINGLE_RATES_ID)
            rates = newValue
            refreshLiveData()
        }
    }

    override suspend fun deleteCacheCurrencyRates(ratesItem: RatesItem) {
        rates = null
    }

    override fun getCacheCurrenciesRates(): LiveData<RatesItem> {
        return observableCurrentRates
    }
}