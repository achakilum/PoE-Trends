package com.bluelithalo.poetrends.poe_ninja

import com.bluelithalo.poetrends.model.currency.CurrencyHistory
import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemGraphDatum
import com.bluelithalo.poetrends.model.item.ItemOverview

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

import io.reactivex.Observable;

interface GetPoeNinjaDataService
{
    @GET("currencyoverview")
    fun getCurrencyOverview(
            @Query("league") league: String,
            @Query("type") type: String): Observable<CurrencyOverview>

    @GET("itemoverview")
    fun getItemOverview(
            @Query("league") league: String,
            @Query("type") type: String): Observable<ItemOverview>

    @GET("currencyhistory")
    fun getCurrencyHistory(
            @Query("league") league: String,
            @Query("type") type: String,
            @Query("currencyId") currencyId: Int) : Observable<CurrencyHistory>

    @GET("itemhistory")
    fun getItemHistory(
            @Query("league") league: String,
            @Query("type") type: String,
            @Query("itemId") itemId: Int) : Observable<List<ItemGraphDatum>>
}
