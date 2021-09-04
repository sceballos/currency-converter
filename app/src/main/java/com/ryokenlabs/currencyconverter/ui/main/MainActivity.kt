package com.ryokenlabs.currencyconverter.ui.main

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ryokenlabs.currencyconverter.R
import com.ryokenlabs.currencyconverter.databinding.ActivityMainBinding
import com.ryokenlabs.currencyconverter.ui.main.adapters.CurrenciesAdapter
import com.ryokenlabs.currencyconverter.ui.main.adapters.RatesAdapter
import com.ryokenlabs.currencyconverter.ui.main.viewmodel.CurrencyConversionViewModel
import com.ryokenlabs.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.concurrent.schedule

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CurrencyConversionViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding
    private lateinit var selectCurrencyDialog: Dialog
    private lateinit var ratesAdapter: RatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        ratesAdapter = RatesAdapter(emptyList(), this) { }
        setupRatesRecyclerView(ratesAdapter)

        setupCurrenciesDialog()

        subscribeObservers()

        setupAmountTextViewAfterTextChanged()
    }

    private fun setupAmountTextViewAfterTextChanged() {
        binding.amountToConvertEt.addTextChangedListener(object : TextWatcher {
            var timer = Timer()

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()
                val sleep = when (s?.length) {
                    1 -> 1000L
                    2, 3 -> 700L
                    4, 5 -> 500L
                    else -> 300L
                }
                timer = Timer()
                timer.schedule(sleep) {
                    if (s.isNullOrEmpty()) {

                    } else {
                        Log.e("TAG", "afterTextChanged2: ${s}")
                        ratesAdapter.conversionAmount = s.toString().toDouble()

                        this@MainActivity.runOnUiThread(Runnable {
                            ratesAdapter.notifyDataSetChanged()
                        })
                    }
                }
            }
        })
    }

    private fun setupRatesRecyclerView(adapter : RatesAdapter) {
        val ratesRecyclerView = binding.ratesRv
        ratesRecyclerView.adapter = adapter
        ratesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupCurrenciesDialog() {
        binding.changeCurrencyBtn.setOnClickListener {

            if (viewModel.upToDateCurrencies.value == null) {
                return@setOnClickListener
            }

            selectCurrencyDialog = Dialog(this)
            selectCurrencyDialog.setContentView(R.layout.currency_select_dialog)

            val entries: List<Pair<String, String>> =
                viewModel.upToDateCurrencies.value!!.currencies.toList()

            val currenciesAdapter = CurrenciesAdapter(
                entries,
                this
            ) { item ->
                setUserCurrency(item)
            }

            val currencyRecyclerView: RecyclerView =
                selectCurrencyDialog.findViewById(R.id.currencies_rv)
            currencyRecyclerView.adapter = currenciesAdapter
            currencyRecyclerView.layoutManager = LinearLayoutManager(this)

            selectCurrencyDialog.show()
        }
    }

    private fun setUserCurrency(currencyCode : String) {
        viewModel.setCurrency(currencyCode)
        selectCurrencyDialog.hide()
    }

    private fun doSomethingWithRatePlaceholder(currencyCode : String) {}

    private fun subscribeObservers() {

        viewModel.selectedCurrency.observe(this, { updatedValue ->
            binding.changeCurrencyBtn.text = updatedValue
        })

        viewModel.selectedRate.observe(this, { updatedValue ->
            ratesAdapter.conversionRate = updatedValue
            ratesAdapter.notifyDataSetChanged()
        })

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

                    val entries: List<Pair<String, Double>> =
                        updatedRates.quotes!!.toList() //todo : use a display list instead, do not touch reference

                    ratesAdapter = RatesAdapter(entries, this) {
                        doSomethingWithRatePlaceholder(it)
                    }
                    setupRatesRecyclerView(ratesAdapter)
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