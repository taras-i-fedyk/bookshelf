package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo

interface BookInfosAdapterFactory {
    fun createBookInfosAdapter(
        itemsDiffCallback: DiffUtil.ItemCallback<BookInfo>,
        onItemClickListener: BookInfosAdapter.OnItemClickListener
    ): BookInfosAdapter<out RecyclerView.ViewHolder>
}
