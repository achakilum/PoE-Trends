package com.bluelithalo.poetrends

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity;
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.currency.Line
import com.bluelithalo.poetrends.view.CurrencyViewHolder
import com.bluelithalo.poetrends.view.FragmentViewHolder
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_currency_history.*

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

    var linearLayout: LinearLayout? = null
    var currencySummaryView: View? = null

    var wikiButton: Button? = null
    var searchBuyButton: Button? = null
    var searchSellButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_history)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        linearLayout = findViewById<LinearLayout>(R.id.currency_history_layout)

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
                val tradeCurrencyId = if (poeTradeId == 24 || poeTradeId == 520 || poeTradeId == 25) 6 else 4 // for expensive currency items: mirror, mirror shard, eternal orb
                val searchBuyUrl: String = "http://currency.poe.trade/search?league=${leagueId}&online=x&stock=&want=${poeTradeId}&have=${tradeCurrencyId}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchBuyUrl))
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        })

        searchSellButton = findViewById<Button>(R.id.currency_search_sell_button)
        searchSellButton?.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val tradeCurrencyId = if (poeTradeId == 24 || poeTradeId == 520 || poeTradeId == 25) 6 else 4 // for expensive currency items: mirror, mirror shard, eternal orb
                val searchSellUrl: String = "http://currency.poe.trade/search?league=${leagueId}&online=x&stock=&want=${tradeCurrencyId}&have=${poeTradeId}"
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
}
