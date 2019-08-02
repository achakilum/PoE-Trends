package com.bluelithalo.poetrends.model

open class Overview
{
    enum class Type
    {
        NONE, CURRENCY, FRAGMENT,
        INCUBATOR, SCARAB, FOSSIL, RESONATOR, BEAST,
        ESSENCE, DIVINATION_CARD, PROPHECY, SKILL_GEM, BASE_TYPE, HELMET_ENCHANT, MAP,
        UNIQUE_MAP, UNIQUE_JEWEL, UNIQUE_FLASK, UNIQUE_WEAPON, UNIQUE_ARMOUR, UNIQUE_ACCESSORY

    }

    var type : Type

    constructor()
    {
        type = Type.NONE
    }
}