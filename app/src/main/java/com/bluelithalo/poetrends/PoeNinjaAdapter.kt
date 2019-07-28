package com.bluelithalo.poetrends

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.model.Overview

import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.bluelithalo.poetrends.view.CurrencyViewHolder
import com.bluelithalo.poetrends.view.ItemViewHolder
import com.squareup.picasso.Picasso

class PoeNinjaAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private val MAX_ITEMS_LOADED = 50
    private var overview: Overview? = null

    constructor(newOverview: Overview?)
    {
        overview = newOverview
    }

    override fun getItemCount(): Int
    {
        var count = 0

        when (overview?.type)
        {
            Overview.CURRENCY ->
            {
                val currencyOverview = overview as CurrencyOverview
                count = currencyOverview?.lines?.size ?: 0
            }
            Overview.ITEM ->
            {
                val itemOverview = overview as ItemOverview
                count = itemOverview?.lines?.size ?: 0
            }
        }

        return Math.min(count, MAX_ITEMS_LOADED)
    }

    override fun getItemViewType(position: Int): Int
    {
        return overview?.type ?: Overview.NONE
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val inflater = LayoutInflater.from(viewGroup.context)

        return when (viewType)
        {
            Overview.CURRENCY -> CurrencyViewHolder(inflater.inflate(R.layout.currency_list_item, viewGroup, false))
            Overview.ITEM -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            else -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int)
    {
        when (viewHolder.itemViewType)
        {
            Overview.CURRENCY ->
            {
                val cvh = viewHolder as CurrencyViewHolder
                cvh.configureViewHolder(overview, position)
            }
            Overview.ITEM ->
            {
                val ivh = viewHolder as ItemViewHolder
                ivh.configureViewHolder(overview, position)
            }
        }
    }
}