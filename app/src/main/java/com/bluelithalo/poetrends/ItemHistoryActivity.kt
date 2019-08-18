package com.bluelithalo.poetrends

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity;
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.bluelithalo.poetrends.model.item.Line
import com.bluelithalo.poetrends.view.*
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_item_history.*

class ItemHistoryActivity : AppCompatActivity()
{
    companion object
    {
        val LEAGUE_ID = "LEAGUE_ID"
        val ITEM_TYPE_ORDINAL = "ITEM_TYPE_ORDINAL"
        val ITEM_MODEL_STRING = "ITEM_MODEL_STRING"
    }

    var linearLayout: LinearLayout? = null
    var itemSummaryView: View? = null

    var wikiButton: Button? = null
    var listingsButton: Button? = null
    var offlineListingsButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_history)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        linearLayout = findViewById<LinearLayout>(R.id.item_history_layout)

        val leagueId: String = intent?.extras?.getString(LEAGUE_ID) ?: "Standard"
        val itemType: Overview.Type = intent?.extras?.getInt(ITEM_TYPE_ORDINAL)?.let { Overview.Type.values()[it] } ?: Overview.Type.NONE
        val itemLine: Line = Gson().fromJson(intent?.extras?.getString(ITEM_MODEL_STRING), Line::class.java)

        this.title = "History (${leagueId})"
        insertItemSummary(itemType, itemLine)
        configureItemButtons(leagueId, itemLine)
    }

    private fun insertItemSummary(itemType: Overview.Type, itemLine: Line)
    {
        val itemOverview = ItemOverview(); itemOverview.type = itemType
        val itemList: ArrayList<Line> = ArrayList<Line>(); itemList.add(itemLine); itemOverview.lines = itemList
        var itemViewHolder : PoeNinjaViewHolder? = null

        when (itemType)
        {
            Overview.Type.INCUBATOR -> itemViewHolder = IncubatorViewHolder(layoutInflater.inflate(R.layout.incubator_list_item, linearLayout, false))
            Overview.Type.SCARAB -> itemViewHolder = ScarabViewHolder(layoutInflater.inflate(R.layout.scarab_list_item, linearLayout, false))
            Overview.Type.FOSSIL -> itemViewHolder = FossilViewHolder(layoutInflater.inflate(R.layout.fossil_list_item, linearLayout, false))
            Overview.Type.RESONATOR -> itemViewHolder = ResonatorViewHolder(layoutInflater.inflate(R.layout.resonator_list_item, linearLayout, false))
            Overview.Type.ESSENCE -> itemViewHolder = EssenceViewHolder(layoutInflater.inflate(R.layout.essence_list_item, linearLayout, false))
            Overview.Type.DIVINATION_CARD -> itemViewHolder = DivinationCardViewHolder(layoutInflater.inflate(R.layout.divination_card_list_item, linearLayout, false))
            Overview.Type.PROPHECY -> itemViewHolder = ProphecyViewHolder(layoutInflater.inflate(R.layout.prophecy_list_item, linearLayout, false))
            Overview.Type.SKILL_GEM -> itemViewHolder = SkillGemViewHolder(layoutInflater.inflate(R.layout.skill_gem_list_item, linearLayout, false))
            Overview.Type.BASE_TYPE -> itemViewHolder = BaseTypeViewHolder(layoutInflater.inflate(R.layout.base_type_list_item, linearLayout, false))
            Overview.Type.HELMET_ENCHANT -> itemViewHolder = HelmetEnchantViewHolder(layoutInflater.inflate(R.layout.helmet_enchant_list_item, linearLayout, false))
            Overview.Type.UNIQUE_MAP -> itemViewHolder = UniqueMapViewHolder(layoutInflater.inflate(R.layout.unique_map_list_item, linearLayout, false))
            Overview.Type.MAP -> itemViewHolder = MapViewHolder(layoutInflater.inflate(R.layout.map_list_item, linearLayout, false))
            Overview.Type.UNIQUE_JEWEL -> itemViewHolder = UniqueJewelViewHolder(layoutInflater.inflate(R.layout.unique_jewel_list_item, linearLayout, false))
            Overview.Type.UNIQUE_FLASK -> itemViewHolder = UniqueFlaskViewHolder(layoutInflater.inflate(R.layout.unique_flask_list_item, linearLayout, false))
            Overview.Type.UNIQUE_WEAPON -> itemViewHolder = UniqueWeaponViewHolder(layoutInflater.inflate(R.layout.unique_weapon_list_item, linearLayout, false))
            Overview.Type.UNIQUE_ARMOUR -> itemViewHolder = UniqueArmourViewHolder(layoutInflater.inflate(R.layout.unique_armour_list_item, linearLayout, false))
            Overview.Type.UNIQUE_ACCESSORY -> itemViewHolder = UniqueAccessoryViewHolder(layoutInflater.inflate(R.layout.unique_accessory_list_item, linearLayout, false))
            Overview.Type.BEAST -> itemViewHolder = BeastViewHolder(layoutInflater.inflate(R.layout.beast_list_item, linearLayout, false))
        }

        itemViewHolder?.let {
            it.configureViewHolder(itemOverview, 0)
            itemSummaryView = it.itemView
            linearLayout?.addView(itemSummaryView, 0)
        }
    }

    private fun configureItemButtons(leagueId: String, itemLine: Line)
    {
        wikiButton = findViewById<Button>(R.id.item_wiki_button)
        wikiButton?.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val wikiUrl: String = "https://pathofexile.gamepedia.com/${itemLine.name}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl))
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        })

        listingsButton = findViewById<Button>(R.id.item_listings_button)
        listingsButton?.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                // search for listing of online players
                val searchUrl: String = "https://poe.trade/search?league=${leagueId}&online=x&name=${itemLine.name}&link_min=${itemLine.links}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        })

        offlineListingsButton = findViewById<Button>(R.id.item_offline_listings_button)
        offlineListingsButton?.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                // search for listings of offline players
                val searchUrl: String = "https://poe.trade/search?league=${leagueId}&name=${itemLine.name}&link_min=${itemLine.links}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        })
    }
}
