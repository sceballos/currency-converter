package com.ryokenlabs.currencyconverter.data.local.rates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItemsDBConstants.SINGLE_RATES_ID
import com.ryokenlabs.currencyconverter.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RatesDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RatesItemsDatabase
    private lateinit var dao: RatesDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RatesItemsDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.ratesDao()
    }

    @After
    fun finish() {
        database.close()
    }

    @Test
    fun insertRatesItem() = runBlockingTest {
        val ratesItem = RatesItem(true, "", "", 0, "", mapOf<String, Double>("USD" to 1.0), id = SINGLE_RATES_ID)
        dao.insertRatesItem(ratesItem)

        val allRatesItems = dao.observeRatesItem().getOrAwaitValue()

        assertThat(allRatesItems).isEqualTo(ratesItem)
    }

    @Test
    fun deleteRatesItem() = runBlockingTest {
        val ratesItem = RatesItem(true, "", "", 0, "", mapOf<String, Double>("USD" to 1.0), id = SINGLE_RATES_ID)
        dao.insertRatesItem(ratesItem)
        dao.deleteRatesItem(ratesItem)

        val allRatesItems = dao.observeRatesItem().getOrAwaitValue()

        assertThat(allRatesItems).isNull()
    }

    @Test
    fun replaceExistingItem() = runBlockingTest {
        val ratesItem1 = RatesItem(true, "", "", 0, "", mapOf<String, Double>("USD" to 1.0), id = SINGLE_RATES_ID)
        val ratesItem2 = RatesItem(true, "", "", 0, "", mapOf<String, Double>("USD" to 1.0), id = SINGLE_RATES_ID)
        val ratesItem3 = RatesItem(true, "", "", 0, "", mapOf<String, Double>("USD" to 1.0), id = SINGLE_RATES_ID)

        dao.insertRatesItem(ratesItem1)
        dao.insertRatesItem(ratesItem2)
        dao.insertRatesItem(ratesItem3)

        val allRatesItems = dao.observeRatesItem().getOrAwaitValue()

        assertThat(allRatesItems).isEqualTo(ratesItem3)
    }

    @Test
    fun addSeveralRates() = runBlockingTest {
        val ratesItem1 = RatesItem(true, "", "", 0, "", mapOf<String, Double>("USD" to 1.0), id = SINGLE_RATES_ID)
        val ratesItem2 = RatesItem(true, "", "", 1, "", mapOf<String, Double>("USD" to 1.0), id = SINGLE_RATES_ID)
        val ratesItem3 = RatesItem(true, "", "", 2, "", mapOf<String, Double>("USD" to 1.0), id = SINGLE_RATES_ID)

        dao.insertRatesItem(ratesItem1)
        dao.insertRatesItem(ratesItem3)
        dao.insertRatesItem(ratesItem2)
        dao.insertRatesItem(ratesItem1)

        dao.insertRatesItem(ratesItem3)

        val allRatesItems = dao.observeRatesItem().getOrAwaitValue()

        assertThat(allRatesItems).isEqualTo(ratesItem3)
    }
}