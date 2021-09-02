package com.ryokenlabs.currencyconverter.model.api

import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.di.RetrofitModule
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyInterface {
    @GET("/list?access_key=${RetrofitModule.apiKey}")
    suspend fun requestCurrencyList(@Query("query")  query: String?) : Response<Currencies>
    @GET("/live?access_key=${RetrofitModule.apiKey}")
    suspend fun requestRates(@Query("query")  query: String?) : Response<Rates>
}