package com.bluelithalo.poetrends

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemOverview
import androidx.appcompat.widget.SearchView
import com.bluelithalo.poetrends.poe_ninja.OverviewAdapter
import com.bluelithalo.poetrends.poe_ninja.OverviewViewModel


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
                                        , OverviewAdapter.OverviewContainer
{
    private lateinit var poeNinjaViewModel : OverviewViewModel

    private var loading : Boolean = true
    private var loadingProgressBar: ProgressBar? = null
    private var searchQueryBar: SearchView? = null

    private var recyclerView: RecyclerView? = null
    private var rvLayoutManager: RecyclerView.LayoutManager? = null
    private var rvAdapter: OverviewAdapter? = null
    private var rvScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener()
    {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
        {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount: Int = rvLayoutManager?.childCount ?: 0
            val totalItemCount: Int = rvLayoutManager?.itemCount ?: 0
            val firstVisibleItemPosition: Int = (rvLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val overviewSize = (rvAdapter?.getOverviewSize() ?: 0)

            if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && (firstVisibleItemPosition >= 0) && (totalItemCount < overviewSize))
            {
                rvAdapter?.growItemCount()
                rvAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        navView.itemIconTintList = null
        navView.menu.getItem(0).isChecked = true

        this.loadingProgressBar = findViewById<ProgressBar>(R.id.loading_progress_bar)
        setLoadingState()

        poeNinjaViewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
        poeNinjaViewModel.getOverview().observe(this, Observer<Overview> { overview ->

            when (overview.type)
            {
                Overview.Type.CURRENCY -> updateRecyclerViewWithCurrency(overview as CurrencyOverview)
                Overview.Type.FRAGMENT -> updateRecyclerViewWithCurrency(overview as CurrencyOverview)
                Overview.Type.INCUBATOR -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.SCARAB -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.FOSSIL -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.RESONATOR -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.ESSENCE -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.DIVINATION_CARD -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.PROPHECY -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.SKILL_GEM -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.BASE_TYPE -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.HELMET_ENCHANT -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.UNIQUE_MAP -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.MAP -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.UNIQUE_JEWEL -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.UNIQUE_FLASK -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.UNIQUE_WEAPON -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.UNIQUE_ARMOUR -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.UNIQUE_ACCESSORY -> updateRecyclerViewWithItems(overview as ItemOverview)
                Overview.Type.BEAST -> updateRecyclerViewWithItems(overview as ItemOverview)
            }

            loading = false
            this.loadingProgressBar?.visibility = View.GONE
            this.title = "${overview.leagueId}: ${getString(overview.typeAffixResourceId)}"
        })

        this.searchQueryBar = findViewById<SearchView>(R.id.search_query_bar)
        this.searchQueryBar?.let {
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener
            {
                override fun onQueryTextSubmit(query: String?): Boolean
                {
                    query?.let {
                        poeNinjaViewModel.setSearchQuery(it)
                    }
                    loadingProgressBar?.visibility = View.VISIBLE
                    searchQueryBar?.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean
                {
                    newText?.let {
                        poeNinjaViewModel.setSearchQuery(it)
                    }
                    loadingProgressBar?.visibility = View.VISIBLE
                    return true
                }
            })
        }
    }

    private fun updateRecyclerViewWithCurrency(currencyOverview: CurrencyOverview?)
    {
        recyclerView?.let {

            val newRVAdapter = OverviewAdapter(this, currencyOverview)
            it.swapAdapter(newRVAdapter, false)
            it.smoothScrollToPosition(0)
            rvAdapter = newRVAdapter

        } ?: run {

            recyclerView = findViewById<RecyclerView?>(R.id.main_recycler_view)
            recyclerView?.setHasFixedSize(true)

            rvLayoutManager = LinearLayoutManager(this)
            recyclerView?.layoutManager = rvLayoutManager

            rvAdapter = OverviewAdapter(this, currencyOverview)
            recyclerView?.adapter = rvAdapter

            recyclerView?.addOnScrollListener(rvScrollListener)
        }
    }

    private fun updateRecyclerViewWithItems(itemOverview: ItemOverview?)
    {
        recyclerView?.let {

            val newRVAdapter = OverviewAdapter(this, itemOverview)
            it.swapAdapter(newRVAdapter, false)
            it.smoothScrollToPosition(0)
            rvAdapter = newRVAdapter

        } ?: run {

            recyclerView = findViewById<RecyclerView?>(R.id.main_recycler_view)
            recyclerView?.setHasFixedSize(true)

            rvLayoutManager = LinearLayoutManager(this)
            recyclerView?.layoutManager = rvLayoutManager

            rvAdapter = OverviewAdapter(this, itemOverview)
            recyclerView?.adapter = rvAdapter

            recyclerView?.addOnScrollListener(rvScrollListener)
        }
    }

    override fun onClickCurrencyOverviewItem(overviewType: Overview.Type, iconUrl: String?, lineString: String?, poeTradeId: Int)
    {
        if (overviewType == Overview.Type.CURRENCY || overviewType == Overview.Type.FRAGMENT)
        {
            val intent = Intent(this, CurrencyHistoryActivity::class.java).apply {
                putExtra(CurrencyHistoryActivity.POE_TRADE_ID, poeTradeId)
                putExtra(CurrencyHistoryActivity.LEAGUE_ID, poeNinjaViewModel.getLeagueId())
                putExtra(CurrencyHistoryActivity.CURRENCY_TYPE_ORDINAL, overviewType.ordinal)
                putExtra(CurrencyHistoryActivity.CURRENCY_MODEL_STRING, lineString)
                putExtra(CurrencyHistoryActivity.CURRENCY_ICON_URL, iconUrl)
            }

            startActivity(intent)
        }
    }

    override fun onBackPressed()
    {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        // Inflate the menu; this adds items to the action bar if it is present.

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId)
        {
            R.id.action_change_league -> startActivityForResult(Intent(this, LeagueSelectorActivity::class.java), MainActivity.CHANGE_LEAGUE)
        }

        return when (item.itemId)
        {
            R.id.action_change_league -> return true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        // Handle navigation view item clicks here.

        when (item.itemId)
        {
            R.id.nav_currency -> poeNinjaViewModel.setOverviewType(Overview.Type.CURRENCY)
            R.id.nav_fragments -> poeNinjaViewModel.setOverviewType(Overview.Type.FRAGMENT)
            R.id.nav_incubators -> poeNinjaViewModel.setOverviewType(Overview.Type.INCUBATOR)
            R.id.nav_scarabs -> poeNinjaViewModel.setOverviewType(Overview.Type.SCARAB)
            R.id.nav_fossils -> poeNinjaViewModel.setOverviewType(Overview.Type.FOSSIL)
            R.id.nav_resonators -> poeNinjaViewModel.setOverviewType(Overview.Type.RESONATOR)
            R.id.nav_essences -> poeNinjaViewModel.setOverviewType(Overview.Type.ESSENCE)
            R.id.nav_divination_cards -> poeNinjaViewModel.setOverviewType(Overview.Type.DIVINATION_CARD)
            R.id.nav_prophecies -> poeNinjaViewModel.setOverviewType(Overview.Type.PROPHECY)
            R.id.nav_skill_gems -> poeNinjaViewModel.setOverviewType(Overview.Type.SKILL_GEM)
            R.id.nav_base_types -> poeNinjaViewModel.setOverviewType(Overview.Type.BASE_TYPE)
            R.id.nav_helmet_enchants -> poeNinjaViewModel.setOverviewType(Overview.Type.HELMET_ENCHANT)
            R.id.nav_unique_maps -> poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_MAP)
            R.id.nav_maps -> poeNinjaViewModel.setOverviewType(Overview.Type.MAP)
            R.id.nav_unique_jewels -> poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_JEWEL)
            R.id.nav_unique_flasks -> poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_FLASK)
            R.id.nav_unique_weapons -> poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_WEAPON)
            R.id.nav_unique_armours -> poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_ARMOUR)
            R.id.nav_unique_accessories -> poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_ACCESSORY)
            R.id.nav_beasts -> poeNinjaViewModel.setOverviewType(Overview.Type.BEAST)
        }

        setLoadingState()
        this.searchQueryBar?.setQuery("", true)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            val extras = data?.extras
            if (requestCode == MainActivity.CHANGE_LEAGUE)
            {
                extras?.let {
                    val leagueId = extras.getString(LeagueSelectorActivity.RESULT_LEAGUE_ID)
                    this.poeNinjaViewModel.setLeagueId(leagueId)
                    setLoadingState()
                }
            }
        }

    }

    private fun setLoadingState()
    {
        this.loading = true
        this.title = getString(R.string.menu_loading)
        this.loadingProgressBar?.visibility = View.VISIBLE
    }

    companion object
    {
        val CHANGE_LEAGUE = 1
    }
}
