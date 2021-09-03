package com.ryokenlabs.currencyconverter.data.local.currencies

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ryokenlabs.currencyconverter.data.local.currencies.CurrenciesDBConstants.SINGLE_CURRENCIES_ID

@Dao
interface CurrenciesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrenciesItem(currenciesItem: CurrenciesItem)

    @Delete
    suspend fun deleteCurrenciesItem(currenciesItem: CurrenciesItem)

    @Query("SELECT * FROM currencies_items where id = :id")
    fun observeRatesItem(id: Int = SINGLE_CURRENCIES_ID) : LiveData<CurrenciesItem>
}