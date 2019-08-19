package com.bluelithalo.poetrends.poe_ninja

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bluelithalo.poetrends.model.Overview

class CurrencyHistoryViewModelFactory(newLeagueId: String,
                                      newOverviewType: Overview.Type,
                                      newCurrencyId: Int)
    : ViewModelProvider.Factory
{

    private var leagueId: String = newLeagueId
    private var overviewType: Overview.Type = newOverviewType
    private var currencyId: Int = newCurrencyId

    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        return CurrencyHistoryViewModel(leagueId, overviewType, currencyId) as T
    }
}