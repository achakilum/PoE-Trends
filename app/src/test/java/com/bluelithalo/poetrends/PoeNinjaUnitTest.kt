package com.bluelithalo.poetrends

import com.bluelithalo.poetrends.model.Overview
import com.bluelithalo.poetrends.model.currency.*
import org.junit.Test
import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PoeNinjaUnitTest
{
    private val overviewNamesByType: HashMap<Overview.Type, String> by lazy {
        HashMap<Overview.Type, String>().also {
            it[Overview.Type.CURRENCY] = "Currency"
            it[Overview.Type.FRAGMENT] = "Fragment"
            it[Overview.Type.OIL] = "Oil"
            it[Overview.Type.INCUBATOR] = "Incubator"
            it[Overview.Type.SCARAB] = "Scarab"
            it[Overview.Type.FOSSIL] = "Fossil"
            it[Overview.Type.RESONATOR] = "Resonator"
            it[Overview.Type.ESSENCE] = "Essence"
            it[Overview.Type.DIVINATION_CARD] = "DivinationCard"
            it[Overview.Type.PROPHECY] = "Prophecy"
            it[Overview.Type.SKILL_GEM] = "SkillGem"
            it[Overview.Type.BASE_TYPE] = "BaseType"
            it[Overview.Type.HELMET_ENCHANT] = "HelmetEnchant"
            it[Overview.Type.UNIQUE_MAP] = "UniqueMap"
            it[Overview.Type.MAP] = "Map"
            it[Overview.Type.UNIQUE_JEWEL] = "UniqueJewel"
            it[Overview.Type.UNIQUE_FLASK] = "UniqueFlask"
            it[Overview.Type.UNIQUE_WEAPON] = "UniqueWeapon"
            it[Overview.Type.UNIQUE_ARMOUR] = "UniqueArmour"
            it[Overview.Type.UNIQUE_ACCESSORY] = "UniqueAccessory"
            it[Overview.Type.BEAST] = "Beast"
        }
    }

    private val testPoeNinjaService: TestGetPoeNinjaDataService? by lazy {
        TestPoeNinjaClientInstance.create()
    }

    @Test
    fun currencyOverviewsAreValid()
    {
        val currencyCall = testPoeNinjaService?.getCurrencyOverview("Ultimatum", overviewNamesByType?.get(Overview.Type.CURRENCY) ?: "")
        val currencyOverview = currencyCall?.execute()?.body()

        assertNotNull(currencyOverview)

        val fragmentCall = testPoeNinjaService?.getCurrencyOverview("Ultimatum", overviewNamesByType?.get(Overview.Type.FRAGMENT) ?: "")
        val fragmentOverview = fragmentCall?.execute()?.body()

        assertNotNull(fragmentOverview)
    }

    @Test
    fun currencyOverviewFilteredBySextantShowsThreeSextants()
    {
        val currencyCall = testPoeNinjaService?.getCurrencyOverview("Ultimatum", overviewNamesByType?.get(Overview.Type.CURRENCY) ?: "")
        var currencyOverview = currencyCall?.execute()?.body()?.byName("sextant")

        assertEquals(3, currencyOverview?.lines?.size)
    }
}
