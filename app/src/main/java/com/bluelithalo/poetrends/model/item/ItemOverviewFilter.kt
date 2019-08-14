package com.bluelithalo.poetrends.model.item

import java.util.ArrayList

class ItemOverviewFilter
{
    companion object
    {
        fun byName(itemOverview: ItemOverview, name: String) : ItemOverview
        {
            var newLines: ArrayList<Line> = ArrayList()

            itemOverview.lines?.let {
                for (line in it)
                {
                    line.name?.let {
                        if (it.toLowerCase().contains(name.toLowerCase()))
                        {
                            newLines.add(line)
                        }
                    }
                }
            }

            itemOverview.lines = newLines
            return itemOverview
        }
    }
}