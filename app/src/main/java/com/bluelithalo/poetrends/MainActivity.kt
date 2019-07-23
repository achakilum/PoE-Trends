package com.bluelithalo.poetrends

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemOverview

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    //private var listLabel: TextView? = null
    private lateinit var poeNinjaViewModel : PoeNinjaViewModel

    private var loadingProgressBar: ProgressBar? = null

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

        //listLabel = findViewById<TextView?>(R.id.main_list_label)
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
    }

    private fun updateRecyclerViewWithCurrency(currencyOverview: CurrencyOverview?)
    {
        recyclerView?.let {

            recyclerView?.swapAdapter(PoeNinjaAdapter(currencyOverview, PoeNinjaAdapter.DISPLAY_CURRENCY), false)

        } ?: run {

            recyclerView = findViewById<RecyclerView?>(R.id.main_recycler_view)
            recyclerView?.setHasFixedSize(true)

            rvLayoutManager = LinearLayoutManager(this)
            recyclerView?.layoutManager = rvLayoutManager

            rvAdapter = PoeNinjaAdapter(currencyOverview, PoeNinjaAdapter.DISPLAY_CURRENCY)
            recyclerView?.adapter = rvAdapter
        }
    }

    private fun updateRecyclerViewWithItems(itemOverview: ItemOverview?)
    {
        recyclerView?.let {

            recyclerView?.swapAdapter(PoeNinjaAdapter(itemOverview, PoeNinjaAdapter.DISPLAY_ITEM), false)

        } ?: run {

            recyclerView = findViewById<RecyclerView?>(R.id.main_recycler_view)
            recyclerView?.setHasFixedSize(true)

            rvLayoutManager = LinearLayoutManager(this)
            recyclerView?.layoutManager = rvLayoutManager

            rvAdapter = PoeNinjaAdapter(itemOverview, PoeNinjaAdapter.DISPLAY_ITEM)
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

        when (item.itemId)
        {
            R.id.nav_currency ->
            {
                poeNinjaViewModel.loadCurrencyOverview("Legion", "Currency")
                this.title = getString(R.string.menu_currency)
            }
            R.id.nav_fragments ->
            {
                poeNinjaViewModel.loadCurrencyOverview("Legion", "Fragment")
                this.title = getString(R.string.menu_fragments)
            }
            R.id.nav_incubators ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "Incubator")
                this.title = getString(R.string.menu_incubators)
            }
            R.id.nav_scarabs ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "Scarab")
                this.title = getString(R.string.menu_scarabs)
            }
            R.id.nav_fossils ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "Fossil")
                this.title = getString(R.string.menu_fossils)
            }
            R.id.nav_resonators ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "Resonator")
                this.title = getString(R.string.menu_resonators)
            }
            R.id.nav_essences ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "Essence")
                this.title = getString(R.string.menu_essences)
            }
            R.id.nav_divination_cards ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "DivinationCard")
                this.title = getString(R.string.menu_divination_cards)
            }
            R.id.nav_prophecies ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "Prophecy")
                this.title = getString(R.string.menu_prophecies)
            }
            R.id.nav_skill_gems ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "SkillGem")
                this.title = getString(R.string.menu_skill_gems)
            }
            R.id.nav_base_types ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "BaseType")
                this.title = getString(R.string.menu_base_types)
            }
            R.id.nav_helmet_enchantments ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "HelmetEnchant")
                this.title = getString(R.string.menu_helmet_enchantments)
            }
            R.id.nav_unique_maps ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "UniqueMap")
                this.title = getString(R.string.menu_unique_maps)
            }
            R.id.nav_maps ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "Map")
                this.title = getString(R.string.menu_maps)
            }
            R.id.nav_unique_jewels ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "UniqueJewel")
                this.title = getString(R.string.menu_unique_jewels)
            }
            R.id.nav_unique_flasks ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "UniqueFlask")
                this.title = getString(R.string.menu_unique_flasks)
            }
            R.id.nav_unique_weapons ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "UniqueWeapon")
                this.title = getString(R.string.menu_unique_weapons)
            }
            R.id.nav_unique_armours ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "UniqueArmour")
                this.title = getString(R.string.menu_unique_armours)
            }
            R.id.nav_unique_accessories ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "UniqueAccessory")
                this.title = getString(R.string.menu_unique_accessories)
            }
            R.id.nav_beasts ->
            {
                poeNinjaViewModel.loadItemOverview("Legion", "Beast")
                this.title = getString(R.string.menu_beasts)
            }
        }

        this.title = "Trends: ${this.title}"
        this.loadingProgressBar?.visibility = View.VISIBLE

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
