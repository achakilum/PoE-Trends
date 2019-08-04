package com.bluelithalo.poetrends.model.item

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Line
{
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("icon")
    var icon: String? = null

    @SerializedName("mapTier")
    var mapTier: Int? = null

    @SerializedName("levelRequired")
    var levelRequired: Int? = null

    @SerializedName("baseType")
    var baseType: String? = null

    @SerializedName("stackSize")
    var stackSize: Int? = null

    @SerializedName("variant")
    var variant: String? = null

    @SerializedName("prophecyText")
    var prophecyText: String? = null

    @SerializedName("artFilename")
    var artFilename: String? = null

    @SerializedName("links")
    var links: Int? = null

    @SerializedName("itemClass")
    var itemClass: Int? = null

    @SerializedName("sparkline")
    var sparkline: Sparkline? = null

    @SerializedName("lowConfidenceSparkline")
    var lowConfidenceSparkline: LowConfidenceSparkline? = null

    @SerializedName("implicitModifiers")
    var implicitModifiers: List<Any>? = null

    @SerializedName("explicitModifiers")
    var explicitModifiers: List<ExplicitModifier>? = null

    @SerializedName("flavourText")
    var flavourText: String? = null

    @SerializedName("corrupted")
    var corrupted: Boolean? = null

    @SerializedName("gemLevel")
    var gemLevel: Int? = null

    @SerializedName("gemQuality")
    var gemQuality: Int? = null

    @SerializedName("itemType")
    var itemType: String? = null

    @SerializedName("chaosValue")
    var chaosValue: Double? = null

    @SerializedName("exaltedValue")
    var exaltedValue: Double? = null

    @SerializedName("count")
    var count: Int? = null

    @SerializedName("detailsId")
    var detailsId: String? = null
}
