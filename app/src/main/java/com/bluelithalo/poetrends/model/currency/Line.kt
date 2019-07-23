package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.SerializedName

class Line
{
    @SerializedName("currencyTypeName")
    var currencyTypeName: String? = null

    @SerializedName("pay")
    var pay: Pay? = null

    @SerializedName("receive")
    var receive: Receive? = null

    @SerializedName("paySparkLine")
    var paySparkLine: PaySparkLine? = null

    @SerializedName("receiveSparkLine")
    var receiveSparkLine: ReceiveSparkLine? = null

    @SerializedName("chaosEquivalent")
    var chaosEquivalent: Double? = null

    @SerializedName("lowConfidencePaySparkLine")
    var lowConfidencePaySparkLine: LowConfidencePaySparkLine? = null

    @SerializedName("lowConfidenceReceiveSparkLine")
    var lowConfidenceReceiveSparkLine: LowConfidenceReceiveSparkLine? = null

    @SerializedName("detailsId")
    var detailsId: String? = null
}
