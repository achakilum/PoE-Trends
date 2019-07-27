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
import com.bluelithalo.poetrends.view.CurrencyViewHolder
import com.bluelithalo.poetrends.view.ItemViewHolder
import com.squareup.picasso.Picasso

class PoeNinjaAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private val MAX_ITEMS_LOADED = 50

    private var currencyOverview: CurrencyOverview? = null
    private var itemOverview: ItemOverview? = null
    private var displayType: Int = 0

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
            cvh.configureViewHolder(currencyOverview, position)
        }
        else
        if (viewHolder.itemViewType == DISPLAY_ITEM)
        {
            val ivh = viewHolder as ItemViewHolder
            ivh.configureViewHolder(itemOverview, position)
        }
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
