package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo

abstract class BookInfosAdapter<VH : RecyclerView.ViewHolder> (
    itemsDiffCallback: DiffUtil.ItemCallback<BookInfo>,
    protected val itemClickCallback: BookInfoClickCallback
): PagingDataAdapter<BookInfo, VH>(itemsDiffCallback)
