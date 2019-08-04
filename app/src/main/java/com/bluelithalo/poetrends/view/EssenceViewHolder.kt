package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class EssenceViewHolder : PoeNinjaViewHolder
{
    var essenceNameTextView: TextView
    var essenceIconImageView: ImageView

    var essenceChaosValueAffix: TextView
    var essenceExaltValueAffix: TextView

    var essenceValueChange: TextView

    var essenceTierTextView: TextView

    constructor(v: View) : super(v)
    {
        essenceNameTextView = v.findViewById<View>(R.id.essence_name_text_view) as TextView
        essenceIconImageView = v.findViewById<View>(R.id.essence_icon_image_view) as ImageView

        essenceChaosValueAffix = v.findViewById<View>(R.id.essence_chaos_value_affix) as TextView
        essenceExaltValueAffix = v.findViewById<View>(R.id.essence_exalt_value_affix) as TextView

        essenceValueChange = v.findViewById<View>(R.id.essence_value_change) as TextView

        essenceTierTextView = v.findViewById<View>(R.id.essence_tier_text_view) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val essenceOverview = overview as ItemOverview
        val essenceLine = essenceOverview?.lines?.let { it[position] }

        essenceLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_essence)
                .error(R.drawable.load_error_essence)
                .into(essenceIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            essenceNameTextView.text = it.name
            essenceChaosValueAffix.text = chaosValueAffixText
            essenceExaltValueAffix.text = exaltValueAffixText
            essenceTierTextView.text = "${it.mapTier}"

            it.sparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                essenceValueChange.text = valueChangeText
                essenceValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            essenceIconImageView.setImageResource(R.drawable.load_error_essence)
            essenceNameTextView.text = "N/A"
            essenceChaosValueAffix.text = "N/A \u00D7"
            essenceExaltValueAffix.text = "N/A \u00D7"
            essenceTierTextView.text = "X"
            essenceValueChange.text = "N/A"
            essenceValueChange.setTextColor(Color.GRAY)
        }
    }
}