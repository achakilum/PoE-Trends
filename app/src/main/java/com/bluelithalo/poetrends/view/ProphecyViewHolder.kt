package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class ProphecyViewHolder : PoeNinjaViewHolder
{
    var prophecyNameTextView: TextView
    var prophecyIconImageView: ImageView

    var prophecyChaosValueAffix: TextView
    var prophecyExaltValueAffix: TextView
    var prophecyConfidenceMarker: View

    var prophecyValueChange: TextView

    constructor(v: View) : super(v)
    {
        prophecyNameTextView = v.findViewById<View>(R.id.prophecy_name_text_view) as TextView
        prophecyIconImageView = v.findViewById<View>(R.id.prophecy_icon_image_view) as ImageView

        prophecyChaosValueAffix = v.findViewById<View>(R.id.prophecy_chaos_value_affix) as TextView
        prophecyExaltValueAffix = v.findViewById<View>(R.id.prophecy_exalt_value_affix) as TextView
        prophecyConfidenceMarker = v.findViewById<View>(R.id.prophecy_confidence_marker) as View

        prophecyValueChange = v.findViewById<View>(R.id.prophecy_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val prophecyOverview = overview as ItemOverview
        val prophecyLine = prophecyOverview?.lines?.let { it[position] }

        prophecyLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_prophecy)
                .error(R.drawable.load_error_prophecy)
                .into(prophecyIconImageView)

            val prophecyName = it.name + if (it.variant?.length ?: 0 > 0) ", ${it.variant}" else ""
            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            prophecyNameTextView.text = prophecyName
            prophecyChaosValueAffix.text = chaosValueAffixText
            prophecyExaltValueAffix.text = exaltValueAffixText
            prophecyConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                prophecyValueChange.text = valueChangeText
                prophecyValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            prophecyIconImageView.setImageResource(R.drawable.load_error_prophecy)
            prophecyNameTextView.text = "N/A"
            prophecyChaosValueAffix.text = "N/A \u00D7"
            prophecyExaltValueAffix.text = "N/A \u00D7"
            prophecyValueChange.text = "N/A"
            prophecyValueChange.setTextColor(Color.GRAY)
            prophecyConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}