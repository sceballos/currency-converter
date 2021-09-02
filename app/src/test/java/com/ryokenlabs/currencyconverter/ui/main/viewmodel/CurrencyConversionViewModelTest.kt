package com.ryokenlabs.currencyconverter.ui.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ryokenlabs.currencyconverter.MainCoroutineRule
import com.ryokenlabs.currencyconverter.data.api.Currencies
import com.ryokenlabs.currencyconverter.data.api.Rates
import com.ryokenlabs.currencyconverter.getOrAwaitValueTest
import com.ryokenlabs.repository.FakeCurrencyRepository
import com.ryokenlabs.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyConversionViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CurrencyConversionViewModel

    @Before
    fun setup() {
        viewModel = CurrencyConversionViewModel(FakeCurrencyRepository())
    }

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

    @Test
    fun `test fake getRates() request, returns success`() {
        viewModel.getRates("")
        val value = viewModel.rates.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
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
}