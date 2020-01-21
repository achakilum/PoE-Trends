package com.bluelithalo.poetrends.poe_ninja

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview

import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.bluelithalo.poetrends.view.*
import com.google.gson.Gson
import java.util.*

class OverviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private var itemsLoaded = 50
    private var overview: Overview? = null
    private var overviewContainer: OverviewContainer? = null

    constructor(newOverviewContainer: OverviewContainer, newOverview: Overview?)
    {
        overviewContainer = newOverviewContainer
        overview = newOverview
    }

    fun growItemCount()
    {
        itemsLoaded = Math.min(getOverviewSize(), itemsLoaded + 50)
    }

    override fun getItemCount(): Int
    {
        return Math.min(getOverviewSize(), itemsLoaded)
    }

    fun getOverviewSize() : Int
    {
        var size = 0

        if (overview is CurrencyOverview)
        {
            val currencyOverview = overview as CurrencyOverview
            size = currencyOverview?.lines?.size ?: 0
        }
        else
        {
            val itemOverview = overview as ItemOverview
            size = itemOverview?.lines?.size ?: 0
        }

        return size
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
            Overview.Type.WATCHSTONE -> WatchstoneViewHolder(inflater.inflate(R.layout.watchstone_list_item, viewGroup, false))
            Overview.Type.OIL -> OilViewHolder(inflater.inflate(R.layout.oil_list_item, viewGroup, false))
            Overview.Type.INCUBATOR -> IncubatorViewHolder(inflater.inflate(R.layout.incubator_list_item, viewGroup, false))
            Overview.Type.SCARAB -> ScarabViewHolder(inflater.inflate(R.layout.scarab_list_item, viewGroup, false))
            Overview.Type.FOSSIL -> FossilViewHolder(inflater.inflate(R.layout.fossil_list_item, viewGroup, false))
            Overview.Type.RESONATOR -> ResonatorViewHolder(inflater.inflate(R.layout.resonator_list_item, viewGroup, false))
            Overview.Type.ESSENCE -> EssenceViewHolder(inflater.inflate(R.layout.essence_list_item, viewGroup, false))
            Overview.Type.DIVINATION_CARD -> DivinationCardViewHolder(inflater.inflate(R.layout.divination_card_list_item, viewGroup, false))
            Overview.Type.PROPHECY -> ProphecyViewHolder(inflater.inflate(R.layout.prophecy_list_item, viewGroup, false))
            Overview.Type.SKILL_GEM -> SkillGemViewHolder(inflater.inflate(R.layout.skill_gem_list_item, viewGroup, false))
            Overview.Type.BASE_TYPE -> BaseTypeViewHolder(inflater.inflate(R.layout.base_type_list_item, viewGroup, false))
            Overview.Type.HELMET_ENCHANT -> HelmetEnchantViewHolder(inflater.inflate(R.layout.helmet_enchant_list_item, viewGroup, false))
            Overview.Type.UNIQUE_MAP -> UniqueMapViewHolder(inflater.inflate(R.layout.unique_map_list_item, viewGroup, false))
            Overview.Type.MAP -> MapViewHolder(inflater.inflate(R.layout.map_list_item, viewGroup, false))
            Overview.Type.UNIQUE_JEWEL -> UniqueJewelViewHolder(inflater.inflate(R.layout.unique_jewel_list_item, viewGroup, false))
            Overview.Type.UNIQUE_FLASK -> UniqueFlaskViewHolder(inflater.inflate(R.layout.unique_flask_list_item, viewGroup, false))
            Overview.Type.UNIQUE_WEAPON -> UniqueWeaponViewHolder(inflater.inflate(R.layout.unique_weapon_list_item, viewGroup, false))
            Overview.Type.UNIQUE_ARMOUR -> UniqueArmourViewHolder(inflater.inflate(R.layout.unique_armour_list_item, viewGroup, false))
            Overview.Type.UNIQUE_ACCESSORY -> UniqueAccessoryViewHolder(inflater.inflate(R.layout.unique_accessory_list_item, viewGroup, false))
            Overview.Type.BEAST -> BeastViewHolder(inflater.inflate(R.layout.beast_list_item, viewGroup, false))
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
            Overview.Type.WATCHSTONE -> (viewHolder as WatchstoneViewHolder).configureViewHolder(overview, position)
            Overview.Type.OIL -> (viewHolder as OilViewHolder).configureViewHolder(overview, position)
            Overview.Type.INCUBATOR -> (viewHolder as IncubatorViewHolder).configureViewHolder(overview, position)
            Overview.Type.SCARAB -> (viewHolder as ScarabViewHolder).configureViewHolder(overview, position)
            Overview.Type.FOSSIL -> (viewHolder as FossilViewHolder).configureViewHolder(overview, position)
            Overview.Type.RESONATOR -> (viewHolder as ResonatorViewHolder).configureViewHolder(overview, position)
            Overview.Type.ESSENCE -> (viewHolder as EssenceViewHolder).configureViewHolder(overview, position)
            Overview.Type.DIVINATION_CARD -> (viewHolder as DivinationCardViewHolder).configureViewHolder(overview, position)
            Overview.Type.PROPHECY -> (viewHolder as ProphecyViewHolder).configureViewHolder(overview, position)
            Overview.Type.SKILL_GEM -> (viewHolder as SkillGemViewHolder).configureViewHolder(overview, position)
            Overview.Type.BASE_TYPE -> (viewHolder as BaseTypeViewHolder).configureViewHolder(overview, position)
            Overview.Type.HELMET_ENCHANT -> (viewHolder as HelmetEnchantViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_MAP -> (viewHolder as UniqueMapViewHolder).configureViewHolder(overview, position)
            Overview.Type.MAP -> (viewHolder as MapViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_JEWEL -> (viewHolder as UniqueJewelViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_FLASK -> (viewHolder as UniqueFlaskViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_WEAPON -> (viewHolder as UniqueWeaponViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_ARMOUR -> (viewHolder as UniqueArmourViewHolder).configureViewHolder(overview, position)
            Overview.Type.UNIQUE_ACCESSORY -> (viewHolder as UniqueAccessoryViewHolder).configureViewHolder(overview, position)
            Overview.Type.BEAST -> (viewHolder as BeastViewHolder).configureViewHolder(overview, position)
        }

        viewHolder.itemView.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                if (overviewType == Overview.Type.CURRENCY || overviewType == Overview.Type.FRAGMENT)
                {
                    val currencyOverview = overview as CurrencyOverview
                    val currencyLine = currencyOverview?.lines?.let { it[position] }
                    val currencyTypeName = currencyLine?.currencyTypeName

                    var iconUrl: String? = ""
                    var poeTradeId: Int = -1

                    currencyOverview?.currencyDetails?.let {
                        for (cd in it)
                        {
                            if (cd.name == currencyTypeName)
                            {
                                iconUrl = cd.icon
                                poeTradeId = cd.poeTradeId ?: -1
                            }
                        }
                    }

                    overviewContainer?.onClickCurrencyOverviewItem(overviewType, iconUrl, Gson().toJson((overview as CurrencyOverview)?.lines?.let { it[position] }), poeTradeId)
                }
                else
                {
                    overviewContainer?.onClickItemOverviewItem(overviewType, Gson().toJson((overview as ItemOverview)?.lines?.let { it[position] }))
                }
            }
        })
    }

    interface OverviewContainer
    {
        fun onClickCurrencyOverviewItem(overviewType: Overview.Type, iconUrl: String?, lineString: String?, poeTradeId: Int)
        fun onClickItemOverviewItem(overviewType: Overview.Type, lineString: String?)
    }
}