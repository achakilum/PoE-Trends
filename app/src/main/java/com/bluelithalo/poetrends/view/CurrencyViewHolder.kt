package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.squareup.picasso.Picasso

class CurrencyViewHolder : PoeNinjaViewHolder
{
    var currencyTypeNameTextView: TextView
    var currencyIconImageView: ImageView

    var currencyBuyCountAffix: TextView
    var currencyBuyItemIcon: ImageView
    var currencyBuyCostAffix: TextView
    var currencyBuyValueChange: TextView
    var currencyBuyConfidenceMarker: View

    var currencySellCountAffix: TextView
    var currencySellItemIcon: ImageView
    var currencySellCostAffix: TextView
    var currencySellValueChange: TextView
    var currencySellConfidenceMarker: View

    constructor(v: View) : super(v)
    {
        currencyTypeNameTextView = v.findViewById<TextView>(R.id.currency_type_name_text_view)
        currencyIconImageView = v.findViewById<ImageView>(R.id.currency_icon_image_view)

        currencyBuyCountAffix = v.findViewById<TextView>(R.id.currency_buy_count_affix)
        currencyBuyItemIcon = v.findViewById<ImageView>(R.id.currency_buy_item_icon)
        currencyBuyCostAffix = v.findViewById<TextView>(R.id.currency_buy_cost_affix)
        currencyBuyValueChange = v.findViewById<TextView>(R.id.currency_buy_value_change)
        currencyBuyConfidenceMarker = v.findViewById<View>(R.id.currency_buy_confidence_marker)

        currencySellCountAffix = v.findViewById<TextView>(R.id.currency_sell_count_affix)
        currencySellItemIcon = v.findViewById<ImageView>(R.id.currency_sell_item_icon)
        currencySellCostAffix = v.findViewById<TextView>(R.id.currency_sell_cost_affix)
        currencySellValueChange = v.findViewById<TextView>(R.id.currency_sell_value_change)
        currencySellConfidenceMarker = v.findViewById<View>(R.id.currency_sell_confidence_marker)
    }

    fun configureViewHolder(overview: Overview?, position: Int, iconUrl: String)
    {
        val currencyOverview = overview as CurrencyOverview
        val currencyLine = currencyOverview?.lines?.let { it[position] }
        val currencyTypeName = currencyLine?.currencyTypeName

        currencyTypeNameTextView.text = currencyTypeName
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.load_placeholder_currency)
            .error(R.drawable.load_error_currency)
            .into(currencyIconImageView)
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.load_placeholder_currency)
            .error(R.drawable.load_error_currency)
            .into(currencyBuyItemIcon)
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.load_placeholder_currency)
            .error(R.drawable.load_error_currency)
            .into(currencySellItemIcon)

        // Buy data available
        currencyLine?.receive?.let {

            val buyValue = it.value
            buyValue?.let {
                val buyCountAffix = String.format("%.1f", Math.max(1.0, 1.0 / buyValue)) + " \u00D7"
                val buyCostAffix = "for " + String.format("%.1f", Math.max(1.0, buyValue)) + " \u00D7"
                currencyBuyCountAffix.text = buyCountAffix
                currencyBuyCostAffix.text = buyCostAffix
            }

            // Buy value change data available
            currencyLine.lowConfidenceReceiveSparkLine?.let {
                val buyValueChange = it.totalChange
                buyValueChange?.let {
                    val buyValueChangeText = (if (buyValueChange > 0.0) "+" else "") + String.format("%.1f", buyValueChange) + "%"
                    currencyBuyValueChange.text = buyValueChangeText
                    currencyBuyValueChange.setTextColor(if (buyValueChange >= 0.0) Color.GREEN else Color.RED)
                }

            }

            val count = it.count ?: 0
            currencyBuyConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })
        } ?: run {
            val buyCountAffix = "N/A \u00D7"
            val buyCostAffix = "for N/A \u00D7"
            currencyBuyCountAffix.text = buyCountAffix
            currencyBuyCostAffix.text = buyCostAffix
            currencyBuyValueChange.text = "N/A"
            currencyBuyValueChange.setTextColor(Color.GRAY)
            currencyBuyConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }

        // Sell data available
        currencyLine?.pay?.let {
            val sellValue = it.value
            sellValue?.let {
                val sellCountAffix = String.format("%.1f", Math.max(1.0, sellValue)) + " \u00D7"
                val sellCostAffix = "for " + String.format("%.1f", Math.max(1.0, 1.0 / sellValue)) + " \u00D7"
                currencySellCountAffix.text = sellCountAffix
                currencySellCostAffix.text = sellCostAffix
            }
            // Sell value change data available
            currencyLine.lowConfidencePaySparkLine?.let {
                val sellValueChange = it.totalChange
                sellValueChange?.let {
                    val sellValueChangeText = (if (sellValueChange > 0.0) "+" else "") + String.format("%.1f", sellValueChange) + "%"
                    currencySellValueChange.text = sellValueChangeText
                    currencySellValueChange.setTextColor(if (sellValueChange >= 0.0) Color.GREEN else Color.RED)
                }
            }

            val count = it.count ?: 0
            currencySellConfidenceMarker.setBackgroundResource(when {
                count < 5 -> R.color.confidence_low
                count < 10 -> R.color.confidence_medium
                else -> R.color.confidence_high
            })
        } ?: run {
            val sellCountAffix = "N/A \u00D7"
            val sellCostAffix = "for N/A \u00D7"
            currencySellCountAffix.text = sellCountAffix
            currencySellCostAffix.text = sellCostAffix
            currencySellValueChange.text = "N/A"
            currencySellValueChange.setTextColor(Color.GRAY)
            currencySellConfidenceMarker.setBackgroundResource(R.color.confidence_none)
        }
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val currencyOverview = overview as CurrencyOverview
        val currencyLine = currencyOverview?.lines?.let { it[position] }
        val currencyTypeName = currencyLine?.currencyTypeName
        val iconUrl = this.getIconUrlForCurrencyType(overview as CurrencyOverview, currencyLine?.currencyTypeName)

        iconUrl?.let { configureViewHolder(overview, position, it) }
    }

    fun getIconUrlForCurrencyType(currencyOverview: CurrencyOverview?, currencyTypeName: String?): String?
    {
        var iconUrl: String? = ""

        currencyOverview?.currencyDetails?.let {
            for (cd in it)
            {
                if (cd.name == currencyTypeName)
                {
                    iconUrl = cd.icon
                }
            }
        }

        return iconUrl
    }
}