package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class DeliriumOrbViewHolder : PoeNinjaViewHolder
{
    var deliriumOrbNameTextView: TextView
    var deliriumOrbIconImageView: ImageView

    var deliriumOrbChaosValueAffix: TextView
    var deliriumOrbExaltValueAffix: TextView
    var deliriumOrbConfidenceMarker: View

    var deliriumOrbValueChange: TextView

    constructor(v: View) : super(v)
    {
        deliriumOrbNameTextView = v.findViewById<View>(R.id.delirium_orb_name_text_view) as TextView
        deliriumOrbIconImageView = v.findViewById<View>(R.id.delirium_orb_icon_image_view) as ImageView

        deliriumOrbChaosValueAffix = v.findViewById<View>(R.id.delirium_orb_chaos_value_affix) as TextView
        deliriumOrbExaltValueAffix = v.findViewById<View>(R.id.delirium_orb_exalt_value_affix) as TextView
        deliriumOrbConfidenceMarker = v.findViewById<View>(R.id.delirium_orb_confidence_marker) as View

        deliriumOrbValueChange = v.findViewById<View>(R.id.delirium_orb_value_change) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val deliriumOrbOverview = overview as ItemOverview
        val deliriumOrbLine = deliriumOrbOverview?.lines?.let { it[position] }

        deliriumOrbLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_delirium_orb)
                .error(R.drawable.load_error_delirium_orb)
                .into(deliriumOrbIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val count = (it.count ?: 0)
            deliriumOrbNameTextView.text = it.name
            deliriumOrbChaosValueAffix.text = chaosValueAffixText
            deliriumOrbExaltValueAffix.text = exaltValueAffixText
            deliriumOrbConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                deliriumOrbValueChange.text = valueChangeText
                deliriumOrbValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            deliriumOrbIconImageView.setImageResource(R.drawable.load_error_delirium_orb)
            deliriumOrbNameTextView.text = "N/A"
            deliriumOrbChaosValueAffix.text = "N/A \u00D7"
            deliriumOrbExaltValueAffix.text = "N/A \u00D7"
            deliriumOrbValueChange.text = "N/A"
            deliriumOrbValueChange.setTextColor(Color.GRAY)
            deliriumOrbConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}