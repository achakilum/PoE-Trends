package com.bluelithalo.poetrends.poe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.league.*
import com.bluelithalo.poetrends.view.*

class PoeLeagueAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private var container: PoeLeagueContainer? = null
    private var leagueList: List<League>? = null

    constructor(newContainer: PoeLeagueContainer?, newLeagueList: List<League>?)
    {
        container = newContainer
        leagueList = newLeagueList
    }

    override fun getItemCount(): Int {
        return leagueList?.size ?: 0
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val inflater = LayoutInflater.from(viewGroup.context)
        return LeagueViewHolder(inflater.inflate(R.layout.league_list_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int)
    {
        val league = leagueList?.let { it[position] }
        val leagueViewHolder = viewHolder as LeagueViewHolder

        leagueViewHolder.configureViewHolder(league)
        leagueViewHolder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                container?.onLeagueSelect(league?.id ?: "Standard")
            }
        })
    }

    interface PoeLeagueContainer
    {
        fun onLeagueSelect(leagueId: String)
    }
}