package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class VialViewHolder : PoeNinjaViewHolder
{
    var vialNameTextView: TextView
    var vialIconImageView: ImageView

    var vialChaosValueAffix: TextView
    var vialExaltValueAffix: TextView
    var vialConfidenceMarker: View

    var vialValueChange: TextView

    constructor(v: View) : super(v)
    {
        vialNameTextView = v.findViewById<View>(R.id.vial_name_text_view) as TextView
        vialIconImageView = v.findViewById<View>(R.id.vial_icon_image_view) as ImageView

        vialChaosValueAffix = v.findViewById<View>(R.id.vial_chaos_value_affix) as TextView
        vialExaltValueAffix = v.findViewById<View>(R.id.vial_exalt_value_affix) as TextView
        vialConfidenceMarker = v.findViewById<View>(R.id.vial_confidence_marker) as View

        vialValueChange = v.findViewById<View>(R.id.vial_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val vialOverview = overview as ItemOverview
        val vialLine = vialOverview?.lines?.let { it[position] }

        vialLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_vial)
                .error(R.drawable.load_error_vial)
                .into(vialIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            vialNameTextView.text = it.name
            vialChaosValueAffix.text = chaosValueAffixText
            vialExaltValueAffix.text = exaltValueAffixText
            vialConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                vialValueChange.text = valueChangeText
                vialValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            vialIconImageView.setImageResource(R.drawable.load_error_vial)
            vialNameTextView.text = "N/A"
            vialChaosValueAffix.text = "N/A \u00D7"
            vialExaltValueAffix.text = "N/A \u00D7"
            vialValueChange.text = "N/A"
            vialValueChange.setTextColor(Color.GRAY)
            vialConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}