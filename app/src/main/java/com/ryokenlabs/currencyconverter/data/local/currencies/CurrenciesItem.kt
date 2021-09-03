package com.ryokenlabs.currencyconverter.data.local.currencies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies_items")
data class CurrenciesItem(
    var success : Boolean,
    var terms : String,
    var privacy : String,
    var currencies : Map<String,String>,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)