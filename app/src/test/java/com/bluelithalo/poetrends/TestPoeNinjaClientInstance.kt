package com.bluelithalo.poetrends

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object TestPoeNinjaClientInstance
{
    private val POE_NINJA_API_URL = "https://poe.ninja/api/Data/"

    fun create(): TestGetPoeNinjaDataService?
    {
        return Retrofit.Builder()
                .baseUrl(POE_NINJA_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TestGetPoeNinjaDataService::class.java)
    }
}
