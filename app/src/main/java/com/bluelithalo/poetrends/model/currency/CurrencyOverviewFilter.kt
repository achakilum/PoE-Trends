package com.bluelithalo.poetrends.model.currency

import java.util.ArrayList

class CurrencyOverviewFilter
{
    companion object
    {
        fun byName(currencyOverview: CurrencyOverview, name: String) : CurrencyOverview
        {
            var newLines: ArrayList<Line> = ArrayList()

            currencyOverview.lines?.let {
                for (line in it)
                {
                    line.currencyTypeName?.let {
                        if (it.toLowerCase().contains(name.toLowerCase()))
                        {
                            newLines.add(line)
                        }
                    }
                }
            }

            currencyOverview.lines = newLines
            return currencyOverview
        }
    }
}