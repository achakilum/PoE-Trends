package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class BaseTypeViewHolder : PoeNinjaViewHolder
{
    var baseTypeNameTextView: TextView
    var baseTypeIconImageView: ImageView

    var baseTypeChaosValueAffix: TextView
    var baseTypeExaltValueAffix: TextView
    var baseTypeConfidenceMarker: View

    var baseTypeValueChange: TextView

    var baseTypeItemLevelTextView: TextView
    var baseTypeVariantImageView: ImageView

    constructor(v: View) : super(v)
    {
        baseTypeNameTextView = v.findViewById<View>(R.id.base_type_name_text_view) as TextView
        baseTypeIconImageView = v.findViewById<View>(R.id.base_type_icon_image_view) as ImageView

        baseTypeChaosValueAffix = v.findViewById<View>(R.id.base_type_chaos_value_affix) as TextView
        baseTypeExaltValueAffix = v.findViewById<View>(R.id.base_type_exalt_value_affix) as TextView
        baseTypeConfidenceMarker = v.findViewById<View>(R.id.base_type_confidence_marker) as View

        baseTypeValueChange = v.findViewById<View>(R.id.base_type_value_change) as TextView

        baseTypeItemLevelTextView = v.findViewById<View>(R.id.base_type_item_level_text_view) as TextView
        baseTypeVariantImageView = v.findViewById<View>(R.id.base_type_variant_image_view) as ImageView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val baseTypeOverview = overview as ItemOverview
        val baseTypeLine = baseTypeOverview?.lines?.let { it[position] }

        baseTypeLine?.let {
            var iconUrl = it.icon

            it.variant?.let {
                if (it.equals("Elder")) {
                    baseTypeVariantImageView.setImageResource(R.drawable.ic_elder)
                    iconUrl += "&elder=1"
                }
                else if (it.equals("Shaper")) {
                    baseTypeVariantImageView.setImageResource(R.drawable.ic_shaper)
                    iconUrl += "&shaper=1"
                }
            } ?: run {
                baseTypeVariantImageView.setImageResource(android.R.color.transparent)
            }

            Picasso.get()
                .load(iconUrl)
                .placeholder(R.drawable.load_placeholder_skill_gem)
                .error(R.drawable.load_error_skill_gem)
                .into(baseTypeIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val baseTypeLevelText = if (it.levelRequired ?: 0 >= 86) "86+" else "${it.levelRequired}"
            val count = (it.count ?: 0)
            baseTypeNameTextView.text = it.name
            baseTypeChaosValueAffix.text = chaosValueAffixText
            baseTypeExaltValueAffix.text = exaltValueAffixText
            baseTypeItemLevelTextView.text = baseTypeLevelText
            baseTypeConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                baseTypeValueChange.text = valueChangeText
                baseTypeValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            baseTypeIconImageView.setImageResource(R.drawable.load_error_skill_gem)
            baseTypeNameTextView.text = "N/A"
            baseTypeChaosValueAffix.text = "N/A \u00D7"
            baseTypeExaltValueAffix.text = "N/A \u00D7"
            baseTypeItemLevelTextView.text = "X"
            baseTypeValueChange.text = "N/A"
            baseTypeValueChange.setTextColor(Color.GRAY)
            baseTypeConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}