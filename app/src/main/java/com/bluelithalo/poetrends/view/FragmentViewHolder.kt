package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.squareup.picasso.Picasso

class FragmentViewHolder : PoeNinjaViewHolder
{
    var fragmentTypeNameTextView: TextView
    var fragmentIconImageView: ImageView

    var fragmentBuyCountAffix: TextView
    var fragmentBuyItemIcon: ImageView
    var fragmentBuyCostAffix: TextView
    var fragmentBuyValueChange: TextView

    var fragmentSellCountAffix: TextView
    var fragmentSellItemIcon: ImageView
    var fragmentSellCostAffix: TextView
    var fragmentSellValueChange: TextView

    constructor(v: View) : super(v)
    {
        fragmentTypeNameTextView = v.findViewById<TextView>(R.id.fragment_type_name_text_view)
        fragmentIconImageView = v.findViewById<ImageView>(R.id.fragment_icon_image_view)

        fragmentBuyCountAffix = v.findViewById<TextView>(R.id.fragment_buy_count_affix)
        fragmentBuyItemIcon = v.findViewById<ImageView>(R.id.fragment_buy_item_icon)
        fragmentBuyCostAffix = v.findViewById<TextView>(R.id.fragment_buy_cost_affix)
        fragmentBuyValueChange = v.findViewById<TextView>(R.id.fragment_buy_value_change)

        fragmentSellCountAffix = v.findViewById<TextView>(R.id.fragment_sell_count_affix)
        fragmentSellItemIcon = v.findViewById<ImageView>(R.id.fragment_sell_item_icon)
        fragmentSellCostAffix = v.findViewById<TextView>(R.id.fragment_sell_cost_affix)
        fragmentSellValueChange = v.findViewById<TextView>(R.id.fragment_sell_value_change)
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val fragmentOverview = overview as CurrencyOverview
        val fragmentLine = fragmentOverview?.lines?.let { it[position] }
        val fragmentTypeName = fragmentLine?.currencyTypeName
        val iconUrl = this.getIconUrlForFragmentType(fragmentOverview, fragmentTypeName)

        fragmentTypeNameTextView.text = fragmentTypeName
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.fragment_load_placeholder)
            .error(R.drawable.fragment_load_error)
            .into(fragmentIconImageView)
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.fragment_load_placeholder)
            .error(R.drawable.fragment_load_error)
            .into(fragmentBuyItemIcon)
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.fragment_load_placeholder)
            .error(R.drawable.fragment_load_error)
            .into(fragmentSellItemIcon)

        // Buy data available
        fragmentLine?.receive?.let {
            val buyValue = it.value
            buyValue?.let {
                val buyCountAffix = String.format("%.1f", Math.max(1.0, 1.0 / buyValue)) + " \u00D7"
                val buyCostAffix = "for " + String.format("%.1f", Math.max(1.0, buyValue)) + " \u00D7"
                fragmentBuyCountAffix.text = buyCountAffix
                fragmentBuyCostAffix.text = buyCostAffix
            }
            // Buy value change data available
            fragmentLine.lowConfidenceReceiveSparkLine?.let {
                val buyValueChange = it.totalChange
                buyValueChange?.let {
                    val buyValueChangeText = (if (buyValueChange > 0.0) "+" else "") + String.format("%.1f", buyValueChange) + "%"
                    fragmentBuyValueChange.text = buyValueChangeText
                    fragmentBuyValueChange.setTextColor(if (buyValueChange >= 0.0) Color.GREEN else Color.RED)
                }
            }
        } ?: run {
            val buyCountAffix = "N/A \u00D7"
            val buyCostAffix = "for N/A \u00D7"
            fragmentBuyCountAffix.text = buyCountAffix
            fragmentBuyCostAffix.text = buyCostAffix
            fragmentBuyValueChange.text = "N/A"
            fragmentBuyValueChange.setTextColor(Color.GRAY)
        }

        // Sell data available
        fragmentLine?.pay?.let {
            val sellValue = it.value
            sellValue?.let {
                val sellCountAffix = String.format("%.1f", Math.max(1.0, sellValue)) + " \u00D7"
                val sellCostAffix = "for " + String.format("%.1f", Math.max(1.0, 1.0 / sellValue)) + " \u00D7"
                fragmentSellCountAffix.text = sellCountAffix
                fragmentSellCostAffix.text = sellCostAffix
            }
            // Sell value change data available
            fragmentLine.lowConfidencePaySparkLine?.let {
                val sellValueChange = it.totalChange
                sellValueChange?.let {
                    val sellValueChangeText = (if (sellValueChange > 0.0) "+" else "") + String.format("%.1f", sellValueChange) + "%"
                    fragmentSellValueChange.text = sellValueChangeText
                    fragmentSellValueChange.setTextColor(if (sellValueChange >= 0.0) Color.GREEN else Color.RED)
                }
            }
        } ?: run {
            val sellCountAffix = "N/A \u00D7"
            val sellCostAffix = "for N/A \u00D7"
            fragmentSellCountAffix.text = sellCountAffix
            fragmentSellCostAffix.text = sellCostAffix
            fragmentSellValueChange.text = "N/A"
            fragmentSellValueChange.setTextColor(Color.GRAY)
        }
    }

    private fun getIconUrlForFragmentType(fragmentOverview: CurrencyOverview?, fragmentTypeName: String?): String?
    {
        var iconUrl: String? = ""

        fragmentOverview?.currencyDetails?.let {
            for (cd in it)
            {
                if (cd.name == fragmentTypeName)
                {
                    iconUrl = cd.icon
                }
            }
        }

        return iconUrl
    }
}