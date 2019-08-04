package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class HelmetEnchantViewHolder : PoeNinjaViewHolder
{
    var helmetEnchantNameTextView: TextView
    var helmetEnchantIconImageView: ImageView

    var helmetEnchantChaosValueAffix: TextView
    var helmetEnchantExaltValueAffix: TextView

    var helmetEnchantValueChange: TextView

    constructor(v: View) : super(v)
    {
        helmetEnchantNameTextView = v.findViewById<View>(R.id.helmet_enchant_name_text_view) as TextView
        helmetEnchantIconImageView = v.findViewById<View>(R.id.helmet_enchant_icon_image_view) as ImageView

        helmetEnchantChaosValueAffix = v.findViewById<View>(R.id.helmet_enchant_chaos_value_affix) as TextView
        helmetEnchantExaltValueAffix = v.findViewById<View>(R.id.helmet_enchant_exalt_value_affix) as TextView

        helmetEnchantValueChange = v.findViewById<View>(R.id.helmet_enchant_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val helmetEnchantOverview = overview as ItemOverview
        val helmetEnchantLine = helmetEnchantOverview?.lines?.let { it[position] }

        helmetEnchantLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_helmet_enchant)
                .error(R.drawable.load_error_helmet_enchant)
                .into(helmetEnchantIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            helmetEnchantNameTextView.text = it.name
            helmetEnchantChaosValueAffix.text = chaosValueAffixText
            helmetEnchantExaltValueAffix.text = exaltValueAffixText

            it.sparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                helmetEnchantValueChange.text = valueChangeText
                helmetEnchantValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            helmetEnchantIconImageView.setImageResource(R.drawable.load_error_helmet_enchant)
            helmetEnchantNameTextView.text = "N/A"
            helmetEnchantChaosValueAffix.text = "N/A \u00D7"
            helmetEnchantExaltValueAffix.text = "N/A \u00D7"
            helmetEnchantValueChange.text = "N/A"
            helmetEnchantValueChange.setTextColor(Color.GRAY)
        }
    }
}