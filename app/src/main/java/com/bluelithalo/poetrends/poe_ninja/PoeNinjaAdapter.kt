package com.bluelithalo.poetrends.poe_ninja

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview

import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.bluelithalo.poetrends.view.CurrencyViewHolder
import com.bluelithalo.poetrends.view.FragmentViewHolder
import com.bluelithalo.poetrends.view.ItemViewHolder

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

        if (overview is CurrencyOverview)
        {
            val currencyOverview = overview as CurrencyOverview
            count = currencyOverview?.lines?.size ?: 0
        }
        else
        {
            val itemOverview = overview as ItemOverview
            count = itemOverview?.lines?.size ?: 0
        }

        return Math.min(count, MAX_ITEMS_LOADED)
    }

    override fun getItemViewType(position: Int): Int
    {
        return overview?.type?.ordinal ?: Overview.Type.NONE.ordinal
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val inflater = LayoutInflater.from(viewGroup.context)
        val overviewType = Overview.Type.values()[viewType]

        return when (overviewType)
        {
            Overview.Type.CURRENCY -> CurrencyViewHolder(inflater.inflate(R.layout.currency_list_item, viewGroup, false))
            Overview.Type.FRAGMENT -> FragmentViewHolder(inflater.inflate(R.layout.fragment_list_item, viewGroup, false))
            Overview.Type.INCUBATOR -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.SCARAB -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.FOSSIL -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.RESONATOR -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.ESSENCE -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.DIVINATION_CARD -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.PROPHECY -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.SKILL_GEM -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.BASE_TYPE -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.HELMET_ENCHANT -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.UNIQUE_MAP -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.MAP -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.UNIQUE_JEWEL -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.UNIQUE_FLASK -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.UNIQUE_WEAPON -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.UNIQUE_ARMOUR -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.UNIQUE_ACCESSORY -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.BEAST -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
            Overview.Type.NONE -> ItemViewHolder(inflater.inflate(R.layout.item_list_item, viewGroup, false))
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int)
    {
        val overviewType = Overview.Type.values()[viewHolder.itemViewType]

        when (overviewType)
        {
            Overview.Type.CURRENCY -> (viewHolder as CurrencyViewHolder).configureViewHolder(overview, position)
            Overview.Type.FRAGMENT -> (viewHolder as FragmentViewHolder).configureViewHolder(overview, position)
            Overview.Type.INCUBATOR -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.SCARAB -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.FOSSIL -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.RESONATOR -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.ESSENCE -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.DIVINATION_CARD -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.PROPHECY -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.SKILL_GEM -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.BASE_TYPE -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.HELMET_ENCHANT -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_MAP -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.MAP -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_JEWEL -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_FLASK -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_WEAPON -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_ARMOUR -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_ACCESSORY -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
            Overview.Type.BEAST -> (viewHolder as ItemViewHolder).configureViewHolder(overview, position)
        }
    }
}