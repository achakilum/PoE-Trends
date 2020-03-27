package com.bluelithalo.poetrends

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemGraphDatum
import com.bluelithalo.poetrends.model.item.ItemOverview
import com.bluelithalo.poetrends.model.item.Line
import com.bluelithalo.poetrends.poe_ninja.ItemHistoryViewModel
import com.bluelithalo.poetrends.poe_ninja.ItemHistoryViewModelFactory
import com.bluelithalo.poetrends.view.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_item_history.*
import java.util.*
import kotlin.collections.ArrayList

class ItemHistoryActivity : AppCompatActivity()
{
    companion object
    {
        val LEAGUE_ID = "LEAGUE_ID"
        val ITEM_TYPE_ORDINAL = "ITEM_TYPE_ORDINAL"
        val ITEM_MODEL_STRING = "ITEM_MODEL_STRING"
    }

    private lateinit var itemHistoryViewModel: ItemHistoryViewModel

    var linearLayout: LinearLayout? = null
    var itemSummaryView: View? = null

    var wikiButton: Button? = null
    var listingsButton: Button? = null
    var offlineListingsButton: Button? = null

    var lineChart: LineChart? = null
    var buyDateTextView: TextView? = null
    var buyValueTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_history)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        linearLayout = findViewById<LinearLayout>(R.id.item_history_linear_layout)

        val leagueId: String = intent?.extras?.getString(LEAGUE_ID) ?: "Standard"
        val itemType: Overview.Type = intent?.extras?.getInt(ITEM_TYPE_ORDINAL)?.let { Overview.Type.values()[it] } ?: Overview.Type.NONE
        val itemLine: Line = Gson().fromJson(intent?.extras?.getString(ITEM_MODEL_STRING), Line::class.java)

        this.title = "History (${leagueId})"
        insertItemSummary(itemType, itemLine)
        configureItemButtons(leagueId, itemLine, itemType)
        configureItemDatumDisplay(itemLine)

        itemHistoryViewModel = ViewModelProviders.of(this, ItemHistoryViewModelFactory(leagueId, itemType, itemLine?.id ?: 0)).get(ItemHistoryViewModel::class.java)
        itemHistoryViewModel.getItemHistory().observe(this, Observer<List<ItemGraphDatum>> { itemHistory ->
            configureLineChart(itemHistory)
        })
    }

    private fun insertItemSummary(itemType: Overview.Type, itemLine: Line)
    {
        val itemOverview = ItemOverview(); itemOverview.type = itemType
        val itemList: ArrayList<Line> = ArrayList<Line>(); itemList.add(itemLine); itemOverview.lines = itemList
        var itemViewHolder : PoeNinjaViewHolder? = null

        when (itemType)
        {
            Overview.Type.OIL -> itemViewHolder = OilViewHolder(layoutInflater.inflate(R.layout.oil_list_item, linearLayout, false))
            Overview.Type.INCUBATOR -> itemViewHolder = IncubatorViewHolder(layoutInflater.inflate(R.layout.incubator_list_item, linearLayout, false))
            Overview.Type.DELIRIUM_ORB -> itemViewHolder = DeliriumOrbViewHolder(layoutInflater.inflate(R.layout.delirium_orb_list_item, linearLayout, false))
            Overview.Type.WATCHSTONE -> itemViewHolder = WatchstoneViewHolder(layoutInflater.inflate(R.layout.watchstone_list_item, linearLayout, false))
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
            Overview.Type.VIAL -> itemViewHolder = VialViewHolder(layoutInflater.inflate(R.layout.vial_list_item, linearLayout, false))
        }

        itemViewHolder?.let {
            it.configureViewHolder(itemOverview, 0)
            itemSummaryView = it.itemView
            linearLayout?.addView(itemSummaryView, 0)
        }
    }

    private fun configureItemButtons(leagueId: String, itemLine: Line, itemType: Overview.Type)
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

    private fun configureItemDatumDisplay(itemLine: Line)
    {
        buyDateTextView = findViewById<TextView>(R.id.item_buy_date_text_view)
        buyValueTextView = findViewById<TextView>(R.id.item_buy_value_text_view)

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val itemValue = itemLine.chaosValue

        val dateString = "${calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)} ${calendar.get(Calendar.DAY_OF_MONTH)}"
        val itemValueString = "${String.format("%.1f", itemValue)} ×"

        buyDateTextView?.text = dateString
        buyValueTextView?.text = itemValueString
    }

    private fun configureLineChart(itemHistory: List<ItemGraphDatum>)
    {
        val values: ArrayList<Entry> = ArrayList()
        for (i in 0 until itemHistory.size)
        {
            itemHistory[i]?.let {
                val daysAgo: Float = -1 * (it.daysAgo ?: 0).toFloat()
                val itemValue: Float = (it.value ?: 0.0).toFloat()
                values.add(Entry(daysAgo, itemValue))
            }
        }

        lineChart = findViewById<LineChart>(R.id.item_line_chart)
        lineChart?.let {

            it.setExtraOffsets(0.0f, 0.0f, 0.0f, 8.0f)
            it.setBackgroundColor(Color.rgb(40, 40, 40))
            it.description.isEnabled = false
            it.setTouchEnabled(true)
            it.setDragEnabled(true)
            it.setScaleEnabled(true)
            it.setPinchZoom(false)
            it.setDrawGridBackground(false)
            it.setMaxHighlightDistance(50.0f)

            val x = it.xAxis
            x.setLabelCount(6, false)
            x.textColor = Color.WHITE
            x.setPosition(XAxis.XAxisPosition.BOTTOM)
            x.setDrawGridLines(true)
            x.gridColor = Color.rgb(125, 125, 125)
            x.axisLineColor = Color.WHITE
            x.setValueFormatter(object : IndexAxisValueFormatter()
            {
                override fun getFormattedValue(value: Float): String
                {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    val daysAgo = value.toInt()
                    calendar.add(Calendar.DATE, daysAgo)
                    return "${calendar.get(Calendar.MONTH)+1}/${calendar.get(Calendar.DAY_OF_MONTH)}"
                }
            })

            val y = it.axisRight
            y.setLabelCount(6, false)
            y.textColor = Color.WHITE
            y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            y.setDrawGridLines(true)
            y.gridColor = Color.rgb(125, 125, 125)
            y.axisLineColor = Color.WHITE

            it.axisLeft.isEnabled = false
            it.legend.isEnabled = false

            val dataset = LineDataSet(values, "Currency History Dataset")
            dataset.cubicIntensity = 0.2f
            dataset.setDrawFilled(true)
            dataset.lineWidth = 2.0f
            dataset.color = Color.rgb(250, 200, 0)
            dataset.setCircleColor(Color.rgb(250, 200, 0))
            dataset.circleHoleColor = Color.rgb(250, 200, 0)
            dataset.highLightColor = Color.rgb(0, 255, 0)
            dataset.fillColor = Color.rgb(75, 50, 0)

            val data = LineData(dataset)
            data.setValueTextSize(9.0f)
            data.setDrawValues(false)

            it.data = data
            it.invalidate()
            it.setOnChartValueSelectedListener(object : OnChartValueSelectedListener
            {
                override fun onNothingSelected()
                {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    val itemValue = itemHistory.get(itemHistory.size - 1).value

                    val dateString = "${calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)} ${calendar.get(Calendar.DAY_OF_MONTH)}"
                    val itemValueString = "${String.format("%.1f", itemValue)} ×"

                    buyDateTextView?.text = dateString
                    buyValueTextView?.text = itemValueString
                }

                override fun onValueSelected(e: Entry?, h: Highlight?)
                {
                    val daysAgo: Int = e?.x?.toInt() ?: 0
                    val itemValue: Float = e?.y ?: 0.0f

                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.add(Calendar.DATE, daysAgo)

                    val dateString = "${calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)} ${calendar.get(Calendar.DAY_OF_MONTH)}"
                    val itemValueString = "${String.format("%.1f", itemValue)} ×"

                    buyDateTextView?.text = dateString
                    buyValueTextView?.text = itemValueString
                }
            })
        }
    }
}
