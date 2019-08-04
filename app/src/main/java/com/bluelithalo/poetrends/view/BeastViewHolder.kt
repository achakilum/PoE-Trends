package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class BeastViewHolder : PoeNinjaViewHolder
{
    var beastNameTextView: TextView
    var beastIconImageView: ImageView

    var beastChaosValueAffix: TextView
    var beastExaltValueAffix: TextView

    var beastValueChange: TextView

    constructor(v: View) : super(v)
    {
        beastNameTextView = v.findViewById<View>(R.id.beast_name_text_view) as TextView
        beastIconImageView = v.findViewById<View>(R.id.beast_icon_image_view) as ImageView

        beastChaosValueAffix = v.findViewById<View>(R.id.beast_chaos_value_affix) as TextView
        beastExaltValueAffix = v.findViewById<View>(R.id.beast_exalt_value_affix) as TextView

        beastValueChange = v.findViewById<View>(R.id.beast_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val beastOverview = overview as ItemOverview
        val beastLine = beastOverview?.lines?.let { it[position] }

        beastLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_beast)
                .error(R.drawable.load_error_beast)
                .into(beastIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            beastNameTextView.text = it.name
            beastChaosValueAffix.text = chaosValueAffixText
            beastExaltValueAffix.text = exaltValueAffixText

            it.sparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                beastValueChange.text = valueChangeText
                beastValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            beastIconImageView.setImageResource(R.drawable.load_error_beast)
            beastNameTextView.text = "N/A"
            beastChaosValueAffix.text = "N/A \u00D7"
            beastExaltValueAffix.text = "N/A \u00D7"
            beastValueChange.text = "N/A"
            beastValueChange.setTextColor(Color.GRAY)
        }
    }
}