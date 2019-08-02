package com.bluelithalo.poetrends.poe_ninja

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemOverview
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class PoeNinjaViewModel : ViewModel()
{
    private var leagueId: String = "Legion"
    private var overviewType: Overview.Type = Overview.Type.CURRENCY
    private var searchQuery: String = ""

    private val poeNinjaService: GetPoeNinjaDataService? by lazy {
        PoeNinjaClientInstance.create()
    }

    private val overview : MutableLiveData<Overview> by lazy {
        MutableLiveData<Overview>().also {
            reloadOverview()
        }
    }

    private val overviewNamesByType: HashMap<Overview.Type, String> by lazy {
        HashMap<Overview.Type, String>().also {
            it[Overview.Type.CURRENCY] = "Currency"
            it[Overview.Type.FRAGMENT] = "Fragment"
            it[Overview.Type.INCUBATOR] = "Incubator"
            it[Overview.Type.SCARAB] = "Scarab"
            it[Overview.Type.FOSSIL] = "Fossil"
            it[Overview.Type.RESONATOR] = "Resonator"
            it[Overview.Type.ESSENCE] = "Essence"
            it[Overview.Type.DIVINATION_CARD] = "DivinationCard"
            it[Overview.Type.PROPHECY] = "Prophecy"
            it[Overview.Type.SKILL_GEM] = "SkillGem"
            it[Overview.Type.BASE_TYPE] = "BaseType"
            it[Overview.Type.HELMET_ENCHANT] = "HelmetEnchant"
            it[Overview.Type.UNIQUE_MAP] = "UniqueMap"
            it[Overview.Type.MAP] = "Map"
            it[Overview.Type.UNIQUE_JEWEL] = "UniqueJewel"
            it[Overview.Type.UNIQUE_FLASK] = "UniqueFlask"
            it[Overview.Type.UNIQUE_WEAPON] = "UniqueWeapon"
            it[Overview.Type.UNIQUE_ARMOUR] = "UniqueArmour"
            it[Overview.Type.UNIQUE_ACCESSORY] = "UniqueAccessory"
            it[Overview.Type.BEAST] = "Beast"
        }
    }

    private var disposable: Disposable? = null

    private fun reloadOverview()
    {
        disposable?.let {
            if (!it.isDisposed)
            {
                it.dispose()
            }
        }

        when (overviewType)
        {
            Overview.Type.CURRENCY -> loadCurrencyOverview()
            Overview.Type.FRAGMENT -> loadCurrencyOverview()
            Overview.Type.INCUBATOR -> loadItemOverview()
            Overview.Type.SCARAB -> loadItemOverview()
            Overview.Type.FOSSIL -> loadItemOverview()
            Overview.Type.RESONATOR -> loadItemOverview()
            Overview.Type.ESSENCE -> loadItemOverview()
            Overview.Type.DIVINATION_CARD -> loadItemOverview()
            Overview.Type.PROPHECY -> loadItemOverview()
            Overview.Type.SKILL_GEM -> loadItemOverview()
            Overview.Type.BASE_TYPE -> loadItemOverview()
            Overview.Type.HELMET_ENCHANT -> loadItemOverview()
            Overview.Type.UNIQUE_MAP -> loadItemOverview()
            Overview.Type.MAP -> loadItemOverview()
            Overview.Type.UNIQUE_JEWEL -> loadItemOverview()
            Overview.Type.UNIQUE_FLASK -> loadItemOverview()
            Overview.Type.UNIQUE_WEAPON -> loadItemOverview()
            Overview.Type.UNIQUE_ARMOUR -> loadItemOverview()
            Overview.Type.UNIQUE_ACCESSORY -> loadItemOverview()
            Overview.Type.BEAST -> loadItemOverview()
        }
    }

    private fun loadCurrencyOverview()
    {
        disposable = poeNinjaService?.let {
            it.getFullCurrencyOverview(leagueId, overviewNamesByType?.get(overviewType) ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { currencyOverview: CurrencyOverview -> filterCurrencyOverview(currencyOverview) }
            .subscribe(
                { result -> handleOverviewResult(result) },
                { error -> handleOverviewError(error.message) }
            )
        }
    }

    private fun loadItemOverview()
    {
        disposable = poeNinjaService?.let {
            it.getFullItemOverview(leagueId, overviewNamesByType?.get(overviewType) ?: "")
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

        currencyOverview.lines?.let {
            for (line in it)
            {
                line.currencyTypeName?.let {
                    if (it.toLowerCase().contains(searchQuery.toLowerCase()))
                    {
                        newLines.add(line)
                    }
                }
            }
        }

        currencyOverview.lines = newLines
    }

    private fun filterItemOverview(itemOverview: ItemOverview)
    {
        var newLines : ArrayList<com.bluelithalo.poetrends.model.item.Line> = ArrayList()

        itemOverview.lines?.let {
            for (line in it)
            {
                line.name?.let {
                    if (it.toLowerCase().contains(searchQuery.toLowerCase()))
                    {
                        newLines.add(line)
                    }
                }
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

    fun getLeagueId() : String
    {
        return leagueId
    }

    fun setLeagueId(newLeagueId: String)
    {
        leagueId = newLeagueId
        reloadOverview()
    }

    fun setOverviewType(newOverviewType: Overview.Type)
    {
        overviewType = newOverviewType
        this.searchQuery = ""
        reloadOverview()
    }

    fun getSearchQuery() : String
    {
        return searchQuery
    }

    fun setSearchQuery(newSearchQuery: String)
    {
        searchQuery = newSearchQuery
        reloadOverview()
    }

    fun getOverview() : LiveData<Overview>
    {
        return overview
    }
}

