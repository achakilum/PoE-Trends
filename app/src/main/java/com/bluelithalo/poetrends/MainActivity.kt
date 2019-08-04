package com.bluelithalo.poetrends

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemOverview
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.bluelithalo.poetrends.poe_ninja.PoeNinjaAdapter
import com.bluelithalo.poetrends.poe_ninja.PoeNinjaViewModel


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var poeNinjaViewModel : PoeNinjaViewModel

    private var loadingProgressBar: ProgressBar? = null
    private var searchQueryBar: EditText? = null

    private var recyclerView: RecyclerView? = null
    private var rvLayoutManager: RecyclerView.LayoutManager? = null
    private var rvAdapter: PoeNinjaAdapter? = null

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

        this.title = "Current Trends"
        this.loadingProgressBar = findViewById<ProgressBar>(R.id.loading_progress_bar)

        poeNinjaViewModel = ViewModelProviders.of(this).get(PoeNinjaViewModel::class.java)
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

            this.loadingProgressBar?.visibility = View.GONE
        })

        this.searchQueryBar = findViewById<EditText>(R.id.search_query_bar)
        this.searchQueryBar?.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                s?.let { poeNinjaViewModel.setSearchQuery(it.toString()) }
                loadingProgressBar?.visibility = View.VISIBLE
            }
        })

        this.searchQueryBar?.setOnEditorActionListener(object : TextView.OnEditorActionListener
        {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                    searchQueryBar?.clearFocus()

                    return true
                }

                return false
            }
        })
    }

    private fun updateRecyclerViewWithCurrency(currencyOverview: CurrencyOverview?)
    {
        recyclerView?.let {

            it.swapAdapter(PoeNinjaAdapter(currencyOverview), false)
            it.smoothScrollToPosition(0)

        } ?: run {

            recyclerView = findViewById<RecyclerView?>(R.id.main_recycler_view)
            recyclerView?.setHasFixedSize(true)

            rvLayoutManager = LinearLayoutManager(this)
            recyclerView?.layoutManager = rvLayoutManager

            rvAdapter = PoeNinjaAdapter(currencyOverview)
            recyclerView?.adapter = rvAdapter
        }
    }

    private fun updateRecyclerViewWithItems(itemOverview: ItemOverview?)
    {
        recyclerView?.let {

            it.swapAdapter(PoeNinjaAdapter(itemOverview), false)
            it.smoothScrollToPosition(0)

        } ?: run {

            recyclerView = findViewById<RecyclerView?>(R.id.main_recycler_view)
            recyclerView?.setHasFixedSize(true)

            rvLayoutManager = LinearLayoutManager(this)
            recyclerView?.layoutManager = rvLayoutManager

            rvAdapter = PoeNinjaAdapter(itemOverview)
            recyclerView?.adapter = rvAdapter
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
        return when (item.itemId)
        {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        // Handle navigation view item clicks here.
        //listLabel?.text = "Loading..."

        // TODO: Move activity title changes to LiveData in ViewModel (as part of Overview)

        when (item.itemId)
        {
            R.id.nav_currency ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.CURRENCY)
                this.title = getString(R.string.menu_currency)
            }
            R.id.nav_fragments ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.FRAGMENT)
                this.title = getString(R.string.menu_fragments)
            }
            R.id.nav_incubators ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.INCUBATOR)
                this.title = getString(R.string.menu_incubators)
            }
            R.id.nav_scarabs ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.SCARAB)
                this.title = getString(R.string.menu_scarabs)
            }
            R.id.nav_fossils ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.FOSSIL)
                this.title = getString(R.string.menu_fossils)
            }
            R.id.nav_resonators ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.RESONATOR)
                this.title = getString(R.string.menu_resonators)
            }
            R.id.nav_essences ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.ESSENCE)
                this.title = getString(R.string.menu_essences)
            }
            R.id.nav_divination_cards ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.DIVINATION_CARD)
                this.title = getString(R.string.menu_divination_cards)
            }
            R.id.nav_prophecies ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.PROPHECY)
                this.title = getString(R.string.menu_prophecies)
            }
            R.id.nav_skill_gems ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.SKILL_GEM)
                this.title = getString(R.string.menu_skill_gems)
            }
            R.id.nav_base_types ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.BASE_TYPE)
                this.title = getString(R.string.menu_base_types)
            }
            R.id.nav_helmet_enchants ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.HELMET_ENCHANT)
                this.title = getString(R.string.menu_helmet_enchant)
            }
            R.id.nav_unique_maps ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_MAP)
                this.title = getString(R.string.menu_unique_maps)
            }
            R.id.nav_maps ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.MAP)
                this.title = getString(R.string.menu_maps)
            }
            R.id.nav_unique_jewels ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_JEWEL)
                this.title = getString(R.string.menu_unique_jewels)
            }
            R.id.nav_unique_flasks ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_FLASK)
                this.title = getString(R.string.menu_unique_flasks)
            }
            R.id.nav_unique_weapons ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_WEAPON)
                this.title = getString(R.string.menu_unique_weapons)
            }
            R.id.nav_unique_armours ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_ARMOUR)
                this.title = getString(R.string.menu_unique_armours)
            }
            R.id.nav_unique_accessories ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.UNIQUE_ACCESSORY)
                this.title = getString(R.string.menu_unique_accessories)
            }
            R.id.nav_beasts ->
            {
                poeNinjaViewModel.setOverviewType(Overview.Type.BEAST)
                this.title = getString(R.string.menu_beasts)
            }
        }

        this.title = "Trends: ${this.title}"
        this.loadingProgressBar?.visibility = View.VISIBLE
        this.searchQueryBar?.text?.clear()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
