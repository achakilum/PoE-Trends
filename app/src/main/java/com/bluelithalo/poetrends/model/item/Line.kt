package com.bluelithalo.poetrends.model.item

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Line
{
    @Expose
	@SerializedName("id")
    var id: Int? = null

    @Expose
	@SerializedName("name")
    var name: String? = null

    @Expose
	@SerializedName("icon")
    var icon: String? = null

    @Expose
	@SerializedName("mapTier")
    var mapTier: Int? = null

    @Expose
	@SerializedName("levelRequired")
    var levelRequired: Int? = null

    @Expose
	@SerializedName("baseType")
    var baseType: String? = null

    @Expose
	@SerializedName("stackSize")
    var stackSize: Int? = null

    @Expose
	@SerializedName("variant")
    var variant: String? = null

    @Expose
	@SerializedName("prophecyText")
    var prophecyText: String? = null

    @Expose
	@SerializedName("artFilename")
    var artFilename: String? = null

    @Expose
	@SerializedName("links")
    var links: Int? = null

    @Expose
	@SerializedName("itemClass")
    var itemClass: Int? = null

    @Expose
	@SerializedName("sparkline")
    var sparkline: Sparkline? = null

    @Expose
	@SerializedName("lowConfidenceSparkline")
    var lowConfidenceSparkline: LowConfidenceSparkline? = null

    @Expose
	@SerializedName("implicitModifiers")
    var implicitModifiers: List<Any>? = null

    @Expose
	@SerializedName("explicitModifiers")
    var explicitModifiers: List<ExplicitModifier>? = null

    @Expose
	@SerializedName("flavourText")
    var flavourText: String? = null

    @Expose
	@SerializedName("corrupted")
    var corrupted: Boolean? = null

    @Expose
	@SerializedName("gemLevel")
    var gemLevel: Int? = null

    @Expose
	@SerializedName("gemQuality")
    var gemQuality: Int? = null

    @Expose
	@SerializedName("itemType")
    var itemType: String? = null

    @Expose
	@SerializedName("chaosValue")
    var chaosValue: Double? = null

    @Expose
	@SerializedName("exaltedValue")
    var exaltedValue: Double? = null

    @Expose
	@SerializedName("count")
    var count: Int? = null

    @Expose
	@SerializedName("detailsId")
    var detailsId: String? = null
}
