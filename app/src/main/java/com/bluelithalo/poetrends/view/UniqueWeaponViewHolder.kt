package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class UniqueWeaponViewHolder : PoeNinjaViewHolder
{
    var uniqueWeaponNameTextView: TextView
    var uniqueWeaponBaseTextView: TextView
    var uniqueWeaponIconImageView: ImageView

    var uniqueWeaponChaosValueAffix: TextView
    var uniqueWeaponExaltValueAffix: TextView

    var uniqueWeaponValueChange: TextView

    var uniqueWeapoItemLevelTextView: TextView

    constructor(v: View) : super(v)
    {
        uniqueWeaponNameTextView = v.findViewById<View>(R.id.unique_weapon_name_text_view) as TextView
        uniqueWeaponBaseTextView = v.findViewById<View>(R.id.unique_weapon_base_text_view) as TextView
        uniqueWeaponIconImageView = v.findViewById<View>(R.id.unique_weapon_icon_image_view) as ImageView

        uniqueWeaponChaosValueAffix = v.findViewById<View>(R.id.unique_weapon_chaos_value_affix) as TextView
        uniqueWeaponExaltValueAffix = v.findViewById<View>(R.id.unique_weapon_exalt_value_affix) as TextView

        uniqueWeaponValueChange = v.findViewById<View>(R.id.unique_weapon_value_change) as TextView

        uniqueWeapoItemLevelTextView = v.findViewById<View>(R.id.unique_weapon_item_level_text_view) as TextView
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val uniqueWeaponOverview = overview as ItemOverview
        val uniqueWeaponLine = uniqueWeaponOverview?.lines?.let { it[position] }

        uniqueWeaponLine?.let {
            Picasso.get()
                .load(it.icon)
                .placeholder(R.drawable.load_placeholder_unique_weapon)
                .error(R.drawable.load_error_unique_weapon)
                .into(uniqueWeaponIconImageView)

            val uniqueWeaponName = it.name + (if (it.links ?: 0 >= 5) ", ${it.links}L" else "") + (if (it.variant?.length ?: 0 > 0) ", ${it.variant}" else "")
            val chaosValueAffixText = String.format("%.1f", it.chaosValue) + " \u00D7"
            val exaltValueAffixText = String.format("%.1f", it.exaltedValue) + " \u00D7"
            uniqueWeaponNameTextView.text = uniqueWeaponName
            uniqueWeaponBaseTextView.text = it.baseType
            uniqueWeaponChaosValueAffix.text = chaosValueAffixText
            uniqueWeaponExaltValueAffix.text = exaltValueAffixText
            uniqueWeapoItemLevelTextView.text = "${it.levelRequired}"

            it.sparkline?.totalChange?.let {
                val valueChangeText = (if (it > 0.0) "+" else "") + String.format("%.1f", it) + "%"
                uniqueWeaponValueChange.text = valueChangeText
                uniqueWeaponValueChange.setTextColor(if (it >= 0.0) Color.GREEN else Color.RED)
            }
        } ?: run {
            uniqueWeaponIconImageView.setImageResource(R.drawable.load_error_unique_weapon)
            uniqueWeaponNameTextView.text = "N/A"
            uniqueWeaponBaseTextView.text = "N/A"
            uniqueWeaponChaosValueAffix.text = "N/A \u00D7"
            uniqueWeaponExaltValueAffix.text = "N/A \u00D7"
            uniqueWeapoItemLevelTextView.text = "X"
            uniqueWeaponValueChange.text = "N/A"
            uniqueWeaponValueChange.setTextColor(Color.GRAY)
        }
    }
}