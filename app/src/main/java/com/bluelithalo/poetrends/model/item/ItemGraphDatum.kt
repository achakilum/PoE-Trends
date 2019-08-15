package com.bluelithalo.poetrends.model.item

import com.google.gson.annotations.SerializedName

class ItemGraphDatum
{
    @SerializedName("count")
    var count: Int? = null

    @SerializedName("value")
    var value: Double? = null

    @SerializedName("daysAgo")
    var daysAgo: Int? = null
}
