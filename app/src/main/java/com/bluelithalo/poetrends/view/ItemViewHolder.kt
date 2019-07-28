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

    var chaosValueIcon: ImageView
    var exaltValueIcon: ImageView

    var chaosValueAffix: TextView
    var exaltValueAffix: TextView

    var itemValueChange: TextView

    constructor(v: View) : super(v)
    {
        itemNameTextView = v.findViewById<View>(R.id.item_name_text_view) as TextView
        itemIconImageView = v.findViewById<View>(R.id.item_icon_image_view) as ImageView

        chaosValueIcon = v.findViewById<View>(R.id.item_chaos_value_icon) as ImageView
        exaltValueIcon = v.findViewById<View>(R.id.item_exalt_value_icon) as ImageView

        chaosValueAffix = v.findViewById<View>(R.id.item_chaos_value_affix) as TextView
        exaltValueAffix = v.findViewById<View>(R.id.item_exalt_value_affix) as TextView

        itemValueChange = v.findViewById<View>(R.id.item_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val itemOverview = overview as ItemOverview
        val itemLine = itemOverview?.lines!![position]
        val itemName = itemLine?.name

        val valueDataAvailable = (itemLine != null)
        val valueChangeDataAvailable = itemLine.sparkline != null

        val chaosValue = if (valueDataAvailable) itemLine.chaosValue else 0.0
        val exaltValue = if (valueDataAvailable) itemLine.exaltedValue else 0.0
        val valueChange = if (valueChangeDataAvailable) itemLine.sparkline?.totalChange else 0.0

        val chaosValueAffixText = if (valueDataAvailable) String.format("%.1f", chaosValue) + " \u00D7" else "N/A"
        val exaltValueAffixText = if (valueDataAvailable) String.format("%.1f", exaltValue) + " \u00D7" else "N/A"
        val valueChangeText = if (valueChangeDataAvailable) (if (valueChange!! > 0.0) "+" else "") + String.format("%.1f", valueChange) + "%" else "N/A"
        val iconUrl = if (valueDataAvailable) itemLine.icon else ""

        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.item_load_placeholder)
            .error(R.drawable.item_load_error)
            .into(itemIconImageView)

        itemNameTextView.text = itemName

        chaosValueAffix.text = chaosValueAffixText
        exaltValueAffix.text = exaltValueAffixText

        itemValueChange.text = if (valueChangeDataAvailable) valueChangeText else "N/A"
        itemValueChange.setTextColor(if (valueChangeDataAvailable) if (valueChange!! >= 0.0) Color.GREEN else Color.RED else Color.GRAY)
    }
}