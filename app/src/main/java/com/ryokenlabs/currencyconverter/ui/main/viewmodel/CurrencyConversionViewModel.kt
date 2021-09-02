package com.ryokenlabs.currencyconverter.ui.main.viewmodel

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

    private val _rates = MutableLiveData<Event<Resource<Rates>>>()
    val rates : LiveData<Event<Resource<Rates>>> = _rates

    fun getRates(query : String) {
        _rates.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = currencyRepository.getCurrenciesRate(query)
            _rates.value = Event(response)
        }
    }


}