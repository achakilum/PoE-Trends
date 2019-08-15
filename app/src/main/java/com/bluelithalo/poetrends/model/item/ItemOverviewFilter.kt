package com.bluelithalo.poetrends.model.item

import java.util.ArrayList

fun ItemOverview.byName(name: String) : ItemOverview
{
    var newLines: ArrayList<Line> = ArrayList()

    this.lines?.let {
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

    this.lines = newLines
    return this
}