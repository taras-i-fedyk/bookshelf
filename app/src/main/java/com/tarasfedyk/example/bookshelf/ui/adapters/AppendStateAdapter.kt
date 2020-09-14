package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class AppendStateAdapter<VH : RecyclerView.ViewHolder> constructor(
    protected val retry: () -> Unit
) : LoadStateAdapter<VH>()
