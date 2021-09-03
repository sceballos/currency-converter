package com.ryokenlabs.currencyconverter.data.local.rates

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ryokenlabs.currencyconverter.data.local.rates.RatesItemsDBConstants.SINGLE_RATES_ID

@Dao
interface RatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRatesItem(ratesItem: RatesItem)

    @Delete
    suspend fun deleteRatesItem(ratesItem: RatesItem)

    @Query("SELECT * FROM rates_items where id = :id")
    fun observeAllRatesItems(id: Int = SINGLE_RATES_ID) : LiveData<RatesItem>
}