package com.ryokenlabs.currencyconverter.data.api

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rates(
    @SerializedName("success")
    @Expose
    var success : String,

    @Expose
    @SerializedName("terms")
    var terms : String,

    @SerializedName("privacy")
    @Expose
    var privacy : String,

    @SerializedName("timestamp")
    @Expose
    var timestamp : String,

    @SerializedName("source")
    @Expose
    var source : String,

    @SerializedName("quotes")
    @Expose
    var quotes : Map<String,String>
    ) : Parcelable