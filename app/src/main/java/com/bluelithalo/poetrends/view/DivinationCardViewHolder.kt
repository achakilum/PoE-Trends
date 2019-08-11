package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class DivinationCardViewHolder : PoeNinjaViewHolder
{
    var divinationCardNameTextView: TextView
    var divinationCardIconImageView: ImageView

    var divinationCardChaosValueAffix: TextView
    var divinationCardExaltValueAffix: TextView
    var divinationCardConfidenceMarker: View

    var divinationCardValueChange: TextView

    var divinationCardStackSizeTextView: TextView

    constructor(v: View) : super(v)
    {
        divinationCardNameTextView = v.findViewById<View>(R.id.divination_card_name_text_view) as TextView
        divinationCardIconImageView = v.findViewById<View>(R.id.divination_card_icon_image_view) as ImageView

        divinationCardChaosValueAffix = v.findViewById<View>(R.id.divination_card_chaos_value_affix) as TextView
        divinationCardExaltValueAffix = v.findViewById<View>(R.id.divination_card_exalt_value_affix) as TextView
        divinationCardConfidenceMarker = v.findViewById<View>(R.id.divination_card_confidence_marker) as View

        divinationCardValueChange = v.findViewById<View>(R.id.divination_card_value_change) as TextView

        divinationCardStackSizeTextView = v.findViewById<View>(R.id.divination_card_stack_size_text_view) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val divinationCardOverview = overview as ItemOverview
        val divinationCardLine = divinationCardOverview?.lines?.let { it[position] }

        divinationCardLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_divination_card)
                .error(R.drawable.load_error_divination_card)
                .into(divinationCardIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            divinationCardNameTextView.text = it.name
            divinationCardChaosValueAffix.text = chaosValueAffixText
            divinationCardExaltValueAffix.text = exaltValueAffixText
            divinationCardStackSizeTextView.text = "${it.stackSize}"
            divinationCardConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                divinationCardValueChange.text = valueChangeText
                divinationCardValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            divinationCardIconImageView.setImageResource(R.drawable.load_error_divination_card)
            divinationCardNameTextView.text = "N/A"
            divinationCardChaosValueAffix.text = "N/A \u00D7"
            divinationCardExaltValueAffix.text = "N/A \u00D7"
            divinationCardStackSizeTextView.text = "X"
            divinationCardValueChange.text = "N/A"
            divinationCardValueChange.setTextColor(Color.GRAY)
            divinationCardConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}