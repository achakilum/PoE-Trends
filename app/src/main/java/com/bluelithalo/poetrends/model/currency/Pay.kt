package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.SerializedName

class Pay
{
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("league_id")
    var leagueId: Int? = null

    @SerializedName("pay_currency_id")
    var payCurrencyId: Int? = null

    @SerializedName("get_currency_id")
    var getCurrencyId: Int? = null

    @SerializedName("sample_time_utc")
    var sampleTimeUtc: String? = null

    @SerializedName("count")
    var count: Int? = null

    @SerializedName("value")
    var value: Double? = null

    @SerializedName("data_point_count")
    var dataPointCount: Int? = null

    @SerializedName("includes_secondary")
    var includesSecondary: Boolean? = null
}
