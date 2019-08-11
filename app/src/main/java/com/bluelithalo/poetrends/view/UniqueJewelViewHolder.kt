package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class UniqueJewelViewHolder : PoeNinjaViewHolder
{
    var uniqueJewelNameTextView: TextView
    var uniqueJewelBaseTextView: TextView
    var uniqueJewelIconImageView: ImageView

    var uniqueJewelChaosValueAffix: TextView
    var uniqueJewelExaltValueAffix: TextView
    var uniqueJewelConfidenceMarker: View

    var uniqueJewelValueChange: TextView

    constructor(v: View) : super(v)
    {
        uniqueJewelNameTextView = v.findViewById<View>(R.id.unique_jewel_name_text_view) as TextView
        uniqueJewelBaseTextView = v.findViewById<View>(R.id.unique_jewel_base_text_view) as TextView
        uniqueJewelIconImageView = v.findViewById<View>(R.id.unique_jewel_icon_image_view) as ImageView

        uniqueJewelChaosValueAffix = v.findViewById<View>(R.id.unique_jewel_chaos_value_affix) as TextView
        uniqueJewelExaltValueAffix = v.findViewById<View>(R.id.unique_jewel_exalt_value_affix) as TextView
        uniqueJewelConfidenceMarker = v.findViewById<View>(R.id.unique_jewel_confidence_marker) as View

        uniqueJewelValueChange = v.findViewById<View>(R.id.unique_jewel_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val uniqueJewelOverview = overview as ItemOverview
        val uniqueJewelLine = uniqueJewelOverview?.lines?.let { it[position] }

        uniqueJewelLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_unique_jewel)
                .error(R.drawable.load_error_unique_jewel)
                .into(uniqueJewelIconImageView)

            val uniqueJewelName = it.name
            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            uniqueJewelNameTextView.text = uniqueJewelName
            uniqueJewelBaseTextView.text = it.baseType
            uniqueJewelChaosValueAffix.text = chaosValueAffixText
            uniqueJewelExaltValueAffix.text = exaltValueAffixText
            uniqueJewelConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                uniqueJewelValueChange.text = valueChangeText
                uniqueJewelValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            uniqueJewelIconImageView.setImageResource(R.drawable.load_error_unique_jewel)
            uniqueJewelNameTextView.text = "N/A"
            uniqueJewelBaseTextView.text = "N/A"
            uniqueJewelChaosValueAffix.text = "N/A \u00D7"
            uniqueJewelExaltValueAffix.text = "N/A \u00D7"
            uniqueJewelValueChange.text = "N/A"
            uniqueJewelValueChange.setTextColor(Color.GRAY)
            uniqueJewelConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}