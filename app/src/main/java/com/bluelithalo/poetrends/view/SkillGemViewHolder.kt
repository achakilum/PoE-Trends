package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class SkillGemViewHolder : PoeNinjaViewHolder
{
    var skillGemNameTextView: TextView
    var skillGemIconImageView: ImageView

    var skillGemChaosValueAffix: TextView
    var skillGemExaltValueAffix: TextView
    var skillGemConfidenceMarker: View

    var skillGemValueChange: TextView

    var skillGemLevelTextView: TextView
    var skillGemQualityTextView: TextView
    var skillGemCorruptedImageView: ImageView

    constructor(v: View) : super(v)
    {
        skillGemNameTextView = v.findViewById<View>(R.id.skill_gem_name_text_view) as TextView
        skillGemIconImageView = v.findViewById<View>(R.id.skill_gem_icon_image_view) as ImageView

        skillGemChaosValueAffix = v.findViewById<View>(R.id.skill_gem_chaos_value_affix) as TextView
        skillGemExaltValueAffix = v.findViewById<View>(R.id.skill_gem_exalt_value_affix) as TextView
        skillGemConfidenceMarker = v.findViewById<View>(R.id.skill_gem_confidence_marker) as View

        skillGemValueChange = v.findViewById<View>(R.id.skill_gem_value_change) as TextView

        skillGemLevelTextView = v.findViewById<View>(R.id.skill_gem_level_text_view) as TextView
        skillGemQualityTextView = v.findViewById<View>(R.id.skill_gem_quality_text_view) as TextView
        skillGemCorruptedImageView = v.findViewById<View>(R.id.skill_gem_corrupted_image_view) as ImageView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val skillGemOverview = overview as ItemOverview
        val skillGemLine = skillGemOverview?.lines?.let { it[position] }

        skillGemLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_skill_gem)
                .error(R.drawable.load_error_skill_gem)
                .into(skillGemIconImageView)

            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            val skillGemQualityText = "+${it.gemQuality}"
            val count = (it.count ?: 0)
            skillGemNameTextView.text = it.name
            skillGemChaosValueAffix.text = chaosValueAffixText
            skillGemExaltValueAffix.text = exaltValueAffixText
            skillGemLevelTextView.text = "${it.gemLevel}"
            skillGemQualityTextView.text = skillGemQualityText
            skillGemCorruptedImageView.setImageResource(if (it.corrupted == true) R.drawable.ic_corrupted else android.R.color.transparent)
            skillGemConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })

            it.lowConfidenceSparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                skillGemValueChange.text = valueChangeText
                skillGemValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            skillGemIconImageView.setImageResource(R.drawable.load_error_skill_gem)
            skillGemNameTextView.text = "N/A"
            skillGemChaosValueAffix.text = "N/A \u00D7"
            skillGemExaltValueAffix.text = "N/A \u00D7"
            skillGemLevelTextView.text = "X"
            skillGemValueChange.text = "N/A"
            skillGemValueChange.setTextColor(Color.GRAY)
            skillGemConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }
}