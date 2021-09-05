package com.ryokenlabs.currencyconverter.ui.main.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItem
import com.ryokenlabs.currencyconverter.repository.CurrencyRepository
import com.ryokenlabs.util.Event
import com.ryokenlabs.util.Resource
import com.ryokenlabs.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    private val _selectedCurrency = MutableLiveData<String>()
    val selectedCurrency: LiveData<String> = _selectedCurrency

    private val _selectedRate = MutableLiveData<Double>()
    val selectedRate: LiveData<Double> = _selectedRate

    private val _currencies = MutableLiveData<Event<Resource<Currencies>>>()
    val currencies: LiveData<Event<Resource<Currencies>>> = _currencies

    private val _rates = MutableLiveData<Event<Resource<Rates>>>()
    val rates: LiveData<Event<Resource<Rates>>> = _rates

    val upToDateCurrencies = currencyRepository.getCacheCurrencies()
    val upToDateRates = currencyRepository.getCacheCurrenciesRates()

    var currenciesCacheWasNull = false

    init {
        _selectedRate.value = 1.0 //default USD
    }

    fun getCurrencies() {
        _currencies.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = currencyRepository.getCurrencies()
            _currencies.value = Event(response)

            when(response.status) {
                Status.SUCCESS -> {
                    updateCachedCurrencies(response)
                }
            }
        }
    }

    fun updateCachedCurrencies(newCurrencies: Resource<Currencies>) {
        viewModelScope.launch {
            currencyRepository.insertCacheCurrencies(newCurrencies)
        }
    }

    fun getRates() {
        requestNetworkRates()
    }

    fun updateIfExpired() : Boolean {
        return if (areRatesExpired()) {
            requestNetworkRates()
            true
        } else {
            false
        }
    }

    private fun areRatesExpired() : Boolean {
        val currentTimestamp = System.currentTimeMillis() / 1000
        val cachedRatesTimestamp = upToDateRates.value?.timestamp
        val difference = (currentTimestamp - cachedRatesTimestamp!!)

        Log.e(
            "TAG",
            "getRates: current timestamp=${currentTimestamp}/ cached=${cachedRatesTimestamp}:::difference is ${difference} "
        )
        return difference > 1800
    }

    private fun requestNetworkRates() {
        _rates.value = Event(Resource.loading(null))
        viewModelScope.launch {
            var response: Resource<Rates>? = null
            if (!currenciesCacheWasNull) {
                response = currencyRepository.getCurrenciesRates("", 0)
            } else {
                response = currencyRepository.getCurrenciesRates("")
            }
            _rates.value = Event(response)
            when(response.status) {
                Status.SUCCESS -> {
                    Log.e("TAG", "requestNetworkRates: updating db rates ")
                    updateCachedRates(response)
                }
            }
        }
    }

    fun updateCachedRates(newRates: Resource<Rates>) {
        viewModelScope.launch {
            currencyRepository.insertCacheCurrencyRates(newRates)
        }
    }

    fun setCurrency(currencyCode: String) {
        _selectedCurrency.value = currencyCode
        getRateForCurrency(currencyCode)
    }

    fun getRateForCurrency(currencyCode: String) {
        val rate = upToDateRates.value?.quotes?.get("USD${currencyCode}")
        _selectedRate.value = rate!!
    }

    fun convertCurrency(amount: Double, from: Double, to: Double): Double {
        if (isNegative(amount) || isNegative(from) || isNegative(to)) {
            return -1.0
        }
        return (to / from) * amount
    }

    private fun isNegative(value: Double): Boolean {
        return value < 0
    }
}