package com.bluelithalo.poetrends.poe

import com.bluelithalo.poetrends.model.league.League

import retrofit2.http.GET
import retrofit2.http.Query

import io.reactivex.Observable;

interface GetPoeDataService
{
    @GET("leagues")
    fun getLeagueList(): Observable<List<League>>
}
