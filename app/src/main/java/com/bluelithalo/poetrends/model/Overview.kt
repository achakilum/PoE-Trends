package com.bluelithalo.poetrends.model

open class Overview
{
    companion object
    {
        val NONE = 0
        val CURRENCY = 1
        val ITEM = 2
    }

    var type : Int

    constructor()
    {
        type = Overview.NONE
    }
}