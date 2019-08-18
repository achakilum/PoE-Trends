package com.bluelithalo.poetrends.model.league

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class League
{
    @Expose
	@SerializedName("id")
    var id: String? = null

    @Expose
	@SerializedName("realm")
    var realm: String? = null

    @Expose
	@SerializedName("description")
    var description: String? = null

    @Expose
	@SerializedName("url")
    var url: String? = null

    @Expose
	@SerializedName("startAt")
    var startAt: String? = null

    @Expose
	@SerializedName("endAt")
    var endAt: String? = null

    @Expose
	@SerializedName("delveEvent")
    var delveEvent: Boolean? = null

    @Expose
	@SerializedName("rules")
    var rules: List<Rule>? = null

    @Expose
	@SerializedName("registerAt")
    var registerAt: String? = null
}
