package com.bluelithalo.poetrends

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluelithalo.poetrends.model.Overview
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PoeNinjaViewModel : ViewModel()
{
    private var league: String = "Legion"
    private var overviewName: String = "Currency"
    private var overviewType: Int = Overview.CURRENCY

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
        disposable = poeNinjaService!!.getFullCurrencyOverview(league, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> handleOverviewResult(result) },
                { error -> handleOverviewError(error.message) }
            )
    }

    private fun loadItemOverview(type : String)
    {
        disposable = poeNinjaService!!.getFullItemOverview(league, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> handleOverviewResult(result) },
                { error -> handleOverviewError(error.message) }
            )
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

        reloadOverview()
    }
}