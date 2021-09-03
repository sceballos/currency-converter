package com.ryokenlabs.currencyconverter.data.local.rates

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rates_items")
data class RatesItem(
    var success : Boolean,
    var terms : String,
    var privacy : String,
    var timestamp : Long,
    var source : String,
    var quotes : Map<String,Double>?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)