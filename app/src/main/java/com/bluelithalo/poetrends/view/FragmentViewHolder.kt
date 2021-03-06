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
    var fragmentBuyConfidenceMarker: View

    var fragmentSellCountAffix: TextView
    var fragmentSellItemIcon: ImageView
    var fragmentSellCostAffix: TextView
    var fragmentSellValueChange: TextView
    var fragmentSellConfidenceMarker: View

    constructor(v: View) : super(v)
    {
        fragmentTypeNameTextView = v.findViewById<TextView>(R.id.fragment_type_name_text_view)
        fragmentIconImageView = v.findViewById<ImageView>(R.id.fragment_icon_image_view)

        fragmentBuyCountAffix = v.findViewById<TextView>(R.id.fragment_buy_count_affix)
        fragmentBuyItemIcon = v.findViewById<ImageView>(R.id.fragment_buy_item_icon)
        fragmentBuyCostAffix = v.findViewById<TextView>(R.id.fragment_buy_cost_affix)
        fragmentBuyValueChange = v.findViewById<TextView>(R.id.fragment_buy_value_change)
        fragmentBuyConfidenceMarker = v.findViewById<View>(R.id.fragment_buy_confidence_marker)

        fragmentSellCountAffix = v.findViewById<TextView>(R.id.fragment_sell_count_affix)
        fragmentSellItemIcon = v.findViewById<ImageView>(R.id.fragment_sell_item_icon)
        fragmentSellCostAffix = v.findViewById<TextView>(R.id.fragment_sell_cost_affix)
        fragmentSellValueChange = v.findViewById<TextView>(R.id.fragment_sell_value_change)
        fragmentSellConfidenceMarker = v.findViewById<View>(R.id.fragment_sell_confidence_marker)
    }

    fun configureViewHolder(overview: Overview?, position: Int, iconUrl: String)
    {
        val fragmentOverview = overview as CurrencyOverview
        val fragmentLine = fragmentOverview?.lines?.let { it[position] }
        val fragmentTypeName = fragmentLine?.currencyTypeName

        fragmentTypeNameTextView.text = fragmentTypeName
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.load_placeholder_fragment)
            .error(R.drawable.load_error_fragment)
            .into(fragmentIconImageView)
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.load_placeholder_fragment)
            .error(R.drawable.load_error_fragment)
            .into(fragmentBuyItemIcon)
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.load_placeholder_fragment)
            .error(R.drawable.load_error_fragment)
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

            val count = it.count ?: 0
            fragmentBuyConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })
        } ?: run {
            val buyCountAffix = "N/A \u00D7"
            val buyCostAffix = "for N/A \u00D7"
            fragmentBuyCountAffix.text = buyCountAffix
            fragmentBuyCostAffix.text = buyCostAffix
            fragmentBuyValueChange.text = "N/A"
            fragmentBuyValueChange.setTextColor(Color.GRAY)
            fragmentBuyConfidenceMarker.setBackgroundResource(R.color.confidence_none)
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

            val count = it.count ?: 0
            fragmentSellConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })
        } ?: run {
            val sellCountAffix = "N/A \u00D7"
            val sellCostAffix = "for N/A \u00D7"
            fragmentSellCountAffix.text = sellCountAffix
            fragmentSellCostAffix.text = sellCostAffix
            fragmentSellValueChange.text = "N/A"
            fragmentSellValueChange.setTextColor(Color.GRAY)
            fragmentSellConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val fragmentOverview = overview as CurrencyOverview
        val fragmentLine = fragmentOverview?.lines?.let { it[position] }
        val fragmentTypeName = fragmentLine?.currencyTypeName
        val iconUrl = this.getIconUrlForFragmentType(fragmentOverview, fragmentTypeName)

        iconUrl?.let { configureViewHolder(overview, position, it) }
    }

    fun getIconUrlForFragmentType(fragmentOverview: CurrencyOverview?, fragmentTypeName: String?): String?
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