package com.bluelithalo.poetrends.poe_ninja

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bluelithalo.poetrends.model.Overview

class ItemHistoryViewModelFactory(newLeagueId: String,
                                      newOverviewType: Overview.Type,
                                      newItemId: Int)
    : ViewModelProvider.Factory
{

    private var leagueId: String = newLeagueId
    private var overviewType: Overview.Type = newOverviewType
    private var itemId: Int = newItemId

    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        return ItemHistoryViewModel(leagueId, overviewType, itemId) as T
    }
}