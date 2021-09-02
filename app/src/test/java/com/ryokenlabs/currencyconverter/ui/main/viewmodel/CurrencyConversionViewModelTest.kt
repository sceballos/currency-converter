package com.ryokenlabs.currencyconverter.ui.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ryokenlabs.currencyconverter.MainCoroutineRule
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
    fun `test fake rates call, returns error`() {
        viewModel.getRates("")

        val value = viewModel.rates.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
}