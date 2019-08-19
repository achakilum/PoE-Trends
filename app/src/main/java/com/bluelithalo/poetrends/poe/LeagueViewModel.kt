package com.bluelithalo.poetrends.poe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluelithalo.poetrends.R
import com.bluelithalo.poetrends.model.league.League
import com.bluelithalo.poetrends.model.league.Rule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class LeagueViewModel : ViewModel()
{
    private val poeNinjaService: GetPoeDataService? by lazy {
        PoeClientInstance.create()
    }

    private val leagueList : MutableLiveData<List<League>> by lazy {
        MutableLiveData<List<League>>().also {
            loadLeagueList()
        }
    }

    private var disposable: Disposable? = null

    private fun loadLeagueList()
    {
        disposable = poeNinjaService?.let {
            it.getLeagueList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> handleLeagueListResult(result) },
                    { error -> handleLeagueListError(error.message) }
                )
        }
    }

    private fun handleLeagueListResult(result : List<League>)
    {
        val newLeagueList: List<League> = filterLeagueList(result)
        leagueList.value = newLeagueList
    }

    private fun handleLeagueListError(errorMessage : String?)
    {
        //errorMessage?.let{ Log.i("LeagueViewModel", errorMessage) }
    }

    private fun filterLeagueList(sourceLeagueList: List<League>) : List<League>
    {
        val newLeagueList: ArrayList<League> = ArrayList<League>()
        sourceLeagueList.iterator().forEach { league ->
            var solo = false
            league.rules?.iterator()?.forEach { rule ->
                if (rule.name.equals("Solo"))
                {
                    solo = true
                }
            }
            if (!solo) newLeagueList.add(league)
        }

        return newLeagueList
    }

    fun getLeagueList() : LiveData<List<League>>
    {
        return leagueList
    }
}

