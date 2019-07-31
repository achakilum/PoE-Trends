package com.bluelithalo.poetrends

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    //private var listLabel: TextView? = null
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
                Overview.CURRENCY ->
                {
                    updateRecyclerViewWithCurrency(overview as CurrencyOverview)
                }
                Overview.ITEM ->
                {
                    updateRecyclerViewWithItems(overview as ItemOverview)
                }
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
                poeNinjaViewModel.setOverviewName("Currency")
                this.title = getString(R.string.menu_currency)
            }
            R.id.nav_fragments ->
            {
                poeNinjaViewModel.setOverviewName("Fragment")
                this.title = getString(R.string.menu_fragments)
            }
            R.id.nav_incubators ->
            {
                poeNinjaViewModel.setOverviewName("Incubator")
                this.title = getString(R.string.menu_incubators)
            }
            R.id.nav_scarabs ->
            {
                poeNinjaViewModel.setOverviewName("Scarab")
                this.title = getString(R.string.menu_scarabs)
            }
            R.id.nav_fossils ->
            {
                poeNinjaViewModel.setOverviewName("Fossil")
                this.title = getString(R.string.menu_fossils)
            }
            R.id.nav_resonators ->
            {
                poeNinjaViewModel.setOverviewName("Resonator")
                this.title = getString(R.string.menu_resonators)
            }
            R.id.nav_essences ->
            {
                poeNinjaViewModel.setOverviewName("Essence")
                this.title = getString(R.string.menu_essences)
            }
            R.id.nav_divination_cards ->
            {
                poeNinjaViewModel.setOverviewName("DivinationCard")
                this.title = getString(R.string.menu_divination_cards)
            }
            R.id.nav_prophecies ->
            {
                poeNinjaViewModel.setOverviewName("Prophecy")
                this.title = getString(R.string.menu_prophecies)
            }
            R.id.nav_skill_gems ->
            {
                poeNinjaViewModel.setOverviewName("SkillGem")
                this.title = getString(R.string.menu_skill_gems)
            }
            R.id.nav_base_types ->
            {
                poeNinjaViewModel.setOverviewName("BaseType")
                this.title = getString(R.string.menu_base_types)
            }
            R.id.nav_helmet_enchantments ->
            {
                poeNinjaViewModel.setOverviewName("HelmetEnchant")
                this.title = getString(R.string.menu_helmet_enchantments)
            }
            R.id.nav_unique_maps ->
            {
                poeNinjaViewModel.setOverviewName("UniqueMap")
                this.title = getString(R.string.menu_unique_maps)
            }
            R.id.nav_maps ->
            {
                poeNinjaViewModel.setOverviewName("Map")
                this.title = getString(R.string.menu_maps)
            }
            R.id.nav_unique_jewels ->
            {
                poeNinjaViewModel.setOverviewName("UniqueJewel")
                this.title = getString(R.string.menu_unique_jewels)
            }
            R.id.nav_unique_flasks ->
            {
                poeNinjaViewModel.setOverviewName("UniqueFlask")
                this.title = getString(R.string.menu_unique_flasks)
            }
            R.id.nav_unique_weapons ->
            {
                poeNinjaViewModel.setOverviewName("UniqueWeapon")
                this.title = getString(R.string.menu_unique_weapons)
            }
            R.id.nav_unique_armours ->
            {
                poeNinjaViewModel.setOverviewName("UniqueArmour")
                this.title = getString(R.string.menu_unique_armours)
            }
            R.id.nav_unique_accessories ->
            {
                poeNinjaViewModel.setOverviewName("UniqueAccessory")
                this.title = getString(R.string.menu_unique_accessories)
            }
            R.id.nav_beasts ->
            {
                poeNinjaViewModel.setOverviewName("Beast")
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
