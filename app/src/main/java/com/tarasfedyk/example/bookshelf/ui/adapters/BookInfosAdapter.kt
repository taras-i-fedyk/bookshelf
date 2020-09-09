package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo

abstract class BookInfosAdapter<VH : RecyclerView.ViewHolder> (
    itemsDiffCallback: DiffUtil.ItemCallback<BookInfo>,
    protected val onItemClickListener: OnItemClickListener
): PagingDataAdapter<BookInfo, VH>(itemsDiffCallback) {

    fun interface OnItemClickListener {
        fun onItemClicked(item: BookInfo)
    }
}
