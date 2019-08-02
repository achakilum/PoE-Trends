package com.bluelithalo.poetrends.model.currency

import com.bluelithalo.poetrends.model.Overview
import com.google.gson.annotations.SerializedName

class CurrencyOverview : Overview()
{
    @SerializedName("lines")
    var lines: List<Line>? = null

    @SerializedName("currencyDetails")
    var currencyDetails: List<CurrencyDetail>? = null
}
