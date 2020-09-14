package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.recyclerview.widget.RecyclerView

interface AppendStateAdapterFactory {
    fun createAppendStateAdapter(retry: () -> Unit): AppendStateAdapter<out RecyclerView.ViewHolder>
}
