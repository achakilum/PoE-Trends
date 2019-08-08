package com.bluelithalo.poetrends.view

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.league.League

class LeagueViewHolder : RecyclerView.ViewHolder
{
    var leagueListItemLayout: ConstraintLayout
    var leagueIdTextView: TextView
    var leagueDescriptionTextView: TextView

    constructor(v: View) : super(v)
    {
        leagueListItemLayout = v.findViewById<View>(R.id.league_list_item_layout) as ConstraintLayout
        leagueIdTextView = v.findViewById<View>(R.id.league_id_text_view) as TextView
        leagueDescriptionTextView = v.findViewById<View>(R.id.league_description_text_view) as TextView
    }

    fun configureViewHolder(league: League?)
    {
        league?.let {
            leagueIdTextView.text = it.id
            leagueDescriptionTextView.text = it.description
            leagueListItemLayout.setBackgroundResource(R.color.colorLeagueSoftcore)
            it.rules?.iterator()?.forEach {
                if (it.name?.equals("Hardcore") == true)
                {
                    leagueListItemLayout.setBackgroundResource(R.color.colorLeagueHardcore)
                }
            }
        } ?: run {
            leagueIdTextView.text = "N/A"
            leagueDescriptionTextView.text = "N/A"
        }
    }
}