package com.ryokenlabs.currencyconverter.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.repository.CurrencyRepository
import com.ryokenlabs.util.Event
import com.ryokenlabs.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    val TAG = "CurrencyCViewModel"

    private val _currencies = MutableLiveData<Event<Resource<Currencies>>>()
    val currencies : LiveData<Event<Resource<Currencies>>> = _currencies

    private val _rates = MutableLiveData<Event<Resource<Rates>>>()
    val rates : LiveData<Event<Resource<Rates>>> = _rates

    fun getCurrencies() {
        _currencies.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = currencyRepository.getCurrencyList()
            _currencies.value = Event(response)
        }
    }

    fun getRates(query : String) {
        _rates.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = currencyRepository.getCurrenciesRate(query)
            _rates.value = Event(response)
        }
    }

    fun convertCurrency(amount : Double, from : Double, to : Double) : Double {
        if (isNegative(amount) || isNegative(from) || isNegative(to)) {
            return -1.0
        }
        return (to/from) * amount
    }

    private fun isNegative(value : Double) : Boolean {
        return value < 0
    }
}