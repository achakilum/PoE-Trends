package com.bluelithalo.poetrends.poe_ninja

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.CurrencyHistory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.HashMap

class CurrencyHistoryViewModel(newleagueId: String,
                               newOverviewType: Overview.Type,
                               newCurrencyId: Int)
    : ViewModel()
{
    private var leagueId: String = newleagueId
    private var overviewType: Overview.Type = newOverviewType
    private var currencyId: Int = newCurrencyId

    private val poeNinjaService: GetPoeNinjaDataService? by lazy {
        PoeNinjaClientInstance.create()
    }

    private val currencyHistory : MutableLiveData<CurrencyHistory> by lazy {
        MutableLiveData<CurrencyHistory>().also {
            reloadCurrencyHistory()
        }
    }

    private val namesByType: HashMap<Overview.Type, String> by lazy {
        HashMap<Overview.Type, String>().also {
            it[Overview.Type.CURRENCY] = "Currency"
            it[Overview.Type.FRAGMENT] = "Fragment"
        }
    }

    private var disposable: Disposable? = null

    private fun reloadCurrencyHistory()
    {
        disposable?.let {
            if (!it.isDisposed)
            {
                it.dispose()
            }
        }

        loadCurrencyHistory()
    }

    private fun loadCurrencyHistory()
    {
        disposable = poeNinjaService?.let {
            it.getCurrencyHistory(leagueId, namesByType?.get(overviewType) ?: "", currencyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> handleCurrencyHistoryResult(result) },
                    { error -> handleCurrencyHistoryError(error.message) }
                )
        }
    }

    private fun handleCurrencyHistoryResult(result : CurrencyHistory)
    {
        currencyHistory.value = result
    }

    private fun handleCurrencyHistoryError(errorMessage : String?)
    {
        //errorMessage?.let{ Log.i("CurrencyHistoryVM", errorMessage) }
    }

    fun getCurrencyHistory() : LiveData<CurrencyHistory>
    {
        return currencyHistory
    }
}
