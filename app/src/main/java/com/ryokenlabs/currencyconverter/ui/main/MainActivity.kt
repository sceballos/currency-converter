package com.ryokenlabs.currencyconverter.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.ryokenlabs.currencyconverter.R
import com.ryokenlabs.currencyconverter.databinding.ActivityMainBinding
import com.ryokenlabs.currencyconverter.ui.main.viewmodel.CurrencyConversionViewModel
import com.ryokenlabs.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CurrencyConversionViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.upToDateCurrencies.observe(this, { updatedValue ->
            Log.e("upToDateCurrencies", "${updatedValue}")
            if (updatedValue == null) {
                Log.e("upToDateCurrencies", "requesting currencies")
                viewModel.currenciesCacheWasNull = true
                viewModel.getCurrencies()
            } else {
                Log.e("upToDateCurrencies", "Building Currencies dropdown & registering rates handling")
                viewModel.getRates("")
            }
        })

        viewModel.upToDateRates.observe(this, { updatedRates ->
                Log.e("upToDateRates", "${updatedRates}")
                if (updatedRates == null) {
                    Log.e("upToDateRates", "no cache of rates yet, waiting for currencies first")
                } else {
                    Log.e("upToDateRates", "Checking if time of this rates is older than 30 minutes")
                    Log.e("upToDateRates", "Building Change Rates Recyclerview")
                }
        })


        viewModel.currencies.observe(this, { event ->
            when (event.getContentIfNotHandled()?.status) {
                Status.SUCCESS -> {
                    Log.e("currencies", "onCreate: SUCCESS :::: ${event.peekContent()}")
                    viewModel.updateCachedCurrencies(event.peekContent())
                }
                Status.ERROR -> {
                    Log.e("currencies", "onCreate: ERROR")
                }
                Status.LOADING -> {
                    Log.e("currencies", "onCreate: LOADING")
                }
            }
        })

        viewModel.rates.observe(this, { event ->
            when (event.getContentIfNotHandled()?.status) {
                Status.SUCCESS -> {
                    Log.e("TAG", "onCreate:rates SUCCESS :::: ${event.peekContent()}")
                    viewModel.updateCachedRates(event.peekContent())
                }
                Status.ERROR -> {
                    Log.e("TAG", "onCreate:rates ERROR")
                }
                Status.LOADING -> {
                    Log.e("TAG", "onCreate:rates LOADING")
                }
            }
        })
    }
}