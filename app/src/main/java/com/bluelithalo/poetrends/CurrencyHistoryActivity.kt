package com.bluelithalo.poetrends

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyHistory
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.currency.Line
import com.bluelithalo.poetrends.poe_ninja.CurrencyHistoryViewModel
import com.bluelithalo.poetrends.poe_ninja.CurrencyHistoryViewModelFactory
import com.bluelithalo.poetrends.view.CurrencyViewHolder
import com.bluelithalo.poetrends.view.FragmentViewHolder
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_currency_history.*
import java.util.*
import kotlin.collections.ArrayList

class CurrencyHistoryActivity : AppCompatActivity()
{
    companion object
    {
        val POE_TRADE_ID = "POE_TRADE_ID"
        val LEAGUE_ID = "LEAGUE_ID"
        val CURRENCY_TYPE_ORDINAL = "CURRENCY_TYPE_ORDINAL"
        val CURRENCY_MODEL_STRING = "CURRENCY_MODEL_STRING"
        val CURRENCY_ICON_URL = "CURRENCY_ICON_URL"
    }

    private lateinit var currencyHistoryViewModel: CurrencyHistoryViewModel

    var linearLayout: LinearLayout? = null
    var currencySummaryView: View? = null

    var wikiButton: Button? = null
    var searchBuyButton: Button? = null
    var searchSellButton: Button? = null

    var lineChart: LineChart? = null
    var buyDateTextView: TextView? = null
    var buyValueTextView: TextView? = null
    var sellDateTextView: TextView? = null
    var sellValueTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_history)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        linearLayout = findViewById<LinearLayout>(R.id.currency_history_linear_layout)

        val leagueId: String = intent?.extras?.getString(LEAGUE_ID) ?: "Standard"
        val currencyType: Overview.Type = intent?.extras?.getInt(CURRENCY_TYPE_ORDINAL)?.let { Overview.Type.values()[it] } ?: Overview.Type.NONE
        val currencyLine: Line = Gson().fromJson(intent?.extras?.getString(CURRENCY_MODEL_STRING), Line::class.java)
        val currencyIconUrl: String = intent?.extras?.getString(CURRENCY_ICON_URL) ?: ""
        val poeTradeId: Int = intent?.extras?.getInt(POE_TRADE_ID) ?: -1

        this.title = "History (${leagueId})"
        insertCurrencySummary(currencyType, currencyLine, currencyIconUrl)

        when (currencyType)
        {
            Overview.Type.CURRENCY -> configureCurrencyButtons(leagueId, currencyLine, poeTradeId)
            Overview.Type.FRAGMENT -> configureFragmentButtons(leagueId, currencyLine, poeTradeId)
        }

        configureCurrencyDatumDisplay(currencyLine)

        currencyHistoryViewModel = ViewModelProviders.of(this, CurrencyHistoryViewModelFactory(leagueId, currencyType, currencyLine.pay?.payCurrencyId ?: (currencyLine.receive?.getCurrencyId ?: 0))).get(CurrencyHistoryViewModel::class.java)
        currencyHistoryViewModel.getCurrencyHistory().observe(this, Observer<CurrencyHistory> { currencyHistory ->
            configureLineChart(currencyHistory, currencyLine)
        })
    }

    private fun insertCurrencySummary(currencyType: Overview.Type, currencyLine: Line, currencyIconUrl: String)
    {
        val currencyOverview = CurrencyOverview(); currencyOverview.type = currencyType
        val currencyList: ArrayList<Line> = ArrayList<Line>(); currencyList.add(currencyLine); currencyOverview.lines = currencyList

        when (currencyType)
        {
            Overview.Type.CURRENCY ->
            {
                val currencyViewHolder = CurrencyViewHolder(layoutInflater.inflate(R.layout.currency_list_item, linearLayout, false))
                currencyViewHolder.configureViewHolder(currencyOverview, 0, currencyIconUrl)
                currencySummaryView = currencyViewHolder.itemView
                linearLayout?.addView(currencySummaryView, 0)
            }
            Overview.Type.FRAGMENT ->
            {
                val fragmentViewHolder = FragmentViewHolder(layoutInflater.inflate(R.layout.fragment_list_item, linearLayout, false))
                fragmentViewHolder.configureViewHolder(currencyOverview, 0, currencyIconUrl)
                currencySummaryView = fragmentViewHolder.itemView
                linearLayout?.addView(currencySummaryView, 0)
            }
        }
    }

    private fun configureCurrencyButtons(leagueId: String, currencyLine: Line, poeTradeId: Int)
    {
        wikiButton = findViewById<Button>(R.id.currency_wiki_button)
        wikiButton?.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val wikiUrl: String = "https://pathofexile.gamepedia.com/${currencyLine.currencyTypeName}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl))
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        })

        searchBuyButton = findViewById<Button>(R.id.currency_search_buy_button)
        searchBuyButton?.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val tradeCurrencyId = if (poeTradeId == 24 || poeTradeId == 520 || poeTradeId == 25) 6 else 4 // for expensive currency currencys: mirror, mirror shard, eternal orb
                val searchBuyUrl: String = if (poeTradeId >= 0) "https://currency.poe.trade/search?league=${leagueId}&online=x&stock=&want=${poeTradeId}&have=${tradeCurrencyId}" else "https://poe.trade/search?league=${leagueId}&online=x&name=${currencyLine.currencyTypeName}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchBuyUrl))
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        })

        searchSellButton = findViewById<Button>(R.id.currency_search_sell_button)
        searchSellButton?.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val tradeCurrencyId = if (poeTradeId == 24 || poeTradeId == 520 || poeTradeId == 25) 6 else 4 // for expensive currency currencys: mirror, mirror shard, eternal orb
                val searchSellUrl: String = if (poeTradeId >= 0) "http://currency.poe.trade/search?league=${leagueId}&online=x&stock=&want=${tradeCurrencyId}&have=${poeTradeId}" else "https://poe.trade/search?league=${leagueId}&online=x&name=${currencyLine.currencyTypeName}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchSellUrl))
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        })
    }

    private fun configureFragmentButtons(leagueId: String, fragmentLine: Line, poeTradeId: Int)
    {
        wikiButton = findViewById<Button>(R.id.currency_wiki_button)
        wikiButton?.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val wikiUrl: String = "https://pathofexile.gamepedia.com/${fragmentLine.currencyTypeName}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl))
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        })

        searchBuyButton = findViewById<Button>(R.id.currency_search_buy_button)
        searchBuyButton?.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val tradeCurrencyId = 4 // chaos orbs expected
                val searchBuyUrl: String = if (poeTradeId >= 0) "https://currency.poe.trade/search?league=${leagueId}&online=x&stock=&want=${poeTradeId}&have=${tradeCurrencyId}" else "https://poe.trade/search?league=${leagueId}&online=x&name=${fragmentLine.currencyTypeName}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchBuyUrl))
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        })

        searchSellButton = findViewById<Button>(R.id.currency_search_sell_button)
        searchSellButton?.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val tradeCurrencyId = 4 // chaos orbs expected
                val searchSellUrl: String = if (poeTradeId >= 0) "https://currency.poe.trade/search?league=${leagueId}&online=x&stock=&want=${tradeCurrencyId}&have=${poeTradeId}" else "https://poe.trade/search?league=${leagueId}&online=x&name=${fragmentLine.currencyTypeName}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchSellUrl))
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        })
    }

    private fun configureCurrencyDatumDisplay(currencyLine: Line)
    {
        buyDateTextView = findViewById<TextView>(R.id.currency_buy_date_text_view)
        buyValueTextView = findViewById<TextView>(R.id.currency_buy_value_text_view)
        sellDateTextView = findViewById<TextView>(R.id.currency_sell_date_text_view)
        sellValueTextView = findViewById<TextView>(R.id.currency_sell_value_text_view)

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val dateString = "${calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)} ${calendar.get(
            Calendar.DAY_OF_MONTH)}"

        currencyLine.receive?.let {
            val buyValue = it.value
            val buyValueString = "${String.format("%.1f", buyValue)} ×"
            buyDateTextView?.text = dateString
            buyValueTextView?.text = buyValueString
        } ?: run {
            buyDateTextView?.text = dateString
            buyValueTextView?.text = "N/A \u00D7"
        }

        currencyLine.pay?.let {
            val sellValue = 1.0 / (it.value ?: 0.0)
            val sellValueString = "${String.format("%.1f", sellValue)} ×"
            sellDateTextView?.text = dateString
            sellValueTextView?.text = sellValueString
        } ?: run {
            sellDateTextView?.text = dateString
            sellValueTextView?.text = "N/A \u00D7"
        }
    }

    private fun configureLineChart(currencyHistory: CurrencyHistory, currencyLine: Line)
    {
        val buyValues: ArrayList<Entry> = ArrayList()
        for (i in 0 until (currencyHistory.receiveCurrencyGraphData?.size ?: 0))
        {
            currencyHistory.receiveCurrencyGraphData?.get(i)?.let {
                val daysAgo: Float = -1 * (it.daysAgo ?: 0).toFloat()
                val currencyValue: Float = (it.value ?: 0.0).toFloat()
                buyValues.add(Entry(daysAgo, currencyValue))
            }
        }

        val sellValues: ArrayList<Entry> = ArrayList()
        for (i in 0 until (currencyHistory.payCurrencyGraphData?.size ?: 0))
        {
            currencyHistory.payCurrencyGraphData?.get(i)?.let {
                val daysAgo: Float = -1 * (it.daysAgo ?: 0).toFloat()
                val currencyValue: Float = 1.0f / (it.value ?: 0.0).toFloat()
                sellValues.add(Entry(daysAgo, currencyValue))
            }
        }

        lineChart = findViewById<LineChart>(R.id.currency_line_chart)
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

            val buyDataset = LineDataSet(buyValues, "Currency Buy History Dataset")
            buyDataset.cubicIntensity = 0.2f
            buyDataset.setDrawFilled(true)
            buyDataset.lineWidth = 2.0f
            buyDataset.color = Color.rgb(250, 200, 0)
            buyDataset.setCircleColor(Color.rgb(250, 200, 0))
            buyDataset.circleHoleColor = Color.rgb(250, 200, 0)
            buyDataset.highLightColor = Color.rgb(0, 255, 0)
            buyDataset.fillColor = Color.rgb(75, 50, 0)

            val sellDataset = LineDataSet(sellValues, "Currency Sell History Dataset")
            sellDataset.cubicIntensity = 0.2f
            sellDataset.setDrawFilled(true)
            sellDataset.lineWidth = 2.0f
            sellDataset.color = Color.rgb(0, 200, 250)
            sellDataset.setCircleColor(Color.rgb(0, 200, 250))
            sellDataset.circleHoleColor = Color.rgb(0, 200, 250)
            sellDataset.highLightColor = Color.rgb(0, 255, 0)
            sellDataset.fillColor = Color.rgb(0, 50, 75)

            val datasets = ArrayList<ILineDataSet>()
            datasets.add(sellDataset)
            datasets.add(buyDataset)

            val data = LineData(datasets)
            data.setValueTextSize(9.0f)
            data.setDrawValues(false)

            it.data = data
            it.invalidate()

            it.setOnChartValueSelectedListener(object : OnChartValueSelectedListener
            {
                override fun onNothingSelected()
                {
                    configureCurrencyDatumDisplay(currencyLine)
                }

                override fun onValueSelected(e: Entry?, h: Highlight?)
                {
                    val datasetIdx: Int = h?.dataSetIndex ?: 0

                    val daysAgo: Int = e?.x?.toInt() ?: 0
                    val currencyValue: Float = e?.y ?: 0.0f

                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.add(Calendar.DATE, daysAgo)

                    val dateString = "${calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)} ${calendar.get(
                        Calendar.DAY_OF_MONTH)}"
                    val currencyValueString = "${String.format("%.1f", currencyValue)} ×"

                    if (datasetIdx == 0) // sell dataset inserted first
                    {
                        sellDateTextView?.text = dateString
                        sellValueTextView?.text = currencyValueString
                    }
                    else // buy dataset inserted last
                    {
                        buyDateTextView?.text = dateString
                        buyValueTextView?.text = currencyValueString
                    }
                }
            })

        }
    }
}
