package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class MapViewHolder : PoeNinjaViewHolder
{
    var mapNameTextView: TextView
    var mapIconImageView: ImageView

    var mapChaosValueAffix: TextView
    var mapExaltValueAffix: TextView
    var mapConfidenceMarker: View

    var mapValueChange: TextView

    var mapTierTextView: TextView

    constructor(v: View) : super(v)
    {
        mapNameTextView = v.findViewById<View>(R.id.map_name_text_view) as TextView
        mapIconImageView = v.findViewById<View>(R.id.map_icon_image_view) as ImageView

        mapChaosValueAffix = v.findViewById<View>(R.id.map_chaos_value_affix) as TextView
        mapExaltValueAffix = v.findViewById<View>(R.id.map_exalt_value_affix) as TextView
        mapConfidenceMarker = v.findViewById<View>(R.id.map_confidence_marker) as View

        mapValueChange = v.findViewById<View>(R.id.map_value_change) as TextView

        mapTierTextView = v.findViewById<View>(R.id.map_tier_text_view) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val mapOverview = overview as ItemOverview
        val mapLine = mapOverview?.lines?.let { it[position] }

        mapLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_map)
                .error(R.drawable.load_error_map)
                .into(mapIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            mapNameTextView.text = it.name
            mapChaosValueAffix.text = chaosValueAffixText
            mapExaltValueAffix.text = exaltValueAffixText
            mapTierTextView.text = "${it.mapTier}"
            mapConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                mapValueChange.text = valueChangeText
                mapValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            mapIconImageView.setImageResource(R.drawable.load_error_map)
            mapNameTextView.text = "N/A"
            mapChaosValueAffix.text = "N/A \u00D7"
            mapExaltValueAffix.text = "N/A \u00D7"
            mapTierTextView.text = "X"
            mapValueChange.text = "N/A"
            mapValueChange.setTextColor(Color.GRAY)
            mapConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}