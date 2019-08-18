package com.bluelithalo.poetrends.model.currency

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Line
{
    @Expose
	@SerializedName("currencyTypeName")
    var currencyTypeName: String? = null

    @Expose
	@SerializedName("pay")
    var pay: Pay? = null

    @Expose
	@SerializedName("receive")
    var receive: Receive? = null

    @Expose
	@SerializedName("paySparkLine")
    var paySparkLine: PaySparkLine? = null

    @Expose
	@SerializedName("receiveSparkLine")
    var receiveSparkLine: ReceiveSparkLine? = null

    @Expose
	@SerializedName("chaosEquivalent")
    var chaosEquivalent: Double? = null

    @Expose
	@SerializedName("lowConfidencePaySparkLine")
    var lowConfidencePaySparkLine: LowConfidencePaySparkLine? = null

    @Expose
	@SerializedName("lowConfidenceReceiveSparkLine")
    var lowConfidenceReceiveSparkLine: LowConfidenceReceiveSparkLine? = null

    @Expose
	@SerializedName("detailsId")
    var detailsId: String? = null
}
