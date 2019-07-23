package com.bluelithalo.poetrends

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.squareup.picasso.Picasso

class PoeNinjaAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private val MAX_ITEMS_LOADED = 50

    private var currencyOverview: CurrencyOverview? = null
    private var itemOverview: ItemOverview? = null
    private var displayType: Int = 0

    class CurrencyViewHolder : RecyclerView.ViewHolder
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
    }

    class ItemViewHolder : RecyclerView.ViewHolder
    {
        var itemNameTextView: TextView
        var itemIconImageView: ImageView

        var chaosValueIcon: ImageView
        var exaltValueIcon: ImageView

        var chaosValueAffix: TextView
        var exaltValueAffix: TextView

        var itemValueChange: TextView

        constructor(v: View) : super(v)
        {
            itemNameTextView = v.findViewById<View>(R.id.item_name_text_view) as TextView
            itemIconImageView = v.findViewById<View>(R.id.item_icon_image_view) as ImageView

            chaosValueIcon = v.findViewById<View>(R.id.item_chaos_value_icon) as ImageView
            exaltValueIcon = v.findViewById<View>(R.id.item_exalt_value_icon) as ImageView

            chaosValueAffix = v.findViewById<View>(R.id.item_chaos_value_affix) as TextView
            exaltValueAffix = v.findViewById<View>(R.id.item_exalt_value_affix) as TextView

            itemValueChange = v.findViewById<View>(R.id.item_value_change) as TextView
        }
    }

    constructor(newCurrencyOverview: CurrencyOverview?, newDisplayType: Int)
    {
        currencyOverview = newCurrencyOverview
        itemOverview = null
        displayType = newDisplayType
    }

    constructor(newItemOverview: ItemOverview?, newDisplayType: Int)
    {
        currencyOverview = null
        itemOverview = newItemOverview
        displayType = newDisplayType
    }

    override fun getItemCount(): Int
    {
        var count = 0

        if (displayType == DISPLAY_CURRENCY)
        {
            count = currencyOverview?.lines!!.size
        }
        else
        if (displayType == DISPLAY_ITEM)
        {
            count = itemOverview?.lines!!.size
        }

        return Math.min(count, MAX_ITEMS_LOADED)
    }

    override fun getItemViewType(position: Int): Int
    {
        return displayType
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val inflater = LayoutInflater.from(viewGroup.context)

        return if (viewType == DISPLAY_CURRENCY)
        {
            val currencyView = inflater.inflate(R.layout.currency_list_item, viewGroup, false)
            CurrencyViewHolder(currencyView)
        }
        else
        //if (viewType == DISPLAY_ITEM)
        {
            val itemView = inflater.inflate(R.layout.item_list_item, viewGroup, false)
            ItemViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int)
    {
        if (viewHolder.itemViewType == DISPLAY_CURRENCY)
        {
            val cvh = viewHolder as CurrencyViewHolder
            configureCurrencyViewHolder(cvh, position)
        }
        else
        if (viewHolder.itemViewType == DISPLAY_ITEM)
        {
            val ivh = viewHolder as ItemViewHolder
            configureItemViewHolder(ivh, position)
        }
    }

    private fun configureCurrencyViewHolder(holder: CurrencyViewHolder, position: Int)
    {
        val currencyLine = currencyOverview?.lines!![position]
        val currencyTypeName = currencyLine.currencyTypeName
        val iconUrl = this.getIconUrlForCurrencyType(currencyTypeName)

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


        holder.currencyTypeNameTextView.text = currencyTypeName
        Picasso.get()
                .load(iconUrl)
                .placeholder(R.drawable.currency_load_placeholder)
                .error(R.drawable.currency_load_error)
                .into(holder.iconImageView)
        Picasso.get()
                .load(iconUrl)
                .placeholder(R.drawable.currency_load_placeholder)
                .error(R.drawable.currency_load_error)
                .into(holder.currencyBuyItemIcon)
        Picasso.get()
                .load(iconUrl)
                .placeholder(R.drawable.currency_load_placeholder)
                .error(R.drawable.currency_load_error)
                .into(holder.currencySellItemIcon)

        val buyCountAffix = if (buyDataAvailable) String.format("%.1f", Math.max(1.0, 1.0 / buyValue!!)) + " \u00D7" else "N/A \u00D7"
        val buyCostAffix = if (buyDataAvailable) "for " + String.format("%.1f", Math.max(1.0, buyValue!!)) + " \u00D7" else "for N/A \u00D7"

        val sellCountAffix = if (sellDataAvailable) String.format("%.1f", Math.max(1.0, sellValue!!)) + " \u00D7" else "N/A \u00D7"
        val sellCostAffix = if (sellDataAvailable) "for " + String.format("%.1f", Math.max(1.0, 1.0 / sellValue!!)) + " \u00D7" else "for N/A \u00D7"

        holder.currencyBuyCountAffix.text = buyCountAffix
        holder.currencyBuyCostAffix.text = buyCostAffix

        holder.currencySellCountAffix.text = sellCountAffix
        holder.currencySellCostAffix.text = sellCostAffix

        holder.currencyBuyValueChange.text = if (buyDataAvailable) buyValueChangeText else "N/A"
        holder.currencyBuyValueChange.setTextColor(if (buyDataAvailable) if (buyValueChange >= 0.0) Color.GREEN else Color.RED else Color.GRAY)
        holder.currencySellValueChange.text = if (sellDataAvailable) sellValueChangeText else "N/A"
        holder.currencySellValueChange.setTextColor(if (sellDataAvailable) if (sellValueChange >= 0.0) Color.GREEN else Color.RED else Color.GRAY)
    }

    private fun configureItemViewHolder(holder: ItemViewHolder, position: Int)
    {
        val itemLine = itemOverview?.lines!![position]
        val itemName = itemLine?.name

        val valueDataAvailable = (itemLine != null)
        val valueChangeDataAvailable = itemLine.sparkline != null

        val chaosValue = if (valueDataAvailable) itemLine.chaosValue else 0.0
        val exaltValue = if (valueDataAvailable) itemLine.exaltedValue else 0.0
        val valueChange = if (valueChangeDataAvailable) itemLine.sparkline?.totalChange else 0.0

        val chaosValueAffix = if (valueDataAvailable) String.format("%.1f", chaosValue) + " \u00D7" else "N/A"
        val exaltValueAffix = if (valueDataAvailable) String.format("%.1f", exaltValue) + " \u00D7" else "N/A"
        val valueChangeText = if (valueChangeDataAvailable) (if (valueChange!! > 0.0) "+" else "") + String.format("%.1f", valueChange) + "%" else "N/A"
        val iconUrl = if (valueDataAvailable) itemLine.icon else ""

        Picasso.get()
                .load(iconUrl)
                .placeholder(R.drawable.item_load_placeholder)
                .error(R.drawable.item_load_error)
                .into(holder.itemIconImageView)

        holder.itemNameTextView.text = itemName

        holder.chaosValueAffix.text = chaosValueAffix
        holder.exaltValueAffix.text = exaltValueAffix

        holder.itemValueChange.text = if (valueChangeDataAvailable) valueChangeText else "N/A"
        holder.itemValueChange.setTextColor(if (valueChangeDataAvailable) if (valueChange!! >= 0.0) Color.GREEN else Color.RED else Color.GRAY)
    }

    private fun getIconUrlForCurrencyType(currencyTypeName: String?): String?
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

    fun getCurrencyOverview() : CurrencyOverview?
    {
        return currencyOverview;
    }

    fun setCurrencyOverview(newCO : CurrencyOverview?)
    {
        currencyOverview = newCO
        itemOverview = null
        displayType = DISPLAY_CURRENCY
    }

    fun getItemOverview() : ItemOverview?
    {
        return itemOverview;
    }

    fun setItemOverview(newIO : ItemOverview?)
    {
        currencyOverview = null
        itemOverview = newIO
        displayType = DISPLAY_ITEM
    }

    companion object
    {
        val DISPLAY_CURRENCY = 0
        val DISPLAY_ITEM = 1
    }
}
