package com.bluelithalo.poetrends.model.item

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LowConfidenceSparkline
{
    @SerializedName("data")
    var data: List<Double>? = null

    @SerializedName("totalChange")
    var totalChange: Double? = null
}
