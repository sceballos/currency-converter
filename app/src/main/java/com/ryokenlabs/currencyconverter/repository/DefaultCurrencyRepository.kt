package com.ryokenlabs.currencyconverter.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.data.local.currencies.CurrenciesDBConstants.SINGLE_CURRENCIES_ID
import com.ryokenlabs.currencyconverter.data.local.currencies.CurrenciesDao
import com.ryokenlabs.currencyconverter.data.local.currencies.CurrenciesItem
import com.ryokenlabs.currencyconverter.data.local.rates.RatesDao
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItem
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItemsDBConstants.SINGLE_RATES_ID
import com.ryokenlabs.currencyconverter.model.api.CurrencyInterface
import com.ryokenlabs.util.Resource
import kotlinx.coroutines.delay
import javax.inject.Inject

class DefaultCurrencyRepository @Inject constructor(
    private val currenciesDao: CurrenciesDao,
    private val ratesDao : RatesDao,
    private val currencyAPI: CurrencyInterface
) : CurrencyRepository{
    override suspend fun getCurrenciesRates(currenciesQuery: String, delayTime : Long): Resource<Rates> {
        delay(delayTime) //since free api has limited rates
        return try {
            val response = currencyAPI.requestRates(currenciesQuery)

            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }

        } catch (e : Exception) {
            Resource.error("Unknown error", null)
        }
    }

    override suspend fun insertCacheCurrencyRates(networkRates: Resource<Rates>) {
        if (networkRates.data != null && networkRates.data.success) {
            networkRates.data.apply {
                ratesDao.insertRatesItem(
                    RatesItem(this.success, this.terms, this.privacy, System.currentTimeMillis() / 1000, this.source,
                        this.quotes, id = SINGLE_RATES_ID)
                )
            }
        }
    }

    override suspend fun deleteCacheCurrencyRates(ratesItem: RatesItem) {
        ratesDao.deleteRatesItem(ratesItem)
    }

    override fun getCacheCurrenciesRates(): LiveData<RatesItem> {
        return ratesDao.observeRatesItem(id = SINGLE_RATES_ID)
    }


    override suspend fun getCurrencies(): Resource<Currencies> {
        return try {
            val response = currencyAPI.requestCurrencyList("")

            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }

        } catch (e : Exception) {
            Resource.error("Unknown error", null)
        }
    }

    override suspend fun insertCacheCurrencies(newCurrencies: Resource<Currencies>) {
        if (newCurrencies.data != null && newCurrencies.data.success) {
            newCurrencies.data.apply {
                currenciesDao.insertCurrenciesItem(
                    CurrenciesItem(this.success, this.terms, this.privacy,
                        this.currencies, SINGLE_CURRENCIES_ID)
                )
            }
        }
    }

    override suspend fun deleteCacheCurrencies(currenciesItem: CurrenciesItem) {
        currenciesDao.deleteCurrenciesItem(currenciesItem)
    }

    override fun getCacheCurrencies(): LiveData<CurrenciesItem> {
        return currenciesDao.observeRatesItem()
    }

}