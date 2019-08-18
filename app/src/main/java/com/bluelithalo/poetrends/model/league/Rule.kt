package com.bluelithalo.poetrends.model.league

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rule
{
    @Expose
	@SerializedName("id")
    var id: String? = null

    @Expose
	@SerializedName("name")
    var name: String? = null

    @Expose
	@SerializedName("description")
    var description: String? = null
}
