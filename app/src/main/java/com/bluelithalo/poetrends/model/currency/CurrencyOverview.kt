package com.bluelithalo.poetrends.model.currency

import com.bluelithalo.poetrends.model.Overview
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrencyOverview : Overview()
{
    @Expose
	@SerializedName("lines")
    var lines: List<Line>? = null

    @Expose
	@SerializedName("currencyDetails")
    var currencyDetails: List<CurrencyDetail>? = null
}
