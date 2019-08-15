package com.bluelithalo.poetrends.model.currency

import java.util.ArrayList

fun CurrencyOverview.byName(name: String) : CurrencyOverview
{
    var newLines: ArrayList<Line> = ArrayList()

    this.lines?.let {
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

    this.lines = newLines
    return this
}