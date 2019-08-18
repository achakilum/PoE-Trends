package com.bluelithalo.poetrends

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.model.league.League
import com.bluelithalo.poetrends.poe.LeagueAdapter
import com.bluelithalo.poetrends.poe.LeagueViewModel

import kotlinx.android.synthetic.main.activity_league_selector.*

class LeagueSelectorActivity : AppCompatActivity(), LeagueAdapter.PoeLeagueContainer
{
    private lateinit var leagueViewModel : LeagueViewModel

    private var loadingProgressBar: ProgressBar? = null

    private var recyclerView: RecyclerView? = null
    private var rvLayoutManager: RecyclerView.LayoutManager? = null
    private var rvAdapter: LeagueAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_league_selector)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Choose your League..."

        loadingProgressBar = findViewById<ProgressBar?>(R.id.league_selector_loading_progress_bar)

        leagueViewModel = ViewModelProviders.of(this).get(LeagueViewModel::class.java)
        leagueViewModel.getLeagueList().observe(this, Observer<List<League>> { leagueList ->
            leagueList?.let {
                recyclerView = findViewById<RecyclerView?>(R.id.league_selector_recycler_view)
                recyclerView?.setHasFixedSize(true)

                rvLayoutManager = LinearLayoutManager(this)
                recyclerView?.layoutManager = rvLayoutManager

                rvAdapter = LeagueAdapter(this, leagueList)
                recyclerView?.adapter = rvAdapter
            }

            loadingProgressBar?.visibility = View.GONE
        })
    }

    override fun onLeagueSelect(leagueId: String)
    {
        val result : Intent = Intent()
        result.putExtra(LeagueSelectorActivity.RESULT_LEAGUE_ID, leagueId)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    companion object
    {
        val RESULT_LEAGUE_ID = "RESULT_LEAGUE_ID"
    }
}
