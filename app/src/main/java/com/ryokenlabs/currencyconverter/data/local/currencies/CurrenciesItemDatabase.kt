package com.ryokenlabs.currencyconverter.data.local.currencies

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CurrenciesItem::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CurrenciesItemDatabase : RoomDatabase() {
    abstract fun currenciesDao(): CurrenciesDao
}