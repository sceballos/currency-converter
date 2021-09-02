package com.ryokenlabs.currencyconverter.data.api

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currencies(
    @SerializedName("success")
    @Expose
    var success : Boolean,

    @Expose
    @SerializedName("terms")
    var terms : String,

    @SerializedName("privacy")
    @Expose
    var privacy : String,

    @SerializedName("currencies")
    @Expose
    var currencies : Map<String,String>
    ) : Parcelable