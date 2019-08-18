package com.bluelithalo.poetrends.model.item

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExplicitModifier
{
    @Expose
	@SerializedName("text")
    var text: String? = null

    @Expose
	@SerializedName("optional")
    var optional: Boolean? = null
}
