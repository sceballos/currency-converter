package com.ryokenlabs.currencyconverter.ui.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ryokenlabs.currencyconverter.MainCoroutineRule
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.data.local.currencies.CurrenciesItem
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItem
import com.ryokenlabs.currencyconverter.getOrAwaitValueTest
import com.ryokenlabs.repository.FakeCurrencyRepository
import com.ryokenlabs.util.Resource
import com.ryokenlabs.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class CurrencyConversionViewModelTest {
    private val conversionTolerance = 0.99
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CurrencyConversionViewModel
    private var newRates: Resource<Rates>? = null
    private var newCurrencies: Resource<Currencies>? = null
    @Before
    fun setup() {
        viewModel = CurrencyConversionViewModel(FakeCurrencyRepository())

        viewModel.getRates("")
        newRates = viewModel.rates.getOrAwaitValueTest().getContentIfNotHandled()

        viewModel.getCurrencies()
        newCurrencies = viewModel.currencies.getOrAwaitValueTest().getContentIfNotHandled()
    }

    /************************************************************
     *
     * FAKE API CALLS TEST CASES
     *
     *************************************************************/

    @Test
    fun `test fake getCurrencies() request, returns success`() {
        viewModel.getCurrencies()
        val value = viewModel.currencies.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `test fake getCurrencies() request, returns a Rates object`() {
        viewModel.getCurrencies()
        val value = viewModel.currencies.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.data).isInstanceOf(Currencies::class.java)
    }

    @Test
    fun `test fake getCurrencies() request, returns not null`() {
        viewModel.getCurrencies()
        val value = viewModel.currencies.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.data).isNotNull()
    }

    @Ignore("Run this test case by using setShouldReturnError(true) function inside FakeCurrencyRepository class")
    @Test
    fun `test fake getCurrencies() request, returns null`() {
        viewModel.getCurrencies()
        val value = viewModel.currencies.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.data).isNull()
    }


    @Test
    fun `test fake updateCachedCurrencies() request, returns a cached element after network response`() {
        viewModel.updateCachedCurrencies(newCurrencies!!)
        val value = viewModel.upToDateCurrencies.getOrAwaitValueTest()
        assertThat(value).isInstanceOf(CurrenciesItem::class.java)
    }

    @Test
    fun `test fake getRates() request, returns a Rates object`() {
        viewModel.getRates("")
        val value = viewModel.rates.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.data).isInstanceOf(Rates::class.java)
    }

    @Test
    fun `test fake getRates() request, returns not null`() {
        viewModel.getRates("")
        val value = viewModel.rates.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.data).isNotNull()
    }

    @Ignore("Run this test case by using setShouldReturnError(true) function inside FakeCurrencyRepository class")
    @Test
    fun `test fake getRates() request, returns null`() {
        viewModel.getRates("")
        val value = viewModel.rates.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.data).isNull()
    }

    @Test
    fun `test fake updateCachedRates() request, returns a cached element after network response`() {
        viewModel.updateCachedRates(newRates!!)
        val value = viewModel.upToDateRates.getOrAwaitValueTest()
        assertThat(value).isInstanceOf(RatesItem::class.java)
    }


    /************************************************************
     *
     * CONVERSION TEST CASES
     *
     *************************************************************/

    @Test
    fun `test ARS to JYN conversion`() {
        val amount = 1.0
        val ars = 97.716706
        val yen = 109.997044
        val result = viewModel.convertCurrency(amount, ars, yen)
        assertThat(result).isWithin(conversionTolerance).of(1.12)
    }

    @Test
    fun `test big floating point ARS amount to JYN conversion`() {
        val amount = 49123784.52
        val ars = 97.716706
        val yen = 109.997044
        val result = viewModel.convertCurrency(amount, ars, yen)
        assertThat(result).isWithin(conversionTolerance).of(55297311.06)
    }

    @Test
    fun `test EUR to JYN conversion`() {
        val amount = 1.0
        val eur = 0.843675
        val yen = 109.997044
        val result = viewModel.convertCurrency(amount, eur, yen)
        assertThat(result).isWithin(conversionTolerance).of(130.68)
    }

    @Test
    fun `test USD to JYN conversion`() {
        val amount = 5605.00
        val usd = 1.0
        val yen = 109.997044
        val result = viewModel.convertCurrency(amount, usd, yen)
        assertThat(result).isWithin(conversionTolerance).of(616533.43)
    }

    @Test
    fun `test bigger amount floating point USD amount to JYN conversion`() {
        val amount = 50.45
        val usd = 1.0
        val yen = 109.997044
        val result = viewModel.convertCurrency(amount, usd, yen)
        assertThat(result).isWithin(conversionTolerance).of(5549.35)
    }

    @Test
    fun `test EUR to USD conversion`() {
        val amount = 1.0
        val eur = 0.843675
        val usd = 1.0
        val result = viewModel.convertCurrency(amount, eur, usd)
        assertThat(result).isWithin(conversionTolerance).of(1.19)
    }


    @Test
    fun `test BBD to BTN conversion`() {
        val amount = 1.0
        val bbd = 2.014986
        val btn = 72.915634
        val result = viewModel.convertCurrency(amount, bbd, btn)
        assertThat(result).isWithin(conversionTolerance).of(36.17)
    }

    @Test
    fun `test attempt to convert negative amount`() {
        val amount = -1.0
        val from = 97.716706
        val to = 109.997044
        val result = viewModel.convertCurrency(amount, from, to)
        assertThat(result).isEqualTo(-1.0)
    }

    @Test
    fun `test attempt to convert negative from value`() {
        val amount = 1.0
        val from = -97.716706
        val to = 109.997044
        val result = viewModel.convertCurrency(amount, from, to)
        assertThat(result).isEqualTo(-1.0)
    }

    @Test
    fun `test attempt to convert negative to value`() {
        val amount = 1.0
        val from = 97.716706
        val to = -109.997044
        val result = viewModel.convertCurrency(amount, from, to)
        assertThat(result).isEqualTo(-1.0)
    }
}