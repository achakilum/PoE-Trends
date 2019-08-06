package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class UniqueArmourViewHolder : PoeNinjaViewHolder
{
    var uniqueArmourNameTextView: TextView
    var uniqueArmourBaseTextView: TextView
    var uniqueArmourIconImageView: ImageView

    var uniqueArmourChaosValueAffix: TextView
    var uniqueArmourExaltValueAffix: TextView

    var uniqueArmourValueChange: TextView

    var uniqueArmourLevelTextView: TextView

    constructor(v: View) : super(v)
    {
        uniqueArmourNameTextView = v.findViewById<View>(R.id.unique_armour_name_text_view) as TextView
        uniqueArmourBaseTextView = v.findViewById<View>(R.id.unique_armour_base_text_view) as TextView
        uniqueArmourIconImageView = v.findViewById<View>(R.id.unique_armour_icon_image_view) as ImageView

        uniqueArmourChaosValueAffix = v.findViewById<View>(R.id.unique_armour_chaos_value_affix) as TextView
        uniqueArmourExaltValueAffix = v.findViewById<View>(R.id.unique_armour_exalt_value_affix) as TextView

        uniqueArmourValueChange = v.findViewById<View>(R.id.unique_armour_value_change) as TextView

        uniqueArmourLevelTextView = v.findViewById<View>(R.id.unique_armour_level_text_view) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val uniqueArmourOverview = overview as ItemOverview
        val uniqueArmourLine = uniqueArmourOverview?.lines?.let { it[position] }

        uniqueArmourLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_unique_armour)
                .error(R.drawable.load_error_unique_armour)
                .into(uniqueArmourIconImageView)

            val uniqueArmourName = it.name + (if (it.links ?: 0 >= 5) ", ${it.links}L" else "") + (if (it.variant?.length ?: 0 > 0) ", ${it.variant}" else "")
            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            uniqueArmourNameTextView.text = uniqueArmourName
            uniqueArmourBaseTextView.text = it.baseType
            uniqueArmourChaosValueAffix.text = chaosValueAffixText
            uniqueArmourExaltValueAffix.text = exaltValueAffixText
            uniqueArmourLevelTextView.text = "${it.levelRequired}"

            it.sparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                uniqueArmourValueChange.text = valueChangeText
                uniqueArmourValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            uniqueArmourIconImageView.setImageResource(R.drawable.load_error_unique_armour)
            uniqueArmourNameTextView.text = "N/A"
            uniqueArmourBaseTextView.text = "N/A"
            uniqueArmourChaosValueAffix.text = "N/A \u00D7"
            uniqueArmourExaltValueAffix.text = "N/A \u00D7"
            uniqueArmourLevelTextView.text = "X"
            uniqueArmourValueChange.text = "N/A"
            uniqueArmourValueChange.setTextColor(Color.GRAY)
        }
    }
}