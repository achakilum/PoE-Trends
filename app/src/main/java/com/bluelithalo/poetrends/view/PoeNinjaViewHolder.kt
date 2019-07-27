package com.bluelithalo.poetrends.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bluelithalo.poetrends.model.Overview

abstract class PoeNinjaViewHolder(v: View) : RecyclerView.ViewHolder(v)
{
    abstract fun configureViewHolder(overview: Overview?, position: Int)
}