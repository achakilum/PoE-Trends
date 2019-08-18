package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrencyDetail
{
    @Expose
	@SerializedName("id")
    var id: Int? = null

    @Expose
	@SerializedName("icon")
    var icon: String? = null

    @Expose
	@SerializedName("name")
    var name: String? = null

    @Expose
	@SerializedName("poeTradeId")
    var poeTradeId: Int? = null
}
