package com.ryokenlabs.currencyconverter.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.*
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

    private val _rates = MutableLiveData<Event<Resource<Rates>>>()
    val rates : LiveData<Event<Resource<Rates>>> = _rates

    fun getRates(query : String) {
        Log.e(TAG, "getRates: ", )
        _rates.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = currencyRepository.getCurrenciesRate(query)
            Log.e("TAG", "getRates: ${response.data?.terms}")
            _rates.value = Event(response)
        }
    }


}