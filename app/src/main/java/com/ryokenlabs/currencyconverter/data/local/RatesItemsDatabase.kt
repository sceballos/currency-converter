package com.ryokenlabs.currencyconverter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RatesItem::class],
    version = 1
)
abstract class RatesItemsDatabase : RoomDatabase() {
    abstract fun ratesDao(): RatesDao
}