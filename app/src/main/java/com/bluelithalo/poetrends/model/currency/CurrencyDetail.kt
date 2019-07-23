package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.SerializedName

class CurrencyDetail
{
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("icon")
    var icon: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("poeTradeId")
    var poeTradeId: Int? = null
}
