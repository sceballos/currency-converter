package com.ryokenlabs.currencyconverter.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRatesItem(ratesItem: RatesItem)

    @Delete
    suspend fun deleteRatesItem(ratesItem: RatesItem)

    @Query("SELECT * FROM rates_items")
    fun observeAllRatesItems() : LiveData<List<RatesItem>>
}