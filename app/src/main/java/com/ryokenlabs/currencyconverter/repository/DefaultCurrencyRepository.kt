package com.ryokenlabs.currencyconverter.repository

import android.util.Log
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.model.api.CurrencyInterface
import com.ryokenlabs.util.Resource
import javax.inject.Inject

class DefaultCurrencyRepository @Inject constructor(
    private val currencyAPI: CurrencyInterface
) : CurrencyRepository{
    override suspend fun getCurrenciesRate(currenciesQuery: String): Resource<Rates> {
        return try {

            Log.e("TAG", "getCurrenciesRate: before" )

            val response = currencyAPI.requestRates(currenciesQuery)
            Log.e("TAG", "getCurrenciesRate: ${response}", )

            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Log.e("TAG", "getCurrenciesRate: after 1" )

                Resource.error("An unknown error occurred", null)
            }

        } catch (e : Exception) {
            Log.e("TAG", "getCurrenciesRate: ${e.message}" )

            Resource.error("Unknown error", null)
        }
    }
}