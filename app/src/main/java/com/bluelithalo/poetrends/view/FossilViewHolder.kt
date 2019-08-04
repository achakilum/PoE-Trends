package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class FossilViewHolder : PoeNinjaViewHolder
{
    var fossilNameTextView: TextView
    var fossilIconImageView: ImageView

    var fossilChaosValueAffix: TextView
    var fossilExaltValueAffix: TextView

    var fossilValueChange: TextView

    constructor(v: View) : super(v)
    {
        fossilNameTextView = v.findViewById<View>(R.id.fossil_name_text_view) as TextView
        fossilIconImageView = v.findViewById<View>(R.id.fossil_icon_image_view) as ImageView

        fossilChaosValueAffix = v.findViewById<View>(R.id.fossil_chaos_value_affix) as TextView
        fossilExaltValueAffix = v.findViewById<View>(R.id.fossil_exalt_value_affix) as TextView

        fossilValueChange = v.findViewById<View>(R.id.fossil_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val fossilOverview = overview as ItemOverview
        val fossilLine = fossilOverview?.lines?.let { it[position] }

        fossilLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_fossil)
                .error(R.drawable.load_error_fossil)
                .into(fossilIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            fossilNameTextView.text = it.name
            fossilChaosValueAffix.text = chaosValueAffixText
            fossilExaltValueAffix.text = exaltValueAffixText

            it.sparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                fossilValueChange.text = valueChangeText
                fossilValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            fossilIconImageView.setImageResource(R.drawable.load_error_fossil)
            fossilNameTextView.text = "N/A"
            fossilChaosValueAffix.text = "N/A \u00D7"
            fossilExaltValueAffix.text = "N/A \u00D7"
            fossilValueChange.text = "N/A"
            fossilValueChange.setTextColor(Color.GRAY)
        }
    }
}