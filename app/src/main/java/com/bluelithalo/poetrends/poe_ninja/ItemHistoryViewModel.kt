package com.bluelithalo.poetrends.poe_ninja

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.item.ItemGraphDatum
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.HashMap

class ItemHistoryViewModel(newleagueId: String,
                               newOverviewType: Overview.Type,
                               newItemId: Int)
    : ViewModel()
{
    private var leagueId: String = newleagueId
    private var overviewType: Overview.Type = newOverviewType
    private var itemId: Int = newItemId

    private val poeNinjaService: GetPoeNinjaDataService? by lazy {
        PoeNinjaClientInstance.create()
    }

    private val itemHistory : MutableLiveData<List<ItemGraphDatum>> by lazy {
        MutableLiveData<List<ItemGraphDatum>>().also {
            reloadItemHistory()
        }
    }

    private val namesByType: HashMap<Overview.Type, String> by lazy {
        HashMap<Overview.Type, String>().also {
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

    private fun reloadItemHistory()
    {
        disposable?.let {
            if (!it.isDisposed)
            {
                it.dispose()
            }
        }

        loadItemHistory()
    }

    private fun loadItemHistory()
    {
        disposable = poeNinjaService?.let {
            it.getItemHistory(leagueId, namesByType?.get(overviewType) ?: "", itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> handleItemHistoryResult(result) },
                    { error -> handleItemHistoryError(error.message) }
                )
        }
    }

    private fun handleItemHistoryResult(result : List<ItemGraphDatum>)
    {
        itemHistory.value = result
    }

    private fun handleItemHistoryError(errorMessage : String?)
    {
        errorMessage?.let{ Log.i("ItemHistoryVM", errorMessage) }
    }

    fun getItemHistory() : LiveData<List<ItemGraphDatum>>
    {
        return itemHistory
    }
}
