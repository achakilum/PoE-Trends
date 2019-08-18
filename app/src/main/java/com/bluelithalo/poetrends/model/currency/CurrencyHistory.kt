package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrencyHistory
{
    @Expose
	@SerializedName("payCurrencyGraphData")
    var payCurrencyGraphData: List<PayCurrencyGraphDatum>? = null

    @Expose
	@SerializedName("receiveCurrencyGraphData")
    var receiveCurrencyGraphData: List<ReceiveCurrencyGraphDatum>? = null
}
