package com.bluelithalo.poetrends.model.league

import com.google.gson.annotations.SerializedName

class Rule
{
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null
}
