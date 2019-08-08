package com.bluelithalo.poetrends.model.league

import com.google.gson.annotations.SerializedName

class League
{
    @SerializedName("id")
    var id: String? = null

    @SerializedName("realm")
    var realm: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("startAt")
    var startAt: String? = null

    @SerializedName("endAt")
    var endAt: String? = null

    @SerializedName("delveEvent")
    var delveEvent: Boolean? = null

    @SerializedName("rules")
    var rules: List<Rule>? = null

    @SerializedName("registerAt")
    var registerAt: String? = null
}
