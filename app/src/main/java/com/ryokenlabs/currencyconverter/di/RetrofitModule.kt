package com.ryokenlabs.currencyconverter.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ryokenlabs.currencyconverter.model.api.CurrencyInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    const val apiKey = "e33391174e5f75a4d858f559ab08179b"
    private const val baseUrl = "http://api.currencylayer.com"
    @Singleton
    @Provides
    fun provideGsonBuilder() : Gson {
        return GsonBuilder()
            .setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson : Gson) : Retrofit.Builder {
        return Retrofit.Builder().
        baseUrl(baseUrl).
        addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideContentService(retrofit: Retrofit.Builder) : CurrencyInterface {
        return retrofit.build().create(CurrencyInterface::class.java)
    }
}