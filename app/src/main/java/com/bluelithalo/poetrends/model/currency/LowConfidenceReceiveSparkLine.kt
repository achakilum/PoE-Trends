package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LowConfidenceReceiveSparkLine
{
    @Expose
	@SerializedName("data")
    var data: List<Double>? = null

    @Expose
	@SerializedName("totalChange")
    var totalChange: Double? = null
}
