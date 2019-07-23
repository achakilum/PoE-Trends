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
    private val league: String = "Legion"
    private val overviewName: String = "Currency"
    private val overviewType: Int = Overview.CURRENCY

    private val poeNinjaService: GetPoeNinjaDataService? by lazy {
        PoeNinjaClientInstance.create()
    }

    private val overview : MutableLiveData<Overview> by lazy {
        MutableLiveData<Overview>().also {
            reloadOverview()
        }
    }

    private var disposable: Disposable? = null

    fun getOverview() : LiveData<Overview>
    {
        return overview
    }

    fun reloadOverview()
    {
        when (overviewType)
        {
            Overview.CURRENCY ->
            {
                loadCurrencyOverview(league, overviewName)
            }
            Overview.ITEM ->
            {
                loadItemOverview(league, overviewName)
            }
        }
    }

    fun loadCurrencyOverview(league : String, type : String)
    {
        disposable = poeNinjaService!!.getFullCurrencyOverview(league, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> handleOverviewResult(result) },
                { error -> handleOverviewError(error.message) }
            )
    }

    fun loadItemOverview(league : String, type : String)
    {
        disposable = poeNinjaService!!.getFullItemOverview(league, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> handleOverviewResult(result) },
                { error -> handleOverviewError(error.message) }
            )
    }

    fun handleOverviewResult(result : Overview)
    {
        overview.value = result
    }

    fun handleOverviewError(errorMessage : String?)
    {
        errorMessage?.let{ Log.i("PoeNinjaViewModel", errorMessage) }
    }
}