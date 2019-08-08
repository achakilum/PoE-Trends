package com.bluelithalo.poetrends.poe

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object PoeClientInstance
{
    private val POE_API_URL = "https://api.pathofexile.com/"

    fun create(): GetPoeDataService?
    {
        return Retrofit.Builder()
            .baseUrl(POE_API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GetPoeDataService::class.java)
    }
}
