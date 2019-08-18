package com.bluelithalo.poetrends.model.item

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LowConfidenceSparkline
{
    @Expose
	@SerializedName("data")
    var data: List<Double>? = null

    @Expose
	@SerializedName("totalChange")
    var totalChange: Double? = null
}
