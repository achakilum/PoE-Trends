package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrencyHistory
{
    @SerializedName("payCurrencyGraphData")
    var payCurrencyGraphData: List<PayCurrencyGraphDatum>? = null

    @SerializedName("receiveCurrencyGraphData")
    var receiveCurrencyGraphData: List<ReceiveCurrencyGraphDatum>? = null
}
