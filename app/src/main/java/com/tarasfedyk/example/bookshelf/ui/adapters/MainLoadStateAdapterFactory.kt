package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

class MainLoadStateAdapterFactory @Inject constructor(): LoadStateAdapterFactory {
    override fun createLoadStateAdapter(
        retry: () -> Unit
    ): LoadStateAdapter<out RecyclerView.ViewHolder> = MainLoadStateAdapter(retry)
}