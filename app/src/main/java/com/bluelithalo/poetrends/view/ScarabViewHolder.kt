package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class ScarabViewHolder : PoeNinjaViewHolder
{
    var scarabNameTextView: TextView
    var scarabIconImageView: ImageView

    var scarabChaosValueAffix: TextView
    var scarabExaltValueAffix: TextView

    var scarabValueChange: TextView

    constructor(v: View) : super(v)
    {
        scarabNameTextView = v.findViewById<View>(R.id.scarab_name_text_view) as TextView
        scarabIconImageView = v.findViewById<View>(R.id.scarab_icon_image_view) as ImageView

        scarabChaosValueAffix = v.findViewById<View>(R.id.scarab_chaos_value_affix) as TextView
        scarabExaltValueAffix = v.findViewById<View>(R.id.scarab_exalt_value_affix) as TextView

        scarabValueChange = v.findViewById<View>(R.id.scarab_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val scarabOverview = overview as ItemOverview
        val scarabLine = scarabOverview?.lines?.let { it[position] }

        scarabLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_scarab)
                .error(R.drawable.load_error_scarab)
                .into(scarabIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            scarabNameTextView.text = it.name
            scarabChaosValueAffix.text = chaosValueAffixText
            scarabExaltValueAffix.text = exaltValueAffixText

            it.sparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                scarabValueChange.text = valueChangeText
                scarabValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            scarabIconImageView.setImageResource(R.drawable.load_error_scarab)
            scarabNameTextView.text = "N/A"
            scarabChaosValueAffix.text = "N/A \u00D7"
            scarabExaltValueAffix.text = "N/A \u00D7"
            scarabValueChange.text = "N/A"
            scarabValueChange.setTextColor(Color.GRAY)
        }
    }
}