package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class UniqueFlaskViewHolder : PoeNinjaViewHolder
{
    var uniqueFlaskNameTextView: TextView
    var uniqueFlaskBaseTextView: TextView
    var uniqueFlaskIconImageView: ImageView

    var uniqueFlaskChaosValueAffix: TextView
    var uniqueFlaskExaltValueAffix: TextView
    var uniqueFlaskConfidenceMarker: View

    var uniqueFlaskValueChange: TextView

    var uniqueFlaskLevelTextView: TextView

    constructor(v: View) : super(v)
    {
        uniqueFlaskNameTextView = v.findViewById<View>(R.id.unique_flask_name_text_view) as TextView
        uniqueFlaskBaseTextView = v.findViewById<View>(R.id.unique_flask_base_text_view) as TextView
        uniqueFlaskIconImageView = v.findViewById<View>(R.id.unique_flask_icon_image_view) as ImageView

        uniqueFlaskChaosValueAffix = v.findViewById<View>(R.id.unique_flask_chaos_value_affix) as TextView
        uniqueFlaskExaltValueAffix = v.findViewById<View>(R.id.unique_flask_exalt_value_affix) as TextView
        uniqueFlaskConfidenceMarker = v.findViewById<View>(R.id.unique_flask_confidence_marker) as View

        uniqueFlaskValueChange = v.findViewById<View>(R.id.unique_flask_value_change) as TextView

        uniqueFlaskLevelTextView = v.findViewById<View>(R.id.unique_flask_level_text_view) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val uniqueFlaskOverview = overview as ItemOverview
        val uniqueFlaskLine = uniqueFlaskOverview?.lines?.let { it[position] }

        uniqueFlaskLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_unique_flask)
                .error(R.drawable.load_error_unique_flask)
                .into(uniqueFlaskIconImageView)

            val uniqueFlaskName = it.name + if (it.variant?.length ?: 0 > 0) ", ${it.variant}" else ""
            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            uniqueFlaskNameTextView.text = uniqueFlaskName
            uniqueFlaskBaseTextView.text = it.baseType
            uniqueFlaskChaosValueAffix.text = chaosValueAffixText
            uniqueFlaskExaltValueAffix.text = exaltValueAffixText
            uniqueFlaskLevelTextView.text = "${it.levelRequired}"
            uniqueFlaskConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                uniqueFlaskValueChange.text = valueChangeText
                uniqueFlaskValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            uniqueFlaskIconImageView.setImageResource(R.drawable.load_error_unique_flask)
            uniqueFlaskNameTextView.text = "N/A"
            uniqueFlaskBaseTextView.text = "N/A"
            uniqueFlaskChaosValueAffix.text = "N/A \u00D7"
            uniqueFlaskExaltValueAffix.text = "N/A \u00D7"
            uniqueFlaskLevelTextView.text = "X"
            uniqueFlaskValueChange.text = "N/A"
            uniqueFlaskValueChange.setTextColor(Color.GRAY)
            uniqueFlaskConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}