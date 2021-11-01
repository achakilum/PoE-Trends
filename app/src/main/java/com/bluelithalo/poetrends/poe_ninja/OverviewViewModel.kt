package com.bluelithalo.poetrends.poe_ninja

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.*
import com.bluelithalo.poetrends.model.item.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class OverviewViewModel : ViewModel()
{
    private var leagueId: String = "Scourge"
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
            it[Overview.Type.DELIRIUM_ORB] = "DeliriumOrb"
            it[Overview.Type.WATCHSTONE] = "Watchstone"
            it[Overview.Type.OIL] = "Oil"
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
            it[Overview.Type.VIAL] = "Vial"
        }
    }

    private val titleAffixesByType: HashMap<Overview.Type, Int> by lazy {
        HashMap<Overview.Type, Int>().also {
            it[Overview.Type.CURRENCY] = R.string.menu_currency
            it[Overview.Type.FRAGMENT] = R.string.menu_fragments
            it[Overview.Type.DELIRIUM_ORB] = R.string.menu_delirium_orbs
            it[Overview.Type.WATCHSTONE] = R.string.menu_watchstones
            it[Overview.Type.OIL] = R.string.menu_oils
            it[Overview.Type.INCUBATOR] = R.string.menu_incubators
            it[Overview.Type.SCARAB] = R.string.menu_scarabs
            it[Overview.Type.FOSSIL] = R.string.menu_fossils
            it[Overview.Type.RESONATOR] = R.string.menu_resonators
            it[Overview.Type.ESSENCE] = R.string.menu_essences
            it[Overview.Type.DIVINATION_CARD] = R.string.menu_divination_cards
            it[Overview.Type.PROPHECY] = R.string.menu_prophecies
            it[Overview.Type.SKILL_GEM] = R.string.menu_skill_gems
            it[Overview.Type.BASE_TYPE] = R.string.menu_base_types
            it[Overview.Type.HELMET_ENCHANT] = R.string.menu_helmet_enchant
            it[Overview.Type.UNIQUE_MAP] = R.string.menu_unique_maps
            it[Overview.Type.MAP] = R.string.menu_maps
            it[Overview.Type.UNIQUE_JEWEL] = R.string.menu_unique_jewels
            it[Overview.Type.UNIQUE_FLASK] = R.string.menu_unique_flasks
            it[Overview.Type.UNIQUE_WEAPON] = R.string.menu_unique_weapons
            it[Overview.Type.UNIQUE_ARMOUR] = R.string.menu_unique_armours
            it[Overview.Type.UNIQUE_ACCESSORY] = R.string.menu_unique_accessories
            it[Overview.Type.BEAST] = R.string.menu_beasts
            it[Overview.Type.VIAL] = R.string.menu_vials

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
            Overview.Type.DELIRIUM_ORB -> loadItemOverview()
            Overview.Type.WATCHSTONE -> loadItemOverview()
            Overview.Type.OIL -> loadItemOverview()
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
            Overview.Type.VIAL -> loadItemOverview()
        }
    }

    private fun loadCurrencyOverview()
    {
        disposable = poeNinjaService?.let {
            it.getCurrencyOverview(leagueId, overviewNamesByType?.get(overviewType) ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { currencyOverview: CurrencyOverview -> currencyOverview.byName(searchQuery) }
            .subscribe(
                { result -> handleOverviewResult(result) },
                { error -> handleOverviewError(error.message) }
            )
        }
    }

    private fun loadItemOverview()
    {
        disposable = poeNinjaService?.let {
            it.getItemOverview(leagueId, overviewNamesByType?.get(overviewType) ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { itemOverview: ItemOverview -> itemOverview.byName(searchQuery) }
            .subscribe(
                { result -> handleOverviewResult(result) },
                { error -> handleOverviewError(error.message) }
            )
        }
    }

    private fun handleOverviewResult(result : Overview)
    {
        result.leagueId = leagueId
        result.typeAffixResourceId = titleAffixesByType[overviewType] ?: R.string.menu_currency
        result.type = overviewType
        overview.value = result
    }

    private fun handleOverviewError(errorMessage : String?)
    {
        //errorMessage?.let{ Log.i("OverviewViewModel", errorMessage) }
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

