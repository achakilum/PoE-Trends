package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.SerializedName

class PaySparkLine
{
    @SerializedName("data")
    var data: List<Double>? = null

    @SerializedName("totalChange")
    var totalChange: Double? = null
}
