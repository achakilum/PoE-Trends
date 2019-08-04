package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class ItemViewHolder : PoeNinjaViewHolder
{
    var itemNameTextView: TextView
    var itemIconImageView: ImageView

    var itemChaosValueAffix: TextView
    var itemExaltValueAffix: TextView

    var itemValueChange: TextView

    constructor(v: View) : super(v)
    {
        itemNameTextView = v.findViewById<View>(R.id.item_name_text_view) as TextView
        itemIconImageView = v.findViewById<View>(R.id.item_icon_image_view) as ImageView

        itemChaosValueAffix = v.findViewById<View>(R.id.item_chaos_value_affix) as TextView
        itemExaltValueAffix = v.findViewById<View>(R.id.item_exalt_value_affix) as TextView

        itemValueChange = v.findViewById<View>(R.id.item_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val itemOverview = overview as ItemOverview
        val itemLine = itemOverview?.lines?.let { it[position] }

        itemLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_item)
                .error(R.drawable.load_error_item)
                .into(itemIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            itemNameTextView.text = it.name
            itemChaosValueAffix.text = chaosValueAffixText
            itemExaltValueAffix.text = exaltValueAffixText

            it.sparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                itemValueChange.text = valueChangeText
                itemValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            itemIconImageView.setImageResource(R.drawable.load_error_item)
            itemNameTextView.text = "N/A"
            itemChaosValueAffix.text = "N/A \u00D7"
            itemExaltValueAffix.text = "N/A \u00D7"
            itemValueChange.text = "N/A"
            itemValueChange.setTextColor(Color.GRAY)
        }
    }
}