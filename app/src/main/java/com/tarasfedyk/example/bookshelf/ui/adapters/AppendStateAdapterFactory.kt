package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

interface AppendStateAdapterFactory {
    fun createLoadStateAdapter(retry: () -> Unit): LoadStateAdapter<out RecyclerView.ViewHolder>
}
