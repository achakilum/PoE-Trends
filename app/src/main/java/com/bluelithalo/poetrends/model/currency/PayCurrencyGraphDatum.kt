package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PayCurrencyGraphDatum
{
    @Expose
	@SerializedName("count")
    var count: Int? = null

    @Expose
	@SerializedName("value")
    var value: Double? = null

    @Expose
	@SerializedName("daysAgo")
    var daysAgo: Int? = null
}
