package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class UniqueMapViewHolder : PoeNinjaViewHolder
{
    var uniqueMapNameTextView: TextView
    var uniqueMapBaseTextView: TextView
    var uniqueMapIconImageView: ImageView

    var uniqueMapChaosValueAffix: TextView
    var uniqueMapExaltValueAffix: TextView
    var uniqueMapConfidenceMarker: View

    var uniqueMapValueChange: TextView

    var uniqueMapTierTextView: TextView

    constructor(v: View) : super(v)
    {
        uniqueMapNameTextView = v.findViewById<View>(R.id.unique_map_name_text_view) as TextView
        uniqueMapBaseTextView = v.findViewById<View>(R.id.unique_map_base_text_view) as TextView
        uniqueMapIconImageView = v.findViewById<View>(R.id.unique_map_icon_image_view) as ImageView

        uniqueMapChaosValueAffix = v.findViewById<View>(R.id.unique_map_chaos_value_affix) as TextView
        uniqueMapExaltValueAffix = v.findViewById<View>(R.id.unique_map_exalt_value_affix) as TextView

        uniqueMapValueChange = v.findViewById<View>(R.id.unique_map_value_change) as TextView
        uniqueMapConfidenceMarker = v.findViewById<View>(R.id.unique_map_confidence_marker) as View

        uniqueMapTierTextView = v.findViewById<View>(R.id.unique_map_tier_text_view) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val uniqueMapOverview = overview as ItemOverview
        val uniqueMapLine = uniqueMapOverview?.lines?.let { it[position] }

        uniqueMapLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_unique_map)
                .error(R.drawable.load_error_unique_map)
                .into(uniqueMapIconImageView)

            val uniqueMapName = it.name + if (it.variant?.length ?: 0 > 0) ", ${it.variant}" else ""
            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            uniqueMapNameTextView.text = uniqueMapName
            uniqueMapBaseTextView.text = it.baseType
            uniqueMapChaosValueAffix.text = chaosValueAffixText
            uniqueMapExaltValueAffix.text = exaltValueAffixText
            uniqueMapTierTextView.text = "${it.mapTier}"
            uniqueMapConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                uniqueMapValueChange.text = valueChangeText
                uniqueMapValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            uniqueMapIconImageView.setImageResource(R.drawable.load_error_unique_map)
            uniqueMapNameTextView.text = "N/A"
            uniqueMapBaseTextView.text = "N/A"
            uniqueMapChaosValueAffix.text = "N/A \u00D7"
            uniqueMapExaltValueAffix.text = "N/A \u00D7"
            uniqueMapTierTextView.text = "X"
            uniqueMapValueChange.text = "N/A"
            uniqueMapValueChange.setTextColor(Color.GRAY)
            uniqueMapConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}