package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class IncubatorViewHolder : PoeNinjaViewHolder
{
    var incubatorNameTextView: TextView
    var incubatorIconImageView: ImageView

    var incubatorChaosValueAffix: TextView
    var incubatorExaltValueAffix: TextView

    var incubatorValueChange: TextView

    constructor(v: View) : super(v)
    {
        incubatorNameTextView = v.findViewById<View>(R.id.incubator_name_text_view) as TextView
        incubatorIconImageView = v.findViewById<View>(R.id.incubator_icon_image_view) as ImageView

        incubatorChaosValueAffix = v.findViewById<View>(R.id.incubator_chaos_value_affix) as TextView
        incubatorExaltValueAffix = v.findViewById<View>(R.id.incubator_exalt_value_affix) as TextView

        incubatorValueChange = v.findViewById<View>(R.id.incubator_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val incubatorOverview = overview as ItemOverview
        val incubatorLine = incubatorOverview?.lines?.let { it[position] }

        incubatorLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_incubator)
                .error(R.drawable.load_error_incubator)
                .into(incubatorIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            incubatorNameTextView.text = it.name
            incubatorChaosValueAffix.text = chaosValueAffixText
            incubatorExaltValueAffix.text = exaltValueAffixText

            it.sparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                incubatorValueChange.text = valueChangeText
                incubatorValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            incubatorIconImageView.setImageResource(R.drawable.load_error_incubator)
            incubatorNameTextView.text = "N/A"
            incubatorChaosValueAffix.text = "N/A \u00D7"
            incubatorExaltValueAffix.text = "N/A \u00D7"
            incubatorValueChange.text = "N/A"
            incubatorValueChange.setTextColor(Color.GRAY)
        }
    }
}