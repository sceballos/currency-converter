package com.ryokenlabs.currencyconverter.repository

import androidx.lifecycle.LiveData
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.data.local.rates.RatesDao
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItem
import com.ryokenlabs.currencyconverter.model.api.CurrencyInterface
import com.ryokenlabs.util.Resource
import javax.inject.Inject

class DefaultCurrencyRepository @Inject constructor(
    private val ratesDao : RatesDao,
    private val currencyAPI: CurrencyInterface
) : CurrencyRepository{
    override suspend fun getCurrenciesRates(currenciesQuery: String): Resource<Rates> {
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

    override suspend fun insertCacheCurrencyRates(networkRates: Resource<Rates>) {
        if (networkRates.data != null) {
            networkRates.data.apply {
                ratesDao.insertRatesItem(
                    RatesItem(
                    this.success,
                    this.terms,
                    this.privacy,
                    this.timestamp,
                    this.source,
                    this.quotes)
                )
            }
        }
    }

    override suspend fun deleteCacheCurrencyRates(ratesItem: RatesItem) {
        ratesDao.deleteRatesItem(ratesItem)
    }

    override suspend fun getCacheCurrenciesRates(): LiveData<RatesItem> {
        return ratesDao.observeAllRatesItems()
    }

    override suspend fun setCurrencies(newCurrencies: Resource<Currencies>) {
        TODO("Not yet implemented")
    }



}