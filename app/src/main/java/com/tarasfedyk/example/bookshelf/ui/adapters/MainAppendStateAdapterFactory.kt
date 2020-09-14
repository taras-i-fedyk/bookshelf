package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

class MainAppendStateAdapterFactory @Inject constructor(): AppendStateAdapterFactory {
    override fun createAppendStateAdapter(
        retry: () -> Unit
    ): AppendStateAdapter<out RecyclerView.ViewHolder> = MainAppendStateAdapter(retry)
}
