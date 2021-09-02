package com.ryokenlabs.currencyconverter.repository

import android.util.Log
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.model.api.CurrencyInterface
import com.ryokenlabs.util.Resource
import javax.inject.Inject

class DefaultCurrencyRepository @Inject constructor(
    private val currencyAPI: CurrencyInterface
) : CurrencyRepository{
    override suspend fun getCurrenciesRate(currenciesQuery: String): Resource<Rates> {
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

    override suspend fun getCurrencyList(): Resource<Currencies> {
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
}