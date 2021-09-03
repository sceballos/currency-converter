package com.ryokenlabs.currencyconverter.data.local.currencies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.ryokenlabs.currencyconverter.data.local.currencies.CurrenciesDBConstants.SINGLE_CURRENCIES_ID
import com.ryokenlabs.currencyconverter.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrenciesDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: CurrenciesItemDatabase
    private lateinit var dao: CurrenciesDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrenciesItemDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.currenciesDao()
    }

    @After
    fun finish() {
        database.close()
    }

    @Test
    fun insertCurrenciesItem() = runBlockingTest {
        val currenciesItem = CurrenciesItem(true, "", "", mapOf(), id = SINGLE_CURRENCIES_ID)
        dao.insertCurrenciesItem(currenciesItem)

        val result = dao.observeRatesItem().getOrAwaitValue()

        assertThat(result).isEqualTo(currenciesItem)
    }

    @Test
    fun deleteCurrenciesItem() = runBlockingTest {
        val currenciesItem = CurrenciesItem(true, "", "", mapOf(), id = SINGLE_CURRENCIES_ID)
        dao.deleteCurrenciesItem(currenciesItem)

        val result = dao.observeRatesItem().getOrAwaitValue()

        assertThat(result).isNull()
    }

    @Test
    fun replaceExistingItem() = runBlockingTest {
        val currenciesItem = CurrenciesItem(true, "", "", mapOf(), id = SINGLE_CURRENCIES_ID)
        dao.insertCurrenciesItem(currenciesItem)
        val currenciesItem2 = CurrenciesItem(true, "", "", mapOf(), id = SINGLE_CURRENCIES_ID)
        dao.insertCurrenciesItem(currenciesItem2)

        val result = dao.observeRatesItem().getOrAwaitValue()

        assertThat(result).isEqualTo(currenciesItem2)
    }
}