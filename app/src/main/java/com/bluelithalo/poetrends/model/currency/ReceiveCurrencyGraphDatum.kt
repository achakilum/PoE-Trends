package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReceiveCurrencyGraphDatum
{
    @SerializedName("count")
    var count: Int? = null

    @SerializedName("value")
    var value: Double? = null

    @SerializedName("daysAgo")
    var daysAgo: Int? = null
}
