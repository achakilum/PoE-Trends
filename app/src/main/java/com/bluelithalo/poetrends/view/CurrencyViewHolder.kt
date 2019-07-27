package com.bluelithalo.poetrends.view

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.squareup.picasso.Picasso

class CurrencyViewHolder : PoeNinjaViewHolder
{
    var currencyTypeNameTextView: TextView
    var iconImageView: ImageView

    var currencyBuyCountAffix: TextView
    var currencyBuyItemIcon: ImageView
    var currencyBuyCostAffix: TextView
    var currencyBuyValueChange: TextView

    var currencySellCountAffix: TextView
    var currencySellItemIcon: ImageView
    var currencySellCostAffix: TextView
    var currencySellValueChange: TextView

    constructor(v: View) : super(v)
    {
        currencyTypeNameTextView = v.findViewById<TextView>(R.id.currency_type_name_text_view)
        iconImageView = v.findViewById<ImageView>(R.id.icon_image_view)

        currencyBuyCountAffix = v.findViewById<TextView>(R.id.currency_buy_count_affix)
        currencyBuyItemIcon = v.findViewById<ImageView>(R.id.currency_buy_item_icon)
        currencyBuyCostAffix = v.findViewById<TextView>(R.id.currency_buy_cost_affix)
        currencyBuyValueChange = v.findViewById<TextView>(R.id.currency_buy_value_change)

        currencySellCountAffix = v.findViewById<TextView>(R.id.currency_sell_count_affix)
        currencySellItemIcon = v.findViewById<ImageView>(R.id.currency_sell_item_icon)
        currencySellCostAffix = v.findViewById<TextView>(R.id.currency_sell_cost_affix)
        currencySellValueChange = v.findViewById<TextView>(R.id.currency_sell_value_change)
    }

    override fun configureViewHolder(overview: Overview?, position: Int)
    {
        val currencyOverview = overview as CurrencyOverview
        val currencyLine = currencyOverview?.lines!![position]
        val currencyTypeName = currencyLine.currencyTypeName
        val iconUrl = this.getIconUrlForCurrencyType(currencyOverview, currencyTypeName)

        val buyDataAvailable = (currencyLine.receive != null)
        val sellDataAvailable = (currencyLine.pay != null)
        val buyValueChangeDataAvailable = currencyLine.lowConfidenceReceiveSparkLine != null
        val sellValueChangeDataAvailable = currencyLine.lowConfidencePaySparkLine != null

        val buyValue = if (buyDataAvailable) currencyLine.receive?.value else 1.0 // Pay 23220.000000000000000000000000 Chaos, Get 1.0 Mirror of Kalandra
        val sellValue = if (sellDataAvailable) currencyLine.pay?.value else 1.0 // Pay 1.0 Mirror of Kalandra, Get (1 / 0.0000437502569411764705882353 = 22857.0086193) Chaos
        val buyValueChange = if (buyValueChangeDataAvailable) currencyLine.lowConfidenceReceiveSparkLine?.totalChange else 0.0
        val sellValueChange = if (sellValueChangeDataAvailable) currencyLine.lowConfidencePaySparkLine?.totalChange else 0.0
        val buyValueChangeText = (if (buyValueChange!! > 0.0) "+" else "") + String.format("%.1f", buyValueChange) + "%"
        val sellValueChangeText = (if (sellValueChange!! > 0.0) "+" else "") + String.format("%.1f", sellValueChange) + "%"


        currencyTypeNameTextView.text = currencyTypeName
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.currency_load_placeholder)
            .error(R.drawable.currency_load_error)
            .into(iconImageView)
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.currency_load_placeholder)
            .error(R.drawable.currency_load_error)
            .into(currencyBuyItemIcon)
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.currency_load_placeholder)
            .error(R.drawable.currency_load_error)
            .into(currencySellItemIcon)

        val buyCountAffix = if (buyDataAvailable) String.format("%.1f", Math.max(1.0, 1.0 / buyValue!!)) + " \u00D7" else "N/A \u00D7"
        val buyCostAffix = if (buyDataAvailable) "for " + String.format("%.1f", Math.max(1.0, buyValue!!)) + " \u00D7" else "for N/A \u00D7"

        val sellCountAffix = if (sellDataAvailable) String.format("%.1f", Math.max(1.0, sellValue!!)) + " \u00D7" else "N/A \u00D7"
        val sellCostAffix = if (sellDataAvailable) "for " + String.format("%.1f", Math.max(1.0, 1.0 / sellValue!!)) + " \u00D7" else "for N/A \u00D7"

        currencyBuyCountAffix.text = buyCountAffix
        currencyBuyCostAffix.text = buyCostAffix

        currencySellCountAffix.text = sellCountAffix
        currencySellCostAffix.text = sellCostAffix

        currencyBuyValueChange.text = if (buyDataAvailable) buyValueChangeText else "N/A"
        currencyBuyValueChange.setTextColor(if (buyDataAvailable) if (buyValueChange >= 0.0) Color.GREEN else Color.RED else Color.GRAY)
        currencySellValueChange.text = if (sellDataAvailable) sellValueChangeText else "N/A"
        currencySellValueChange.setTextColor(if (sellDataAvailable) if (sellValueChange >= 0.0) Color.GREEN else Color.RED else Color.GRAY)
    }

    private fun getIconUrlForCurrencyType(currencyOverview: CurrencyOverview?, currencyTypeName: String?): String?
    {
        var iconUrl: String? = ""

        for (cd in currencyOverview?.currencyDetails!!)
        {
            if (cd.name == currencyTypeName)
            {
                iconUrl = cd.icon
            }
        }

        return iconUrl
    }
}