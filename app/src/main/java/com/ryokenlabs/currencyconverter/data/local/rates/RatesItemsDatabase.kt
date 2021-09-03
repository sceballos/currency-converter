package com.ryokenlabs.currencyconverter.data.local.rates

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [RatesItem::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RatesItemsDatabase : RoomDatabase() {
    abstract fun ratesDao(): RatesDao
}