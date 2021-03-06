package com.ryokenlabs.currencyconverter.ui.main.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItem
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItemsDBConstants.CACHE_MAX_TIME_SEC
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

    var currenciesList = listOf<Pair<String, String>>()

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

    fun convertCurrenciesMapToList(map : Map<String, String>) : List<Pair<String, String>>{
        return map.toList()
    }

    fun setAvailableCurrencies(data : List<Pair<String, String>>) {
        currenciesList = data
    }

    fun getRates() {
        requestNetworkRates()
    }

    fun updateIfExpired() : Boolean {
        return if (upToDateRates.value?.timestamp?.let { cacheTimestamp ->
                areRatesExpired(
                    System.currentTimeMillis() / 1000,
                    cacheTimestamp
                )
            } == true) {
            requestNetworkRates()
            true
        } else {
            false
        }
    }

    fun areRatesExpired(currentTimestamp : Long, cachedRatesTimestamp : Long) : Boolean {
        val difference = (currentTimestamp - cachedRatesTimestamp)
        return difference >= CACHE_MAX_TIME_SEC
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
                    //Log.e("TAG", "requestNetworkRates: updating db rates ")
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
    }

    fun setRates(rate : Double) {
        _selectedRate.value = rate
    }

    fun setRateForCurrency(currencyCode: String) {
        upToDateRates.value?.quotes?.let {
            val rate = it["USD${currencyCode}"]
            if (rate != null) {
                setRates(rate)
            }
        }
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