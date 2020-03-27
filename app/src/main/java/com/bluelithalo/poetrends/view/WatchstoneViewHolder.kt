package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class WatchstoneViewHolder : PoeNinjaViewHolder
{
    var watchstoneNameTextView: TextView
    var watchstoneIconImageView: ImageView

    var watchstoneChaosValueAffix: TextView
    var watchstoneExaltValueAffix: TextView
    var watchstoneConfidenceMarker: View

    var watchstoneValueChange: TextView

    var watchstoneUsesTextView: TextView

    constructor(v: View) : super(v)
    {
        watchstoneNameTextView = v.findViewById<View>(R.id.watchstone_name_text_view) as TextView
        watchstoneIconImageView = v.findViewById<View>(R.id.watchstone_icon_image_view) as ImageView

        watchstoneChaosValueAffix = v.findViewById<View>(R.id.watchstone_chaos_value_affix) as TextView
        watchstoneExaltValueAffix = v.findViewById<View>(R.id.watchstone_exalt_value_affix) as TextView
        watchstoneConfidenceMarker = v.findViewById<View>(R.id.watchstone_confidence_marker) as View

        watchstoneValueChange = v.findViewById<View>(R.id.watchstone_value_change) as TextView

        watchstoneUsesTextView = v.findViewById<View>(R.id.watchstone_uses_text_view) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val watchstoneOverview = overview as ItemOverview
        val watchstoneLine = watchstoneOverview?.lines?.let { it[position] }

        watchstoneLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_watchstone)
                .error(R.drawable.load_error_watchstone)
                .into(watchstoneIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            watchstoneNameTextView.text = it.name
            watchstoneChaosValueAffix.text = chaosValueAffixText
            watchstoneExaltValueAffix.text = exaltValueAffixText
            watchstoneUsesTextView.text = "${it.mapTier}"
            watchstoneConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                watchstoneValueChange.text = valueChangeText
                watchstoneValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            watchstoneIconImageView.setImageResource(R.drawable.load_error_watchstone)
            watchstoneNameTextView.text = "N/A"
            watchstoneChaosValueAffix.text = "N/A \u00D7"
            watchstoneExaltValueAffix.text = "N/A \u00D7"
            watchstoneUsesTextView.text = "X"
            watchstoneValueChange.text = "N/A"
            watchstoneValueChange.setTextColor(Color.GRAY)
            watchstoneConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}