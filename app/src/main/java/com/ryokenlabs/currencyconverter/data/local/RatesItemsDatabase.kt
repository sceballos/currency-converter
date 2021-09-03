package com.ryokenlabs.currencyconverter.data.local

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