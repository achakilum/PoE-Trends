package com.bluelithalo.poetrends.model.item

import com.bluelithalo.poetrends.model.Overview
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ItemOverview : Overview()
{
    @Expose
	@SerializedName("lines")
    var lines: List<Line>? = null
}
