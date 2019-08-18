package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Receive
{
    @Expose
	@SerializedName("id")
    var id: Int? = null

    @Expose
	@SerializedName("league_id")
    var leagueId: Int? = null

    @Expose
	@SerializedName("pay_currency_id")
    var payCurrencyId: Int? = null

    @Expose
	@SerializedName("get_currency_id")
    var getCurrencyId: Int? = null

    @Expose
	@SerializedName("sample_time_utc")
    var sampleTimeUtc: String? = null

    @Expose
	@SerializedName("count")
    var count: Int? = null

    @Expose
	@SerializedName("value")
    var value: Double? = null

    @Expose
	@SerializedName("data_point_count")
    var dataPointCount: Int? = null

    @Expose
	@SerializedName("includes_secondary")
    var includesSecondary: Boolean? = null
}
