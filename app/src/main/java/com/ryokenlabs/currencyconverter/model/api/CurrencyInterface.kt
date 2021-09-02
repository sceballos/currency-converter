package com.ryokenlabs.currencyconverter.model.api

import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.di.RetrofitModule
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyInterface {
    @GET("/live?access_key=${RetrofitModule.apiKey}&currencies=AUD,EUR,GBP,PLN")
    suspend fun requestRates(@Query("query")  query: String?) : Rates
}