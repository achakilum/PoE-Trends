package com.bluelithalo.poetrends

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemOverview
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class PoeNinjaViewModel : ViewModel()
{
    private var league: String = "Legion"
    private var overviewName: String = "Currency"
    private var overviewType: Int = Overview.CURRENCY
    private var searchQuery: String = ""

    private val poeNinjaService: GetPoeNinjaDataService? by lazy {
        PoeNinjaClientInstance.create()
    }

    private val overview : MutableLiveData<Overview> by lazy {
        MutableLiveData<Overview>().also {
            reloadOverview()
        }
    }

    private var disposable: Disposable? = null

    private fun reloadOverview()
    {
        when (overviewType)
        {
            Overview.CURRENCY ->
            {
                loadCurrencyOverview(overviewName)
            }
            Overview.ITEM ->
            {
                loadItemOverview(overviewName)
            }
        }
    }

    private fun loadCurrencyOverview(type : String)
    {
        disposable = poeNinjaService?.let {
            it.getFullCurrencyOverview(league, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { currencyOverview: CurrencyOverview -> filterCurrencyOverview(currencyOverview) }
            .subscribe(
                { result -> handleOverviewResult(result) },
                { error -> handleOverviewError(error.message) }
            )
        }
    }

    private fun loadItemOverview(type : String)
    {
        disposable = poeNinjaService?.let {
            it.getFullItemOverview(league, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { itemOverview: ItemOverview -> filterItemOverview(itemOverview) }
            .subscribe(
                { result -> handleOverviewResult(result) },
                { error -> handleOverviewError(error.message) }
            )
        }
    }

    private fun filterCurrencyOverview(currencyOverview: CurrencyOverview)
    {
        var newLines : ArrayList<com.bluelithalo.poetrends.model.currency.Line> = ArrayList()
        for (line in currencyOverview.lines!!)
        {
            if (line.currencyTypeName!!.toLowerCase().contains(searchQuery.toLowerCase()))
            {
                newLines.add(line)
            }
        }

        currencyOverview.lines = newLines
    }

    private fun filterItemOverview(itemOverview: ItemOverview)
    {
        var newLines : ArrayList<com.bluelithalo.poetrends.model.item.Line> = ArrayList()
        for (line in itemOverview.lines!!)
        {
            if (line.name!!.toLowerCase().contains(searchQuery.toLowerCase()))
            {
                newLines.add(line)
            }
        }

        itemOverview.lines = newLines
    }

    private fun handleOverviewResult(result : Overview)
    {
        result.type = overviewType
        overview.value = result
    }

    private fun handleOverviewError(errorMessage : String?)
    {
        errorMessage?.let{ Log.i("PoeNinjaViewModel", errorMessage) }
    }

    fun getOverview() : LiveData<Overview>
    {
        return overview
    }

    fun setOverviewName(newOverviewName: String)
    {
        overviewName = newOverviewName

        if (overviewName.equals("Currency") || overviewName.equals("Fragment"))
        {
            overviewType = Overview.CURRENCY
        }
        else
        {
            overviewType = Overview.ITEM
        }

        this.searchQuery = ""
        reloadOverview()
    }

    fun setSearchQuery(newSearchQuery: String)
    {
        searchQuery = newSearchQuery
        reloadOverview()
    }
}