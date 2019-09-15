package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class OilViewHolder : PoeNinjaViewHolder
{
    var oilNameTextView: TextView
    var oilIconImageView: ImageView

    var oilChaosValueAffix: TextView
    var oilExaltValueAffix: TextView
    var oilConfidenceMarker: View

    var oilValueChange: TextView

    constructor(v: View) : super(v)
    {
        oilNameTextView = v.findViewById<View>(R.id.oil_name_text_view) as TextView
        oilIconImageView = v.findViewById<View>(R.id.oil_icon_image_view) as ImageView

        oilChaosValueAffix = v.findViewById<View>(R.id.oil_chaos_value_affix) as TextView
        oilExaltValueAffix = v.findViewById<View>(R.id.oil_exalt_value_affix) as TextView
        oilConfidenceMarker = v.findViewById<View>(R.id.oil_confidence_marker) as View

        oilValueChange = v.findViewById<View>(R.id.oil_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val oilOverview = overview as ItemOverview
        val oilLine = oilOverview?.lines?.let { it[position] }

        oilLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_oil)
                .error(R.drawable.load_error_oil)
                .into(oilIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            oilNameTextView.text = it.name
            oilChaosValueAffix.text = chaosValueAffixText
            oilExaltValueAffix.text = exaltValueAffixText
            oilConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                oilValueChange.text = valueChangeText
                oilValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            oilIconImageView.setImageResource(R.drawable.load_error_oil)
            oilNameTextView.text = "N/A"
            oilChaosValueAffix.text = "N/A \u00D7"
            oilExaltValueAffix.text = "N/A \u00D7"
            oilValueChange.text = "N/A"
            oilValueChange.setTextColor(Color.GRAY)
            oilConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}