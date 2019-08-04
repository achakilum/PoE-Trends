package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class ResonatorViewHolder : PoeNinjaViewHolder
{
    var resonatorNameTextView: TextView
    var resonatorIconImageView: ImageView

    var resonatorChaosValueAffix: TextView
    var resonatorExaltValueAffix: TextView

    var resonatorValueChange: TextView

    constructor(v: View) : super(v)
    {
        resonatorNameTextView = v.findViewById<View>(R.id.resonator_name_text_view) as TextView
        resonatorIconImageView = v.findViewById<View>(R.id.resonator_icon_image_view) as ImageView

        resonatorChaosValueAffix = v.findViewById<View>(R.id.resonator_chaos_value_affix) as TextView
        resonatorExaltValueAffix = v.findViewById<View>(R.id.resonator_exalt_value_affix) as TextView

        resonatorValueChange = v.findViewById<View>(R.id.resonator_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val resonatorOverview = overview as ItemOverview
        val resonatorLine = resonatorOverview?.lines?.let { it[position] }

        resonatorLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_resonator)
                .error(R.drawable.load_error_resonator)
                .into(resonatorIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            resonatorNameTextView.text = it.name
            resonatorChaosValueAffix.text = chaosValueAffixText
            resonatorExaltValueAffix.text = exaltValueAffixText

            it.sparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                resonatorValueChange.text = valueChangeText
                resonatorValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            resonatorIconImageView.setImageResource(R.drawable.load_error_resonator)
            resonatorNameTextView.text = "N/A"
            resonatorChaosValueAffix.text = "N/A \u00D7"
            resonatorExaltValueAffix.text = "N/A \u00D7"
            resonatorValueChange.text = "N/A"
            resonatorValueChange.setTextColor(Color.GRAY)
        }
    }
}