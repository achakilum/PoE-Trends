package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class UniqueAccessoryViewHolder : PoeNinjaViewHolder
{
    var uniqueAccessoryNameTextView: TextView
    var uniqueAccessoryBaseTextView: TextView
    var uniqueAccessoryIconImageView: ImageView

    var uniqueAccessoryChaosValueAffix: TextView
    var uniqueAccessoryExaltValueAffix: TextView
    var uniqueAccessoryConfidenceMarker: View

    var uniqueAccessoryValueChange: TextView

    var uniqueAccessoryLevelTextView: TextView

    constructor(v: View) : super(v)
    {
        uniqueAccessoryNameTextView = v.findViewById<View>(R.id.unique_accessory_name_text_view) as TextView
        uniqueAccessoryBaseTextView = v.findViewById<View>(R.id.unique_accessory_base_text_view) as TextView
        uniqueAccessoryIconImageView = v.findViewById<View>(R.id.unique_accessory_icon_image_view) as ImageView

        uniqueAccessoryChaosValueAffix = v.findViewById<View>(R.id.unique_accessory_chaos_value_affix) as TextView
        uniqueAccessoryExaltValueAffix = v.findViewById<View>(R.id.unique_accessory_exalt_value_affix) as TextView
        uniqueAccessoryConfidenceMarker = v.findViewById<View>(R.id.unique_accessory_confidence_marker) as View

        uniqueAccessoryValueChange = v.findViewById<View>(R.id.unique_accessory_value_change) as TextView

        uniqueAccessoryLevelTextView = v.findViewById<View>(R.id.unique_accessory_level_text_view) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val uniqueAccessoryOverview = overview as ItemOverview
        val uniqueAccessoryLine = uniqueAccessoryOverview?.lines?.let { it[position] }

        uniqueAccessoryLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_unique_accessory)
                .error(R.drawable.load_error_unique_accessory)
                .into(uniqueAccessoryIconImageView)

            val uniqueAccessoryName = it.name + if (it.variant?.length ?: 0 > 0) ", ${it.variant}" else ""
            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            uniqueAccessoryNameTextView.text = uniqueAccessoryName
            uniqueAccessoryBaseTextView.text = it.baseType
            uniqueAccessoryChaosValueAffix.text = chaosValueAffixText
            uniqueAccessoryExaltValueAffix.text = exaltValueAffixText
            uniqueAccessoryLevelTextView.text = "${it.levelRequired}"
            uniqueAccessoryConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                uniqueAccessoryValueChange.text = valueChangeText
                uniqueAccessoryValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            uniqueAccessoryIconImageView.setImageResource(R.drawable.load_error_unique_accessory)
            uniqueAccessoryNameTextView.text = "N/A"
            uniqueAccessoryBaseTextView.text = "N/A"
            uniqueAccessoryChaosValueAffix.text = "N/A \u00D7"
            uniqueAccessoryExaltValueAffix.text = "N/A \u00D7"
            uniqueAccessoryLevelTextView.text = "X"
            uniqueAccessoryValueChange.text = "N/A"
            uniqueAccessoryValueChange.setTextColor(Color.GRAY)
            uniqueAccessoryConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}