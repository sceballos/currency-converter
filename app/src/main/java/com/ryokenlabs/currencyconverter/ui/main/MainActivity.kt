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


        viewModel.upToDateRates.observe(this, {

            Log.e("TAG", "upToDateRates.observe: ${it}", )

        })

        viewModel.currencies.observe(this, { event ->
            when (event.getContentIfNotHandled()?.status) {
                Status.SUCCESS -> {
                    Log.e("TAG", "onCreate: SUCCESS", )
                }
                Status.ERROR -> {
                    Log.e("TAG", "onCreate: ERROR", )
                }
                Status.LOADING -> {
                    Log.e("TAG", "onCreate: LOADING", )
                }
            }
        })


        viewModel.rates.observe(this, { event ->
            when (event.getContentIfNotHandled()?.status) {
                Status.SUCCESS -> {
                    Log.e("TAG", "onCreate:rates SUCCESS ${event.peekContent()}", )
                    viewModel.updateCachedRates(event.peekContent())
                }
                Status.ERROR -> {
                    Log.e("TAG", "onCreate:rates ERROR", )
                }
                Status.LOADING -> {
                    Log.e("TAG", "onCreate:rates LOADING", )
                }
            }
        })

        viewModel.getRates("")

        //viewModel.getCurrencies()

    }
}