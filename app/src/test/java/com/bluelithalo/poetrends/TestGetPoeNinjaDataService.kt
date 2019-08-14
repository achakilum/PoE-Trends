package com.bluelithalo.poetrends

import com.bluelithalo.poetrends.model.currency.CurrencyOverview
import com.bluelithalo.poetrends.model.item.ItemOverview

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

import io.reactivex.Observable;

interface TestGetPoeNinjaDataService
{
    @GET("currencyoverview")
    fun getFullCurrencyOverview(
            @Query("league") league: String,
            @Query("type") type: String): Call<CurrencyOverview>

    @GET("itemoverview")
    fun getFullItemOverview(
            @Query("league") league: String,
            @Query("type") type: String): Call<ItemOverview>
}
